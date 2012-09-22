package quadtree;

import java.util.Iterator;
import java.util.List;

public interface QuadTree {

	public abstract Boundary boundary();

	public abstract int dimension();

	public abstract void split();

	/**
	 * insert a point into the QuadTree
	 * @param point item to be inserted
	 * @return if point is inserted, true
	 */
	public abstract boolean insert(Point point);

	//insert point into sub-quadtree
	public abstract boolean insertIntoChildren(Point point);

	public abstract int size();

	public abstract List<QuadTree> leaves();

	public abstract String toString();
	
	public abstract String name();

	public abstract boolean isLeaf();

	public abstract Iterator<Point> values();
	
	
}