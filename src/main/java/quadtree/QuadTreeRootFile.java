package quadtree;


import java.util.Iterator;
import java.util.List;

public class QuadTreeRootFile implements QuadTree{

	private QuadTreeFileNode node;
	
	public QuadTreeRootFile(int capacity, Boundary boundary, String name) {
		this.node = new QuadTreeFileNode(capacity, boundary, name);
		
	}
	@Override
	public Boundary boundary() {
		return this.node.boundary();
	}

	@Override
	public int dimension() {
		return this.node.dimension();
	}


	@Override
	public boolean insert(Point point) {
		return this.insert(point);
	}

	@Override
	public boolean insertIntoChildren(Point point) {
		return this.node.insertIntoChildren(point);
	}

	@Override
	public boolean isLeaf() {
		return this.node.isLeaf();
	}

	@Override
	public List<QuadTree> leaves() {
		return this.node.leaves();
	}

	@Override
	public String name() {
		return this.node.name();
	}

	@Override
	public int size() {
		return this.node.size();
	}

	@Override
	public void split() {
		this.node.split();		
	}

	@Override
	public Iterator<Point> values() {
		return node.values();
	}

}
