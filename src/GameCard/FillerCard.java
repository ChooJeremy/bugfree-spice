package GameCard;

import Side8Items.Side8Board;
import Side8Items.Side8BoardTarget;
import Side8Items.Side8Wrapper;

import java.util.*;

public class FillerCard extends BaseCard
{
	public FillerCard()
	{
		super("Card Filler", "Description Description Description Description Description Description", "This is a filler card.", "It's said that this was one of the first cards ever created...", BOTH, 0);
	}

	@Override
	protected void startAnimations()
	{
		//This card does not do any animations
		informListeners(0);
	}

	@Override
	public boolean performAction(Side8Wrapper s8w, ArrayList<Side8BoardTarget> targets)
	{
		//Filler card deals 0 damage and does absolutely nothing
		return false;
	}

	@Override
	public ArrayList<Integer> getAISelectionOfTargets(Side8Wrapper currentStatus)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isValidTarget(Side8Wrapper currentStatus, ArrayList<Side8BoardTarget> targets)
	{
		return targets.size() == 0;
	}


}
