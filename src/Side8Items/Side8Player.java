package Side8Items;

import GameCard.*;
import Helper.*;

import java.util.*;

public class Side8Player
{
	ArrayList<BaseCard> hand;
	ArrayList<BaseCard> deck;

	public ArrayList<BaseCard> getHand() {return hand; }
	public ArrayList<BaseCard> getDeck() {return deck; }

	public Side8Player ()
	{
		hand = new ArrayList<>();
		deck = new ArrayList<>();
	}

	public BaseCard draw()
	{
		hand.add(deck.get(0));
		deck.remove(0);
		return hand.get(hand.size() - 1);
	}

	public void shuffleDeck()
	{
		JeremyCopy.randomize(deck);
	}

	public String getHandInString()
	{
		String result = "";
		for(int i = 0; i < hand.size(); i++)
		{
			result += hand.get(i).getShortDescription();
			if(i != hand.size() - 1)
			{
				result += "  ";
			}
		}
		return result;
	}

	public void removeCardFromHand(BaseCard cardToRemove)
	{
		hand.remove(cardToRemove);
	}

	public int getIndexOfCardInHand(BaseCard card)
	{
		return JeremyCopy.searchInArrayList(hand, card);
	}

	public boolean needsToDiscard()
	{
		return hand.size() > 1;
	}

	public void startTurn()
	{
		for(int i = 0; i < 4; i++)
		{
			try
			{
				draw();
			}
			catch(IndexOutOfBoundsException e)
			{
				if(i == 0)
				{
					throw e;
				}
				else
				{
					//Ignore, it may just be the last turn, caused by an effect.
					return;
				}
			}
		}
	}

	public void endTurn()
	{

	}

	public void initDeck()
	{
		Random random = new Random();
		deck.clear();
		//BETA
		while(deck.size() < 45)
		{
			deck.add(new NumAtkCard(random.nextInt(10) + 1));
		}
	}
}
