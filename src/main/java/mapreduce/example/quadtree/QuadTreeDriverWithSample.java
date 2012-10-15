package mapreduce.example.quadtree;

import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.*;


import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


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
	 * class for first job
	 * @author dke
	 *
	 */
	public static class Sampling extends Configured implements Tool {

		/**
		 * Sample from random points in the input. General-purpose sampler.
		 * Takes numSamples / maxSplitsSampled inputs from each split.  
		 * @author dke
		 *
		 */
		public static class RandomSampleMapper extends MapReduceBase
		implements Mapper<LongWritable, Text, NullWritable, Text> {

			private double probability = 0.05;
			private Random rand = new Random();
			@Override
			public void map(LongWritable key, Text value,
					OutputCollector<NullWritable, Text> output, Reporter arg3)
			throws IOException {

				if (rand.nextDouble() < this.probability) {
					output.collect(NullWritable.get(), value);
				}
			}
		}

		@Override
		public int run(String[] args) throws Exception {

			Configuration conf = new Configuration();
			
			//sampling
			JobConf job1 = new JobConf(conf, Sampling.class);
			job1.setJobName("sampling");
			job1.setOutputKeyClass(NullWritable.class);
			job1.setOutputValueClass(Text.class);
			job1.setMapperClass(Sampling.RandomSampleMapper.class);
			FileInputFormat.addInputPath(job1, new Path(args[0]));
			FileOutputFormat.setOutputPath(job1, samplePath);
			job1.setNumReduceTasks(numReduceTasks);
			RunningJob runningJob = JobClient.runJob(job1);
			
			//get number of sample
			numSample = runningJob.getCounters().findCounter(
					"org.apache.hadoop.mapred.Task$Counter",
					"MAP_OUTPUT_RECORDS").getCounter();
			return 0;
		}
	}
		
	public static class Preprocessing extends Configured implements Tool {

	
		public static class GatherIntoOneReducerMapper extends MapReduceBase
		implements Mapper <LongWritable, Text, NullWritable, Text>{
	
			@Override
			public void map(LongWritable key, Text value,
					OutputCollector<NullWritable, Text> output, Reporter arg3)
					throws IOException {
				output.collect(NullWritable.get(), value);
			}
		}

		public static class SampleQuadtreeReducer extends MapReduceBase
		implements Reducer<NullWritable, Text, NullWritable, Text> {
			

			private int capacity;
			private FileSystem fileSystem;
			private String dst = "quadtree.dat";

			@Override
			public void configure(JobConf job) {
				this.capacity = (job.getInt("capacity", 10));
				try {
					this.fileSystem = FileSystem.get(
							URI.create(dst),
							job);
				} catch (IOException e) {
					e.printStackTrace();
				}
				super.configure(job);
			}

			@Override
			public void reduce(NullWritable arg0, Iterator<Text> iValues,
					OutputCollector<NullWritable, Text> output, Reporter arg3)
					throws IOException {
				
				// TODO should remove follow line
				System.out.println("capacity: "+ this.capacity);
				
				QuadTree quadTree = new QuadTreeFile(
						this.capacity, 
						new Boundary(new Range(0, 100), new Range(0, 100)),
						"Q");				
				
				// build a quadtree
				while(iValues.hasNext()) {
					Point point = new Point(iValues.next().toString().split(" "));
					quadTree.insert(point);
				}		
				
				//write quadtree in to HDFS
				FSDataOutputStream os = this.fileSystem.create(new Path(dst), true);				
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(quadTree);
				
				Iterator<QuadTree> qTrees = quadTree.leaves().iterator();
				while(qTrees.hasNext()) {
					output.collect(NullWritable.get(), 
							new Text(qTrees.next().name()));
				}
					
			}
		}

		@Override
		public int run(String[] args) throws Exception {
			Configuration conf = new Configuration();

			conf.setInt("capacity", 100000);
			//build quadtree
			JobConf job = new JobConf(conf, Preprocessing.class);
			
			int capacity = (int) (numSample / job.getNumReduceTasks());
			
			job.setJobName("build quadtree on sample");
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Text.class);
			job.setMapperClass(Preprocessing.GatherIntoOneReducerMapper.class);
			job.setReducerClass(Preprocessing.SampleQuadtreeReducer.class);
			FileInputFormat.addInputPath(job, samplePath);
			FileOutputFormat.setOutputPath(job, sampleQuadPath);
			job.setNumReduceTasks(numReduceTasks);
			JobClient.runJob(job);
			return 0;
		}
		
	}
	
	/**
	 * class for second job
	 * @author dke
	 *
	 */
	public static class BuldingQuadtree extends Configured implements Tool{

		
		public static class PartitionMapper extends MapReduceBase
		implements Mapper <LongWritable, Text, Text, PointWritable>{

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
			public void map(LongWritable key, Text value,
					OutputCollector<Text, PointWritable> output, Reporter arg3)
			throws IOException {

				Point point = new Point(value.toString().split(" "));
				output.collect(
						new Text("Q" + this.quadtree.getindex(point)),
						new PointWritable(point)
				);
			}
		}
		
		@Override
		public int run(String[] args) throws Exception {

			JobConf conf = new JobConf(getConf(), getClass());
			conf.setJobName("Build local quadtrees");
			
			conf.setInt("capacity", 100000);
			conf.setStrings("boundary",
					"0-100",
					"0-100"				
			);
			
			DistributedCache.addCacheFile(
					new URI("quadtree.dat"), 
                    conf);
			DistributedCache.createSymlink(conf);
			
			FileInputFormat.addInputPath(conf, new Path(args[0]));
			FileOutputFormat.setOutputPath(conf, new Path(args[1]));
			
			conf.setMapOutputKeyClass(Text.class);
			conf.setMapOutputValueClass(PointWritable.class);		
			conf.setMapperClass(PartitionMapper.class);
			conf.setOutputValueClass(Text.class);		
			conf.setReducerClass(QuadTreeReducer.class);
			conf.setNumReduceTasks(numReduceTasks);
			
			JobClient.runJob(conf);
			return 0;
		}		
	}


	public static void main(String[] args) throws Exception{
		
		if(args.length != 2) {
			System.err.printf("Usage: %s [generic option] <input> <output>\n", 
					QuadTreeDriverWithSample.class.getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(-1);
		}
		
		ToolRunner.run(new Sampling(), args);
		ToolRunner.run(new Preprocessing(), args);
		ToolRunner.run(new BuldingQuadtree(), args);

		System.out.print("number of sample : " + numSample);
		System.exit(0);
	}
}
