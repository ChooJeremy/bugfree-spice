package GameCard;

import java.util.*;

public abstract class HealCard extends BaseCard
{
	public HealCard(String n, String sd, String d, int targets)
	{
		super(n, sd, d, ALLY, targets);
	}
}
