package mapreduce.bnl.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TupleIterator implements Iterator<TupleWritable> {
	
	private ObjectInputStream is;

	public TupleIterator(ObjectInputStream is) {
		this.is = is;
	}

	/**
	 * Returns true if the iterator has more tuples.
	 */
	public boolean hasNext() {		
		try {
			return (is.available() != 0);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return false;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();		
	}

	@Override
	public TupleWritable next() throws NoSuchElementException{

		TupleWritable tuple = new TupleWritable();
		try {		
			tuple.readFields(is);
		} catch (IOException e) {			
			e.printStackTrace();
			throw new NoSuchElementException();
		}
		return tuple;
	}
}