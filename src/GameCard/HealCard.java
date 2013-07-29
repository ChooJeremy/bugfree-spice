package GameCard;

import java.util.*;

public class HealCard extends BaseCard
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
