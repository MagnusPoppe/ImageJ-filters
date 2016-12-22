import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Rotate_RGB implements PlugInFilter 
{
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_RGB;
	}

	public void run(ImageProcessor ip) 
	{	
		rotateColors((ColorProcessor)ip);
	}

	public void rotateColors( ColorProcessor cp )
	{
		for(int y = 0; y < cp.getHeight( ); y++)
		for(int x = 0; x < cp.getWidth( );  x++)
		{
			int pixel = cp.getPixel(x, y);
			int temp = pixel & 0xff;
			pixel = (pixel >> 8);
			pixel = ( pixel & ( (temp << 16) + 0xffff ) );
			cp.putPixel(x, y, pixel);
		}
	}

}
