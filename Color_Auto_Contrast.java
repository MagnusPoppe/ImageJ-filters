import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Color_Auto_Contrast implements PlugInFilter 
{
	ImagePlus imp;


	float CONTRAST = 0.05f;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_RGB;
	}

	public void run(ImageProcessor ip) 
	{
		// Oppgave 1:
		ImagePlus opg1 = new ImagePlus(
			"Task 1: RGB CONTRAST CHANGE",
			AutoContrast.RGB(ip, CONTRAST)
		);
		opg1.show();

		// Oppgave 2:
		/**
		 * HOVEDSAKELIG HSB metoden i AutoContrast.java
		 * Bildet som hører med er opg2b
		 */


		ImagePlus opg2a = new ImagePlus(
			"Task 2: HSB ENHANCE",
			AutoContrast.HSBEnhance(ip, CONTRAST)
		);
		opg2a.show();

		ImagePlus opg2b = new ImagePlus(
			"Task 2: HSB CONTRAST CHANGE",
			AutoContrast.HSB(ip, CONTRAST)
		);
		opg2b.show();
	}

}
