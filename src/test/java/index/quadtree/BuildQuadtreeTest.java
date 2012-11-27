package index.quadtree;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Test;

public class BuildQuadtreeTest {
	
	@Test 
	public void dataFromFileTest() {		
		
		try {
			
			String fileName = "src/test/resources/sample2D.txt";
			Boundary boundaray = new Boundary(new Range(0, 1000), new Range(0, 1000));

			diskQuadtreeTest(fileName, boundaray);
			memoryQuadtreeTest(fileName, boundaray);
			
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@After
	public void delete() {
		QuadTreeFile.delete("Q");
	}

	

	@Test 
	public void eightDataTest() {

		try {

			String fileName = "src/test/resources/sample8D.txt";

			Range[] ranges = new Range[8];
			for(int i=0; i< 8; i++) {
				ranges[i] = new Range(0, 1000);
			}
			
			Boundary boundaray = new Boundary(ranges);

			diskQuadtreeTest(fileName, boundaray);
			memoryQuadtreeTest(fileName, boundaray);

		} catch (Exception e) {
			fail(e.getLocalizedMessage());			
		}
	}
	
	private void diskQuadtreeTest(String fileName, Boundary boundaray) {
		
		// line of sample file is 100. And 2-Dimension,;
		
		QuadTreeFile quadTree = new QuadTreeFile(50, 
				boundaray,
				"Q"
				);
		
		
		int count = 0;
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			String line;
			try {
				while((line = in.readLine()) != null) {
					
					Point point = Point.stringToPoint(line);
										
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
	}
	

	
	private void memoryQuadtreeTest(String fileName, Boundary boundaray) {
		
		QuadTree quadTree = 
				new QuadTreeMemory(10,	boundaray,	"Q");
		
		int count = 0;
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			String line;
			try {
				while((line = in.readLine()) != null) {
					Point point = Point.stringToPoint(line);
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
	
	

}
