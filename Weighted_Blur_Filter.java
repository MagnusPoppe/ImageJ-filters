import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

/**
 * "Blur Filter" bruker to en-dimensjonale filtere for å 
 * lage en "utsmørings effekt" (blur). 
 *
 * NOTAT: Bokstavene H og V:
 * H = Horisontalt. 
 * V = Vertikalt. 
 * @author Magnus Poppe Wang
 */
public class Weighted_Blur_Filter implements PlugInFilter 
{
	ImagePlus imp;

	static int[] HFILTER = {1, 10, 20};
	static int[] VFILTER = {1, 10, 20};
	final static int[] FAILFILTER = {1, 1, 1};

	static int hfiltersize = HFILTER.length / 2;
	static int vfiltersize = VFILTER.length / 2;

	int height, width;

	/**
	 * @return KUN 8-bit gråskala bilder tillat
	 */
	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

	/**
	 * Metoden henter ut og lager de viktige objektene for å kunne
	 * kjøre filterene. Den tar så for seg jobben av å gå igjennom 
	 * hvert bildepunkt og filtrere det. Dette gjør den for både
	 * horisontalt og vertikalt filter. Tilslutt settes de to 
	 * resultatbildene sammen.
	 * @author Magnus Poppe Wang
	 */
	public void run(ImageProcessor ip)
	{
		height = ip.getHeight();
		width = ip.getWidth();

		int filtersum = getWeightedSum(HFILTER, VFILTER);

		ImageProcessor hcopy = ip.duplicate();
		ImageProcessor vcopy = ip.duplicate();

		for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
		{	
			hcopy.putPixel( x, y, useHFilter(x, y, ip)/filtersum );
			vcopy.putPixel( x, y, useVFilter(x, y, ip)/filtersum );
		}

		for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
			ip.putPixel( x, y, hcopy.getPixel(x,y)+vcopy.getPixel(x,y) );

		// imp = combineIntoImage(hcopy, vcopy);
		// imp.updateAndDraw();
	}

	/**
	 * Bruker det horisontale filteret. Denne går igjennom piksler før
	 * og etter pikselen i fokus, så gjør en summering av denne og dens
	 * naboer.
	 *
	 * Jeg fant ingen god måte å kombinere denne 
	 * med "useVFilter( )" metoden. 
	 * @author Magnus Poppe Wang
	 */
	private int useHFilter(int x, int y, ImageProcessor ip)
	{
		int sum = 0; 
		// HORISONTALT FILTER:
		for (int filter = 0; filter < HFILTER.length; filter++)
		{
			// Vi forsikrer oss om at vi er innenfor bildet.
			if ( y-(filter-hfiltersize) < 0 
			||   y+(filter-hfiltersize) > height-1) 
				continue;

			sum += ip.getPixel(x, y+(filter-hfiltersize) )*HFILTER[filter];
		}
		return sum;
	}

	/**
	 * Bruker det vertikale filteret. Denne går igjennom piksler før
	 * og etter pikselen i fokus, så gjør en summering av denne og dens
	 * naboer.
	 *
	 * Jeg fant ingen god måte å kombinere denne 
	 * med "useHFilter( )" metoden. 
	 * @author Magnus Poppe Wang
	 */
	private int useVFilter(int x, int y, ImageProcessor ip)
	{
		int sum=0;
		// VERTIKALT FILTER:
		for (int filter = 0; filter < VFILTER.length; filter++)
		{
			// Vi forsikrer oss om at vi er innenfor bildet.
			if ( x-(filter-vfiltersize) < 0 
			  || x+(filter-vfiltersize) > width-1) 
				continue;

			sum += ip.getPixel(x+(filter-vfiltersize), y )*VFILTER[filter];
		}
		return sum;
	}

	/**
	 * Får inn to ferdigfiltrerte bilder og kombinerer dem inn i et bilde.
	 * Denne er ikke i bruk fordi den lager en ny kopi. For å kunne kjøre
	 * filteringen mange ganger, er det gunstigere å kun gjøre endringer
	 * i originalen.
	 * @author Magnus Poppe Wang
	 */
	private ImagePlus combineIntoImage(ImageProcessor img1, ImageProcessor img2) 
	{
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
			return null;

		ImageProcessor processor = new ByteProcessor(height,width);  
		ImagePlus result = new ImagePlus("New filtered version", processor);  
	
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				processor.putPixel( x, y, img1.getPixel(x,y)+img2.getPixel(x,y) );

		return result;
	}

	/**
	 * Heter ut summen av alle verdiene i de to filterene slik at man får en 
	 * faktor å dele på i run metoden. Denne metoden tillater også at man
	 * bruker vekting i filterene. 
 	 * @author Magnus Poppe Wang
	 */
	private int getWeightedSum(int[] f1, int[] f2)
	{
		try{
			int sum = 0;
			for (int i = 0; i < Math.max(f1.length, f2.length); i++) sum += f1[i] + f2[i];
			return sum;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Filters must be same size.");
		}
		return -1;
	}
			
	/**
	 * Metoden gir brukeren et dialogvindu som spør etter prosent
	 * @author Magnus Poppe Wang
	 * @return tall mellom 0 og 1 oppgitt av bruker.
	 */	
	private int[] promptUserFilter() 
	{
		GenericDialog gd = new GenericDialog("Filterstørrelse");
		gd.addNumericField("Antall felter i filteret", 3, 1);
		gd.showDialog();

		if (gd.wasCanceled()) return FAILFILTER;

		int p = (int)gd.getNextNumber();
		if (p % 2 == 0) p += 1;

		int[] filter = new int[p];
		for (int i = 0; i < p; i++) filter[i] = 1;
		return filter;
	}
}

// END OF FILE
