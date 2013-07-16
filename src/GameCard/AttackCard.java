package GameCard;

import java.util.*;

public class AttackCard extends BaseCard
{
	public AttackCard()
	{
		super();
	}

	public AttackCard(String n, String d)
	{
		super(n, d, ENEMY);
	}
}
