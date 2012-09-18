package quadtree;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.*;





public class QuadTreeMemoryTest {

		
	
	
	@Test
	public void connectTree() {
		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		QuadTreeMemory quadTree =  new QuadTreeMemory(2, boundary);
		
		QuadTreeMemory subQuadTree = new QuadTreeMemory(2, boundary);
		
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
		QuadTreeMemory quadTree =  new QuadTreeMemory(2, boundary);
		
		assertFalse(quadTree.hasChildren());
		
		quadTree.insert(new Point(1, 2));
		quadTree.insert(new Point(10, 10));		
	
		quadTree.insert(new Point(15, 15));
		quadTree.insert(new Point(24, 24));
		
		assertEquals(10, quadTree.leaves().size());
		
		assertEquals(4, quadTree.size());		
		assertEquals(4, quadTree.children.get(0).size());
	}
	

	@Test
	public void bulidQuadTree() {		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		QuadTree quadTree =  new QuadTreeMemory(3, boundary);		
		
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
			
			String[] strings = line.split(" ");
			
			double x = Double.parseDouble(strings[0]);
			double y = Double.parseDouble(strings[1]);
			
			Point point = new Point(x, y);
			quadTree.insert(point);
			
			count++;
			assertEquals(count, quadTree.size());
			
		}
	}
	
	@Test
	public void buildQuadTreeWithFile() {
		
		String fileName = "src/test/resources/sample2D-quad.txt";
		QuadTree quadTree = new QuadTreeMemory(10, new Boundary(new Range(0, 100), new Range(0, 100)));
		
		int count = 0;
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			String line;
			try {
				while((line = in.readLine()) != null) {
					String[] strings = line.split(" ");
					
					double x = Double.parseDouble(strings[0]);
					double y = Double.parseDouble(strings[1]);

					Point point = new Point(x, y);
					
					quadTree.insert(point);					
					count++;					
					assertEquals(count, quadTree.size());					
				}
				
				assertEquals(count,quadTree.size());
				
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.err.println(fileName + "is not existed in " +
					System.getProperty("user.dir"));
			e.printStackTrace();
		} 
	}
	
	@Test
	public void index() {
		Point point = new Point(20, 20);
		Boundary boundary = 
			new Boundary(new Range(0, 100), new Range(0, 100));
		
		QuadTreeMemory quadTree = new QuadTreeMemory(10, boundary);
		assertEquals("", quadTree.getindex(point));
		
		quadTree = QuadTreeMemory.getQuadtree(2, boundary, 1);
				
		assertEquals("1", quadTree.getindex(point));
		
	}
	
	@Test
	public void quadtreeWithDepth() {
				
		Boundary boundary = 
			new Boundary(new Range(0, 100), new Range(0, 100));
		
		QuadTreeMemory quadTree = new QuadTreeMemory(2, boundary);		
		assertEquals(1, quadTree.leaves().size());
		
		quadTree = QuadTreeMemory.getQuadtree(2, boundary, 1);
		assertEquals(4, quadTree.leaves().size());
		
		quadTree = QuadTreeMemory.getQuadtree(2, boundary, 2);
		assertEquals(16, quadTree.leaves().size());
	}
}