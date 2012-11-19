package index.quadtree;

import static org.junit.Assert.*;
import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Test;



public class DiskQuadtreeTest {

	
	

	
	@Test 
	public void bulidandVerify() throws FileNotFoundException {

		Boundary boundary = new Boundary(new Range(0, 50), new Range(0, 50));
		QuadTreeFile quadtree =  new QuadTreeFile(4, boundary, "Q");

		
				
		Iterator<Point> points = Arrays.asList(
				new Point(10, 10),
				new Point(1, 1),
				new Point(30, 30),
				new Point(20, 20),
				new Point(30, 15)).iterator();


		//build a quadTree
		while(points.hasNext()) {
			Point point = points.next();
			quadtree.insert(point);
		}		
		
		assertEquals(4, quadtree.leaves().size());
		
		quadtree.save();
		
		
		//reload a quadtree
		quadtree = QuadTreeFile.load("Q");
		
		//convert Iterator of values to ArrayList
		Iterator<Point> iPoints = quadtree.points();
		List<Point> lQuadPoints = new ArrayList<Point>();
		while(iPoints.hasNext()) {			
			lQuadPoints.add(iPoints.next());			
		}

		//Verify
		assertEquals("Q", quadtree.name());
		assertTrue(lQuadPoints.contains(new Point(10, 10)));
		assertTrue(lQuadPoints.contains(new Point(1, 1)));
		assertTrue(lQuadPoints.contains(new Point(30, 30)));

		assertEquals(4, quadtree.leaves().size());
		
	}
	

	@After
	public void delete() {
		QuadTreeFile.delete("Q");
	}
}
	