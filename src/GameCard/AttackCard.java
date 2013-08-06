package GameCard;

import java.util.*;

public abstract class AttackCard extends BaseCard
{
	public AttackCard(String n, String sd, String d, String ft, int targets)
	{
		super(n, sd, d, ft, ENEMY, targets);
	}
}
