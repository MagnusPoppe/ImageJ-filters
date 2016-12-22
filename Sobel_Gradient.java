import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Sobel_Gradient implements PlugInFilter 
{

	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

	// SOBEL-FILTER FOR TRAVERSERING AV X-AKSEN
	int[][] SOBEL_FILTER_EAST = {
		{-1, 0, 1},
		{-2, 0, 2},
		{-1, 0, 1}
	};

	// SOBEL-FILTER FOR TRAVERSERING AV Y-AKSEN
	int[][] SOBEL_FILTER_SOUTH = {
		{-1, -2, -1},
		{ 0,  0,  0},
		{ 1,  2,  1}
	};	

	// SOBEL-FILTER FOR TRAVERSERING AV X-AKSEN
	int[][] PREWITT_FILTER_EAST = {
		{-1, 0, 1},
		{-2, 0, 2},
		{-1, 0, 1}
	};

	// SOBEL-FILTER FOR TRAVERSERING AV Y-AKSEN
	int[][] PREWITT_FILTER_SOUTH = {
		{-1, -2, -1},
		{ 0,  0,  0},
		{ 1,  2,  1}
	};

	// FILTER MED GLATTING:
	int[][] LAPLACE_GRADIENT = {
		{ 0, -1,  0},
		{-1,  4, -1},
		{ 0, -1,  0}
	};
	
	int LAPLACE_WEIGHT = 5;

	int[][] xFilterInUse;
	int[][] yFilterInUse;

	float COLOR_IMAGE_BRIGHTNESS = 0.5f;
	float GREYSCALE = 255.0f;

	/** 
	 * Run metoden utfører det overordnete arbeidet for Sobel-kantdeteksjon.
	 * 
	 */
	public void run(ImageProcessor ip) 
	{
		// DEFINERER VERDIER OG VARIABLER:
		int height = ip.getHeight();
		int width  = ip.getWidth();

		// DEFINTERER FILTRE I BRUK. DISSE BRUKES GJENNOM KODEN.
		xFilterInUse = SOBEL_FILTER_EAST;
		yFilterInUse = SOBEL_FILTER_SOUTH;
		
		// DUPLISERER BILDET FOR BRUK SOM FILTRERT BILDE.
		ImageProcessor copyX = ip.duplicate();
		ImageProcessor copyY = ip.duplicate();

		// GÅR IGJENNOM BILDET:
		for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
		{
			copyX.putPixel(x, y, FilterTools.sumFromFilter(x, y, xFilterInUse, ip));
			copyY.putPixel(x, y, FilterTools.sumFromFilter(x, y, yFilterInUse, ip));
		}	

		// HENTER UT OG VISER DE TO FORSKJELLIGE GRADIENTBASERTE BILDENE:
		ImagePlus intensity = FilterTools.getGradiantIntensity(copyX, copyY);
		ImagePlus direction = FilterTools.getGradiantDirection(copyX, copyY);
		direction.show();
		intensity.show();

		// OPPGAVE 2: LAGER FARGEVERSJON AV BILDET:
		ImagePlus color = combineForColor(intensity, direction);
		color.show();
	}

	private ImagePlus combineForColor(ImagePlus intensity, ImagePlus direction)
	{
		ImageProcessor i 		= intensity.getChannelProcessor();
		ImageProcessor d 		= direction.getChannelProcessor(); 
		ImageProcessor ip 		= new ColorProcessor(i.getWidth(), i.getHeight());
		ImagePlus colorVersion 	= new ImagePlus("Colorized Version", ip);

		for (int y = 0; y < i.getHeight(); y++)
		for (int x = 0; x < i.getWidth(); x++)
		{
			float hue 		 = (float)d.getPixel(x, y) / GREYSCALE;
			float saturation = (float)i.getPixel(x, y) / GREYSCALE;

			ip.setColor(Color.getHSBColor(hue, saturation, COLOR_IMAGE_BRIGHTNESS));
			ip.drawPixel(x, y);
		}
		return colorVersion;
	}
}
