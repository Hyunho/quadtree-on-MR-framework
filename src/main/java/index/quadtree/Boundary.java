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
					"But number of dimension of boundary is " +  this.dimension() +
					" and number of dimension of point is " + point.dimension()+ "\n" +
					"Point is" + point.toString()) ;
		
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
			
			Range[] ranges = new Range[lr.size()];
			for(int i=0; i< ranges.length; i++)
				ranges[i]= lr.get(i);
			
			subBoundaries.add(new Boundary(ranges));
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

	/**
	 * Does this boundary overlap given other boundary partially?
	 * @param other
	 * @return
	 */
	public boolean overlab(Boundary other) {

		
		if (this.dimension() !=  other.dimension())
			throw new InvalidParameterException(
					"dimension of boundraries must be same. " +
					"But number of dimension of a boundary is " +  this.dimension() +
					" and number of dimension of other boudnary is " + other.dimension());
		
		for(int i=0; i< ranges.length; i++) {
			if (this.ranges[i].min <= other.ranges[i].min &&
					this.ranges[i].max < other.ranges[i].min) {
				return false;
			}else if(this.ranges[i].min >= other.ranges[i].max &&
					this.ranges[i].max > other.ranges[i].max) {
				return false;
				
			}
		}	
		return true;		
	}

	/**
	 * Does this boundary cover given other boundary completely?
	 * @param other
	 * @return
	 */
	public boolean cover(Boundary other) {
		if (this.dimension() !=  other.dimension())
			throw new InvalidParameterException(
					"dimension of boundraries must be same. " +
							"But number of dimension of a boundary is " +  this.dimension() +
							" and number of dimension of other boudnary is " + other.dimension());

		for(int i=0; i< ranges.length; i++) {
			
			if (this.ranges[i].min <= other.ranges[i].min &&
					this.ranges[i].max < other.ranges[i].max) {
				return false;
			}else if(this.ranges[i].min >= other.ranges[i].min &&
					this.ranges[i].max < other.ranges[i].max) {
				return false;

			}

		}
		return true;
	}
}