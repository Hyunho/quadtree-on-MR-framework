package mapreduce.example.quadtree;



import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

import java.io.IOException;
import mapreduce.example.quadtree.QuadTreeDriverWithSample.BuldingQuadtree.QuadtreeNameMultipleSequenceOutputFormat;
import mapreduce.io.BinaryFileInputFormat;
import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
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
	implements Mapper<NullWritable, PointWritable, Text, PointWritable> {

		private QuadTreeFile quadTree;
		private int dimension;


		@Override   
		public void configure(JobConf conf) {		
			this.dimension = (conf.getInt("dimension", 2));
			if(dimension > 5) {
				dimension = 4;
			}
			
			if(this.quadTree == null) {
				Range[] ranges = new Range[dimension];
				for(int i=0; i< this.dimension ; i++ ) {
					ranges[i] = new Range(0, 1000);
				}

				Boundary boundary = new Boundary(ranges);

				int depth;
				if(dimension > 2) {
					depth = 1; 
				}
				else {
					depth = 2;
				}

				this.quadTree = QuadTreeFile.makeQuadtree(3, boundary, depth);
			}
		}	

		@Override
		public void map(NullWritable key, PointWritable iValue,
				OutputCollector<Text, PointWritable> output, Reporter reporter)
						throws IOException {

			Point point = iValue.point();	
			Point tempPoint;
			
			if(point.dimension() > 5) {				
				tempPoint = new Point(point.get(0), point.get(1), point.get(2), point.get(3));
			}
			else {
				tempPoint = point;
			}		
			
			output.collect(
					new Text(this.quadTree.getindex(tempPoint)),
					new PointWritable(point)
					);
		}
	}

	

	private static int mumReduceTask = 2 * 9;
	@Override
	public int run(String[] args) throws Exception {
		if(args.length != 3) {
			System.err.printf("Usage: %s [generic option] <input> <output> <dimensio>\n", 
					getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		String input = args[0];
		String output = args[1];
		int dimension = Integer.parseInt(args[2]);

		JobConf conf = new JobConf(getConf(), getClass());
		conf.setJobName("QuadTreeWithoutSample");

		conf.setInt("capacity", 1700000);
		conf.setInt("dimension", dimension);		

		FileInputFormat.addInputPath(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));
		
		conf.setInputFormat(BinaryFileInputFormat.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(PointWritable.class);
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(PointWritable.class);
		conf.setOutputFormat(QuadtreeNameMultipleSequenceOutputFormat.class);

		
		conf.setMapperClass(QuadTreeMapper.class);
		conf.setReducerClass(QuadTreeReducer.class);
		
		conf.setNumReduceTasks(mumReduceTask);

		JobClient.runJob(conf);
		return 0;
	}

	public static void main(String[] args) throws Exception{
		int exitCode = ToolRunner.run(new QuadtreeDriverWithoutSample(), args);
		System.exit(exitCode);
	}
}
