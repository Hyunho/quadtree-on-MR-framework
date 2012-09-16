package quadtree;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.*;





public class QuadTreeTest {

		
	@Test 
	public void boundary()	{		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		assertTrue(boundary.containsPoint(new Point(10,10)));
		assertFalse(boundary.containsPoint(new Point(100,100)));
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
		
		
	}
	
	@Test
	public void compareBoundary () {
		assertEquals(new Range(0, 50), new Range(0, 50));
		 
		assertEquals(new Boundary(new Range(0, 50), new Range(0, 50)),
				new Boundary(new Range(0, 50), new Range(0, 50)));
	}
	 
	
	@Test
	public void connectTree() {
		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		QuadTree quadTree =  new QuadTree(2, boundary);
		
		QuadTree subQuadTree = new QuadTree(2, boundary);
		
		assertTrue(subQuadTree.insert(new Point(10,10)));
		
		quadTree.split();
		assertEquals(0, quadTree.size());
		assertEquals(1, subQuadTree.size());
		quadTree.children.set(0 ,subQuadTree);
		assertEquals(1, quadTree.size());
	}
	
	
	@Test	
	public void splitQuadTree() {		
		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		QuadTree quadTree =  new QuadTree(2, boundary);
		
		assertFalse(quadTree.hasChildren());
		
		quadTree.insert(new Point(10, 10));
		quadTree.insert(new Point(10, 10));
		quadTree.split();
		
		assertTrue(quadTree.hasChildren());
		assertEquals(4, quadTree.children.size());
		
		quadTree.insertIntoChildren(new Point(90, 90));
		
		assertEquals(3, quadTree.size());		
		assertEquals(2, quadTree.children.get(0).size());
	}
	

	@Test
	public void bulidQuadTree() {		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		QuadTree quadTree =  new QuadTree(2, boundary);		
		
		quadTree.insert(new Point(10,10));
		assertEquals(1, quadTree.size());
		
		quadTree.insert(new Point(10,10));
		assertEquals(2, quadTree.size());
		
		quadTree.insert(new Point(90,90));
		assertTrue(quadTree.hasChildren());
		assertEquals(3, quadTree.size());
		
	}
}
