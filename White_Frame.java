import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import javax.swing.JOptionPane;
import ij.plugin.filter.*;

/**
 * Klassen lager en ramme rundt bildet på gitt størrelse
 *
 * @author Magnus Poppe Wang
 */
public class White_Frame implements PlugInFilter {
	ImagePlus imp;
	int height, width;
	final static private int DEFAULT_FRAMESIZE  = 10;
	final static private int FRAMECOLOR = (int)Math.pow(255, 8);


	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {
		makeFrame(getUserInput(), ip);
	}

	/** 
	 * Lager en ramme rundt bildet med gitt størrelse.
	 * 
	 * @author Magnus Poppe Wang
	 */
	private void makeFrame( int frame, ImageProcessor ip ) 
	{
		height = ip.getHeight();
		width  = ip.getWidth();

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < frame; x++)
			{
				ip.putPixel(x, y, FRAMECOLOR);
			}
			for (int x = width; x > width-frame; x--)
			{
				ip.putPixel(x, y, FRAMECOLOR);
			}
		}

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < frame; y++)
			{
				ip.putPixel(x, y, FRAMECOLOR);
			}
			for (int y = height; y > height-frame; y--)
			{
				ip.putPixel(x, y, FRAMECOLOR);
			}
		}
	}

	/**
	 * Henter inn data fra bruker. Spesialisert for dette caset.
	 *
	 * @return integer given by user.
	 * @author Magnus Poppe Wang
	 */
	private int getUserInput() 
	{
		// Eventuell løsning der rammen er 10% av bildets størrelse:
		//int FRAMESIZE = (int)(width*0.10);

		int frame = DEFAULT_FRAMESIZE;
		boolean inputComplete = false;

		while ( !inputComplete ) 
		{
			String input = JOptionPane.showInputDialog("Velg en rammestørrelse:");
			try 
			{
				frame = Integer.parseInt(input);
				if (frame > width || frame > height) 
					inputComplete = true;
				else
					JOptionPane.showMessageDialog(null, "Rammen kan ikke være større enn bildet.");
			}
			catch( Exception e ) 
			{
				JOptionPane.showMessageDialog(null, "Inndata må være heltall. Prøv igjen");
			}
		}
		return frame;
	}
}
