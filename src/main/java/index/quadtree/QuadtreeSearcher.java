package index.quadtree;

import index.quadtree.QuadTreeFile.PointIterator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


/**
 * for support quadtree function
 * @author hyunho
 *
 */
public class QuadtreeSearcher {	
		
	public interface Searcher {
		
		/**
		 * find a point
		 * @param quadtree
		 * @param queryPoint point to be found
		 * @return point 
		 */
		public Point searchPoint(Point queryPoint);
		
		public QuadTreeFile searchLeafNode(Point queryPoint);
	}
	public static class NormalSearcher implements Searcher {
		private final QuadTreeFile quadtree;
		
		public NormalSearcher(QuadTreeFile quadtree) {
			this.quadtree = quadtree;
		}
			
		
		public Point searchPoint(Point queryPoint) {
			QuadTreeFile leafNode = this.searchLeafNode(queryPoint);
					
			// open a file of leaf and find point
			PointIterator pi = leafNode.points();
			
			while(pi.hasNext()) {
				Point point = pi.next();
				if(point.equals(queryPoint)) {
					pi.close();
					return point;
				}
			}
			return null;
		}
		
		
		public QuadTreeFile searchLeafNode(Point queryPoint) {

			QuadTreeFile leafNode = this.quadtree;

			// find a leaf node whose has query point
			while(!leafNode.isLeaf()) {

				Iterator<QuadTreeFile> qi = leafNode.children();			
				while(qi.hasNext()) {
					QuadTreeFile q = qi.next();

					//add to test
					try {
						Thread.sleep(5, 0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(q.boundary().containsPoint(queryPoint)) {
						leafNode = q;
						break;
					}
				}			
			}

			return leafNode;
		}
	}
	
	public static class SpecialSearcher implements Searcher{
		
	
		
		public static class InfoManager {
			
			public List<QuadTreeFile> nodes  = new ArrayList<QuadTreeFile>();
			
			
			
		
			public InfoManager(QuadTreeFile root) {
	
				nodes.add(root);
			}
			
			public QuadTreeFile findNode(Point point) {
				
				QuadTreeFile properNode = nodes.get(0);
				int size = nodes.size();
				
				for(int i=1; i< size; i++){
					QuadTreeFile node = nodes.get(i);
					if(node.boundary().containsPoint(point)) {
						if(properNode.name().length() < node.name().length())
							properNode = node;
					}
				}
						

				return properNode;
			}
			
			
			public QuadTreeFile findDepthestNode(QuadTreeFile root) {

				
				QuadTreeFile deepNode = root;
				ArrayList<QuadTreeFile> queue = new ArrayList<QuadTreeFile>();
				queue.add(root);
				
				
				while(queue.size() > 0) {
					QuadTreeFile node = queue.get(0);
					queue.remove(0);
					
					if(deepNode.name().length() < node.name().length()) {
						deepNode = node;
					}
					
					if(!node.isLeaf())
					{
						Iterator<QuadTreeFile> iq = node.children();
						while(iq.hasNext()) {
							QuadTreeFile already = iq.next();
							if(!this.has(already))
								queue.add(already);
						}
					}
				}
				
				return deepNode;
			}

			/**
			 * Check given node is saved in infos.
			 * @param node
			 * @return
			 */
			private boolean has(QuadTreeFile node) {
				
				Iterator<QuadTreeFile> ii = this.nodes.iterator();
				while(ii.hasNext()) {
					QuadTreeFile thisnode = ii.next();
					if(node.name().equals(thisnode.name()))
						return true;
				}
				return false;
			}

			public void refresh() {
				
				int size = nodes.size();
				QuadTreeFile foundNode = null;
				QuadTreeFile middleNode = null;
				for(int i=0; i< size ; i++) {
					
					QuadTreeFile root = nodes.get(i);
					
					QuadTreeFile deepNode = this.findDepthestNode(root);
					
					if(foundNode == null) {
						foundNode = deepNode;
						middleNode = root.findMiddle(deepNode);
					}	
					else {
						if(foundNode.name().length() < deepNode.name().length()) {
							foundNode = deepNode;
							middleNode = root.findMiddle(deepNode);
						}
					}
				}
				nodes.add(middleNode);
			}
		}
		
		/**
		 * how many query this class process
		 */
		int queryCount = 0;
		
		public InfoManager infoManager = null;
		public SpecialSearcher(QuadTreeFile quadtree) {
			infoManager = new InfoManager(quadtree);
			
		}
		public void refresh() {
			infoManager.refresh();
		}
		
		@Override
		public Point searchPoint(Point queryPoint) {
			
			QuadTreeFile leaf = this.searchLeafNode(queryPoint);
			Point answer = new NormalSearcher(leaf).searchPoint(queryPoint);
			
			return answer;
		}

		@Override
		public QuadTreeFile searchLeafNode(Point queryPoint) {

			// find a proper node and checkTime
			QuadTreeFile node = infoManager.findNode(queryPoint);

			//find a point
			QuadTreeFile leaf = new NormalSearcher(node).searchLeafNode(queryPoint);
			
			return leaf;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String fileName = args[0];
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
		
		QuadTreeFile quadtree = QuadTreeFile.load("Q");
		QuadtreeSearcher.NormalSearcher ns = new QuadtreeSearcher.NormalSearcher(quadtree);

		
		System.out.print("Start normal Search : ");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
				
		searchAll(ns, fileName);

		System.out.print("\nEnd normal Search : ");
		cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));


		
		QuadtreeSearcher.SpecialSearcher ss = new QuadtreeSearcher.SpecialSearcher(quadtree);

		for(int i=0; i< 10; i++) {
			ss.refresh();
		}

		
		System.out.print("Start special Search : ");
		cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
		searchAll(ss, fileName);
		
		
		//print nodes
		System.out.println("info size is " + ss.infoManager.nodes.size());
		
		System.out.print("\nEnd special Search : ");		
		cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
	
	}
	
	private static void searchAll(Searcher searcher, String fileName) {

		try {

			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			try {
				

				long max = Long.MIN_VALUE;
				long min = Long.MAX_VALUE;
				while((line = in.readLine()) != null) {
					
					long before = Calendar.getInstance().getTimeInMillis();
					Point point = Point.stringToPoint(line);				
					searcher.searchLeafNode(point);
					long after = Calendar.getInstance().getTimeInMillis();
					long diff = after-before;
					
					if(diff > max ) {
						max = diff;
					}
					if(diff < min)
						min = diff;
				}
				in.close();

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
