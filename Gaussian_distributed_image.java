import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.Random;

public class Gaussian_distributed_image implements PlugInFilter 
{
	ImagePlus imp;

	final static int IMAGE_WIDTH 	= 500;
	final static int IMAGE_HEIGHT 	= 500;
	final static int MY 			= 50;
	final static int SIGMA			= 128;

	/**
	 * Siden dette programmet leverer et bilde fra ingenting, bruker jeg
	 * taggen "NO_IMAGE_REQUIRED". Denne tillater at programmet kjører uten
	 * åpent bilde.
	 * @author Magnus Poppe Wang
	 * @return NO_IMAGE_REQUIRED flagg
	 */
	public int setup(String arg, ImagePlus imp) 
	{
		return NO_IMAGE_REQUIRED;
	}

	/**
	 * Jeg valgte å ikke bruke hjelpemetoder men heller kjøre alt i run metdoden.
	 * Metoden lager et bilde fordelt med tilfeldig plasserte verdier to å matche 
	 * normalfordelt histogram.
	 * @author Magnus Poppe Wang
	 */
	public void run(ImageProcessor ip) 
	{
		// CREATING A NEW IMAGE
		ImageProcessor processor = new ByteProcessor(IMAGE_WIDTH,IMAGE_HEIGHT);  
		ImagePlus image = new ImagePlus("Guassian Distributed Image", processor);  
		image.show();

		Random r = new Random();

		for (int y = 0; y < IMAGE_HEIGHT; y++)
		{
			for (int x = 0; x < IMAGE_WIDTH; x++)
			{
				processor.putPixel(x, y,(int) Math.round(r.nextGaussian() * MY + SIGMA));
				// FORMEL FOR BRUK AV Random.nextGaussian BLE FUNNET HER:
				// http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
			}
		}
	}

}
