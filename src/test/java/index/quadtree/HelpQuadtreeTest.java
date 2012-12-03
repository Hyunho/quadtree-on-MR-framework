package index.quadtree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import index.quadtree.QuadtreeSearcher.NormalSearcher;

import java.util.ArrayList;
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
	
	@Test
	public void rangeQuery() {
		
		Boundary boundary = new Boundary(new Range(0, 11), new Range(0, 11));

		QuadTreeFile quadtree = HelpQuadtreeTest.build();
		
		NormalSearcher searcher = new QuadtreeSearcher.NormalSearcher(quadtree);
		
		ArrayList<Point> answers = new ArrayList<Point>();
				
		ArrayList<QuadTreeFile> nodes = searcher.searchNodes(boundary);
		
		assertThat(0, not(nodes.size()));
		for(QuadTreeFile node : nodes) {
			Iterator<Point> points = node.points();
			while(points.hasNext()) {
				Point point = points.next();
				if(boundary.containsPoint(point)) {
					answers.add(point);
				}
					
			}
		}

		assertThat(answers.size(), is(2));
	}
}


