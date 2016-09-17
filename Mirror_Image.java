import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

/**
 * Klassen speiler et bilde på Y aksen.
 *
 * @author Magnus Poppe Wang
 */
public class Mirror_Image implements PlugInFilter {
	
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {
		
		flip(ip);
		imp.updateAndDraw();
	}

	/**
	 * Flip flytter piksler fra den ene siden av et bilde
	 * til en andre på X aksen. Dette gjøres for alle linjer.
	 *
	 * @author Magnus Poppe Wang
	 */
	private void flip( ImageProcessor ip ) 
	{
		int width = ip.getWidth();
		int height = ip.getHeight();
		for (int y = 0; y < height; y++)
		{
			int[] row = getRowOfImage(y, width, ip);

			for(int x = 0; x < width; x++) 
			{
				ip.putPixel(x, y, row[x]);
			}
		}
	}

	/**
	 * Henter ut en tabell fylt med en hel X rad av bildets pikselverdier.
	 *
	 * @return rad med piksler.
	 * @author Magnus Poppe Wang
	 */
	private int[] getRowOfImage(int y, int width, ImageProcessor ip) 
	{
		int i = 0;
		int [] row = new int[width];

		for (int x = width-1; x >= 0; x--) 
			row[i++] = ip.getPixel(x, y);
		return row;
	}	

}
