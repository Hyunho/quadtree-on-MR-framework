package mapreduce.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;


/**
 * The class for iterating a disk, if tuples not fits into memory.
 * @author dke
 *
 */
public class TupleFile {

	
	private ObjectOutputStream os;
	private File file;
	
	public TupleFile(String filename) throws IOException {
		
		this.file = new File(filename);
		os = new ObjectOutputStream(new FileOutputStream(filename));				 
	}
	
	
	/**
	 * insert a tuple onto the tuple file.
	 * @param tuple - the tuple to be inserted.
	 * @throws IOException 
	 */
	public void insertTuple(TupleWritable tuple) throws IOException {
		tuple.write(os);		
	}
	
	/**
	 * returns a tuple iterator on this file starting with first tuple.
	 * @return a tuple iterator starting with first tuples.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static Iterator<TupleWritable> iterator(String fileName) throws FileNotFoundException, IOException {
		return new TupleIterator(new ObjectInputStream(new FileInputStream(fileName)));
	}

	public void close() throws IOException {
		os.close();		
	}
	
	public void delete(String fileName) {				
		this.file.delete();
	}	
	
	public boolean renamedTo(String filename) {
		File renamed = new File(filename);		
		return this.file.renameTo(renamed);		
	}
	
}
