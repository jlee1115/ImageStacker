import java.applet.Applet;
import java.io.*;
import java.util.*;



public class ImageStacker extends Applet {
	//this is an applet because for some reason reading and opening files in a static main method is a lot harder
	public static noisy[] blurry;
	
	public void init() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the image you want:");
		//currently only has carina and zwicky, but more can be imported
		String input = sc.next();
		blurry = new noisy[10];
		for (int i = 0; i < blurry.length; i++) {
			String s;
			//This determines which file to choose
			if (i >= 9) s = "" + input + "/" + input + "_0" + (i+1) + ".ppm"; 
			else s = "" + input + "/" + input + "_00" + (i+1) + ".ppm";
			//readImage fills a noisy with all of the rgb values
			blurry[i] = readImage(s, blurry[i]);
		}
		noisy toPrint = new noisy(blurry[0].getRows(),blurry[0].getCols());
		for (int i = 0; i < toPrint.getCols(); i++) {
			for (int j = 0; j < toPrint.getRows(); j++) {
				//this averages all of the rgb values given
				double red = 0;
				double green = 0;
				double blue = 0;
				for (int k = 0; k < blurry.length; k++) {
					red += blurry[k].getRed(i, j);
					green += blurry[k].getGreen(i, j);
					blue += blurry[k].getBlue(i, j);
				}
				red /= blurry.length;
				green /= blurry.length;
				blue /= blurry.length;
				toPrint.addPoint((int)red, (int)green, (int)blue, j, i);
			}
		}
		try {
			writeImage(toPrint);
		} catch (IOException e) {
			System.out.println("not found");
			e.printStackTrace();
		}
	}

	public class noisy {
		int[][] red;
		int[][] blue;
		int[][] green;
		int rows;
		int cols;
		public noisy (int row, int col) {
			rows = row;
			cols = col;
			red = new int[cols][rows];
			blue = new int[cols][rows];
			green = new int[cols][rows];
		}
		public int getRows() {
			return rows;
		}
		public int getCols() {
			return cols;
		}
		public void addPoint(int r, int b, int g, int row, int col) {
			red[col][row] = r;
			blue[col][row] = b;
			green[col][row] = g;
		}
		public int getRed(int col, int row) {
			return red[col][row];
		}
		public int getGreen(int col, int row) {
			return green[col][row];
		}
		public int getBlue(int col, int row) {
			return blue[col][row];
		}
	}
	

	
	public noisy readImage(String fileName, noisy n) {
		try{
			Scanner scanner = 
		               new Scanner(new BufferedReader(new FileReader(fileName)));
			scanner.nextLine();
			int cols = Integer.parseInt(scanner.next());
			int rows = Integer.parseInt(scanner.next());
			n = new noisy (rows, cols);
			scanner.nextLine();
			scanner.nextLine();
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int r = scanner.nextInt();
					int b = scanner.nextInt();
					int g = scanner.nextInt();
					n.addPoint(r, b, g, i, j);
				}
			}
			scanner.close();
		}	
			
			catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
		
		return n;
	}
	
	
	public static void writeImage(noisy n) throws IOException
	{
		//MODIFY for where you want file to be saved and named
		String fName = "beautiful.ppm";
		String fPath = "C:/Users/Minnie/Desktop/Results/";
		File theFile = new File(fPath + fName);
		if (!theFile.exists()) {
			theFile.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(theFile.getAbsoluteFile()));
		bw.write("P3");
		bw.newLine();
		bw.write(n.getCols() + " " + n.getRows() + " 255");
		bw.newLine();
		int rows = 0, cols = 0;
		while (true)
		{
			int redVal = n.getRed(cols, rows);
			int greenVal = n.getGreen(cols, rows);
			int blueVal = n.getBlue(cols, rows);
			bw.write(""+redVal + " " + greenVal + " " + blueVal);
			bw.newLine();
			cols++;
			if (cols >= n.getCols())
			{
				rows++;
				cols = 0;
			}
			if (rows >= n.getRows()) break;
		}
		System.out.println("Complete");
		System.out.println("File is saved in: " + fPath + " as " + fName);
		bw.close();
	}
	

}
