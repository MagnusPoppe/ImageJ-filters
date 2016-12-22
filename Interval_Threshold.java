import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Interval_Threshold implements PlugInFilter 
{
	ImagePlus imp;

	final static String DIALOG_TITLE = "Threshold";
	final static int DEFAULT_LOW 	 = 100;
	final static int DEFAULT_HIGH 	 = 200;

	final static int WHITE   		 = 255;
	final static int BLACK   		 = 0;

	static int rLow = DEFAULT_LOW, rHigh = DEFAULT_HIGH; // Red thresholds
	static int gLow = DEFAULT_LOW, gHigh = DEFAULT_HIGH; // Green thresholds
	static int bLow = DEFAULT_LOW, bHigh = DEFAULT_HIGH; // Blue thresholds

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_RGB;
	}

	public void run(ImageProcessor ip) 
	{
		int w = ip.getWidth(), h = ip.getHeight();
		
		boolean legal = false;
		while ( !promptForThresholds() ) ;

		// lager 3 forskjellige bilder: 
		ImageProcessor res_ip = new ByteProcessor(w, h);
		ImagePlus res 		  = new ImagePlus("Result", res_ip);
		res.show();

		for (int y = 0; y < h; y++)
		for (int x = 0; x < w; x++)
		{			
			res_ip.putPixel(x, y, RGBThresholdTest(ip.getPixel(x, y)));
			res.updateAndDraw();
		}

	}

	/**
	 * Tests if the RGB values are within the boundries
	 * given by the "rLow, rHigh, gLow, gHigh, bLow, bHigh"
	 * variables. These needs to be set for the method to work.
	 * @param rgb: regular RGB integer with one byte per color
	 * @return WHITE if the color is within all six thresholds. else black.
	 */
	private int RGBThresholdTest( int rgb )
	{
		// Splitter opp farger: 		
		int r = (rgb & 0xFF0000) >> 16;
		int g = (rgb & 0x00FF00) >> 8;
		int b = (rgb & 0x0000FF);
			
		// Tester om verdien er innenfor alle terskler:	
		if ( r >= rLow && r <= rHigh ) 
			if ( g >= gLow && g <= gHigh ) 
				if ( b >= bLow && b <= bHigh )
					return WHITE;
				else return BLACK;
			else return BLACK;
		else return BLACK;
	}

	/**
	 * Prompts the user for the lower and higher thresholds
	 * for all three colorchannels, red, green and blue.
	 * @return true if the values are in bounds and legal.
	 */
	private boolean promptForThresholds( )
	{
		GenericDialog gd = new GenericDialog(DIALOG_TITLE);
		
		gd.addNumericField("Red lower: ", 	 rLow,  0);
		gd.addNumericField("Red higher: ", 	 rHigh, 0);
		gd.addNumericField("Green lower: ",  gLow,  0);
		gd.addNumericField("Green higher: ", gHigh, 0);
		gd.addNumericField("Blue lower: ", 	 bLow,  0);
		gd.addNumericField("Blue higher: ",  bHigh, 0);
		gd.showDialog();

		if (gd.wasCanceled()) return true;

		rLow  = (int)gd.getNextNumber();
		rHigh = (int)gd.getNextNumber();
		gLow  = (int)gd.getNextNumber();
		gHigh = (int)gd.getNextNumber();
		bLow  = (int)gd.getNextNumber();
		bHigh = (int)gd.getNextNumber();

		//IJ.newImage(DIALOG_TITLE, "8-bit", DIALOG_WIDTH, DIALOG_HEIGHT, 1);
		
		if (rLow < rHigh && rLow >= 0 && rHigh < 256)
		if (gLow < gHigh && gLow >= 0 && gHigh < 256)
		if (bLow < bHigh && bLow >= 0 && bHigh < 256)
			return true;
		return false;
	}
}
