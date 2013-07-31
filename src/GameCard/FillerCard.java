package GameCard;

import Side8Items.Side8Board;
import java.util.*;

public class FillerCard extends BaseCard
{
	public FillerCard()
	{
		super("Card Filler", "Description Description Description Description Description Description", "This is a filler card.", BOTH);
	}

	@Override
	public int performAction(Side8Board board, ArrayList<Integer> targets)
	{
		//Filler card deals 0 damage and does absolutely nothing
		return 0;
	}


}
