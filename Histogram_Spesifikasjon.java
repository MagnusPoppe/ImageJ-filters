import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.*;

public class Histogram_Spesifikasjon implements PlugInFilter 
{

	final static private int GREY_SCALE = Toolbelt.GREY_SCALE;

	final static int MY 			= 50;
	final static int SIGMA			= 128;

	int h, b; // HØYDE OG BREDDE VARIABLENE.


	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

	public void run(ImageProcessor ip) 
	{
		h = ip.getHeight();
		b = ip.getWidth();

		Toolbelt.displayMessage("Pixelcount="+(h*b));
		//int [] guassian = getGuassianHistogram(MY, SIGMA);
		//guassian = HistogramTools.toCumulative(guassian);
		//spec (ip, guassian);
	}


	public void spec(ImageProcessor ip, int[] ref)
	{
		ref = HistogramTools.scale(ref, GREY_SCALE, h*b);

		for (int y = 0; y < h; y++)
			for (int x = 0; x < b; x++)
				ip.putPixel(
					x, 
					y, 
					findCorrectPositionRelativeTo(
						ip.getPixel(x, y),
						ref
					)
				);

	}

	private int[] getGuassianHistogram(float my, float sigma) 
	{
		int[] g = new int[GREY_SCALE];
		Random r = new Random();

		for (int i = 0; i < h*b; i++) 
			g[(int) Math.round(r.nextGaussian() * my + sigma)]++;

		return g;
	}

	/**
	 * Det ble vurdert å bruke binært søk her, men grunnet liten tabell 
	 * lot jeg være.
	 */
	private int findCorrectPositionRelativeTo(int columnHeight, int[] ref)
	{
		columnHeight = (int)((float)(columnHeight/(h*b))*255.0f);
		int i = 0;
		while (columnHeight < ref[i]) i++;
		return i;
	}
}
