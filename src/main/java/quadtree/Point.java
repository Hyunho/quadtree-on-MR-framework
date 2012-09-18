package quadtree;

import java.io.Serializable;

public class Point implements Serializable {
		
	private double[] values;
	public double[] values() {
		return this.values;
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
	
}
