package quadtree;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import quadtree.file.Node;

public class QuadTreeFileTest {

	@Test
	public void buildQuadTreeWithFile() {
		
		String fileName = "src/test/reso" +
				"urces/sample2D-quad.txt";
		QuadTree quadTree = new Node(10, 
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
		
		int pointSizes = 0;
		
		
		List<QuadTree> leaves = quadTree.leaves();
		
		
		for(QuadTree leaf : leaves) {
			
			Iterator<Point> points = leaf.values();
			
			while (points.hasNext()) {
				points.next();
				
				pointSizes++;
			}			
		}
		
		assertEquals(count, pointSizes);
		
		Node.delete("Q");
	}
	
	@Test 
	public void deleteQuadtreeFile() {
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));
		Node quadTree =  new Node(3, boundary, "Q");
		
		quadTree.insert(new Point(10, 10));
		
		Node.delete(quadTree.name());
		
//		File dir = new File(".");
//		FileFilter fileFilter = new WildcardFileFilter("Q*");
//		File[] files = dir.listFiles(fileFilter);	
//		assertEquals(0, files.length);
	}
}
