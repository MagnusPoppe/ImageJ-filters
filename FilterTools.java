import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class FilterTools
{
	public static int sumFromFilter(int origoX, int origoY, int[][] filter, ImageProcessor image)
	{
		// NØDVENDIGE VERDIER FOR KJØRING AV FILTER:
		int filterRadius = filter.length/2;
		int sum = 0;
	
		// Kjører over filterets koordinater:
		for (int y = 0; y < filter.length; y++)		
		for (int x = 0; x < filter[y].length; x++)
		{	
			int xoffset = x-filterRadius;
			int yoffset = y-filterRadius;
			
			sum += filter[x][y] * image.getPixel( origoX-xoffset, origoY-yoffset ); 
		}

		return sum;
	}


	public static int getFilterSum( int[][] filter )
	{
		int sum = 0;
		for (int y = 0; y < filter.length; y++)		
		for (int x = 0; x < filter[y].length; x++)
		{	
			sum += filter[x][y];
		}
		return sum;
	}

	/**
	 * Get gradiant image strength = nytt bilde som gir pytagoras verdi for styrken
	 */
	public static ImagePlus getGradiantIntensity(ImageProcessor p1, ImageProcessor p2)
	{
		// DEFINERER VERDIER OG VARIABLER:
		int height = p1.getHeight();
		int width  = p1.getWidth();
		ImageProcessor ip = new ByteProcessor(width, height);
		ImagePlus image = new ImagePlus("Gradient Intensity", ip);

		// LAGER NYTT BILDE:
		for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
		{
			int v1 = p1.getPixel(x, y);
			int v2 = p2.getPixel(x, y);
			ip.putPixel(x, y, (int)Math.round(Math.sqrt( Math.pow(v1, 2) + Math.pow(v2, 2))));
		}	
		return image;
	}

	/**
	 * Get gradiant direction = arcTan( Dy / Dx ); Dx/Dy er de forskjellige filtrerte verdiene
	 */
	public static ImagePlus getGradiantDirection(ImageProcessor p1, ImageProcessor p2)
	{
		// DEFINERER VERDIER OG VARIABLER:
		int height = p1.getHeight();
		int width  = p1.getWidth();
		ImageProcessor ip = new ByteProcessor(width, height);
		ImagePlus image = new ImagePlus("Gradient Direction", ip);

		// LAGER NYTT BILDE:
		for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
		{
			int v1 = p1.getPixel(x, y);
			int v2 = p2.getPixel(x, y);
			
			double direction = (Math.atan(( (v1 + 0.0f) / (v2 + 0.0f) )));
			double max = Math.PI;

			int output = (int)Math.round((direction * 255.0f/max));
			ip.putPixel(x, y, output);
		}	
		return image;
	}
}