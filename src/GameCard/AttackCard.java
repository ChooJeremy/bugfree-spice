package GameCard;

import java.util.*;

public class AttackCard extends BaseCard
{
	public AttackCard()
	{
		super();
	}

	public AttackCard(String n, String sd, String d)
	{
		super(n, sd, d, ENEMY);
	}
}
