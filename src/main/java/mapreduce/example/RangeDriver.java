package mapreduce.example;

import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTreeFile;
import index.quadtree.QuadtreeSearcher;
import index.quadtree.QuadtreeSearcher.NormalSearcher;
import index.quadtree.Range;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import mapreduce.example.quadtree.QuadTreeDriverWithSample;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class RangeDriver {

	
	public static class RangeSearch extends Configured implements Tool {
	
		public static class RangeMapper extends MapReduceBase
		implements Mapper<LongWritable, Text, NullWritable, Text> {
				
			public Boundary boundary =new Boundary(new Range(100, 300), new Range(100, 300)); 

			@Override
			public void map(LongWritable key, Text value,
					OutputCollector<NullWritable, Text> output, Reporter arg3)
							throws IOException {

				Point point = Point.stringToPoint(value.toString());

				if (boundary.containsPoint(point)) {
					output.collect(NullWritable.get(), value);
				}
			}
		}
		
		
		@Override
		public int run(String[] args) throws Exception {

			Configuration conf = new Configuration();
			
			Boundary boundary =  new Boundary(new Range(100, 300), new Range(100, 300));
			
			JobConf job = new JobConf(conf, RangeSearch.class);
			job.setJobName("range search");
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Text.class);
			job.setMapperClass(RangeSearch.RangeMapper.class);
			
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			
			int method = Integer.parseInt(args[2]);
			
			
			if(method == 1) {
			
				String outputPath = args[0];
				
				// load global quadtree
				QuadTreeFile global = null;
				FileSystem fs = null;
				try {				
					fs = FileSystem.get(conf);
					FSDataInputStream fis=  fs.open(new Path(outputPath + "/Q"));
					ObjectInputStream os = new ObjectInputStream(fis);
					global = (QuadTreeFile)os.readObject();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				QuadtreeSearcher.NormalSearcher ns = new NormalSearcher(global);
				ArrayList<QuadTreeFile> nodes = ns.searchNodes(boundary);
				
				for(QuadTreeFile node : nodes) {				
					FileInputFormat.addInputPath(job, new Path(outputPath + "/" + node.name() + "*-points"));
				}
				
			} else {
				FileInputFormat.addInputPath(job, new Path(args[0]));
			}
			
			
			
			
			JobClient.runJob(job);
			return 0;
		}
	}
		

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		if(args.length != 3) {
			System.err.printf("Usage: %s [generic option] <input> <output> <method>\n", 
					QuadTreeDriverWithSample.class.getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(-1);
		}
		
		ToolRunner.run(new RangeSearch(), args);
		
		// TODO Auto-generated method stub

	}
}
