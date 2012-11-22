package mapreduce.example;

import index.quadtree.QuadTreeFile;
import index.quadtree.QuadtreeBuilder;


import mapreduce.example.quadtree.QuadTreeDriverWithSample;
import mapreduce.example.quadtree.QuadtreeDriverWithoutSample;

import org.junit.Test;
import static org.junit.Assert.fail;

public class QuadtreeBuilderTest {


	@Test
	public void testGridMapReduce () {
		try {
			QuadtreeBuilder.main(new String[]{"src/test/resources/sample2D.txt"});
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}finally {
			QuadTreeFile.delete("Q");
		}

		
	}
	//	

	//	 @Test
	//	 public void testGridMapReduce () {
	//		 try {
	//			 QuadtreeDriverWithoutSample.main(new String[]{"src/test/resources/sample2D-quad.txt ."});
	//		} catch (Exception e) {
	//			fail(e.getLocalizedMessage());
	//		}
	//	 }
	//	 
	//	 @Test
	//	 public void testQuadMapReduce () {
	//		 try {
	//			 QuadtreeDriverWithoutSample.main(new String[]{"src/test/resources/sample2D-quad.txt ."});
	//		} catch (Exception e) {
	//			fail(e.getLocalizedMessage());
	//		}
	//	 }
	//	 

}
