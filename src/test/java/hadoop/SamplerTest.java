package hadoop;

import static org.junit.Assert.*;
import hadoop.Sampler;
import index.quadtree.Point;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class SamplerTest {

	
	@Test
	public void resevoirSampling() throws IOException {
		int numSample = 10;
		String filename = "src/test/resources/sample2D.txt";

		Point[] points = Sampler.reservoirSampling(filename, numSample);
		
		assertEquals(numSample, points.length);
		
		assertNotNull(points[numSample-1]);
	}
	
	private String input= "src/test/resources/sample2D.txt";
	private String output = "sample.txt";
	
	@Test
	public void sampleTest(){
		
		String numSample = "50";
		
		
		String[] args =  new String[]{input , numSample, output};
		
		Sampler.main(args);
		
		File file = new File(output);
		assertTrue(file.exists());
	}	
	
	@After 
	public void delete() {
		File file = new File(output);
		file.delete();
	}
}
