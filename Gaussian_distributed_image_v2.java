import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.Random;

public class Gaussian_distributed_image_v2 implements PlugInFilter {

	final static int IMAGE_WIDTH 	= 500;
	final static int IMAGE_HEIGHT 	= 500;
	final static int MY 			= 50;
	final static int SIGMA			= 128;

	final static private int HISTOGRAM_WIDTH  = 255;
	final static private int HISTOGRAM_HEIGHT = 150;
	final static private int PICTURE_HEIGHT   = HISTOGRAM_HEIGHT + 25;
	final static private int SCALE_START      = HISTOGRAM_HEIGHT + 3;
	final static private int WHITE_PIXEL	  = 255;
	final static private int BLACK_PIXEL	  = 100;

	ImageProcessor hp;
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return NO_IMAGE_REQUIRED;
	}

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

		int [] h  = processor.getHistogram( );   // h står for histogram.
		int [] kh = kumulativtHistogram( h );

		drawHistogram( kh );		
		drawHistogram( h );		
	}

	/**
	 * Metoden tar imot et histogram og leverer tilbake et ferdig
	 * kalkulert kumulativt histogram. 
	 * @param  vanlig histogram som skal omgjøres.
	 * @return kumulativt histogram
	 * @author Magnus Poppe Wang
	 */
	int[] kumulativtHistogram( int[] histogram ) 
	{
		int[] kh = new int[ histogram.length ]; // kh står for kumulativt histogram.

		for (int i = 0; i < histogram.length; i++)
		{
			if (i <= 0) kh[i] = histogram[i];
			else 	   kh[i] = kh[i-1] + histogram[i];
		}
		return kh;
	}

	/**
	 * Denne metoden tegner selve histogrammet til skjerm. Den kan tegne 
	 * hvilket som helst histogram.
	 * @param  Histogrammet som skal tegnes. Vanlig/kumulativt.
	 * @author Magnus Poppe Wang
	 */
	void drawHistogram( int[] histogram )
	{
		// Lager nytt bilde tilpasset nye formatet:
		hp = new ByteProcessor(
			histogram.length,  // HISTOGRAM_WIDTH
			PICTURE_HEIGHT 
		);  
		String title = "Histogram of Guassian";  
		ImagePlus histogram_image = new ImagePlus(title, hp);  

		// Jeg definerer histogrammets størrelse på den 
		// høyeste verdien i histogrammet.
		int maxY = getMaxValue( histogram ) - HISTOGRAM_HEIGHT;
		int	factorY =  maxY / HISTOGRAM_HEIGHT;

		int centerX = (HISTOGRAM_WIDTH-histogram.length) / 2;

		// Jeg begynner med X for å tilpasse logikken med søylediagram.
		for ( int x = 0; x < histogram.length; x++ )
		{
			int printheight = Math.abs((histogram[x]/factorY)-HISTOGRAM_HEIGHT);

			for (int y = 0; y < HISTOGRAM_HEIGHT; y++) 
			{
				if( y < printheight) hp.putPixel(x, y, WHITE_PIXEL);
				else  				 hp.putPixel(x, y, BLACK_PIXEL);
			}
		}
		drawValueScale(0, 255);
		histogram_image.show();
	}

	/**
	 * Metoden tegner en skala med verdier mellom minimum og maksimum
	 * @param  min = minimum verdi på skalaen.
	 * @param  max = maksimum verdi på skalaen.
	 * @author Magnus Poppe Wang
	 */
	void drawValueScale(int min, int max)
	{
		// Tegner skalaen:
		for ( int y = HISTOGRAM_HEIGHT+3; y < PICTURE_HEIGHT; y++)
		{
			for ( int x = min; x < max; x++)
			{
				if (y == HISTOGRAM_HEIGHT+1)
					hp.putPixel(x, y, BLACK_PIXEL);
				else if (y < SCALE_START)
					hp.putPixel(x, y, WHITE_PIXEL);
				else
					hp.putPixel(x, y, x);
			}
		}
	}

	/** 
	 * Henter ut høyeste verdi i et histogram og dens posisjon.
	 * @param  histogrammet som skal granskes.
	 * @return høyeste verdi i et gitt histogram
	 * @author Magnus Poppe Wang
	 */
	int getMaxValue( int [] histogram )
	{
		int max = 0;
		int maxIndex = 0;
		for (int i = 0; i < histogram.length; i++) 
		{
			if ( max < Math.max(max, histogram[i]) )
			{
				max = histogram[i];
				maxIndex = i;
			}
		}
		return max;
	}
}