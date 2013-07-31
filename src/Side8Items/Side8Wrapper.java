package Side8Items;

import GameCard.*;
import Helper.JeremyCopy;

import java.util.*;

public class Side8Wrapper
{
	private Side8Player player;
	private Side8Player opponent;
	private Side8Board board;
	//Attributes to store the cards
	private ArrayList<Integer> cardsBeingUsed;

	public Side8Player getPlayer() {return player; }
	public Side8Player getOpponent() {return opponent; }
	public Side8Board getBoard() {return board; }

	public Side8Wrapper()
	{
		board = new Side8Board();
		player = new Side8Player();
		opponent = new Side8Player();
		cardsBeingUsed = new ArrayList<>();
		restart();
	}

	public void restart()
	{
		//Reset all the variables
		board = new Side8Board();
		player = new Side8Player();
		opponent = new Side8Player();
		cardsBeingUsed = new ArrayList<>();

		ArrayList<BaseCard> startingDeck = initDeck(4);
		for(int i = 0; i < 7; i++)
		{
			player.getHand().add(startingDeck.get(i));
		}
		startingDeck = initDeck(4);
		for(int i = 0; i < 7; i++)
		{
			opponent.getHand().add(startingDeck.get(i));
		}
		Collections.sort(player.getHand());
		Collections.sort(opponent.getHand());
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
		JeremyCopy.randomize(result);
		return result;
	}

	public String getEverythingInString()
	{
		String result = "[" + opponent.getHandInString() + "]\n";
		result += this.getBoard().getBoardView() + "\n";
		result += "[" + player.getHandInString() + "]";
		return result;
	}
}
