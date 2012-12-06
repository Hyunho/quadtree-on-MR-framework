package index.quadtree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;


/**
 * Requirements:
 * This quadtree has to worked on disk based.
 * nodes of quadtree is on memory, and data point is saved into disk.
 * There are two kind of nodes, consisted of leaf node and non-leaf node.
 * Leaf node save data points into disk, 
 * and non-left node has pointer of other nodes to indicate.
 * All nodes have a capacity, boundary and data point which has.  
 */
public class QuadTreeFile implements QuadTree, Serializable {

	
	private static final long serialVersionUID = 10661782942512120L;
	
	private String name;
	public String name() { return this.name; }

	private int capacity;
	
	private Boundary boundary;	
	public Boundary boundary() { return this.boundary; }
	public int dimension() { return this.boundary.dimension(); }
	
	private int size = 0;
	public int size() {	return size; }

	ArrayList<QuadTreeFile> children;
	
	public QuadTreeFile(int capacity, Boundary boundary, String name) {

		if (name == "")
			throw new InvalidParameterException(
			"QuadtreeFile instance must has a name for saving");

		this.capacity = capacity;
		this.boundary = boundary;
		this.name = name;
		
	}

	public static class Spliter {
		
		public static void split(QuadTreeFile quadtree) {
			List<Boundary> boundaries = quadtree.boundary.split();	
			Iterator<Point>  iterator  = quadtree.points();

			int count = 0;

			quadtree.children = new ArrayList<QuadTreeFile>();

			for(Boundary bound : boundaries) {
				quadtree.children.add(new QuadTreeFile(
						quadtree.capacity,
						bound,
						quadtree.name + ( ++count)
				));
			}

			while(iterator.hasNext()) {
				Point point = iterator.next();
				quadtree.insertIntoChildren(point);
			}
			
			new File(quadtree.name + "-points").delete();
		}
	}
	
	public static class Traveler {
		
		public static List<QuadTree> getLeaves(QuadTreeFile quadtree) {
			
			List<QuadTree> leaves = new ArrayList<QuadTree>();			
			ArrayList<QuadTreeFile> queue = new ArrayList<QuadTreeFile>();
			queue.add(quadtree);
			
			while(!queue.isEmpty()) {
				QuadTreeFile node = queue.get(0);
				queue.remove(0);
				
				if(!node.isLeaf()) {
					queue.addAll(node.children);
					
				}else {
					leaves.add(node);
				}					
			}
			return leaves;
		}
	}
	
	
	public void split() {
		Spliter.split(this);
	}

	public PointIterator points() {
		return new PointIterator(this);
	}
	
	
	public class PointIterator implements Iterator<Point> {

		private NextBufferedReader reader;		
		private Iterator<File> files;
		
		@Override
		protected void finalize() throws Throwable {
			this.reader.close();
			super.finalize();
		}
		
