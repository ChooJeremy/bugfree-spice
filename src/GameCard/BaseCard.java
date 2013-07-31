package GameCard;

import Side8Items.*;
import java.util.*;

public abstract class BaseCard implements Comparable<BaseCard>
{
	public static final int ALLY = 1;
	public static final int ENEMY = 2;
	public static final int BOTH = 3;

	private String name;
	private String description;
	private String shortDescription;
	private int type;
	private int change;

	public BaseCard()
	{

	}

	public BaseCard(String n, String sd, String d, int t)
	{
		if(t != ALLY && t != ENEMY && t != BOTH)
		{
			throw new IllegalArgumentException("Type: " + type);
		}
		name = n;
		shortDescription = sd;
		description = d;
		type = t;
	}

	public String getName() { return name;}
	public String getLongDescription() {return description;}
	public String getShortDescription() {return shortDescription;}
	public int getType() {return type;}

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

	/**
	 * Performs the action for this card. Pass in the current board and the target selected (0-8) and the method will perform
	 * the rest, removing and increasing certain numbers on the board as necessary
	 *
	 * @param currentStatus the current game board and the player's hands.
	 * @param targets the targets selected (0-8), horizontal first. Cards that only take in 1 target should have a limit of 1 in this
	 *                arraylist.
	 * @return the total damage or healing done to the board. If both healing and damage is dealt, the total is given.
	 *         For example, this card deals 2 damage to a unit, heals another unit for 2 hp. The targeted unit in this board
	 *         currently has a number of 1. This will cause it to deal 1 damage, heal another for 2, then, because the "1" card
	 *         is defeated, in turns to 3 in favour of the user, thereby causing a total difference of 1 + 2 + 3, or 6. The method
	 *         will thus return 6.
	 */
	public abstract int performAction(Side8Wrapper currentStatus, ArrayList<Integer> targets);

	@Override
	public String toString()
	{
		return name + "(" + getAttackType(type) + ") :\n" + description;
	}

	@Override
	public int compareTo(BaseCard o)
	{
		//This method is really only used for the start card.
		return 0;
	}
}