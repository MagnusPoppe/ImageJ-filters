import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Hist_test implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}

	public void run(ImageProcessor ip) {
		int[] hist = HistogramTools.toCumulative(ip.getHistogram());
		int[] normal = HistogramTools.normalize(
			ip.getHistogram(),
			ip.getWidth()*ip.getHeight()
		);
		ImagePlus img = HistogramTools.drawHistogram(hist, imp.getTitle(), ip.getWidth()*ip.getHeight());
		img.show();

		ImagePlus img2 = HistogramTools.drawHistogram(normal, imp.getTitle(), ip.getWidth()*ip.getHeight());
		img2.show();
	}

}