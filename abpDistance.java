import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.*;
import ij.plugin.filter.PlugInFilter;
import ij.measure.ResultsTable;

import java.awt.*;

public class ABP_Distance implements PlugInFilter {
	private int lLength, rLength;
	private ImagePlus imp;
	private String xInput, yInput;
	private int[] xArray, yArray;
	private boolean bEnt, bArr;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {
		//creates a result table
		ResultsTable rt = new ResultsTable();

		//gets input from user
		bEnt = false;
		xInput = getInput();
		yInput = getInput();

		//changes inputs into integer arrays
		bArr = false;
		xArray = makeArray();
		yArray = makeArray();

		//takes (x,y) of an ABP and finds distance
		int counter = 1;
		for(int i = 0; i < xArray.length; i++) {
			lLength = 0;
			rLength = 0;
			lLength = leftPixelValue(ip, xArray[i], yArray[i]);
			rLength = rightPixelValue(ip, xArray[i], yArray[i]);
			rt.incrementCounter();
			rt.addValue("ABP Number", counter);
			rt.addValue("X-coordinate", xArray[i]);
			rt.addValue("Y-coordinate", yArray[i]);
			rt.addValue("Distance to left border", lLength);
			rt.addValue("Distance to right border", rLength);
			rt.addValue("Cell Width", rLength+lLength);
			rt.addValue("End X-coordinate right", xArray[i]+rLength);
			rt.addValue("End X-coordinate left", xArray[i]-lLength);
			counter++;
		}

		rt.show("Results");
	}

	//loop to find the distance to the right boundary
	public int rightPixelValue(ImageProcessor ip, int x, int y) {
		int pix = ip.getPixel(x,y) & 0xff;
		
		if (x <= 0 || x >= ip.getWidth()) {
			return rLength;
		}
		
		if (pix == 255) {
			return rLength;
		}
		else{
			rLength += 1;
			rightPixelValue(ip,x+1,y);
		}
		return rLength;
	}

	//loop to find the distance to the left boundary
	public int leftPixelValue(ImageProcessor ip, int x, int y) {
		int pix = ip.getPixel(x,y) & 0xff;

		if (x <= 0 || x >= ip.getWidth()) {
			return lLength;
		}		

		if (pix == 255) {
			return lLength;
		}
		else{
			lLength += 1;
			leftPixelValue(ip,x-1,y);
		}
		return lLength;
	}

	public String getInput() {
		String input = "";

		if(bEnt == false) {
			GenericDialog gd = new GenericDialog("ABP Distance", IJ.getInstance());
			gd.addMessage("Enter x-coordinates separated by a comma: ");
			gd.addStringField("X: ", "", 50);
			gd.showDialog();
			input = gd.getNextString();
			bEnt = true;
		}
		else {
			GenericDialog gd = new GenericDialog("ABP Distance", IJ.getInstance());
			gd.addMessage("Enter y-coordinates separated by a comma: ");
			gd.addStringField("Y: ", "", 50);
			gd.showDialog();
			input = gd.getNextString();
		}

		return input;
	}
	
	public int[] makeArray() {
		if(bArr == false) {
			String[] xStr = xInput.split(",");
			int[] xArr = new int[xStr.length];
		
			for(int i = 0; i < xStr.length; i++) {
				xArr[i] = Integer.parseInt(xStr[i]);
			}

			bArr = true;
			return xArr;
		}
		else {
			String[] yStr = yInput.split(",");
			int[] yArr = new int[yStr.length];
		
			for(int i = 0; i < yStr.length; i++) {
				yArr[i] = Integer.parseInt(yStr[i]);
			}
		
			return yArr;
		}
	}
}
