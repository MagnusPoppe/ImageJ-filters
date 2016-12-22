import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Region_Labeling implements PlugInFilter 
{
	ImagePlus imp;
	final static public int WHITE = 255;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

	public void run(ImageProcessor ip) 
	{
		for (int y = 0; y < ip.getWidth(); y++)
			for (int x = 0; x < ip.getWidth(); x++)

				// RUN ANY FLOODFILL:

				 FloodFill.recursive( ip, x, y, WHITE );
				// FloodFill.depth( ip, x, y, WHITE );
				// FloodFill.breadth( ip, x, y, WHITE );
	}
}
