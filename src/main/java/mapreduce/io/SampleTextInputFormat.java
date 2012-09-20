package mapreduce.io;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class SampleTextInputFormat
	extends FileInputFormat<NullWritable, Text>{

	@Override
	public RecordReader<NullWritable, Text> createRecordReader(InputSplit arg0,
			TaskAttemptContext arg1) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new SampleTextRecordReader();
	}
	
	class SampleTextRecordReader extends RecordReader<NullWritable, Text> {

		@Override
		public void close() throws IOException {
			//do nothing				
		}

		@Override
		public NullWritable getCurrentKey() throws IOException,
				InterruptedException {
			return NullWritable.get();
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return new Text();
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {			
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void initialize(InputSplit arg0, TaskAttemptContext arg1)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
