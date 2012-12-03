package hadoop;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;


public class ConverterTest {

	String output = "src/test/resources/temp.bin";
	@Test
	public void StringToBinary() throws IOException {
		String filename = "src/test/resources/sample2D.txt";
		StringToBinaryConverter.main(new String[] { filename, output });
	}
	
	@After
	public void delete() {
		File file = new File(output);
		file.delete();
	}
}
