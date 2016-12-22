import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class AutoContrast 
{
	static int height;
	static int width;
	static float percent;

	/**
	 * Hovedmetoden. Denne metoden modifiserer kontrasten i et bilde. 
	 * dette gjøres ved å finne en ny lineær funksjon til å hente verdier fra.
	 *
	 * Det er 5 oppgaver metoden utfører:
	 * 1. Hent prosentandel bruker vil forbedre. Dette tilsvarer cutoff.
	 * 2. Finn grenseverdier for optimal økning av kontrast.
	 * 3. Gå igjennom bildet.
	 * 4. Utfør endringer i pikselverdier.
	 * 5. Vis det vakre bildet.
	 *
 	 * @author Magnus Poppe Wang
 	 * @param ip ImageProcessor of the photo
 	 * @param userPercent is a number between 0 and 1.
	 */
	public static ImageProcessor greyscale( ImageProcessor ip, float userPercent ) 
	{
		// 1. Hent prosentandel bruker vil forbedre
		percent 		= userPercent;

	 	// 2. Finn grenseverdier for optimal økning av kontrast:
	 	width 			= ip.getWidth( );
		height 			= ip.getHeight( );
		int[] histogram = ip.getHistogram( ); 
		int low			= getLowValue( histogram );
		int high		= getHighValue( histogram );

	 	// 3. Gå igjennom bildet:
		for (int y = 0; y < ip.getHeight(); y++)
		{
			for (int x = 0; x < ip.getWidth(); x++)
			{
	 			// 4. Utfør endringer i pikselverdier:
				ip.putPixel(x, y, f(low, high, ip.getPixel(x,y)));
			}
		}

	 	// 5. Vis det vakre bildet:
	 	return ip;
	}

	public void color( ImageProcessor ip ) 
	{
		ImageProcessor red_ip = new ByteProcessor(ip.getWidth(), ip.getHeight());
		ImagePlus red = new ImagePlus("Red", red_ip);

		ImageProcessor green_ip = new ByteProcessor(ip.getWidth(), ip.getHeight());
		ImagePlus green = new ImagePlus("Green", green_ip);

		ImageProcessor blue_ip = new ByteProcessor(ip.getWidth(), ip.getHeight());
		ImagePlus blue = new ImagePlus("Blue", blue_ip);

		ImageProcessor result_ip = new ImageProcessor(ip.getHeight(), ip.getWidth());
		ImagePlus result = new ImagePlus("Result image of " + ip.getTitle(), result_ip);

		for( int y = 0; y < ip.getHeight(); y++ )
		for( int x = 0; x < ip.getWidth();  x++ )
		{
			// Splitter opp farger: 
			int r = (ip.getPixel(x, y) & 0xFF0000) >> 16;
			int g = (ip.getPixel(x, y) & 0x00FF00) >> 8;
			int b = (ip.getPixel(x, y) & 0x0000FF);
		
			// Plasserer fargene i separate bilder:
			red_ip.putPixel(x, y, r);
			green_ip.putPixel(x, y, g);
			blue_ip.putPixel(x, y, b);
		}

		// Endrer kontrast:
		red_ip 	 = greyscale(red_ip, 	PERCENTAGE_CONTRAST_CHANGE);
		green_ip = greyscale(green_ip, 	PERCENTAGE_CONTRAST_CHANGE);
		blue_ip  = greyscale(blue_ip, 	PERCENTAGE_CONTRAST_CHANGE);

		// Setter sammen resultatet: 
		for( int y = 0; y < ip.getHeight(); y++ )
		for( int x = 0; x < ip.getWidth();  x++ )
		{
			int pixelvalue = 0;
			pixelvalue = (red_ip.getPixel(x, y)  << 16) | pixelvalue;
			pixelvalue = (green_ip.getPixel(x, y) << 8) | pixelvalue;
			pixelvalue = blue_ip.getPixel(x, y) | pixelvalue;
			result_ip.putPixel(x, y, pixelvalue);
		}
		//red.show(); blue.show(); green.show();
		return result;
	}

	/**
	 * Metoden henter ut en rett linje mellom to punkter LOW og HIGH.
	 * Formelen metoden går etter er f(x) = ax + b. Derav metodenavnene
	 * getA() og getB().
	 * @author Magnus Poppe Wang
	 * @return pikselverdi beregnet etter en lineær funksjon
	 */
	private static int f( int low, int high, int x )
	{
		return (int)(getA(low, high) * x + getB(low, high));
	}

	/**
	 * Metoden henter ut Koeffisienten a i formelen f(x) = ax + b.
	 * @author Magnus Poppe Wang
	 * @return Koeffisienten A i formelen f(x) = ax + b
	 */
	private static float getA( int low, int high ) 
	{
		return (float)( (255.0f) / (high-low) );
	}

	/**
	 * Metoden henter ut b i formelen f(x) = ax + b. Dette tallet
	 * viser til hvor linjen krysser y-aksen.
	 * @author Magnus Poppe Wang
	 * @return b i formelen f(x) = ax + b.
	 */
	private static float getB( int low, int high ) 
	{
		return (float)( (255.0f * low) / (high-low) );
	}

	/**
	 * @param prosentandel av bildet.
	 * @return gitt % av totalt antall piksler i bildet.
	 * @author Magnus Poppe Wang
	 */
	private static int getSelectedPercent( float selectedPercent )
	{
		return (int)((height * width) * selectedPercent);
	}

	/**
	 * Metode som går igjennom et histogram fra bunn og teller opp til 
	 * den er nådd en viss prosent gitt i PERCENT variablen.
	 * @return laveste verdi etter cutoff
	 * @author Magnus Poppe Wang
	 */
	private static int getLowValue( int[] histogram )
	{
		int count = 0;
		int cutoff = getSelectedPercent(percent);
		int i = 0;

		for (; i < histogram.length; i++)
		{
			if (count >= cutoff) break;
			count += histogram[i];
		}
		return i;
	}

	/**
	 * Metode som går igjennom et histogram fra topp og teller ned til 
	 * den er nådd en viss prosent gitt i PERCENT variablen.
	 *
	 * @return høyeste verdi etter cutoff
	 * @author Magnus Poppe Wang
	 */
	private static int getHighValue( int[] histogram )
	{
		int count = 0;
		int cutoff = getSelectedPercent(percent);
		int i = histogram.length-1;

		for (; i > 0; i--)
		{
			if (count >= cutoff) break;
			count += histogram[i];
		}
		return i;
	}
}
