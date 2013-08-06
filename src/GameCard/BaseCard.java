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
	private String flavourText;
	private int type;
	private int totalTargetsRequired;

	public BaseCard(String n, String sd, String d, String ft, int t, int targets)
	{
		if(t != ALLY && t != ENEMY && t != BOTH)
		{
			throw new IllegalArgumentException("Type: " + type);
		}
		name = n;
		shortDescription = sd;
		description = d;
		flavourText  = ft;
		type = t;
		totalTargetsRequired = targets;
	}

	public String getName() { return name;}
	public String getLongDescription() {return description;}
	public String getShortDescription() {return shortDescription;}
	public String getFlavourText() {return flavourText;}
	public int getType() {return type;}
	public int getTotalTargetsRequired() {return totalTargetsRequired; }

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
	 * @return whether the player gets to play again or not. Most cards will return false, some may return true.
	 */
	public abstract boolean performAction(Side8Wrapper currentStatus, ArrayList<Integer> targets);

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