		/**
		 *  after this class is used, should close for the file close issue 
		 */
		public void close() {
			try {
				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public PointIterator(QuadTreeFile quadtree){
			
			//initialize files
			List<QuadTree> leaves = quadtree.leaves();
			List<File> aFiles = new ArrayList<File>();			
			for(QuadTree leaf : leaves) {
				File file = new File(leaf.name()+"-points");
				
				if(file.exists())
					aFiles.add(file);
			}
			
			this.files = aFiles.iterator();
			
			
			try {			
				if(this.files.hasNext())
					this.reader = new NextBufferedReader(
							new FileReader(this.files.next()));
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		}
		
		@Override
		public boolean hasNext() {
			if(this.files.hasNext())
				return true;
			
			if((this.reader != null) &&
					this.reader.hasNext())
				return true;
			
			if (this.reader != null) {
				try {
					this.reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return false;
		}

		@Override
		public Point next() {
			
			String line = this.reader.readLine();
			if(line == null) {
				
				try {
					File file = this.files.next();
					
					while(!file.exists())
						file = this.files.next();
					if (this.reader != null) {
						this.reader.close();
					}
					
					this.reader = new NextBufferedReader(new FileReader(file));
					line = this.reader.readLine();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			
			Point point = new Point(line.split(" "));
			return point;

		}
		
		

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		class NextBufferedReader extends BufferedReader {
			
			@Override
			protected void finalize() throws Throwable {
				this.close();
				super.finalize();
			}
			
			private String line;

			public NextBufferedReader(Reader in) {
				super(in);
			}

			public boolean hasNext() {
				if(this.line == null)
					this.line = this.readLine();
				return line != null;
			}
			
			@Override 
			public String readLine() {
				if(this.line == null) {
					try {
						line =  super.readLine();
												
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				String result = this.line;
				this.line = null;
				
				return result;
			}
		}
	}

	private void savePointIntoFile(Point point) {

		 try {				
			 PrintWriter out = new PrintWriter(
					 new BufferedWriter(
							 new FileWriter(this.name() +"-points", true)));


			 out.write(point.toString());
			 out.println();
			 out.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 } 
	}


	/**
	 * insert a point into the QuadTree
	 * @param point item to be inserted
	 * @return if point is inserted, true
	 */
	public boolean insert(Point point) {		

		//ignore objects which do not belong in this quad tree
		if(!boundary.containsPoint(point)) {
			return false;
		}		

		this.size++;		

		if(this.isLeaf()) {
			if(size() < this.capacity) {
				this.savePointIntoFile(point);
				return true;
			} else {
				this.split();
			}
		}		

		return this.insertIntoChildren(point);
	}



	//insert point into sub-quadtree
	public boolean insertIntoChildren(Point point) {

		for(QuadTreeFile quadtree : this.children) {
			if(quadtree.insert(point))
				return true;
		}

		return false;
	}


	public List<QuadTree> leaves() {
		List<QuadTree> leaves = Traveler.getLeaves(this);
		return leaves;
	}

	/**
	 * return all descendant including self
	 * @return
	 */
	public List<QuadTreeFile> descendant() {

		List<QuadTreeFile> descendant = new ArrayList<QuadTreeFile>();

		if (!this.isLeaf()) {

			Iterator<QuadTreeFile> iterator = this.children();

			while(iterator.hasNext()) {
				QuadTreeFile quadTree = iterator.next();
				descendant.addAll(quadTree.descendant());
			}
		}		

		descendant.add(this);
		return descendant;
	}

	public Iterator<QuadTreeFile> children() {					
		return this.children.iterator();
	}

	@Override
	public String toString() {
		String str = new String();		
		str += "name : " + name() + "\n";
		str += "number of data point : " + size() + "\n";
		str += "number of leaves of qaudtree : " + leaves().size() + "\n";
		str += "boundary : " + boundary();

		return str;		
	}


	/**
	 * return true, if this quadtree has no children. 
	 * @return
	 */
	public boolean isLeaf() {
		return this.children == null;		
	}
	
	public void save() {
		try {
			FileOutputStream fs = new FileOutputStream(name);
			ObjectOutputStream os = new ObjectOutputStream(fs);

			os.writeObject(this);
		    os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public static QuadTreeFile load(String name) {

		try {
			File file =  new File(name);
			FileInputStream fileStream = new FileInputStream(file);
			ObjectInputStream os = new ObjectInputStream(fileStream);

			QuadTreeFile quadtree = (QuadTreeFile)os.readObject();
			os.close();
			return quadtree;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * delete a quadtree which has given name including descendant"
	 * @param name
	 */
	public static void delete(String name) {

		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter(name+ "*");
		File[] files = dir.listFiles(fileFilter);
		
		for (int i = 0; i < files.length; i++) {
		   files[i].delete();
		}
	}

	public String getindex(Point point) { 
		
		List<QuadTree> quadtrees = this.leaves();
		Iterator<QuadTree> iq = quadtrees.iterator();
		
		while(iq.hasNext()){
			QuadTree quad = iq.next();
			if(quad.boundary().containsPoint(point))
				return quad.name();
		}
		
		System.err.print("check this point " + point);
		return null;
	}
	
	/**
	 * find a node whose has given name
	 * @param nameOfMiddleNode
	 */
	public QuadTreeFile getNode(String nodeName) {
		Iterator<QuadTreeFile> iq =  this.descendant().iterator();
		while(iq.hasNext()) {
			QuadTreeFile node = iq.next();
			if (nodeName.equals(node.name()))
				return node;
		}
		return null;		
	}
	
	

	/**
	 * generate quadtree with given depth
	 * @param capacity
	 * @param boundary
	 * @param depth
	 * @return
	 */
	public static QuadTreeFile makeQuadtree(
			int capacity, Boundary boundary, int depth){
		
		QuadTreeFile quadTree = 
			new QuadTreeFile(capacity, boundary, "Q");
		
		for(int i=0; i < depth; i++ ) {
			List<QuadTree> leaves = quadTree.leaves();
			for(QuadTree q : leaves) 
				q.split();
		}
				
		return quadTree;
	}

	
	/**
	 * helper class to travel a quadtree
	 * @author hyunho
	 *
	 */
	public static class QuadtreeTraveler {

		private QuadTreeFile quadtree;

		public QuadtreeTraveler(QuadTreeFile quadtree) {
			this.quadtree = quadtree;
		}

		/** 
		 * find parents of leaves of a quadtree
		 */
		public ArrayList<QuadTreeFile> parentsOfleaves() {
			
			ArrayList<QuadTreeFile> parents = new ArrayList<QuadTreeFile>();
			
			QuadTreeFile root = this.quadtree;
			ArrayList<QuadTreeFile> queue = new ArrayList<QuadTreeFile>();
			queue.add(root);
			
			
			while(queue.size() > 0) {
				QuadTreeFile node = queue.get(0);
				queue.remove(0);

				Iterator<QuadTreeFile> iq = node.children();
				while(iq.hasNext()) {
					QuadTreeFile child = iq.next();

					if(child.isLeaf())  {
						parents.add(node);
						break;
					}
					else {
						queue.add(child);
					}
				}
			}
			return parents;			
		}
	}
}