package quadtree;

public class Range {

	public double min;
	public double max;

	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}
	

	@Override 
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Range)) return false;		                 
		Range other = (Range)o;
		return (this.min == other.min) &&
				(this.max == other.max);		      
	}	
	
	@Override
	public String toString() {
		String str = new String();
		str += " min : "+ this.max + ", max : "+ this.min;
		return str;
	}
	
}
