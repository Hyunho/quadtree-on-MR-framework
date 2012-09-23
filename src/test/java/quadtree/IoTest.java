package quadtree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class IoTest {
	
	@Test
	public void point() {
		
		Point p = new Point(1.0001, 1.0000);
		Point q = new Point(1.1212, 145454);
		List<Point> points = new ArrayList<Point>();
		
		points.add(p);
		
		assertFalse(points.contains(q));

		
		assertNotSame(new Point(1.0001, 1.0000), new Point(1.1212, 145454));

	}

}
