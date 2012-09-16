package mapreduce.tool;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

public class JobBuilder {
	
	public static JobConf parseInputAndOutput(Tool tool, Configuration conf, 
			String[] args)	{
		
		if(args.length != 2) {
			printUsage(tool, "<input> <output>");
			return null;
		}
		
		JobConf jobConf = new JobConf(conf, tool.getClass());
		FileInputFormat.addInputPath(jobConf, new Path(args[0]));
		FileOutputFormat.setOutputPath(jobConf, new Path(args[1]));
	
		return jobConf;		
	}
	
	public static void printUsage(Tool tool, String extraArgsUsage)  {		
		System.err.printf("Usage: %s [generic option] %s \n\n",
				tool.getClass().getSimpleName(), extraArgsUsage);
		GenericOptionsParser.printGenericCommandUsage(System.err);
	}

}
