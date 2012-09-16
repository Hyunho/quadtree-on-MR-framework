package quadtree;
public class Point {
	
	
	public int[] values;

	public Point(int... values) {
		this.values = values;
	}	
	
	public int dimension() {
		return values.length;
	}
}
