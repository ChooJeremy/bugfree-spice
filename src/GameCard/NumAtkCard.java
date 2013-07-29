package GameCard;

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
}
