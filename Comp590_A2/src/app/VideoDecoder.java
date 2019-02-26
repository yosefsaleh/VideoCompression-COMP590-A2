package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ac.ArithmeticDecoder;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class VideoDecoder {
	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		//String input_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/compressedvid.dat";
		//String output_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/uncompressedvid.dat";
		String input_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/encoderOutput.dat";
		String output_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/decoder.txt";
		
		FileInputStream fis = new FileInputStream(input_file_name);

		InputStreamBitSource bit_source = new InputStreamBitSource(fis);
		
		Integer[] pixels = new Integer[256];
		
		for (int i = 0; i < 256; i++) {
			pixels[i] = i;
		}
		FreqCountIntegerSymbolModel[] models = new FreqCountIntegerSymbolModel[256];
		
		
		for (int i=0; i<256; i++) {
			// Create new model with default count of 1 for all symbols
			models[i] = new FreqCountIntegerSymbolModel(pixels);
		}
		int pixelLength = 4096*300;
		int rangeBitWidth = 40;
		
		ArithmeticDecoder<Integer> decoder = new ArithmeticDecoder<Integer>(rangeBitWidth);
		
		FileOutputStream fos = new FileOutputStream(output_file_name);
		FreqCountIntegerSymbolModel model;
		
		Integer[] previousFrame = new Integer[4096];
		for (int i = 0; i < 4096; i++) {
			previousFrame[i] = 0;
		}
		for (int i = 0; i < pixelLength; i++ ) {
			model = models[previousFrame[i % 4096]];
			
			int p = decoder.decode(model, bit_source);
			fos.write(p);
			model.addToCount(p);
		}
		System.out.println("Done.");
		fos.flush();
		fos.close();
		fis.close();
		
	}
}
