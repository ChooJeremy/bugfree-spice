package GameCard;

import Side8Items.*;

import java.util.*;

public class StartCard extends BaseCard
{
	private int thisNum;

	public int getThisNum() {return thisNum; }

	public StartCard(int num)
	{
		super("", "" + num, "" + num, BOTH);
		thisNum = num;
	}

	@Override
	public int performAction(Side8Wrapper s8w, ArrayList<Integer> targets)
	{
		Side8Board board = s8w.getBoard();
		if(targets.size() > 1)
		{
			throw new IllegalArgumentException(targets.toString());
		}
		else
		{
			int target = targets.get(0);
			board.setBoardNumber(target, thisNum);
			board.setStatus(target, Side8Board.ALLY);
			return thisNum;
		}
	}

	@Override
	public int compareTo(BaseCard o)
	{
		StartCard sc = (StartCard) o;
		return sc.getThisNum() - this.getThisNum();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof StartCard && ((StartCard) obj).getThisNum() == thisNum;
	}
}
