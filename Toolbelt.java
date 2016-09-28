import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import ij.gui.GenericDialog;

public class Toolbelt {

	final static public int GREYSCALE = 256;

	public static int[] getCumulative(int[] histogram)
	{
		int [] cumulative = new int[histogram.length];
		cumulative[0] = histogram[0];

		for (int i = 1; i < histogram.length; i++)
			cumulative[i] = cumulative[i-1] + histogram[i];

		return cumulative;
	}

	public static int[] scale( int[] histogram, int scale, int pixelcount ) 
	{
		int[] scaled 	 = new int[GREYSCALE];

		for (int i = 0; i < GREYSCALE; i++)
			scaled[i] = (histogram[i] * GREYSCALE) / pixelcount;

		return scaled;
	}

	public static void displayMessage( String message ) 
	{
		String title = "Output";


		GenericDialog gd = new GenericDialog(title);
		gd.textArea1(message);
		gd.showDialog();
		IJ.newImage(title, "8-bit");
		
	}
}
