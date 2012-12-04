package index.quadtree;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import org.junit.After;
import org.junit.Test;


public class QuadtreeTest {
	
	/**
	 * we assume there are no same points.
	 */
//	@Test 
	public void inputSamePoint() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		QuadTreeFile quadTree =  new QuadTreeFile(3, boundary, "Q");
		
		assertTrue(quadTree.insert(new Point(10.5454, 10.5254)));
		assertTrue(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		
		assertEquals(1, quadTree.descendant().size());
	}

	

	@Test	
	public void splitQuadTree() {		
		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		QuadTreeFile quadTree =  new QuadTreeFile(3, boundary, "Q");
		
		
		quadTree.insert(new Point(1, 2));
		quadTree.insert(new Point(10, 10));		
	
		quadTree.insert(new Point(15, 15));
		quadTree.insert(new Point(24, 24));
		
		assertEquals(10, quadTree.leaves().size());
		
		assertEquals(4, quadTree.size());		
		assertEquals(4, quadTree.children.get(0).size());
	}
	

	@Test
	public void build() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));	
		
		QuadTree quadTreeFile =  new QuadTreeFile(3, boundary, "Q");		
		bulidQuadTreeTest(quadTreeFile);
	}
	
	
	
	
	private void bulidQuadTreeTest(QuadTree quadTree ) {
		
		List<String> str = Arrays.asList(
				"34.9198960931972 9.3901681713760", 
				"35.271448479034 66.4713265141472",
				"87.9154678201303 28.4757010638714",
				"26.5008917078376 58.3467065123841",
				"43.6861026799306 82.5742253800854",
				"71.0774731822312 28.0028196051717",
				"21.2149743456393 99.315042141825",
				"94.2635245388374 95.3292045276612",
		"85.5743957217783 60.1300568319857");

		Iterator<String> is = str.iterator();
		int count = 0; 

		while(is.hasNext()) {
			String line = is.next();
			Point point = new Point(line.split(" "));
			quadTree.insert(point);

			count++;
			assertEquals(count, quadTree.size());

		}
	}
	
	


	@Test
	public void index() {
		Point point = new Point(80, 80);
		
		Boundary boundary = 
			new Boundary(new Range(0, 100), new Range(0, 100));
		
		QuadTreeFile quadTree = new QuadTreeFile(10, boundary, "Q");
		
		assertEquals("Q", quadTree.getindex(point));
		
		quadTree = QuadTreeFile.makeQuadtree(2, boundary, 1);
				
		assertEquals("Q4", quadTree.getindex(point));
		
	}
	
	@Test
	public void quadtreeWithDepth() {
				
		Boundary boundary = 
			new Boundary(new Range(0, 100), new Range(0, 100));
		
		QuadTreeFile quadTree = new QuadTreeFile(2, boundary, "Q");		
		assertEquals(1, quadTree.leaves().size());
		
		quadTree = QuadTreeFile.makeQuadtree(2, boundary, 1);
		assertEquals(4, quadTree.leaves().size());
		
		quadTree = QuadTreeFile.makeQuadtree(2, boundary, 2);
		assertEquals(16, quadTree.leaves().size());
	}
	
	@After
	public void deleteFiles() {
		QuadTreeFile.delete("Q");

	}
	
	
}
