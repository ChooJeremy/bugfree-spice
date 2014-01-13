package GameCard;

import Helper.JeremyCopy;
import ImageHelpers.FadeImage;
import Side8Items.Side8Board;
import Side8Items.Side8BoardItem;
import Side8Items.Side8BoardTarget;
import Side8Items.Side8Wrapper;
import sun.util.logging.resources.logging_es;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class PunchCard extends AttackCard
{
	private JLayeredPane mainPane;
	private Point coordinates;

	public PunchCard()
	{
		super("The Punch",
				"Deal 4 damage to target enemy unit.",
				"Target an enemy unit on the board. It takes 4 damage.",
				"Simple, but effective. Never underestimate punching.",
				1);
	}

	@Override
	protected void startAnimations()
	{
		FadeImage fi = new FadeImage("images/punchicon.png", 1000);
		mainPane.add(fi, BaseCard.ANIMATIONPANE);
		//Align the coordinates such that the image appears in the middle of side8BoardItem
		coordinates.translate(fi.getImageWidth()/-2, fi.getImageHeight()/-2);
		//Add it
		fi.setBounds((int) coordinates.getX(), (int) coordinates.getY(), fi.getImageWidth(), fi.getImageHeight());

		new Thread(fi).start();
		JeremyCopy.pause(1000);
		informListeners(0);
	}

	@Override
	public boolean performAction(Side8Wrapper currentStatus, ArrayList<Side8BoardTarget> targets)
	{
		Side8Board board = currentStatus.getBoard();
		mainPane = currentStatus.getContentPane();
		Side8BoardTarget theTarget = targets.get(0);
		Side8BoardItem target = board.getBoardItem(theTarget.getLocation());

		coordinates = new Point(theTarget.getXCoordinate(), theTarget.getYCoordinate());
		//Move coordinates to the middle of the Side8BoardItem
		coordinates.translate(target.getWidth()/2, target.getHeight()/2);

		target.takeDamage(4);
		target.updateValues();
		return false;
	}

	@Override
	public ArrayList<Integer> getAISelectionOfTargets(Side8Wrapper currentStatus)
	{
		ArrayList<Integer> results = new ArrayList<>();
		Side8Board board = currentStatus.getBoard();
		int lowestNum = 9999;
		int lowestLocation = -1;
		for(int i = 0; i < board.getBoard().length; i++)
		{
			Side8BoardItem b = board.getBoardItem(i);
			if(b.getType() == Side8BoardItem.ALLY)
			{
				if(b.getCurrentNum() < lowestNum)           //Can be optimized further
				{                                           //You preferably want to punch someone lowest to 4 without going over
					lowestNum = b.getCurrentNum();
					lowestLocation = i;
				}
			}
		}
		results.add(lowestLocation);
		return results;
	}

	@Override
	public boolean isValidTarget(Side8Wrapper currentStatus, ArrayList<Side8BoardTarget> targets)
	{
		if(targets.size() == 1)
		{
			int targetType =  currentStatus.getBoard().getBoardItem(
								targets.get(0).getLocation()
								).getType();
			return targetType != Side8BoardItem.ALLY;
		}
		return false;
	}
}
