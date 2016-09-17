import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Filter_Plugin implements PlugInFilter 
{
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		return DOES_8G;
	}

	public void run(ImageProcessor ip) 
	{

	}

}
