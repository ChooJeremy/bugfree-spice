import java.util.*;

public class TowerOfHanoiItem
{
	private int size;

	public TowerOfHanoiItem(int sizeToStart)
	{
		size = sizeToStart;
	}

	public int getSize() {
		return size;
	}

	public String toString()
	{
		String result = "";
		for(int i = 1; i < size * 2; i++)
		{
			result += "X";
		}
		return result;
	}
}
