package GameCard;

import Side8Items.Side8Board;

import java.util.*;

public class NumAtkCard extends AttackCard
{
	int selectedNumber;

	public NumAtkCard()
	{

	}

	public NumAtkCard(int number)
	{
		super(number + " destructor", "All cards with " + number + " takes 3 damage", "Targets all cards on the battlefield with the number " + number + ". Deal 3 damage to all of them.");
		selectedNumber = number;
	}

	@Override
	public int performAction(Side8Board board, ArrayList<Integer> targets)
	{
		int totalDamage = 0;
		for(int i = 0; i < 9; i++)
		{
			if(board.getBoardNumber(i) == selectedNumber)
			{
				if(selectedNumber > 3)
				{
					totalDamage += 3;
					board.setBoardNumber(i, selectedNumber - 3);
				}
				else
				{
					totalDamage += selectedNumber;
					if(board.getStatus(i) == Side8Board.ALLY)
					{
						board.setBoardNumber(i, 0);
					}
					else
					{
						board.setBoardNumber(i, 3);
						board.setStatus(i, Side8Board.ALLY);
						totalDamage += 3;
					}
				}
			}
		}
		return totalDamage;
	}
}
