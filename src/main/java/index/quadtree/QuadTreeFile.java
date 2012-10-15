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

	private ArrayList<QuadTreeFile> children;
	
	public QuadTreeFile(int capacity, Boundary boundary, String name) {

		if (name == "")
			throw new InvalidParameterException(
			"QuadtreeFile instance must has a name for saving");

		this.capacity = capacity;
		this.boundary = boundary;
		this.name = name;
		
		// if there are any file related with this quadtree, delete
//		new File(name).delete();
//		new File(name + "-points").delete();		
		
		
		
	}
	





	public void split() {


		List<Boundary> boundaries = this.boundary.split();	
		Iterator<Point>  iterator  = this.points();


		int count = 0;



		this.children = new ArrayList<QuadTreeFile>();

		for(Boundary bound : boundaries) {
			this.children.add(new QuadTreeFile(
					this.capacity,
					bound,
					name + ( ++count)
			));
		}



		while(iterator.hasNext()) {
			Point point = iterator.next();
			insertIntoChildren(point);
		}
		
		new File(this.name + "-points").delete();
	}

	public Iterator<Point> points() {
		return new PointIterator(this);
	}

	public class PointIterator implements Iterator<Point> {

		private NextBufferedReader reader;		
		private Iterator<File> files;

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
					
					this.reader = new NextBufferedReader(new FileReader(file));
					line = this.reader.readLine();
				} catch (FileNotFoundException e) {
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
			PrintWriter out
			= new PrintWriter(
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

		List<QuadTree> sub = new ArrayList<QuadTree>();

		if (this.isLeaf()) {
			sub.add(this);

		} else {

			Iterator<QuadTreeFile> iterator = this.children();

			while(iterator.hasNext()) {
				QuadTree quadTree = iterator.next();
				sub.addAll(quadTree.leaves());
			}
		}		
		return sub;		
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

			return (QuadTreeFile)os.readObject();

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



	
}