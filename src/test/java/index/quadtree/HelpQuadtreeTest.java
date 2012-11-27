package index.quadtree;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class HelpQuadtreeTest {

	public static QuadTreeFile build() {
		
		String filename = "Q";
		
		List<Point> points = Arrays.asList(
				new Point(10, 10),
				new Point(1, 1),
				new Point(30, 30),
				new Point(31, 31),
				new Point(32, 32),
				new Point(33, 33),
				new Point(20, 20),
				new Point(30, 15));
		
		
		Boundary boundary = new Boundary(new Range(0, 50), new Range(0, 50));
		QuadTreeFile quadtree =  new QuadTreeFile(2, boundary, filename);
				
		Iterator<Point> ip = points.iterator();

		//build a quadTree
		while(ip.hasNext()) {
			Point point = ip.next();
			quadtree.insert(point);
		}		
		
		return quadtree;
	}
}


