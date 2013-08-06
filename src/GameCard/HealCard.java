package GameCard;

import java.util.*;

public abstract class HealCard extends BaseCard
{
	public HealCard(String n, String sd, String d, String ft, int targets)
	{
		super(n, sd, d, ft, ALLY, targets);
	}
}
