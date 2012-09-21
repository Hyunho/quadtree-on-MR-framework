package quadtree;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public class QuadTreeFileNode implements QuadTree, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 10661782942512120L;
	private String name;
	public String name() { return this.name; }
	
	
	private int capacity;
	private Boundary boundary;

	public QuadTreeFileNode(int capacity, Boundary boundary, String name) {
		
		if (name == "")
			throw new InvalidParameterException(
					"QuadtreeFile instance must has a name for saving");
		
		this.capacity = capacity;
		this.boundary = boundary;
		this.name = name;
		
		this.save();
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
	
	public static QuadTreeFileNode load(File file) {
		try {
			FileInputStream fileStream = new FileInputStream(file);
			ObjectInputStream os = new ObjectInputStream(fileStream);
			
			return (QuadTreeFileNode)os.readObject();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	

	

	public Boundary boundary() { return this.boundary(); }
	public int dimension() { return this.dimension(); }

	private ArrayList<Point> points = new ArrayList<Point>();
	
	private String[] childenNames; 
	
	public void split() {
		
		
		List<Boundary> boundaries = this.boundary.split();	
		
		int count = 0;
		
		this.childenNames = new String[boundaries.size()];
		
		for(int i=0; i< boundaries.size(); i++ ) {

			Boundary subBoundary = boundaries.get(i);
			childenNames[i] = name + (++count); 

			new QuadTreeFileNode(this.capacity, 
					subBoundary,
					childenNames[i]
					);

		}

		//
		Iterator<Point>  iterator  = this.points.iterator();

		while(iterator.hasNext()) {
			Point point = iterator.next();
			insertIntoChildren(point);
			iterator.remove();
		}
		
		this.save();
		
	}
	
	private int size = 0;

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
		

		// if there is space in this quad tree, add the object here
		if(size() < this.capacity) {
			
			if(!this.points.contains(point)) {
				this.points.add(point);
				size++;
				this.save();
				return true;
			}			
			
			return false;
		}
		
		// Otherwise, we need to subdivide than add the point to whichever node will accept it
		if (this.isLeaf()) {
			this.split();
		}

		if (this.insertIntoChildren(point)) {
			size++;
			return true;
		}

		//otherwise. the point can not be inserted for some unknown reason
		//(which should never happen)
		return false;		
	}
	
	//insert point into sub-quadtree
	public boolean insertIntoChildren(Point point) {		

		
		File[] files = childrenFiles();		
		
		for(int i=0; i< files.length; i++ ) {
			QuadTreeFileNode qtf = QuadTreeFileNode.load(files[i]);
			if(qtf.insert(point)) 
				return true;
			
		}		
		return false;
	}

	public int size() {		
		return size;
	}



	public List<QuadTree> leaves() {
		
		List<QuadTree> sub = new ArrayList<QuadTree>();
		
		if (this.isLeaf()) {
			sub.add(this);
			
		} else {
					
			Iterator<QuadTreeFileNode> iterator = this.children();
			
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
	public List<QuadTreeFileNode> descendant() {
		
		List<QuadTreeFileNode> descendant = new ArrayList<QuadTreeFileNode>();
		
		if (!this.isLeaf()) {
					
			Iterator<QuadTreeFileNode> iterator = this.children();
			
			while(iterator.hasNext()) {
				QuadTreeFileNode quadTree = iterator.next();
				descendant.addAll(quadTree.descendant());
			}
		}		
		
		descendant.add(this);
		return descendant;
	}
	

	
	public File[] childrenFiles() {
		
		File[] childrenFiles = new File[this.childenNames.length];
		
		for(int i=0; i< childrenFiles.length; i++ ) {
			childrenFiles[i] = new File(this.childenNames[i]);
		}
		
		return childrenFiles;
		
	}
	
	public Iterator<QuadTreeFileNode> children() {					
		
		return new QuadIterator(childrenFiles());
	}
	
	class QuadIterator implements Iterator<QuadTreeFileNode> {

		private File[] files;		
		int i=0;

		QuadIterator(File[] files) {
			this.files = files;
		}
		@Override
		public boolean hasNext() {

			return i < files.length;
		}

		@Override
		public QuadTreeFileNode next() {

			if(this.hasNext()) {
				QuadTreeFileNode qtf = QuadTreeFileNode.load(files[i]);
				i++;
				return qtf;	
			}
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
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
	 * delete a quadtree which has given name including descendant"
	 * @param name
	 */
	public static void delete(String name) {		
		
		File dir = new File(name);
		
		QuadTreeFileNode root = QuadTreeFileNode.load(dir);
		
		List<QuadTreeFileNode> descendant = root.descendant();
		 
		Iterator<QuadTreeFileNode> iq = descendant.iterator();
		
		while(iq.hasNext()) {
			QuadTreeFileNode node = iq.next();
			File deleteFile = new File(node.name());
			deleteFile.delete();
		}
	}

	/**
	 * return true, if this quadtree has no children. 
	 * @return
	 */
	public boolean isLeaf() {
		return this.childenNames == null;
		
	}

	public ArrayList<Point> values() {
		if(isLeaf())
			return this.points;
		return new ArrayList<Point>();
	}
}