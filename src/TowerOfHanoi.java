import java.util.*;

public class TowerOfHanoi
{
	public static final int FIRST = 1;
	public static final int SECOND = 2;
	public static final int THIRD = 3;

	private ArrayList<TowerOfHanoiItem> firstTower;
	private ArrayList<TowerOfHanoiItem> secondTower;
	private ArrayList<TowerOfHanoiItem> thirdTower;
	public static int movements = 0;

	public TowerOfHanoi(int num)
	{
		firstTower = new ArrayList<>();
		secondTower = new ArrayList<>();
		thirdTower = new ArrayList<>();
		for(int i = 1; i <= num; i++)
		{
			firstTower.add(new TowerOfHanoiItem(i));
		}
	}

	public void move(int towerToStart, int towerToEnd)
	{
		TowerOfHanoiItem itemToMove = null;
		TowerOfHanoiItem lowestItemInNewTower = null;
		switch(towerToStart)
		{
			case FIRST:
				itemToMove = firstTower.get(0);
				break;
			case SECOND:
				itemToMove = secondTower.get(0);
				break;
			case THIRD:
				itemToMove = thirdTower.get(0);
				break;
		}
		switch(towerToEnd)
		{
			case FIRST:
				if(firstTower.size() > 0)
				{
					lowestItemInNewTower = firstTower.get(0);
				}
				break;
			case SECOND:
				if(secondTower.size() > 0)
				{
					lowestItemInNewTower = secondTower.get(0);
				}
				break;
			case THIRD:
				if(thirdTower.size() > 0)
				{
					lowestItemInNewTower = thirdTower.get(0);
				}
				break;
		}
		int sizeToCheckAgainst = lowestItemInNewTower == null ? -1 : lowestItemInNewTower.getSize();
		if(itemToMove.getSize() < sizeToCheckAgainst || sizeToCheckAgainst == -1)
		{
			switch(towerToStart)
			{
				case FIRST:
					firstTower.remove(0);
					break;
				case SECOND:
					secondTower.remove(0);
					break;
				case THIRD:
					thirdTower.remove(0);
					break;
			}
			switch(towerToEnd)
			{
				case FIRST:
					firstTower.add(0, itemToMove);
					break;
				case SECOND:
					secondTower.add(0, itemToMove);
					break;
				case THIRD:
					thirdTower.add(0, itemToMove);
					break;
			}
		}
		else
		{
			throw new RuntimeException("FU: Current tower: \n" + this.toString() + "Movement from " + towerToStart + " to " + towerToEnd);
		}
		movements++;
	}

	@Override
	public String toString()
	{
		String result = "";
		int maximumSize = firstTower.size() + secondTower.size() + thirdTower.size();
		int requiredSpacing = 1 + new TowerOfHanoiItem(maximumSize).toString().length()/2;
		int maximumHeight = firstTower.size() + secondTower.size() + thirdTower.size();
		int firstTowerDifference = maximumHeight - firstTower.size();
		int secondTowerDifference = maximumHeight - secondTower.size();
		int thirdTowerDifference = maximumHeight - thirdTower.size();
		int numToCheck;
		String resultPrinted;
		for(int i = 0; i < maximumHeight; i++)
		{
			//First tower
			if(i >= firstTowerDifference)
			{
				numToCheck = i - firstTowerDifference;
				resultPrinted = firstTower.get(numToCheck).toString();
				for(int j = 0; j < requiredSpacing - (resultPrinted.length() - 1)/2; j++)
				{
					result += " ";
				}
				result += resultPrinted;
				for(int j = 0; j < requiredSpacing - (resultPrinted.length() - 1)/2; j++)
				{
					result += " ";
				}
			}
			else
			{
				for(int j = 0; j < requiredSpacing; j++)
				{
					result += " ";
				}
				result += "|";
				for(int j = 0; j < requiredSpacing; j++)
				{
					result += " ";
				}
			}
			result += "N";
			//Second tower
			if(i >= secondTowerDifference)
			{
				numToCheck = i - secondTowerDifference;
				resultPrinted = secondTower.get(numToCheck).toString();
				for(int j = 0; j < requiredSpacing - (resultPrinted.length() - 1)/2; j++)
				{
					result += " ";
				}
				result += resultPrinted;
				for(int j = 0; j < requiredSpacing - (resultPrinted.length() - 1)/2; j++)
				{
					result += " ";
				}
			}
			else
			{
				for(int j = 0; j < requiredSpacing; j++)
				{
					result += " ";
				}
				result += "|";
				for(int j = 0; j < requiredSpacing; j++)
				{
					result += " ";
				}
			}
			result += "N";
			//Third tower
			if(i >= thirdTowerDifference)
			{
				numToCheck = i - thirdTowerDifference;
				resultPrinted = thirdTower.get(numToCheck).toString();
				for(int j = 0; j < requiredSpacing - (resultPrinted.length() - 1)/2; j++)
				{
					result += " ";
				}
				result += resultPrinted;
				for(int j = 0; j < requiredSpacing - (resultPrinted.length() - 1)/2; j++)
				{
					result += " ";
				}
			}
			else
			{
				for(int j = 0; j < requiredSpacing; j++)
				{
					result += " ";
				}
				result += "|";
				for(int j = 0; j < requiredSpacing; j++)
				{
					result += " ";
				}
			}
			result += "\n";
		}
		return result;
	}
}
