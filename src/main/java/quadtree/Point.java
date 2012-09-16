package quadtree;
public class Point {
	
	
	public double[] values;

	public Point(double... values) {
		this.values = values;
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
			str += ", " + values[i];
		}
		
		return str;
	}
	
}
