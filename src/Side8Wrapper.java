import java.util.ArrayList;

public class Side8Wrapper
{
	private Participant player;
	private Participant opponent;
	private Side8Board board;
	//Attributes to store the cards
	private ArrayList<Integer> cardsBeingUsed;

	public Participant getPlayer() {return player; }
	public Participant getOpponent() {return opponent; }
	public Side8Board getBoard() {return board; }

	public Side8Wrapper()
	{
		board = new Side8Board();
		player = new Participant("Player");
		opponent = new Participant("Opponent");
		cardsBeingUsed = new ArrayList<>();
		restart();
	}

	public Side8Wrapper(Participant p, Participant o, Side8Board b)
	{
		player = p;
		opponent = o;
		board = b;
		cardsBeingUsed = new ArrayList<>();
	}

	public void restart()
	{
		//Reset all the variables
		board = new Side8Board();
		player = new Participant("Player");
		opponent = new Participant("Opponent");
		cardsBeingUsed = new ArrayList<>();

		Deck playerHand = initDeck(4);
		playerHand.shuffle();
		for(int i = 0; i < 7; i++)
		{
			player.getCard(playerHand.draw());
		}
		playerHand = initDeck(4);
		playerHand.shuffle();
		for(int i = 0; i < 7; i++)
		{
			opponent.getCard(playerHand.draw());
		}
		player.sortHand();
		opponent.sortHand();
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

	public static Deck initDeck(int totalRepeats)
	{
		Deck deck = new Deck();
		deck.clear();
		for(int i = 0; i < totalRepeats; i++)
		{
			for(int j = 1; j <= 10; j++)
			{
				deck.addCard(new Card(j, Card.DIAMOND));
			}
		}
		return deck;
	}

	public String getEverythingInString()
	{
		String result = "[";
		for(int i = 0; i < opponent.getHand().size(); i++)
		{
			result += opponent.getHand().get(i).getNumber();
			if(i != opponent.getHand().size() - 1)
			{
				result += "  ";
			}
		}
		result += "]\n";
		result += this.getBoard().getBoardView() + "\n[";
		for(int i = 0; i < player.getHand().size(); i++)
		{
			result += player.getHand().get(i).getNumber();
			if(i != player.getHand().size() - 1)
			{
				result += "  ";
			}
		}
		result += "]";
		return result;
	}
}
