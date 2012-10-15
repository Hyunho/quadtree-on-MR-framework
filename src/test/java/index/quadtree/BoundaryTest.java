package index.quadtree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.Range;

import java.util.List;

import org.junit.Test;


public class BoundaryTest {
	
	@Test 
	public void boundary()	{		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		assertTrue(boundary.containsPoint(new Point(34.9198960931972, 9.390168171376)));
		assertFalse(boundary.containsPoint(new Point(100, 100)));
	}
	
	@Test 
	public void splitBoundary() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		
		List<Boundary> subBoundary = boundary.split();
		
		assertEquals(4, subBoundary.size());
		
		assertEquals(new Boundary(new Range(0, 50), new Range(0, 50)), 
				subBoundary.get(0));		
		assertEquals(new Boundary(new Range(50, 100), new Range(0, 50)), 
				subBoundary.get(1));
		assertEquals(new Boundary(new Range(0, 50), new Range(50, 100)), 
				subBoundary.get(2));
		assertEquals(new Boundary(new Range(50, 100), new Range(50, 100)), 
				subBoundary.get(3));
		
		boundary = new Boundary(new Range(50, 100), new Range(50, 100));
		subBoundary = boundary.split();
		
		
	}
	
	@Test
	public void compareBoundary () {
		assertEquals(new Range(0, 50), new Range(0, 50));
		 
		assertEquals(new Boundary(new Range(0, 50), new Range(0, 50)),
				new Boundary(new Range(0, 50), new Range(0, 50)));
	}
	 
	

}
