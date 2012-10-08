package quadtree;

import static org.junit.Assert.*;
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


public class QuadTreeFileTest {

	@Test
	public void buildQuadTreeWithFile() {
		
		// line of sample file is 100. And 2-Dimension,;
		String fileName = "src/test/reso" +
				"urces/sample2D-quad.txt";
		QuadTreeFile quadTree = new QuadTreeFile(50, 
				new Boundary(new Range(0, 100), new Range(0, 100)),
				"Q"
				);
		
		
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
				
				
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.err.println(fileName + "is not existed in " +
					System.getProperty("user.dir"));
			e.printStackTrace();
		}
		
		quadTree.save();

		
		quadTree = QuadTreeFile.load("Q");
		
		int pointSizes = 0;
		int numLeaf = 0;
		
		
		List<QuadTree> leaves = quadTree.leaves();
		
		
		for(QuadTree leaf : leaves) {
			
			numLeaf++;
			Iterator<Point> points = leaf.points();
			
			while (points.hasNext()) {
				points.next();
				
				pointSizes++;
			}				
		}
		
		assertEquals(100, pointSizes);		
		assertEquals(4, numLeaf);
	}
	
	

	
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
	