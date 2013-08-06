package GameCard;

import Side8Items.Side8Board;
import Side8Items.Side8Wrapper;

import java.util.*;

public class FillerCard extends BaseCard
{
	public FillerCard()
	{
		super("Card Filler", "Description Description Description Description Description Description", "This is a filler card.", "It's said that this was one of the first cards ever created...", BOTH, 0);
	}

	@Override
	public boolean performAction(Side8Wrapper s8w, ArrayList<Integer> targets)
	{
		//Filler card deals 0 damage and does absolutely nothing
		return false;
	}


}
