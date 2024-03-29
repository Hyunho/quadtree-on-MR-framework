package mapreduce.example.quadtree;

import hadoop.Sampler;
import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mapreduce.io.BinaryFileInputFormat;
import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 *first job to pre-processing for building a quadtree Q` on sample
 *In this job, mappers sample a data from original data D and single reducer builds a quadtree Q`
 *Quadtree Q' is used to partition a original dataset D in follow job.
 *
 *second job is bulid a global quadtree
 *mapper partition a dataset into disjoint set using
 *a quadtree Q' built in first job.
 */
public class QuadTreeDriverWithSample {
	
	public static final Path samplePath  = new Path("sample");
	public static long numSample;	
	public static final Path sampleQuadPath  = new Path("sample_quadtree");

	public static int numReduceTasks = 2 * 9;
	 
	/**
	 * class for second job
	 * @author dke
	 *
	 */
	public static class BuldingQuadtree extends Configured implements Tool{

		static class QuadtreeNameMultipleSequenceOutputFormat 
		extends MultipleTextOutputFormat<Text, PointWritable> {
			
			@Override
			protected String generateFileNameForKeyValue(Text key, PointWritable value,
					String name) {
				return key.toString() + "-points";
			}
		}
		
		public static class PartitionMapper extends MapReduceBase
		implements Mapper <NullWritable, PointWritable, Text, PointWritable>{

			private QuadTreeFile quadtree;

			@Override
			public void configure(JobConf job) {
				try {
					FileSystem fs = FileSystem.get(job);
					FSDataInputStream fis=  fs.open(new Path("quadtree.dat"));
					ObjectInputStream os = new ObjectInputStream(fis);
					this.quadtree = (QuadTreeFile)os.readObject();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
	
			@Override
			public void map(NullWritable key, PointWritable value,
					OutputCollector<Text, PointWritable> output, Reporter arg3)
			throws IOException {
				
				System.out.println(value.toString());
				output.collect(
						new Text(this.quadtree.getindex(value.point())),
						value);
			}
		}

		@Override
		public int run(String[] args) throws Exception {

			JobConf conf = new JobConf(getConf(), getClass());
			conf.setJobName("Build local quadtrees");
			
			
			String input = args[0];
			String output = args[1];
			int dimension = Integer.parseInt(args[2]);			
			
			
			//configuration
			conf.setInt("capacity", 1700000);
			conf.setStrings("boundary",
					"0-1000",
					"0-1000"				
			);			
			conf.setInt("dimension", dimension);		
			
			DistributedCache.addCacheFile(
					new URI("quadtree.dat"), 
                    conf);
			 
			DistributedCache.createSymlink(conf);
			
			FileInputFormat.addInputPath(conf, new Path(input));
			FileOutputFormat.setOutputPath(conf, new Path(output));
			
			conf.setInputFormat(BinaryFileInputFormat.class);
			conf.setOutputFormat(QuadtreeNameMultipleSequenceOutputFormat.class);
			
			conf.setOutputValueClass(PointWritable.class);

			
			conf.setMapOutputKeyClass(Text.class);
			conf.setMapOutputValueClass(PointWritable.class);		
			
			conf.setMapperClass(PartitionMapper.class);
			conf.setReducerClass(QuadTreeReducer.class);

			conf.setNumReduceTasks(numReduceTasks);
			
			JobClient.runJob(conf);
			return 0;
		}		
	}


	private static void samplingAndBuilding(String[] args) throws IOException {

		String input = args[0];
		int dimension = Integer.parseInt(args[2]);
		
		String dst = "quadtree.dat";
		int numSample = 10000;
		
		int capacityOfBaseQuadtree = numSample / numReduceTasks; 

		Point[] points = Sampler.reservoirSampling(input, numSample, dimension);
		
		System.out.println(points[0].dimension());

		for(int i=0; i< dimension ; i++ ) {
			assertThat(points[i].dimension(), is(dimension));
			
		}
		Range[] ranges = new Range[dimension];
		for(int i=0; i< dimension ; i++ ) {
			ranges[i] = new Range(0, 1000);
		}
		
		Boundary boundary = new Boundary(ranges);
		// initialize a base quadtree
		QuadTree quadTree = new QuadTreeFile(
				capacityOfBaseQuadtree, 
				boundary,
				"Q");				

		int i=0;

		while(i<numSample) {
			quadTree.insert(points[i]);
			i++;
		}

		//write quadtree in to HDFS
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		FSDataOutputStream os = fs.create(new Path(dst), true);				
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(quadTree);
		oos.close();
		
	}
	
	public static void main(String[] args) throws Exception{

		if(args.length != 3) {
			System.err.printf("Usage: %s [generic option] <input> <output> <dimension>\n", 
					QuadTreeDriverWithSample.class.getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(-1);
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		//start
		System.out.println("start sampling and building a quadtree");
		System.out.println(dateFormat.format(cal.getTime()));
		
		samplingAndBuilding(args);
		
		// end
		System.out.println("end building a quadtree");
		cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));

		
		ToolRunner.run(new BuldingQuadtree(), args);

		System.out.print("number of sample : " + numSample);
		System.exit(0);
	}
}
