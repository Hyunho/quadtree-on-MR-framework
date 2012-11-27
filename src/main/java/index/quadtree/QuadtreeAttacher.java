package index.quadtree;


import index.quadtree.QuadTreeFile.QuadtreeTraveler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class QuadtreeAttacher {

	public static QuadTreeFile attach(QuadTreeFile baseQuadtree,
			ArrayList<QuadTreeFile> localQuadtrees) {
		
		QuadtreeTraveler qt = new QuadtreeTraveler(baseQuadtree);
		
		ArrayList<QuadTreeFile> parents = qt.parentsOfleaves();		
		Iterator<QuadTreeFile> iq = parents.iterator();
		
		while(iq.hasNext()) {
			QuadTreeFile parent = iq.next();
			
			ArrayList<QuadTreeFile> children = parent.children;
			int size = children.size();
			
			for(int i=0; i< size; i++) {
				QuadTreeFile leaf = children.get(i);

				Iterator<QuadTreeFile> iLocal = localQuadtrees.iterator();
				while(iLocal.hasNext()) {
					QuadTreeFile rootOfLocal = iLocal.next();
					if(leaf.name().equals(rootOfLocal.name())) {
						children.set(i, rootOfLocal);
						iLocal.remove();
						break;
					}
				}
			}
		}
		
		return baseQuadtree;
	}
	
	public static void main(String[] args) {
		
		QuadTreeFile base;
		String root = "Q";
		//write quadtree in to HDFS
		Configuration conf = new Configuration();


		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create(root), conf);
			
			
			//read a base quadtree
			FSDataInputStream fis=  fs.open(new Path("quadtree.dat"));			
			ObjectInputStream ois = new ObjectInputStream(fis);
			base = (QuadTreeFile)ois.readObject();
			ois.close();
			fis.close();
			
			// read local quadtrees
			FileStatus[] status = fs.globStatus(new Path("Q*"));
			Path[] listedPaths = FileUtil.stat2Paths(status);			
			ArrayList<QuadTreeFile>  locals = new ArrayList<QuadTreeFile>();
			
			for(Path path : listedPaths) {
				fis=  fs.open(path);
				ois = new ObjectInputStream(fis);
				QuadTreeFile local = (QuadTreeFile)ois.readObject();
				locals.add(local);
				ois.close();
				fis.close();
			}
			
			// merge
			System.out.print("attaching " + locals.size() + "local trees");
			QuadTreeFile globalTree = QuadtreeAttacher.attach(base, locals);
			
			//write quadtree in to HDFS
			String globalName = "global.dat";
			fs = FileSystem.get(URI.create(globalName), conf);
			FSDataOutputStream os = fs.create(new Path(globalName), true);				
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(globalTree);
			oos.close();
			os.close();

			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
