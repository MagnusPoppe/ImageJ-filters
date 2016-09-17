import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

/**
 * Filteret speiler et bilde over en 45 graders akse 
 * og leverer et nytt bilde.
 *
 * @author Magnus Poppe Wang
 */
public class Flip_45 implements PlugInFilter 
{
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_ALL;
	}

	/**
	 * Hovedmaten til Flip_45
	 */
	public void run(ImageProcessor ip) 
	{

		// Finner minstemann av bredde og høyde
		int square = Math.min( ip.getWidth( ), ip.getHeight( ) );

			// Lager nytt bilde tilpasset nye formatet:
		ImageProcessor ip2 = new ColorProcessor(ip.getHeight( ), ip.getWidth( ));  
		String title = "FLIP RESULT - "+imp.getTitle();  
		ImagePlus imp2 = new ImagePlus(title, ip2);  

		// Flytter kvadrat av bildet fra origo over til nye bildet:
		flipDiagonal(square, ip, ip2);
		
		// Slenger over resten:
		if ( ip2.getWidth() == square)
			flipResten(0, square, ip, ip2);
		else 
			flipResten(square, 0, ip, ip2);

		imp2.show();  
	}


	/**
	 * Metoden flytter piksler over en akse som går 45 grader fra
	 * origo. 
	 *
	 * @author Magnus poppe Wang
	 */
	private void flipDiagonal(int square, ImageProcessor ip, ImageProcessor ip2) 
	{
		for (int y = 0; y < square; y++) 
		{
			for (int x = 0; x <= y; x++) 
			{
				ip2.putPixel( x, y, ip.getPixel( y, x ) );
				ip2.putPixel( y, x, ip.getPixel( x, y ) );
			}
		}
	}

	/**
	 * Denne metoden flytter over alt som ikke er innenfor 45 graders
	 * diagonalen brukt i "flipDiagonal"
	 *
	 * @author Magnus poppe Wang
	 *
	 * NB! Av ukjente årsaker vil ikke metoden fungere om jeg jobber 
	 * direkte mot variablene x og y. Derfor bruker jeg to identiske variabler
	 * x2 og y2.
	 */
	private void flipResten(int x, int y, ImageProcessor ip, ImageProcessor ip2) 
	{
		for (int y2 = y; y2 < ip2.getHeight(); y2++) 
		{
			for (int x2 = x; x2 < ip2.getWidth(); x2++) 
			{
				ip2.putPixel(x2, y2, ip.getPixel(y2, x2));
			}
		}
	}

}
