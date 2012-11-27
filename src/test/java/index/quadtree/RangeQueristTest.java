package index.quadtree;

import java.util.ArrayList;
import java.util.Iterator;

import index.quadtree.QuadtreeSearcher.NormalSearcher;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;


public class RangeQueristTest {
	
	
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
