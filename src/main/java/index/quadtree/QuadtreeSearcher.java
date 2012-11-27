package index.quadtree;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import index.quadtree.QuadTreeFile.PointIterator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import java.util.Iterator;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


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
					
			leafNode.name();
			
			FileSystem fs = null;
			try {
				Configuration conf = new Configuration();
				fs = FileSystem.get(conf);
				DataInputStream dfs = new DataInputStream(fs.open(new Path("input/sample10.txt")));
				BufferedReader reader = new BufferedReader(new InputStreamReader(dfs));
				
				String line;
				
				while ((line = reader.readLine()) != null){
				
					Point point = Point.stringToPoint(line);
					if(queryPoint.equals(point)) {
						reader.close();
						return point;
					}
												
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					if(q.boundary().containsPoint(queryPoint)) {
						leafNode = q;
						break;
					}
				}			
			}

			return leafNode;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// load global quadtree
		QuadTreeFile global = null;
		FileSystem fs = null;
		try {
			Configuration conf = new Configuration();
			fs = FileSystem.get(conf);
			FSDataInputStream fis=  fs.open(new Path("output/sample10/global.dat"));
			ObjectInputStream os = new ObjectInputStream(fis);
			global = (QuadTreeFile)os.readObject();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// query
		try {
			DataInputStream dfs = new DataInputStream(fs.open(new Path("input/sample10.txt")));
			BufferedReader reader = new BufferedReader(new InputStreamReader(dfs));
			
			String line;
			
			while ((line = reader.readLine()) != null){
				
				QuadtreeSearcher.NormalSearcher ns = new QuadtreeSearcher.NormalSearcher(global);

				Point question = Point.stringToPoint(line);
				
//				System.out.println(ns.searchLeafNode(question));
				Point answer = ns.searchPoint(question);
				
				try {
					System.out.println(answer);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				assertThat(null, not(answer));						
			}
			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
