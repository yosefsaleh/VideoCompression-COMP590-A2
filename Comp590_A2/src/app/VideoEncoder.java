package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ac.ArithmeticEncoder;
import io.OutputStreamBitSink;

public class VideoEncoder {
	public static void main(String[] args) throws IOException {
		String input_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/encoderInput.txt";
		String output_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/encoderOutput.dat";
		//String input_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/compressedvid.dat";

		//String output_file_name = "/Users/khalidsaleh/eclipse-workspace/Comp590_A2/data/encoderOutput.txt";
		
		int rangeBitWidth = 40;
		
		System.out.println("Encoding text file: " + input_file_name);
		System.out.println("Output file: " + output_file_name);
		System.out.println("Range Register Bit Width: " + rangeBitWidth);
		
		int numPixels = (int) new File(input_file_name).length();
		
		Integer[] pixels = new Integer[256];
		for (int i = 0; i < 256; i++) {
			pixels[i] = i;
		}
		FreqCountIntegerSymbolModel[] models = new FreqCountIntegerSymbolModel[256];
		
		for (int i = 0; i < 256; i++) {
			models[i] = new FreqCountIntegerSymbolModel(pixels);
		}
		ArithmeticEncoder<Integer> encoder = new ArithmeticEncoder<Integer>(rangeBitWidth);
		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);
		
		
		FileInputStream fis = new FileInputStream(input_file_name);
		
		FreqCountIntegerSymbolModel model;
		Integer[] previousFrame = new Integer[4096];
		for (int i = 0; i < 4096; i++) {
			previousFrame[i] = 0;
		}
		for (int i = 0; i < numPixels; i++) {
			model = models[previousFrame[i % 4096]];
			int next = fis.read();
			encoder.encode(next, model, bit_sink);
			model.addToCount(next);
		}
		fis.close();
		encoder.emitMiddle(bit_sink);
		bit_sink.padToWord();
		fos.close();
		
		System.out.println("Done");
	}

}
