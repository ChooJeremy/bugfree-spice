package GameCard;

import Listeners.ThrowListener;
import Side8Items.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseCard implements Comparable<BaseCard>, Runnable
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
	private BufferedImage image;
	private ArrayList<ThrowListener> listeners;

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
		listeners = new ArrayList<>();
	}

	public String getName() { return name;}
	public String getLongDescription() {return description;}
	public String getShortDescription() {return shortDescription;}
	public String getFlavourText() {return flavourText;}
	public int getType() {return type;}
	public int getTotalTargetsRequired() {return totalTargetsRequired; }
	public BufferedImage getImage() {return image; }

	public void setImage(String imageFilePath)
	{
		try
		{
			image = ImageIO.read(new File(imageFilePath));
		}
		catch (IOException ioe)
		{
			System.out.println("Cannot set image for: " + this.getName());
			ioe.printStackTrace();
		}
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

	public void addListener(ThrowListener tl)
	{
		if(!listeners.contains(tl))
		{
			listeners.add(tl);
		}
	}

	/**
	 * Informas all the listeners about an event
	 *
	 * @param status the status code to tell the listeners
	 */
	protected void informListeners(int status)
	{
		for(ThrowListener tl : listeners)
		{
			tl.catchEvent(status);
		}
	}

	public void run()
	{
		startAnimations();
	}

	/**
	 * Starts the animations that will be shown when this card is played. On end, please call informListeners.
	 *
	 */
	protected abstract void startAnimations();

	/**
	 * Performs the action for this card. Pass in the current game and the target selected (0-8) and the method will perform
	 * the rest, removing and increasing certain numbers on the board as necessary. Animations may need to be performed during this
	 * period of time. Therefore, the method call may end prematurely while all the values and animations are still upating or
	 * running. To be informed on when everything finishes, implement ThrowListener and add yourself as a listener.
	 *
	 * @param currentStatus the current game board and the player's hands.
	 * @param targets the targets selected (0-8), horizontal first. Cards that only take in 1 target should have a limit of 1 in this
	 *                arraylist.
	 * @return whether the player gets to play again or not. Most cards will return false, some may return true.
	 *
	 * @see BaseCard#getAISelectionOfTargets(Side8Items.Side8Wrapper) Finding the best target selection as an AI.
	 */
	public abstract boolean performAction(Side8Wrapper currentStatus, ArrayList<Integer> targets);

	/**
	 * Gets a list of targets that would be most beneficial to the "opponent" part in Side8Wrapper given the current circumstances,
	 * as dictated by the action of the card
	 *
	 * @param currentStatus the current game board and the player's hands
	 * @return the targets that would be best selected
	 *
	 * @see BaseCard#performAction(Side8Items.Side8Wrapper, java.util.ArrayList) Where the action is actually performed
	 */
	public abstract ArrayList<Integer> getAISelectionOfTargets(Side8Wrapper currentStatus);

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