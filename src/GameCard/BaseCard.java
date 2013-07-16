package GameCard;

import java.util.*;

public abstract class BaseCard
{
	public static final int ALLY = 1;
	public static final int ENEMY = 2;
	public static final int BOTH = 3;

	private String name;
	private String description;
	private int type;
	private int change;

	public BaseCard()
	{

	}

	public BaseCard(String n, String d, int t)
	{
		if(t != ALLY && t != ENEMY && t != BOTH)
		{
			throw new IllegalArgumentException("Type: " + type);
		}
		name = n;
		description = d;
		type = t;
	}

	public static String getAttackType(int type)
	{
		switch(type)
		{
			case ALLY:
				return "Ally";
			case ENEMY:
				return "Enemy";
			case BOTH:
				return "Both";
			default:
				throw new IllegalArgumentException("Type: " + type);
		}
	}

	@Override
	public String toString()
	{
		return
				"---------------------------" +
				"| " + name + " |" +
				"|                    " + getAttackType(type) + " |" +
				"|                          |" +
				"| " + description + " |" +
				"---------------------------";
	}
}