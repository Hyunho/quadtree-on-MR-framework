import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import quadtree.Boundary;
import quadtree.Point;
import quadtree.QuadTree;
import quadtree.QuadTreeFile;
import quadtree.Range;


public class QuadTreeMapReduceBuilder {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = "src/test/resources/data2D-quad.txt";
		QuadTree quadTree = new QuadTreeFile(10000,
				new Boundary(new Range(0, 100), new Range(0, 100)), "Q");
				
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			int count = 0;
			String line;
			try {
				while((line = in.readLine()) != null) {
					
					
					if((count++ % 1000) == 0) {
						System.out.print(".");
					}
						
					
					String[] strings = line.split(" ");
					
					double x = Double.parseDouble(strings[0]);
					double y = Double.parseDouble(strings[1]);

					Point point = new Point(x, y);					
					quadTree.insert(point);
				}	
				
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.err.println(fileName + "is not existed in " +
					System.getProperty("user.dir"));
			e.printStackTrace();
		}
		
		//FileOutputFormat
	}
	
}
