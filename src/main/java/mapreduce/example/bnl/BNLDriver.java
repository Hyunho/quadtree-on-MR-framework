package mapreduce.example.bnl;



import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;


import mapreduce.io.FlagWritable;
import mapreduce.io.TupleWritable;
import mapreduce.tool.JobBuilder;

public class BNLDriver {

	public static class DivisionDriver extends Configured implements Tool {
		
		@Override
		public int run(String[] args) throws Exception {

			JobConf conf = JobBuilder.parseInputAndOutput(this, this.getConf(), args);
			
			if(conf == null) {
				return -1;
			}		
			
			FileInputFormat.addInputPath(conf, new Path(args[0]));
			FileOutputFormat.setOutputPath(conf, new Path(args[1]));		
			
			conf.setInputFormat(TextInputFormat.class);		
			
			conf.setMapOutputKeyClass(Text.class);
			conf.setMapOutputValueClass(TupleWritable.class);
			conf.setMapperClass(DivisionMapper.class);		
			
			conf.setReducerClass(DivisionReducer.class);
						
			JobClient.runJob(conf);
			return 0;
		}		
		
	}	

	public static class  MergingDriver extends Configured implements Tool{
		
		@Override
		public int run(String[] args) throws Exception {	
			
			JobConf conf = JobBuilder.parseInputAndOutput(this, this.getConf(), args);
			
			if(conf == null) {
				return -1;
			}			

			FileInputFormat.addInputPath(conf, new Path(args[0]));
			FileOutputFormat.setOutputPath(conf, new Path(args[1]));

			conf.setInputFormat(TextInputFormat.class);	

			conf.setMapOutputKeyClass(NullWritable.class);
			conf.setMapOutputValueClass(FlagWritable.class);
			conf.setMapperClass(MergingMapper.class);		
			
			conf.setReducerClass(MergingReducer.class);
			
			JobClient.runJob(conf);

			return 0;
		}	
	}
	

	public static void main(String[] args) throws Exception{		
		
		
		int exitCode = ToolRunner.run(new BNLDriver.DivisionDriver(), args);
		exitCode = ToolRunner.run(new BNLDriver.MergingDriver(), args);
		
		System.exit(exitCode);
	}
}