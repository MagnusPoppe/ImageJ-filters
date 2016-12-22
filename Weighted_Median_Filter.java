import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.Arrays;

public class Weighted_Median_Filter implements PlugInFilter 
{
	ImagePlus imp;
	int[][]   ORIGINAL_FILTER = 
	{
		{1, 1, 1, 1, 1},
		{1, 2, 2, 2, 1},
		{1, 2, 3, 2, 1},
		{1, 2, 2, 2, 1},
		{1, 1, 1, 1, 1},
	};

	// MÅL:
	int height;
	int width;
	int filterRadius;
	int filterSum;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) 
	{
		height = ip.getHeight();
		width = ip.getWidth();

		filterRadius = ORIGINAL_FILTER.length / 2;
		filterSum = getFilterSum(ORIGINAL_FILTER);

		for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
		{	
			ip.putPixel(x, y, medianFilter(x, y, ORIGINAL_FILTER, ip) );
		}
	}

	private int medianFilter(int origoX, int origoY, int[][] filter, ImageProcessor image)
	{
		int[] list = new int[filterSum];
		int index = 0;
		// Kjører over filterets koordinater:
		for (int y = 0; y < filter.length; y++)		
		for (int x = 0; x < filter[y].length; x++)
		{	
			int xoffset = x-filterRadius;
			int yoffset = y-filterRadius;

			for (int i = 0; i < filter[y][x];; i++) 
				list[index++] = image.getPixel(origoX-xoffset, origoY-yoffset); 
		}

		Arrays.sort(list);
		return list[filterSum/2];
	}

	private int getFilterSum( int[][] filter )
	{
		int sum = 0;
		for (int y = 0; y < filter.length; y++)		
		for (int x = 0; x < filter[y].length; x++)
		{	
			sum += filter[x][y];
		}
		return sum;
	}
}
