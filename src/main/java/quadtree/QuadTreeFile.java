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

import org.apache.commons.io.filefilter.WildcardFileFilter;

public class QuadTreeFile implements QuadTree, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 10661782942512120L;
	private String name;
	public String name() { return this.name; }
	
	
	private int capacity;
	private Boundary boundary;

	public QuadTreeFile(int capacity, Boundary boundary, String name) {
		
		if (name == "")
			throw new InvalidParameterException(
					"QuadtreeFile instance must has a name for saving");
		
		this.capacity = capacity;
		this.boundary = boundary;
		this.name = name;
		
		try {
			FileOutputStream fs = new FileOutputStream(name);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			os.writeObject(this);
		    os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public static QuadTreeFile load(File file) {
		try {
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
	
	

	

	public Boundary boundary() { return this.boundary(); }
	public int dimension() { return this.dimension(); }

	private ArrayList<Point> points = new ArrayList<Point>();
	
	public void split() {
		
		
		List<Boundary> boundaries = this.boundary.split();	
		Iterator<Boundary> ib = boundaries.iterator();		
		
		int count = 0;
		while(ib.hasNext()) {
			Boundary subBoundary = ib.next();	
			
			new QuadTreeFile(this.capacity, 
					subBoundary,
					name + (++count));
			
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
			this.checkAndSavePoint(point);
			this.save();
			return true;
		}
		
		// Otherwise, we need to subdivide than add the point to whichever node will accept it
		if (!this.hasChildren()) {
			this.split();
		}

		this.insertIntoChildren(point);

		//otherwise. the point can not be inserted for some unknown reason
		//(which should never happen)
		return false;		
	}
	
	/**
	 * check whether a point is already inside quadtree or not.
	 * if not, save a point into quadtree.
	 * @param point
	 */
	private void checkAndSavePoint(Point point) {
		
		if(!this.points.contains(point)) {
			this.points.add(point);
		}
	}

	//insert point into sub-quadtree
	public boolean insertIntoChildren(Point point) {		

		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter( this.name + "?");
		File[] files = dir.listFiles(fileFilter);		
		
		for(int i=0; i< files.length; i++ ) {
			QuadTreeFile qtf = QuadTreeFile.load(files[i]);
			if(qtf.insert(point)) 
				return true;
			
		}		
		return false;
	}

	public int size() {
		
		if(!this.hasChildren())
			return this.points.size();
		
		List<QuadTree> leaves = this.leaves();	
		
		int size = 0;

		Iterator<QuadTree> iFile = leaves.iterator();
		while(iFile.hasNext()) {
			QuadTree quad = iFile.next();			
			size += quad.size();
		}
		
		return size;			
	}

	public boolean hasChildren() {
		File dir = new File(".");		
		FileFilter fileFilter = new WildcardFileFilter( this.name + "?");
		File[] files = dir.listFiles(fileFilter);
		return (files.length != 0);
	}



	public List<QuadTree> leaves() {
		
		List<QuadTree> sub = new ArrayList<QuadTree>();
		
		if (!this.hasChildren()) {
			sub.add(this);
			
		} else {
					
			Iterator<QuadTreeFile> iterator = this.children().iterator();
			
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
		
		if (this.hasChildren()) {
					
			Iterator<QuadTreeFile> iterator = this.children().iterator();
			
			while(iterator.hasNext()) {
				QuadTreeFile quadTree = iterator.next();
				descendant.addAll(quadTree.descendant());
			}
		}		
		
		descendant.add(this);
		return descendant;
	}
	
	public List<QuadTreeFile> children() {
		
		List<QuadTreeFile> children = new ArrayList<QuadTreeFile>();
		
		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter( this.name + "?");
		File[] files = dir.listFiles(fileFilter);		
		
		for(int i=0; i< files.length; i++ ) {
			QuadTreeFile qtf = QuadTreeFile.load(files[i]);
			children.add(qtf);	
		}
		
		return children;
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
		File dir = new File(".");
		FileFilter fileFilter = new WildcardFileFilter(name +"*");
		File[] files = dir.listFiles(fileFilter);	
		
		for(int i = 0; i < files.length ; i ++ )  {
			files[i].delete(); 
		}
	}

	/**
	 * return true, if this quadtree has no children. 
	 * @return
	 */
	public boolean isLeaf() {
		return !this.hasChildren();
	}

	public ArrayList<Point> values() {
		if(isLeaf())
			return this.points;
		return null;
	}
}
	