package quadtree;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.After;
import org.junit.Test;

public class QuadTreeFileTest {

	
	@Test 
	public void construct() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		new QuadTreeFileNode(3, boundary, "Q");
	}

	@Test 
	public void inputSamePoint() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		QuadTreeFileNode quadTree =  new QuadTreeFileNode(3, boundary, "Q");
		
		assertTrue(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		assertFalse(quadTree.insert(new Point(10, 10)));
		
		assertEquals(1, quadTree.descendant().size());
		
	}
	@Test
	public void bulidQuadTree() {
		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));	
		
		QuadTree quadTree =  new QuadTreeFileNode(3, boundary, "Q");		

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
	public void buildQuadTreeWithFile() {
		
		String fileName = "src/test/resources/sample2D-quad.txt";
		QuadTree quadTree = new QuadTreeFileNode(10, 
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
	public void deleteQuadtreeFile() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		QuadTreeFileNode quadTree =  new QuadTreeFileNode(3, boundary, "Q");
		
		QuadTreeFileNode.delete(quadTree.name());
		
		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter("Q*");
		File[] files = dir.listFiles(fileFilter);	
		assertEquals(0, files.length);
	}
	
	@After 
	public void after() {
		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter("Q*");
		File[] files = dir.listFiles(fileFilter);	

		for(int i = 0; i < files.length ; i ++ )  {
			files[i].delete(); 
		}
	}
	
}
