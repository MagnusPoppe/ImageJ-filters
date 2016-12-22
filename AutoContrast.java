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
	public static ImageProcessor HSBEnhance( ImageProcessor ip, float userPercent )
	{
		percent 		= userPercent;
		ImageProcessor res = ip.duplicate();
		for( int y = 0; y < ip.getHeight(); y++ )
		for( int x = 0; x < ip.getWidth();  x++ )
		{
			// Splitter opp farger: 
			int r = (ip.getPixel(x, y) & 0xFF0000) >> 16;
			int g = (ip.getPixel(x, y) & 0x00FF00) >> 8;
			int b = (ip.getPixel(x, y) & 0x0000FF);
		
			float[] hsb = new float[3];
			Color.RGBtoHSB( r, g, b, hsb);
			// Plasserer fargene tilbake i bildet:

			if (hsb[2] < 0.9f && hsb[2] > 0.1f) hsb[2] += 0.01f;
			if (hsb[1] < 0.9f && hsb[1] > 0.1f) hsb[1] += 0.01f;
			res.putPixel(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));

		}
		return res;
	}


	/**
	 * Tar et RGB bilde og gjør en kontrastendring:
	 * 1. Splitter farger, konverterer til HSB verdier ved hjelp av RGBtoHSB
	 *    funnet i AWT.Color klassen.
	 * 2. Setter sammen bilder bestående av kun verdiene fra de tre forskjellige
	 *    Hue, Saturation og Brightness i gråskala. De blie dermed også skalert
	 *    opp.
	 * 3. Kjører kontrastendring ved hjelp av "greyscale( )" metoden.
	 * 4. Setter sammen og returnerer et RGB bilde utifra de tre 
	 *    nylige endrede bildene.
	 * @param ip: Imageprocessor of the photo being enhanced
	 * @param userPercent: Percentage of contrastcut.
	 * @return ImageProcessor of the newly changed image.
	 */		
	public static ImageProcessor HSB( ImageProcessor ip, float userPercent )
	{
		percent = userPercent;
		int h   = ip.getHeight();
		int w   = ip.getWidth();
		// 2. Setter opp nødvendige bildeobjekter:
		ImageProcessor res = ip.duplicate();
		ImageProcessor sat_ip 	 = new ByteProcessor(w, h);
		ImageProcessor bright_ip = new ByteProcessor(w, h);
		ImageProcessor hue_ip 	 = new ByteProcessor(w, h);

		ImagePlus sat 	  = new ImagePlus("sat", sat_ip);
		ImagePlus bright  = new ImagePlus("bright", bright_ip);
		ImagePlus hue     = new ImagePlus("Hue", hue_ip);

		for( int y = 0; y < h; y++ )
		for( int x = 0; x < w; x++ )
		{
			// Splitter opp farger: 
			int r = (ip.getPixel(x, y) & 0xFF0000) >> 16;
			int g = (ip.getPixel(x, y) & 0x00FF00) >> 8;
			int b = (ip.getPixel(x, y) & 0x0000FF);
		
			float[] hsb = new float[3];
			Color.RGBtoHSB( r, g, b, hsb);
			
			// Skalerer bildet			
			int hue_val    = (int) (hsb[0] * 255);
			int saturation = (int) (hsb[1] * 255);
			int brightness = (int) (hsb[2] * 255);

			sat_ip.putPixel(x, y, saturation);
			bright_ip.putPixel(x, y, brightness);
			hue_ip.putPixel(x, y, hue_val);
		}
		sat_ip 		= greyscale(sat_ip, percent);
		//hue_ip 		= greyscale(hue_ip, percent);
		bright_ip 	= greyscale(bright_ip, percent);
		// sat.show(); bright.show(); hue.show();

		// 6. Setter sammen resultatbildene: 
		for( int y = 0; y < h; y++ )
		for( int x = 0; x < w; x++ )
		{
			int value = Color.HSBtoRGB(
				hue_ip.getPixel(x, y)    / 255.0f, 
				sat_ip.getPixel(x, y)    / 255.0f,
				bright_ip.getPixel(x, y) / 255.0f
			);
			res.putPixel(x, y, value);
		}
		return res;
	}


	public static ImageProcessor RGB( ImageProcessor ip, float userPercent ) 
	{
		// 1. Hent prosentandel bruker vil forbedre
		percent 		= userPercent;

		// 2. Setter opp nødvendige bildeobjekter:
		ImageProcessor red_ip = new ByteProcessor(
			ip.getWidth(), 
			ip.getHeight()
		);

		ImageProcessor green_ip = new ByteProcessor(
			ip.getWidth(), 
			ip.getHeight()
		);

		ImageProcessor blue_ip = new ByteProcessor(
			ip.getWidth(), 
			ip.getHeight()
		);
		ImageProcessor res = ip.duplicate();
		ImagePlus red 	 = new ImagePlus("Red", red_ip);
		ImagePlus green  = new ImagePlus("Green", green_ip);
		ImagePlus blue 	 = new ImagePlus("Blue", blue_ip);

		for( int y = 0; y < ip.getHeight(); y++ )
		for( int x = 0; x < ip.getWidth();  x++ )
		{
			// 3. Splitter opp farger: 
			int r = (ip.getPixel(x, y) & 0xFF0000) >> 16;
			int g = (ip.getPixel(x, y) & 0x00FF00) >> 8;
			int b = (ip.getPixel(x, y) & 0x0000FF);
		
			// 4. Plasserer fargene i separate bilder:
			red_ip.putPixel(x, y, r);
			green_ip.putPixel(x, y, g);
			blue_ip.putPixel(x, y, b);
		}

		// 5. Endrer kontrast:
		red_ip 	 = greyscale(red_ip, 	percent);
		green_ip = greyscale(green_ip, 	percent);
		blue_ip  = greyscale(blue_ip, 	percent);

		// 6. Setter sammen resultatbildene: 
		for( int y = 0; y < ip.getHeight(); y++ )
		for( int x = 0; x < ip.getWidth();  x++ )
		{
			int pixelvalue = 0;
			pixelvalue = (red_ip.getPixel(x, y)  << 16) | pixelvalue;
			pixelvalue = (green_ip.getPixel(x, y) << 8) | pixelvalue;
			pixelvalue = blue_ip.getPixel(x, y) | pixelvalue;
			res.putPixel(x, y, pixelvalue);
		}
		//red.show(); blue.show(); green.show();
		return res;
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
