package mapreduce.bnl.test;


import java.io.IOException;

import mapreduce.bnl.BNLDriver;
import mapreduce.bnl.DivisionMapper;
import mapreduce.bnl.DivisionReducer;
import mapreduce.bnl.io.TupleWritable;
import mapreduce.tool.JobBuilder;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.util.Tool;




public class MRBNLTest extends Configured implements Tool {


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
		
		//conf.setOutputFormat(TextOutputFormat.class);
		
		JobClient.runJob(conf);
		return 0;
	}		


	public static void main(String[] args) throws Exception{
		divisionJob();		
		
		System.out.println("===================                        =====================");
		System.out.println("===================                        =====================");
		mergingJob();		
	}

	private static Path divisionJob() throws IOException, Exception {
		//set standalone conf
		JobConf divisionConf = new JobConf();
		divisionConf.set("fs.default.name", "file:///");
		divisionConf.set("mapreded.job.tracker", "local");
		
		// set input and ouput path
		Path division_input = new Path("target/test-classes/data.txt");
		Path division_output = new Path("target/test-classes/division-output");

		FileSystem fs = FileSystem.getLocal(divisionConf);		
		fs.delete(division_output, true);		

		//dibision job
		BNLDriver.DivisionDriver division = new BNLDriver.DivisionDriver();		

		division.setConf(divisionConf);

		division.run(new String[] {
				division_input.toString(), 
				division_output.toString()});
		return division_output;
	}	
	

	private static void mergingJob() throws IOException, Exception {
		//set standalone conf
		JobConf mergingConf = new JobConf();
		mergingConf.set("fs.default.name", "file:///");
		mergingConf.set("mapreded.job.tracker", "local");

		Path mergingInput = new Path("target/test-classes/division-output");
		Path mergingOutput = new Path("target/test-classes/merging-output");

		FileSystem fs = FileSystem.getLocal(mergingConf);		
		fs.delete(mergingOutput, true);
		
		//merging job
		BNLDriver.MergingDriver merging = new BNLDriver.MergingDriver();
		
		merging.setConf(mergingConf);

		merging.run(new String[] {
				mergingInput.toString(), 
				mergingOutput.toString()});
	}	
}
