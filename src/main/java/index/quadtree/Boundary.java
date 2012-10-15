package index.quadtree;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;

public class Boundary implements Serializable{	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8410984052838112510L;
	private Range[] ranges;

	/**
	 * 
	 * @param points to composite boundary
	 * @throws InvalidParameterException 
	 */
	public Boundary(Range... ranges) {
		this.ranges = ranges;		
	}

	/**
	 * check that point is within boundary
	 * @param
	 * @return if given point is within boundary, return true
	 * @exception InvalidParameterException
	 */
	public boolean containsPoint(Point point) {
		
		if (this.dimension() !=  point.dimension())
			throw new InvalidParameterException(
					"dimension of boundrary and that of point must be same. " +
					"number of dimension of boundary is " +  this.dimension() +
					" and number of dimension of point is " + point.dimension());
		
		for(int i=0; i< ranges.length; i++) {
			if ((ranges[i].min > point.values()[i]) ||
					ranges[i].max <= point.values()[i]) {
				return false;
			}
		}	
		return true;			
	}	
	
	/**
	 * return dimension of boundary
	 * @return dimension of boundary
	 */
	public int dimension() {
		return ranges.length;
	}

	public List<Boundary> split() {
		
		List<List<Range>> list = new ArrayList<List<Range>>();		
		for(int i=0; i < dimension() ; i++) {
			double median = (this.ranges[i].max + this.ranges[i].min) / 2;
			List<Range> part = new ArrayList<Range>();
			part.add(new Range(this.ranges[i].min, median));
			part.add(new Range(median, this.ranges[i].max));			
			
			list.add(part);
		}
		
		CartesianIterator<Range> ci = new CartesianIterator<Range>(list);
		
		List<Boundary> subBoundaries= new ArrayList<Boundary>();		
		while(ci.hasNext()) {
			List<Range> lr = ci.next();
			subBoundaries.add(new Boundary(lr.get(0), lr.get(1)));
		}
		
		return subBoundaries;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Boundary)) return false;		                 
		Boundary other = (Boundary) o;
		
		if (!(this.dimension() == other.dimension())) return false;
		
		for(int i=0; i< this.dimension(); i++) {
			if (!this.ranges[i].equals(other.ranges[i])) return false;
		}
		
		return true;	      
	}
	
	@Override
	public String toString() {
		String str = new String();	
		for(int i=0; i< this.dimension(); i++) {
			 str += this.ranges[i].toString();
		}
		str += "\n";
		
		return str;
	}
	
}