import GameCard.*;
import Side8Items.*;

import java.util.*;

public class Side8Wrapper
{
	private ArrayList<BaseCard> playerHand;
	private ArrayList<BaseCard> opponentHand;
	private Side8Board board;
	//Attributes to store the cards
	private ArrayList<Integer> cardsBeingUsed;

	public ArrayList<BaseCard> getPlayer() {return playerHand; }
	public ArrayList<BaseCard> getOpponent() {return opponentHand; }
	public Side8Board getBoard() {return board; }

	public Side8Wrapper()
	{
		board = new Side8Board();
		playerHand = new ArrayList<>();
		opponentHand = new ArrayList<>();
		cardsBeingUsed = new ArrayList<>();
		restart();
	}

	public void restart()
	{
		//Reset all the variables
		board = new Side8Board();
		playerHand = new ArrayList<>();
		opponentHand = new ArrayList<>();
		cardsBeingUsed = new ArrayList<>();

		ArrayList<BaseCard> startingDeck = initDeck(4);
		for(int i = 0; i < 7; i++)
		{
			playerHand.add(startingDeck.get(i));
		}
		startingDeck = initDeck(4);
		for(int i = 0; i < 7; i++)
		{
			opponentHand.add(startingDeck.get(i));
		}
		Collections.sort(playerHand);
		Collections.sort(opponentHand);
	}

	public void setCardNo(int number)
	{
		if(cardsBeingUsed.size() == 0)
		{
			cardsBeingUsed.add(number);
		}
		else
		{
			cardsBeingUsed.remove(0);
			cardsBeingUsed.add(0, number);
		}
	}

	public Integer getCardNo()
	{
		return cardsBeingUsed.size() == 0 ? null : cardsBeingUsed.get(0);
	}

	public boolean addTargetSelection(int number)
	{
		if(cardsBeingUsed.size() != 0)
		{
			cardsBeingUsed.add(number);
			return true;
		}
		else
		{
			return false;
		}
	}

	public ArrayList<Integer> getTargetSelection()
	{
		if(cardsBeingUsed.size() == 0)
		{
			return null;
		}
		ArrayList<Integer> result = new ArrayList<>();
		for(int i = 1; i < cardsBeingUsed.size(); i++)
		{
			result.add(cardsBeingUsed.get(i));
		}
		return result;
	}

	public void finishSelection()
	{
		cardsBeingUsed.clear();
	}

	public static ArrayList<BaseCard> initDeck(int totalRepeats)
	{
		ArrayList<BaseCard> result = new ArrayList<>();
		for(int i = 0; i < totalRepeats; i++)
		{
			for(int j = 1; j <= 10; j++)
			{
				result.add(new StartCard(j));
			}
		}
		Jeremy.randomize(result);
		return result;
	}

	public String getEverythingInString()
	{
		String result = "[";
		for(int i = 0; i < opponentHand.size(); i++)
		{
			result += opponentHand.get(i).getShortDescription();
			if(i != opponentHand.size() - 1)
			{
				result += "  ";
			}
		}
		result += "]\n";
		result += this.getBoard().getBoardView() + "\n[";
		for(int i = 0; i < playerHand.size(); i++)
		{
			result += playerHand.get(i).getShortDescription();
			if(i != playerHand.size() - 1)
			{
				result += "  ";
			}
		}
		result += "]";
		return result;
	}
}
