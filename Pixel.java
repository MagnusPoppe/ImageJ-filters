

public class Pixel
{
	int x;
	int y;
	int value;

	public pixel(int x, int y, int value)
	{
		this.x 		= x;
		this.y 		= y;
		this.value 	= value;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getValue() {
		return value;
	}	

	public int setX(int variable) {
		x = variable;
	}
	public int setY(int variable) {
		y = variable;
	}
	public int setValue(int variable) {
		value = variable;
	}
}