


public class FloodFill {

	final static int BLACK =   0;
	final static int WHITE = 255;

	static void runRecursive(ImageProcessor ip, int x, int y, int label)
	{
		normal(ip, x, y, label);
	}
	

	static void recursive(ImageProcessor ip, int x, int y, int label) 
	{
		// Tester at vi fortsatt er innenfor bildet:
		if (x >= 0 && x < ip.getWidth() && y >= 0 && y < ip.getHeight())
		{
			if ( ip.getPixel(x, y) == WHITE )
			{
				// Marking the position
				ip.putPixel(x, y, label);

				// Preforming recursive discovery
				normal(ip, x+1, y,   label);
				normal(ip, x  , y+1, label);
				normal(ip, x  , y-1, label);
				normal(ip, x-1, y,   label);
			}
		}
	}

	static void breadth(ImageProcessor ip, int x, int y, int label) 
	{
		LinkedList<Pixel> queue = new LinkedList<>();

		queue.add( new Pixel(x, y, ip.getPixel(x, y)) );

		while ( ! queue.isEmpty() )
		{
			Pixel p = queue.getFirst();
			x = p.getX();
			y = p.getY();

			if(x >= 0 && x < ip.getWidth() && y >= 0 && y < ip.getHeight())
			{
				if ( ip.getPixel(x, y) == WHITE )
				{
					ip.putPixel( p.getX(), p.getY(), label );
		
					queue.add( new Pixel( x+1, y  , ip.getPixel(x+1, y  ) ) );
					queue.add( new Pixel( x  , y+1, ip.getPixel(x  , y+1) ) );
					queue.add( new Pixel( x-1, y  , ip.getPixel(x-1, y  ) ) );
					queue.add( new Pixel( x  , y-1, ip.getPixel(x  , y-1) ) );
				}
			}
		}
	}

	static void depth(ImageProcessor ip, int x, int y, int label) 
	{
		LinkedList<Pixel> stack = new LinkedList<>();

		stack.add( new Pixel(x, y, ip.getPixel(x, y)) );

		while ( ! stack.isEmpty() )
		{
			Pixel p = stack.pop();
			x = p.getX( );
			y = p.getY( );

			if(x >= 0 && x < ip.getWidth() && y >= 0 && y < ip.getHeight())
			{
				if ( ip.getPixel(x, y) == WHITE )
				{
					ip.putPixel( p.getX(), p.getY(), label );
		
					stack.push( new Pixel(x+1, y  , ip.getPixel(x+1, y  )) );
					stack.push( new Pixel(x  , y+1, ip.getPixel(x  , y+1)) );
					stack.push( new Pixel(x-1, y  , ip.getPixel(x-1, y  )) );
					stack.push( new Pixel(x  , y-1, ip.getPixel(x  , y-1)) );
				}
			}
		}
	}
	
}