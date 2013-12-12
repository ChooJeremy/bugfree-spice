package GameCard;

import Side8Items.Side8Board;
import Side8Items.Side8BoardItem;
import Side8Items.Side8BoardTarget;
import Side8Items.Side8Wrapper;
import sun.util.logging.resources.logging_es;

import java.util.*;

public class PunchCard extends AttackCard
{
	public PunchCard()
	{
		super("The Punch",
				"Deal 4 damage to target enemy unit.",
				"Target an enemy unit on the board. It takes 4 damage.",
				"Simple, but effective. Never underestimate punching.",
				1);
	}

	@Override
	protected void startAnimations()
	{
		//No animations for now
		informListeners(0);
	}

	@Override
	public boolean performAction(Side8Wrapper currentStatus, ArrayList<Side8BoardTarget> targets)
	{
		Side8Board board = currentStatus.getBoard();
		Side8BoardItem target = board.getBoardItem(targets.get(0).getLocation());
		target.takeDamage(4);
		return false;
	}

	@Override
	public ArrayList<Integer> getAISelectionOfTargets(Side8Wrapper currentStatus)
	{
		ArrayList<Integer> results = new ArrayList<>();
		Side8Board board = currentStatus.getBoard();
		int lowestNum = 9999;
		int lowestLocation = -1;
		for(int i = 0; i < board.getBoard().length; i++)
		{
			Side8BoardItem b = board.getBoardItem(i);
			if(b.getType() == Side8BoardItem.ALLY)
			{
				if(b.getCurrentNum() < lowestNum)           //Can be optimized further
				{                                           //You preferably want to punch someone lowest to 4 without going over
					lowestNum = b.getCurrentNum();
					lowestLocation = i;
				}
			}
		}
		results.add(lowestLocation);
		return results;
	}
}
