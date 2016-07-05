import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class NoiseProcessor {

	private File source;
	private File destDir;

	public NoiseProcessor(File source, File destDir) {
		this.source = source;
		this.destDir = destDir;
	}

	public void process(int numNoiseyImages) throws IOException {
		String filename = source.getName();
		filename = filename.substring(0, filename.length() - 4);

		BufferedImage image = ImageIO.read(source);

		int height = image.getHeight();
		int width = image.getWidth();
		
		PrintWriter[] printers = new PrintWriter[numNoiseyImages];
		for (int i = 0; i < numNoiseyImages; i++) {
			String numFilename = String.format("%s_%03d.ppm", filename, (i+1));
			File destFile = new File(destDir, numFilename);
			printers[i] = new PrintWriter(destFile);
			printers[i].println("P3");
			printers[i].println(width + " " + height);
			printers[i].println("255");
		}
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color pixel = new Color(image.getRGB(x, y));
				Color[] pixels = getNoise(pixel, numNoiseyImages);
				for (int i = 0; i < numNoiseyImages; i++) {
					printers[i].println(
							pixels[i].getRed() + " " + pixels[i].getGreen()
							+ " " + pixels[i].getBlue());
				}
			}
		}
		
		for (PrintWriter p : printers) {
			p.close();
		}

	}

	private Color[] getNoise(Color pixel, int num) {
		List<Integer> red = getIntNoise(pixel.getRed(), num);
		List<Integer> green = getIntNoise(pixel.getGreen(), num);
		List<Integer> blue = getIntNoise(pixel.getBlue(), num);
		Color[] colors = new Color[num];
		
		for (int i = 0; i < num; i++) {
			colors[i] = new Color(red.get(i), green.get(i), blue.get(i));
		}
		return colors;
	}

	private List<Integer> getIntNoise(int val, int num) {
		List<Integer> values = new ArrayList<Integer>();
		int upDiff = Math.abs(255-val);
		int downDiff = val;
		int diff;
		if (downDiff < upDiff) {
			diff = downDiff;
		} else {
			diff = upDiff;
		}
		for (int i = 0; i < num; i++) {
			if (i%2 == 0) {
				values.add(val - diff);
			} else {
				values.add(val + diff);
			}
		}
		Collections.shuffle(values);
		
		return values;
	}


}
