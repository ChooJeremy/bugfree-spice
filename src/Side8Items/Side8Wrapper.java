package Side8Items;

import GameCard.*;
import Helper.JeremyCopy;
import java.util.*;

public class Side8Wrapper
{
	private Side8Player player;
	private Side8Player opponent;
	private Side8Board board;
	//1st stores the location of the card to be played, 2nd stores all the targets
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

	/**
	 * Sets the card to be played. If there is already a card that is planned to be played, it clears the target selection of that
	 * card as well
	 *
	 * @param number the location of the card to be played
	 */
	public void setCardNo(int number)
	{
		if(cardsBeingUsed.size() == 0)
		{
			cardsBeingUsed.add(number);
		}
		else
		{
			cardsBeingUsed.clear();
			cardsBeingUsed.add(number);
		}
	}

	/**
	 * Returns the card number selected, or null if no card is selected
	 *
	 * @return the card number selected, or null.
	 */
	public Integer getCardNo()
	{
		return cardsBeingUsed.size() == 0 ? null : cardsBeingUsed.get(0);
	}

	/**
	 * Attempts to add a target selection for the card. If there is no card selected at this time, fails and returns false
	 *
	 * @param number the selected target on the board
	 * @return success or failure
	 */
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

	/**
	 * Returns the targets selected for the card in an ArrayList<Integer>. Does not return the card location.
	 *
	 * @return the targets selected for the card, or NULL if there is no card selected in the first place.
	 */
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

	/**
	 * To be used when the card selection is finished, for whatever reason. Clears the card selection array.
	 *
	 */
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

	/**
	 * Forwards the start player turn call to every item in this wrapper.
	 *
	 */
	public void startPlayerTurn()
	{
		player.startTurn();
		board.startPlayerTurn();
	}

	/**
	 * Forwards the end player turn call to every item in this wrapper.
	 *
	 */
	public void endPlayerTurn()
	{
		player.endTurn();
		board.endPlayerTurn();
	}

	/**
	 * Forwards the start enemy turn call to every item in this wrapper.
	 *
	 */
	public void startEnemyTurn()
	{
		opponent.startTurn();
		board.startEnemyTurn();
	}

	/**
	 * Forwards the end enemy turn call to every item in this wrapper.
	 *
	 */
	public void endEnemyTurn()
	{
		opponent.endTurn();
		board.endEnemyTurn();
	}
}
