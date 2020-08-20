package Utilities;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class Dimension {

	public final int X;
	public final int Y;
	public final int W;
	public final int H;

	public Dimension(int x, int y, int w, int h) {
		X = x;
		Y = y;
		W = w;
		H = h;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + H;
		result = prime * result + W;
		result = prime * result + X;
		result = prime * result + Y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dimension other = (Dimension) obj;
		if (H != other.H)
			return false;
		if (W != other.W)
			return false;
		if (X != other.X)
			return false;
		if (Y != other.Y)
			return false;
		return true;
	}

}
