package GameCard;

import java.util.*;

public abstract class HealCard extends BaseCard
{
	public HealCard()
	{
		super();
	}

	public HealCard(String n, String sd, String d)
	{
		super(n, sd, d, ALLY);
	}
}
