package index.quadtree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


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
		buildingWithSingleMachine(fileName);

	}

	private static void buildingWithSingleMachine(String fileName) {

		QuadTreeFile quadTree = new QuadTreeFile(10000,
				new Boundary(new Range(0, 1000), new Range(0, 1000)), "Q");

		try {

			BufferedReader in = new BufferedReader(new FileReader(fileName));
			int count = 0;
			String line;
			try {
				while((line = in.readLine()) != null) {

					Point point = Point.stringToPoint(line);

					quadTree.insert(point);
				}	

			} catch (IOException e) {				
				e.printStackTrace();
			}
			
			quadTree.save();
			
		} catch (FileNotFoundException e) {
			System.err.println(fileName + "is not existed in " +
					System.getProperty("user.dir"));
			e.printStackTrace();
		}
	}
}