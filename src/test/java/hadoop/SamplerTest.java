package hadoop;

import static org.junit.Assert.*;
import hadoop.Sampler;
import index.quadtree.Point;

import java.io.IOException;
import org.junit.Test;

public class SamplerTest {

	
	@Test
	public void resevoirSampling() throws IOException {
		int numSample = 10;
		String filename = "src/test/resources/sample2D.bin";

		Point[] points = Sampler.reservoirSampling(filename, numSample, 2);

		assertEquals(numSample, points.length);
		assertNotNull(points[numSample-1]);

	}
}
