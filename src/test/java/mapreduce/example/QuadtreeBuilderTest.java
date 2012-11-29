package mapreduce.example;

import index.quadtree.QuadTreeFile;
import index.quadtree.QuadtreeBuilder;

import org.junit.Test;
import static org.junit.Assert.fail;

public class QuadtreeBuilderTest {


	@Test
	public void testGridMapReduce () {
		try {
			QuadtreeBuilder.main(new String[]{"src/test/resources/sample2D.bin"});
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}finally {
			QuadTreeFile.delete("Q");
		}

		
	}
}
