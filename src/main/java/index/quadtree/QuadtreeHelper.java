package index.quadtree;

import java.util.Iterator;


/**
 * for support quadtree function
 * @author hyunho
 *
 */
public class QuadtreeHelper {	
		
	
	/**
	 * find a point
	 * @param quadtree
	 * @param queryPoint point to be found
	 * @return point 
	 */
	public static Point search(QuadTreeFile quadtree, Point queryPoint) {
		
		// find a leaf node whose has query point
		while(quadtree.isLeaf()) {
			
			Iterator<QuadTreeFile> qi = quadtree.children();			
			while(qi.hasNext()) {
				QuadTreeFile q = qi.next();
				
				if(q.boundary().containsPoint(queryPoint)) {
					quadtree = q;
					break;
				}
			}			
		}
				
		// open a file of leaf and find point
		Iterator<Point> pi = quadtree.points();
		
		while(pi.hasNext()) {
			Point point = pi.next();
			if(point.equals(queryPoint)) {
				return point;
			}
		}
		return null;
	}
}
