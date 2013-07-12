import java.util.ArrayList;
import java.util.Collections;

public class Participant
{
	protected ArrayList<Card> hand;
	protected String name;
	protected boolean active;

	public Participant(String n)
	{
		hand = new ArrayList<Card>();
		name = n;
		active = true;
	}

	public String getName() {return name; }
	public void lose() {active = false;}
	public boolean isActive() {return active;}

	public int handSize()
	{
		return hand.size();
	}

	public void getCard(Card card)
	{
		hand.add(card);
	}

	public int hasCard(Card card)
	{
		return Jeremy.searchInArrayList(hand, card);
	}

	public boolean removeCard(Card card)
	{
		int cardNo = hasCard(card);
		if(cardNo != -1)
		{
			hand.remove(cardNo);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void clearHand()
	{
		hand.clear();
	}

	public void sortHand()
	{
		Collections.sort(hand);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Card> getHand()
	{
		return (ArrayList<Card>) hand.clone();
	}

	@Override
	public String toString()
	{
		String string =  "Name: " + name + ", Hand: ";
		for(Card aCard: hand)
		{
			string = string + aCard + " ";
		}
		return string;
	}
}
