package mapreduce.io;

import hadoop.io.PointInputStream;
import index.quadtree.Point;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

public class BinaryFileInputFormat
	extends FileInputFormat<NullWritable, PointWritable> {

	@Override
	public RecordReader<NullWritable, PointWritable> getRecordReader(
			InputSplit split, JobConf job, Reporter arg2) throws IOException {
		return new BinaryFileRecordReader((FileSplit)split, job);
	}


	public class BinaryFileRecordReader 
	implements RecordReader<NullWritable, PointWritable> {
				
		private PointInputStream pis;
		private long start;
		private long end;
		private FSDataInputStream fileIn;
		private long pos;

		public BinaryFileRecordReader(FileSplit split, JobConf job) throws IOException {
			
			final Path file = split.getPath();						
			final FileSystem fs = file.getFileSystem(job);
			this.fileIn = fs.open(file);
			
			
			pis = new PointInputStream(this.fileIn, job.getInt("dimension", 2));
			
			
			/*
			 *  size of all record in binary is same.
			 *  but didn't split data by default {@link FileInputFormat} class,
			 *  so we reset a split in this class. 
			 */			
			this.start = (split.getStart() / pis.numByteOfLine()) * pis.numByteOfLine();
		    this.end = ((split.getStart() + split.getLength()) / 
		    		pis.numByteOfLine()) * pis.numByteOfLine();
			this.pos = start;
			
			this.fileIn.seek(start);
			
		}

		@Override
		public void close() throws IOException {
			if(pis != null)
				pis.close();
		}

		@Override
		public NullWritable createKey() {
			return NullWritable.get();
		}

		@Override
		public PointWritable createValue() {
			return new PointWritable();
		}

		@Override
		public long getPos() throws IOException {
			return this.pos;
		}

		@Override
		public float getProgress() throws IOException {
			if (start == end) {
				return 0.0f;
			} else {
				return Math.min(1.0f, (this.pos - start) / (float)(end - start));
			}
		}

		@Override
		public boolean next(NullWritable key, PointWritable value)
				throws IOException {

			if(this.pos < this.end)  {
				Point point = pis.readPoint();
				pos = pos + pis.numByteOfLine();
				if(point != null) {
					value.set(point);	
					return true;
				}
				return false;
			}
			return false;
		}
	}
}
