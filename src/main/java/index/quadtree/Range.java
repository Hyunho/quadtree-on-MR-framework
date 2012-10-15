package index.quadtree;

import java.io.Serializable;
import java.security.InvalidParameterException;

public class Range  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9156625161361529947L;
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
	
	/**
	 * create Range instance using string split by 
	 * given regular expression. 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static Range createRange(String str, String regex) {
		String[] strs =  str.split(regex);
		
		if(strs.length != 2)
			throw new InvalidParameterException(
					"given string have to be split by regex"
					);
		
		return new Range(Double.parseDouble(strs[0]),				
				Double.parseDouble(strs[1]));
	}
	
}
