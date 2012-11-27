package index.quadtree;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class QuadtreeSearcherTest {

	@Test
	public void search() {
		String filename = "src/test/resources/sample2D.txt";

		String[] args = new String[] {filename};
		
		QuadtreeBuilder.main(args);		
		QuadtreeSearcher.main(new String[] {filename});
		
	}
	
	
	private QuadTreeFile quadtree = null;
	private String filename = "Q";
	

	
	
	private QuadTreeFile build() {
		
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
		quadtree =  new QuadTreeFile(2, boundary, filename);
				
		Iterator<Point> ip = points.iterator();

		//build a quadTree
		while(ip.hasNext()) {
			Point point = ip.next();
			quadtree.insert(point);
		}		
		
		return quadtree;
	}
	

	
	@Test
	public void search1() {
		
		//reload a quadtree
		QuadTreeFile quadtree = build();

		Point validPoint = new Point(10, 10);
		Point invalidPoint = new Point(11, 11);
		
		QuadtreeSearcher.NormalSearcher ns = new QuadtreeSearcher.NormalSearcher(quadtree);		
		assertEquals(validPoint, ns.searchPoint(validPoint));
		assertThat(invalidPoint, not(ns.searchPoint(invalidPoint)));

	}
	
	@Test 
	public void infos() {
		
		QuadTreeFile quadtree = build();
		
		QuadtreeSearcher.SpecialSearcher ss = new QuadtreeSearcher.SpecialSearcher(quadtree);
		
		ss.refresh();
		ss.refresh();
		
		QuadTreeFile node = ss.infoManager.findNode(new Point(30, 30));
		assertThat("Q", not(node.name()));
	
		ss.searchPoint(new Point(30, 30));
	
	}
	
	@Test 
	public void findDepthestNode() {
		
		System.out.println("<S>find depthest Node test");
		
		QuadTreeFile root = build();
		QuadtreeSearcher.SpecialSearcher ss = new QuadtreeSearcher.SpecialSearcher(root);
		QuadTreeFile deepNode = ss.infoManager.findDepthestNode(root);
		
		
		
		assertEquals(1, ss.infoManager.nodes.size());
		
		deepNode = ss.infoManager.findDepthestNode(root);

		ss.refresh();
		assertEquals(root.findMiddle(deepNode), ss.infoManager.nodes.get(1)); 
		assertEquals(2, ss.infoManager.nodes.size());
		
		
		Iterator<QuadTreeFile> iq = ss.infoManager.nodes.iterator();		
		while(iq.hasNext()) {
			System.out.println(iq.next().name());
		}
		
		System.out.println(deepNode.name());
		System.out.println("<E>find depthest Node test ");
//		deepNode = ss.infoManager.findDepthestNode(root);
//			
//		ss.infoManager.refresh();
//		
//		deepNode = ss.infoManager.findDepthestNode(root);
//		System.out.println(deepNode);
//		
//		ss.infoManager.refresh();
	}
	
	@Test
	public void findMiddleNode() {
		
		QuadTreeFile root = build();
		QuadtreeSearcher.SpecialSearcher ss = new QuadtreeSearcher.SpecialSearcher(root);
		
		QuadTreeFile leaf = ss.searchLeafNode(new Point(30, 30));
		
		String middleName = leaf.name().substring(0, leaf.name().length()/2 +1 );
		
		
		QuadTreeFile middle = root.findMiddle(leaf);

		System.out.println(leaf.name());
		System.out.println(middle.name());
		assertEquals(middleName, middle.name());

	}
	
}
