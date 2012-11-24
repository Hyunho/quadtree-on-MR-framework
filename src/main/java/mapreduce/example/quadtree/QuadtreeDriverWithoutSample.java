package mapreduce.example.quadtree;



import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTreeFile;
import index.quadtree.QuadTreeMemory;
import index.quadtree.Range;

import java.io.IOException;

import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;



public class QuadtreeDriverWithoutSample extends Configured implements Tool{




	/**
	 * get index of tuple using virtual quad-tree
	 * @author hyunho
	 *
	 */
	public static class QuadTreeMapper extends MapReduceBase 
	implements Mapper<LongWritable, Text, Text, PointWritable> {

		private QuadTreeMemory quadTree;


		@Override   
		public void configure(JobConf conf) {		

		}	
		
		@Override
		public void map(LongWritable key, Text iValue,
				OutputCollector<Text, PointWritable> output, Reporter reporter)
						throws IOException {


			Point point = Point.stringToPoint(iValue.toString());

			if(this.quadTree == null) {
					int dimension = point.dimension();
					
					Range[] ranges = new Range[dimension];
					for(int i=0; i< dimension ; i++ ) {
						ranges[i] = new Range(0, 1000);
					}
					
					Boundary boundary = new Boundary(ranges);
					
					int depth;
					if(dimension >= 4) {
						depth = 1; 
					}
					else {
						depth = 2;
					}
							 
					
					this.quadTree = QuadTreeMemory.getQuadtree(3, boundary, depth);
			}
			
			output.collect(
					new Text("Q" + this.quadTree.getindex(point)),
					new PointWritable(point)
					);
		}
	}


	private static int mumReduceTask = 2 * 9;
	@Override
	public int run(String[] args) throws Exception {
		if(args.length != 2) {
			System.err.printf("Usage: %s [generic option] <input> <output>\n", 
					getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		JobConf conf = new JobConf(getConf(), getClass());
		conf.setJobName("QuadTreeWithoutSample");

		conf.setInt("capacity", 10000);

		FileInputFormat.addInputPath(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(PointWritable.class);		
		conf.setMapperClass(QuadTreeMapper.class);

		conf.setOutputValueClass(Text.class);
		conf.setNumReduceTasks(mumReduceTask);

		conf.setReducerClass(QuadTreeReducer.class);


		JobClient.runJob(conf);
		return 0;
	}

	public static void main(String[] args) throws Exception{
		int exitCode = ToolRunner.run(new QuadtreeDriverWithoutSample(), args);
		System.exit(exitCode);
	}
}
