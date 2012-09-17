package quadtree;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * this class is not implemented fully. only support 2-dimensions yet. 
 * @author dke
 *
 * @param <Point> item handled by quad-tree
 */
public class QuadTreeMemory implements QuadTree {
	
	private int capacity;
	
	private Boundary boundary;
	/* (non-Javadoc)
	 * @see quadtree.Quad#boundary()
	 */
	public Boundary boundary() { return this.boundary; }
	
	private int dimension;
	/* (non-Javadoc)
	 * @see quadtree.Quad#dimension()
	 */
	public int dimension() { return this.dimension; }
	
	public ArrayList<QuadTreeMemory> children;
	private ArrayList<Point> points = new ArrayList<Point>();
	
	
	public QuadTreeMemory(int capacity, Boundary boundary) {
		this.capacity = capacity;
		this.boundary = boundary;
		this.dimension = boundary.dimension();
	}
	

	/* (non-Javadoc)
	 * @see quadtree.Quad#split()
	 */
	public void split() {
		
		this.children = new ArrayList<QuadTreeMemory>();
		
		List<Boundary> lb = this.boundary.split();
		
		Iterator<Boundary> ib = lb.iterator();		
		while(ib.hasNext()) {
			Boundary subBoundary = ib.next();			
			this.children.add(new QuadTreeMemory(this.capacity, subBoundary));
		}
		
		Iterator<Point>  iterator  = this.points.iterator();

		while(iterator.hasNext()) {
			Point point = iterator.next();
			insertIntoChildren(point);
			iterator.remove();
		}
	}
		
	/* (non-Javadoc)
	 * @see quadtree.Quad#insert(quadtree.Point)
	 */
	public boolean insert(Point point) {		
		
		//ignore objects which do not belong in this quad tree
		if(!boundary.containsPoint(point)) {
			return false;
		}

		// if there is space in this quad tree, add the object here
		if(size() < this.capacity) {
			this.points.add(point);
			return true;
		}

		// Otherwise, we need to subdivide than add the point to whichever node will accept it
		if (!this.hasChildren()) {
			this.split();
		}

		this.insertIntoChildren(point);
		
		//otherwise. the point can not be inserted for some unknown reason(which should never happen)
		return false;		
	}

	//insert point into sub-quadtree
	/* (non-Javadoc)
	 * @see quadtree.Quad#insertIntoChildren(quadtree.Point)
	 */
	public boolean insertIntoChildren(Point point) {		
		
		Iterator<QuadTreeMemory> iterator = this.children.iterator();
		while(iterator.hasNext()) {
			QuadTree quadTree = iterator.next();
			if(quadTree.insert(point))
				return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see quadtree.Quad#size()
	 */
	public int size() {
		
		int size = 0;

		if(this.hasChildren()) {

			List<QuadTree> leaves = this.leaves();	
			Iterator<QuadTree> iq = leaves.iterator();
			while(iq.hasNext())
				size += iq.next().size();

		} else	{
			size = this.points.size();
		}

		return size;			
	}

	/* (non-Javadoc)
	 * @see quadtree.Quad#hasChildren()
	 */
	public boolean hasChildren() {		
		return this.children != null;
	}

	
	
	/* (non-Javadoc)
	 * @see quadtree.Quad#leaves()
	 */
	public List<QuadTree> leaves() {
		List<QuadTree> sub = new ArrayList<QuadTree>();
	
		if (!this.hasChildren()) {
			sub.add(this);
			
		} else {
					
			Iterator<QuadTreeMemory> iterator = this.children.iterator();
			
			while(iterator.hasNext()) {
				QuadTree quadTree = iterator.next();
				sub.addAll(quadTree.leaves());
			}
		}
		
		return sub;
	}
	
	/* (non-Javadoc)
	 * @see quadtree.Quad#toString()
	 */
	@Override
	public String toString() {
		String str = new String();		
		str += "number of data point : " + size() + "\n";
		str += "number of leaves of qaudtree : " + leaves().size() + "\n";
		str += "boundary : " + boundary();
		str += "Is children null? : " + (this.children == null) + "\n";; 
		
		return str;		
	}




}