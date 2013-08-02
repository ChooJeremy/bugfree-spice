package GameCard;

import java.util.*;

public abstract class AttackCard extends BaseCard
{
	public AttackCard(String n, String sd, String d, int targets)
	{
		super(n, sd, d, ENEMY, targets);
	}
}
