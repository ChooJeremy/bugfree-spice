package Side8Items;

import java.awt.*;
import java.util.*;

public class Side8BoardTarget
{
	private int location;
	private int xCoordinate;
	private int yCoordinate;

	public Side8BoardTarget(int l, int x, int y)
	{
		location = l;
		xCoordinate = x;
		yCoordinate = y;
	}

	public Side8BoardTarget(int l, Point p)
	{
		location = l;
		xCoordinate = p.x;
		yCoordinate = p.y;
	}

	public int getLocation() { return location; }
	public int getXCoordinate() { return xCoordinate; }
	public int getYCoordinate() { return yCoordinate; }

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Side8BoardTarget)
		{
			return location == ((Side8BoardTarget) o).location;
		}
		else
		{
			return super.equals(o);
		}
	}

	@Override
	public String toString()
	{
		return "Board item " + location + ": (" + xCoordinate + ", " + yCoordinate + ")";
	}
}
