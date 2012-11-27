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
	public void splitBoundary1() {
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
	public void splitBoundary2() {
		
		Range[] ranges = new Range[8];
		
		for(int i=0; i<8 ; i++) {
			ranges[i] = new Range(0, 100);
		}
		
		Boundary boundary = new Boundary(ranges);
		
		List<Boundary> boundaries = boundary.split();
		
		assertEquals(256, boundaries.size());
		assertEquals(8, boundaries.get(0).dimension());
		
	}
	
	@Test
	public void compareBoundary () {
		assertEquals(new Range(0, 50), new Range(0, 50));
		 
		assertEquals(new Boundary(new Range(0, 50), new Range(0, 50)),
				new Boundary(new Range(0, 50), new Range(0, 50)));
	}
	 
	@Test
	public void checkOverlap() {
		Boundary a = new Boundary(new Range(0, 10), new Range(0, 10));
		Boundary b = new Boundary(new Range(5, 15), new Range(5, 15));
		Boundary c = new Boundary(new Range(11, 11), new Range(21, 21));
		
		assertTrue(a.overlab(b));
		assertFalse(a.overlab(c));
		
	}
	
	@Test
	public void cover() {
		Boundary a = new Boundary(new Range(0, 30), new Range(0, 30));
		Boundary b = new Boundary(new Range(10, 20), new Range(10, 20));
		Boundary c = new Boundary(new Range(20, 40), new Range(20, 40));
		
		assertTrue(a.cover(b));
		assertFalse(a.cover(c));
		
	}

}
