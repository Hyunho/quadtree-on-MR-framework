package index.quadtree;

import java.io.Serializable;

public class Point implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -3656012765730552099L;
	private double[] values;
	public double[] values() {
		return this.values;
	}
	
	public Point() {
		this.values = new double[0];
	}
	public Point(double... values) {
		this.values = values;
	}	
	
	
	public Point(String... strings) {		
		this.values = new double[strings.length];
		for(int i=0; i< strings.length; i++ ) {
			this.values[i] = Double.parseDouble(strings[i]);	            
		}
	}
	
	public int dimension() {
		return values.length;
	}
	

	@Override
	public String toString() {
		String str = new String();
		
		if (values.length == 0)
			return str;
		
		str += values[0]; 
		for(int i = 1 ; i < values.length; i++) {
			str += " " + values[i];
		}
		
		return str;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o) return true;	
		if (!(o instanceof Point)) return false;		                 
		Point other = (Point) o;
		
		if (!(this.dimension() == other.dimension())) return false;
		
		for(int i=0; i< this.dimension(); i++) {
			if (!(this.values[i] == other.values[i])) return false;
		}
		
		return true;	      
	}

	
	public static Point stringToPoint(String line) {
		String[] strings = line.split(" ");
		
		double[] douArray =  new double[strings.length];

		for(int i=0; i< strings.length; i++) {
			douArray[i] = Double.parseDouble(strings[i]);
		}		

		Point point = new Point(douArray);
		return point;
	}
	
	
}
