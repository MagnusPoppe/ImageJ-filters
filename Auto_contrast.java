import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Auto_contrast implements PlugInFilter 
{
	ImagePlus imp;
	int height;
	int width;
	float percent;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

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
	 */
	public void run( ImageProcessor ip ) 
	{
		// 1. Hent prosentandel bruker vil forbedre
		percent 		= promptPercent( );

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
		imp.show();
	}

	/**
	 * Metoden henter ut en rett linje mellom to punkter LOW og HIGH.
	 * Formelen metoden går etter er f(x) = ax + b. Derav metodenavnene
	 * getA() og getB().
	 * @author Magnus Poppe Wang
	 * @return pikselverdi beregnet etter en lineær funksjon
	 */
	int f( int low, int high, int x )
	{
		return (int)(getA(low, high) * x + getB(low, high));
	}

	/**
	 * Metoden henter ut Koeffisienten a i formelen f(x) = ax + b.
	 * @author Magnus Poppe Wang
	 * @return Koeffisienten A i formelen f(x) = ax + b
	 */
	float getA( int low, int high ) 
	{
		return (float)( (255.0f) / (high-low) );
	}

	/**
	 * Metoden henter ut b i formelen f(x) = ax + b. Dette tallet
	 * viser til hvor linjen krysser y-aksen.
	 * @author Magnus Poppe Wang
	 * @return b i formelen f(x) = ax + b.
	 */
	float getB( int low, int high ) 
	{
		return (float)( (255.0f * low) / (high-low) );
	}

	/**
	 * @param prosentandel av bildet.
	 * @return gitt % av totalt antall piksler i bildet.
	 * @author Magnus Poppe Wang
	 */
	private int getSelectedPercent( float selectedPercent )
	{
		return (int)((height * width) * selectedPercent);
	}

	/**
	 * Metode som går igjennom et histogram fra bunn og teller opp til 
	 * den er nådd en viss prosent gitt i PERCENT variablen.
	 * @return laveste verdi etter cutoff
	 * @author Magnus Poppe Wang
	 */
	int getLowValue( int[] histogram )
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
	int getHighValue( int[] histogram )
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

	/**
	 * Metoden gir brukeren et dialogvindu som spør etter prosent
	 * @author Magnus Poppe Wang
	 * @return tall mellom 0 og 1 oppgitt av bruker.
	 */
	float promptPercent()
	{
		GenericDialog gd = new GenericDialog("Prosentandel forbedring");
		gd.addNumericField("Prosent forbedring: %", 0.01f, 0);
		gd.showDialog();

		if (gd.wasCanceled()) return 0;

		float p = (float)gd.getNextNumber();
		return p/100;
	}
	int count = 0;
	float output = 1.0;
	while (count <= n) {
		output *= x;
		count++;
	}
}
