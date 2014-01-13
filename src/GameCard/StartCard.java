package GameCard;

import Side8Items.*;

import java.util.*;

public class StartCard extends BaseCard
{
	private int thisNum;

	public int getThisNum() {return thisNum; }

	public StartCard(int num)
	{
		super("StartCard", "" + num, "This card represents " + num, "A representation of something... big?", BOTH, 0);
		thisNum = num;
	}

	@Override
	protected void startAnimations()
	{
		//This card does not do any animations
		this.informListeners(0);
	}

	@Override
	public boolean performAction(Side8Wrapper s8w, ArrayList<Side8BoardTarget> targets)
	{
		Side8Board board = s8w.getBoard();
		if(targets.size() > 1)
		{
			throw new IllegalArgumentException(targets.toString());
		}
		else
		{
			int target = targets.get(0).getLocation();
			board.setBoardNumber(target, thisNum);
			board.setStatus(target, Side8Board.ALLY);
			return false;
		}
	}

	@Override
	public ArrayList<Integer> getAISelectionOfTargets(Side8Wrapper currentStatus)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isValidTarget(Side8Wrapper currentStatus, ArrayList<Side8BoardTarget> targets)
	{
		return targets.size() == 0;
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
