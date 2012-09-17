package quadtree;

import java.io.Serializable;

public class Range  implements Serializable{

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
		str += "(min : "+ this.min + ", max : "+ this.max + ")";
		return str;
	}
	
}
