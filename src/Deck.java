import java.util.ArrayList;
import java.util.Collections;

public class Deck
{
	protected ArrayList<Card> deck;

	public Deck()
	{
		deck = new ArrayList<Card>();
		reset();
	}

	public Deck(int totalDecks)
	{
		deck = new ArrayList<Card>();
		reset(totalDecks);
	}

	public void clear()
	{
		deck.clear();
	}

	public void reset()
	{
		deck.clear();
		addDeck();
	}

	public void reset(int totalDecks)
	{
		deck.clear();
		for(int i = 0; i < totalDecks; i++)
		{
			addDeck();
		}
	}

	public void addDeck()
	{
		for(int i = 1; i < 14; i++)
		{
			for(int j = Card.DIAMOND; j <= Card.SPADE; j++)
			{
				deck.add(new Card(i, j));
			}
		}
	}

	public void shuffle()
	{
		Jeremy.randomize(deck);
	}

	public Card draw()
	{
		Card temp = deck.get(deck.size() - 1);
		deck.remove(deck.size() - 1);
		return temp;
	}

	public int size()
	{
		return deck.size();
	}

	public void sort()
	{
		Collections.sort(deck);
	}

	/**
	 * Adds a new card to the bottom of the deck
	 *
	 * @param c the card to add
	 */
	public void addCard(Card c)
	{
		deck.add(0, c);
	}

	@Override
	public String toString()
	{
		String string = "Cards in deck: (bottom first)";
		for(Card aCard:deck)
		{
			string = string + "\n" + aCard;
		}
		return string;
	}
}
