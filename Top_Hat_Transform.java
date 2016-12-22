import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Top_Hat_Transform implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G; // FANT INGEN "DOES_BINARY" eller liknende i API... 
	}

	public void run(ImageProcessor ip) {
		
		//ip.threshold(128); // GYLDEN MIDDELVERDI.
		ImagePlus white = whiteHatTransform(ip.duplicate());
		ImagePlus black = blackHatTransform(ip.duplicate());

		//white.show();
		black.show();
	}

	/**
	 * White hat transform er denne formelen: (bilde 1 - Bilde2) "open" bilde1
	 */
	public ImagePlus whiteHatTransform(ImageProcessor processor)
	{
		ImageProcessor ip 	= processor.duplicate();
		ImagePlus image 	= new ImagePlus("White-Hat", ip);

		// UTFØRER "OPENING" OPERASJONEN:
		// DETTE GJØRES PÅ PROCESSOR FORDI IP SKAL RETURNERES.
		processor.erode();
		processor.dilate();

		ip.copyBits(processor, 0, 0, Blitter.SUBTRACT);

		return image;
	}	

	/**
	 * Black hat transform er denne formelen: (Bilde1 "close" bilde2) - bilde 1
	 */
	public ImagePlus blackHatTransform(ImageProcessor processor)
	{
		ImageProcessor ip 	= processor.duplicate();
		ImagePlus image 	= new ImagePlus("Black-Hat", ip);

		// UTFØRER "Close" OPERASJONEN:
		ip.dilate();
		ip.erode();
		
		ip.copyBits(processor, 0, 0, Blitter.SUBTRACT);

		return image;
	}

}
