package quadtree;
import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;


/**
 * this class is not implemented fully. only support 2-dimensions yet. 
 * @author dke
 *
 * @param <Point> item handled by quad-tree
 */
public class QuadTree{
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	
	private int capacity;
	
	private Boundary boundary;
	public Boundary boundary() { return this.boundary; }
	
	private int dimension = 2;
	
	public ArrayList<QuadTree> children;
	
	
	public QuadTree(int capacity, Boundary boundary) {
		this.capacity = capacity;
		this.boundary = boundary;
	}
	

	public void split() {
		
		this.children = new ArrayList<QuadTree>();
		
		List<Boundary> lb = this.boundary.split();
		
		Iterator<Boundary> ib = lb.iterator();		
		while(ib.hasNext()) {
			Boundary subBoundary = ib.next();			
			this.children.add(new QuadTree(this.capacity, subBoundary));
		}
		
		Iterator<Point>  iterator  = this.points.iterator();

		while(iterator.hasNext()) {
			Point point = iterator.next();
			insertIntoChildren(point);
			iterator.remove();
		}
	}
		
	/**
	 * insert a point into the QuadTree
	 * @param point item to be inserted
	 * @return if point is inserted, true
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
	public boolean insertIntoChildren(Point point) {		
		
		Iterator<QuadTree> iterator = this.children.iterator();
		while(iterator.hasNext()) {
			QuadTree quadTree = iterator.next();
			if(quadTree.insert(point))
				return true;
		}
		
		return false;
	}
	
	public int size() {
		List<QuadTree> leaves = this.leaves();
		int size = 0;
		
		Iterator<QuadTree> iq = leaves.iterator();
		while(iq.hasNext())
			size += iq.next().points.size();
		return size;			
	}

	public boolean hasChildren() {		
		return this.children != null;
	}

	public int dimension() {
		return this.dimension;
	}
	
	public List<QuadTree> leaves() {
		List<QuadTree> sub = new ArrayList<QuadTree>();
	
		if (!this.hasChildren()) {
			sub.add(this);
			
		} else {
					
			Iterator<QuadTree> iterator = this.children.iterator();
			
			while(iterator.hasNext()) {
				QuadTree quadTree = iterator.next();
				sub.addAll(quadTree.leaves());
			}
		}
		
		return sub;
	}
	
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