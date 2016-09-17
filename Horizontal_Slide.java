import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Horizontal_Slide implements PlugInFilter {
	ImagePlus imp;

	private final static int CYLCLES = 1;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {

		SlideHorizontal(ip);
	}

	/**
	 * Denne flytter bildet horisontalt ved å flytte 
	 * rader på X aksen med en per runde i første løkke.
	 *
	 * @author Magnus Poppe Wang
	 */
	private void SlideHorizontal(ImageProcessor ip) 
	{
		for (int i = 0; i < ip.getWidth() * CYLCLES; i+= 1) 
		{
			for (int y = 0; y < ip.getHeight(); y++)
			{
				int ix = ip.getWidth()-1; // invertert X
				for (int x = 0; x < ip.getWidth(); x++)
				{
					int temp = ip.getPixel(x, y);
					ip.putPixel(x, y, ip.getPixel(ix, y));
					ip.putPixel(ix, y, temp);
				}
			}
			imp.updateAndDraw();
		}
	}

}
