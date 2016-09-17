import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Task_1_Oblig_4 implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return NO_IMAGE_REQUIRED;
	}

	public void run(ImageProcessor ip) {
		int[][] set = getTaskExampleSet();

		for (int y = 0; y < set.length; y++)
			for (int x = 0; x < set[0].length; x++)
				ip.putPixel(x, y, set[y][x]);

		imp.show();
	}

	private int[][] getTaskExampleSet()
	{
		return 
		{
			{3, 4, 5, 5, 6},
			{3 ,3 ,4 ,4 ,5},
			{3, 3, 4, 3, 3},
			{2 ,3, 4, 3, 2},
			{1 ,2 ,2 ,3 ,3},
			{0 ,1 ,1 ,2 ,0}
		};
	}

}
