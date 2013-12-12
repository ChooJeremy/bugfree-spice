package GameCard;

import Side8Items.*;

import java.util.*;

public class NumAtkCard extends AttackCard
{
	int selectedNumber;

	public NumAtkCard(int number)
	{
		super(number + " destructor",
				"All cards with " + number + " takes 3 damage",
				"Targets all cards on the battlefield with the number " + number + ". Deal 3 damage to all of them.",
				"When you're hated by others and hunted down, don't expect to survive for long...",
				0);
		selectedNumber = number;
	}

	@Override
	protected void startAnimations()
	{
		//This card does not do any animations
		informListeners(0);
	}

	@Override
	public boolean performAction(Side8Wrapper s8w, ArrayList<Side8BoardTarget> targets)
	{
		Side8Board board = s8w.getBoard();

		for(int i = 0; i < 9; i++)
		{
			if(board.getBoardNumber(i) == selectedNumber)
			{
				board.getBoardItem(i).takeDamage(3);
			}
		}
		return false;
	}

	@Override
	public ArrayList<Integer> getAISelectionOfTargets(Side8Wrapper currentStatus)
	{
		return new ArrayList<>();
	}
}
