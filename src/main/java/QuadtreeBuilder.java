
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import quadtree.*;


public class QuadtreeBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("there is no input arg[0]");
			System.exit(-1);
		}
			
			
		String fileName = args[0];
		QuadTree quadTree = new QuadTreeFile(4,
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
	}
}