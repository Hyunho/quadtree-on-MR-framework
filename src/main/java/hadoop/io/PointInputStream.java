package hadoop.io;

import index.quadtree.Point;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PointInputStream extends DataInputStream {

	private int dimension = 0;
	int numByteOfLine;
	
	public PointInputStream(InputStream in, int dimension) {
		super(in);
		this.dimension = dimension;
		
		//java character takes up 2 bytes of memory.
		this.numByteOfLine = 8* dimension + 2;
	} 
	
	public Point readPoint() throws IOException {
		try {
			double[] values = new double[dimension];						
			for(int i=0; i< dimension ; i++) {
				values[i] = this.readDouble();
			}						
			this.readChar();
			Point point = new Point(values);
			return point;
		}catch(EOFException ee) {
			return null;
		}
	}
	
	public int numByteOfLine() {
		return this.numByteOfLine;
	}

}