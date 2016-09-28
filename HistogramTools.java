import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

/**
 * Ferdig verktøyklasse for å jobbe mot histogrammer. 
 * @author  Magnus Poppe Wang
 * @version 1.0
 */
public class HistogramTools 
{

	final static public  int GREYSCALE 		  = 256;
	final static private int HISTOGRAM_WIDTH  = 255;
	final static private int HISTOGRAM_HEIGHT = 150;
	final static private int PICTURE_HEIGHT   = HISTOGRAM_HEIGHT + 25;
	final static private int SCALE_START      = HISTOGRAM_HEIGHT + 3;
	final static private int WHITE_PIXEL	  = 255;
	final static private int BLACK_PIXEL	  = 100;

	/**
	 * Metoden tar et vilkårlig histogram og skalerer det
	 * ned til en prosentandel av bildets størrelse. Kan brukes til
	 * blant annet histogram-sammenlikning.
	 *
	 * @param histogrammet som skal endres.
	 * @param antall piksler i bildet.
	 * @return ferdig normalisert histogram.
	 * @author Magnus Poppe Wang
	 */
	public static int[] normalize( int[] histogram, int pixelcount )
	{
		if ( histogram.length == GREYSCALE-1 )
		{
			throw new ArrayIndexOutOfBoundsException(
				"Metoden støtter kun 8-bits bilder."
			);
		}

		int [] normalized = new int[ GREYSCALE ];
		for (int i = 0; i < histogram.length; i++) 
		{
			normalized[i] = histogram[i] / pixelcount;
		}

		return normalized;
	}

	public static int[] scale( int [] histogram, int scale, int pixelcount ) 
	{
		int[] scaled 	 = new int[GREYSCALE];

		for (int i = 0; i < GREYSCALE; i++)
			scaled[i] = (histogram[i] * GREYSCALE) / pixelcount;

		return scaled;
	}

	/**
	 * Metoden tar imot et histogram og leverer tilbake et ferdig
	 * kalkulert kumulativt histogram. 
	 *
	 * @param  vanlig histogram som skal omgjøres.
	 * @return kumulativt histogram
	 * @author Magnus Poppe Wang
	 */
	public static int[] toCumulative( int[] histogram )
	{

		int [] cumulative = new int[histogram.length];
		cumulative[0] = histogram[0];

		for (int i = 1; i < histogram.length; i++)
			cumulative[i] = cumulative[i-1] + histogram[i];

		return cumulative;
	}

	/**
	 * Denne metoden tegner selve histogrammet til skjerm. Den kan tegne 
	 * hvilket som helst histogram.
	 *
	 * @param  Histogrammet som skal tegnes. Vanlig/kumulativt.
	 * @author Magnus Poppe Wang
	 * @return Ferdig bilde av histogrammet.
	 */
	public static ImagePlus drawHistogram( int[] histogram, String title, int pixelcount)
	{
		// Lager nytt bilde tilpasset nye formatet:
		ImageProcessor hp = new ByteProcessor(
			histogram.length,  // HISTOGRAM_WIDTH
			PICTURE_HEIGHT 
		);  
		title = "Histogram of " + title;  
		ImagePlus histogram_image = new ImagePlus(title, hp);  

		histogram = scale(histogram, HISTOGRAM_HEIGHT, pixelcount);

		// Jeg definerer histogrammets størrelse på den 
		// høyeste verdien i histogrammet.
		float maxY 		= getMaxValue( histogram ) - HISTOGRAM_HEIGHT;
		float factorY 	= maxY / HISTOGRAM_HEIGHT;

		for ( int x = 0; x < histogram.length; x++ )
		{
			int printheight = (int)Math.abs((float)(histogram[x]/factorY)-HISTOGRAM_HEIGHT);

			for (int y = 0; y < HISTOGRAM_HEIGHT; y++) 
			{
				if( y < printheight) hp.putPixel(x, y, WHITE_PIXEL);
				else  				 hp.putPixel(x, y, BLACK_PIXEL);
			}
		}

		hp = drawValueScale(0, 255, hp); // får feil ved konstantbruk?
		
		return histogram_image;
	}


	/**
	 * Metoden tegner en skala med verdier mellom minimum og maksimum.
	 *
	 * @param  min = minimum verdi på skalaen.
	 * @param  max = maksimum verdi på skalaen.
	 * @author Magnus Poppe Wang
	 */
	public static ImageProcessor drawValueScale(int min, int max, ImageProcessor hp)
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
		return hp;
	}

	/** 
	 * Henter ut høyeste verdi i et histogram og dens posisjon.
	 *
	 * @param  histogrammet som skal granskes.
	 * @return høyeste verdi i et gitt histogram
	 * @author Magnus Poppe Wang
	 */
	public static int getMaxValue( int [] histogram )
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