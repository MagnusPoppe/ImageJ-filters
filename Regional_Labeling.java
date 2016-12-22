import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Regional_Labeling implements PlugInFilter 
{
	ImagePlus imp;
	int threshold; // Threshold variable. T=Threshold
	int objectcount;
	int marker;

	final static int X 	   = 0; // FOR INDEXING
	final static int Y 	   = 1; // FOR INDEXING
	final static int FOUR  = 3; // FOUR WAY NEIGHBOUR-MATRIX
	final static int EIGHT = 7; // EIGHT WAY NEIGHBOUR-MATRIX

	static int[][] DELTA = {
		{0,  -1}, // 0: OPP
		{1,   0}, // 1: HØYRE
		{0,   1}, // 2: NED
		{-1,  0}, // 3: VENSTRE
		{-1, -1}, // 4: OPP-VENSTRE
		{-1,  1}, // 5: OPP-HØYRE
		{1,   1}, // 6: NED-HØYRE
		{-1,  1}  // 7: NED-VENSTRE
	};

	final static int WHITE = 255;
	final static int BLACK = 0;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

	public void run(ImageProcessor ip) 
	{
		// #1: definere område der man skal lete (terskel)
		threshold   = 100; 
		objectcount = 0;
		marker 		= 99;

		// #2: gå igjennom bildet
		for (int y = 0; y < ip.getHeight(); y++)
		for (int x = 0; x < ip.getWidth();  x++)
		{
			int pixel = ip.getPixel(x, y);

			if (pixel >= threshold) //Foreground
			{
				discover( x, y, pixel, ip );
				objectcount++;
				marker--;
			}
			else // Background
			{
				ip.putPixel(x, y, BLACK);
				imp.updateAndDraw();
			}
		}
		// #3: Lokalisere kanter av områder
			// Utforske området
				// Markere av etter besøk.
		
		// #4: REPITER STEG #3

	}

	private void discover( int x, int y, int value, ImageProcessor ip )
	{
		ip.putPixel(x, y, marker);
		imp.updateAndDraw();
		for (int i = 0; i < EIGHT; i++)
		{
			int posx = x + DELTA[i][X];
			int posy = y + DELTA[i][Y];
			int p = ip.getPixel(posx, posy);
			if (p >= threshold)
			{
				discover(posx, posy, p, ip);
			}
		}
	}
}
