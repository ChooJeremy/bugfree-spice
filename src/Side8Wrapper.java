import java.util.*;

public class Side8Wrapper
{
	private Participant player;
	private Participant opponent;
	private Side8Board board;

	public Participant getPlayer() {return player; }
	public Participant getOpponent() {return opponent; }
	public Side8Board getBoard() {return board; }

	public Side8Wrapper()
	{
		board = new Side8Board();
		player = new Participant("Player");
		opponent = new Participant("Opponent");
		Deck playerHand = initDeck(1);
		playerHand.shuffle();
		for(int i = 0; i < 7; i++)
		{
			player.getCard(playerHand.draw());
		}
		playerHand = initDeck(1);
		playerHand.shuffle();
		for(int i = 0; i < 7; i++)
		{
			opponent.getCard(playerHand.draw());
		}
		printBoard(opponent, player, board);
	}

	public static Deck initDeck(int totalRepeats)
	{
		Deck deck = new Deck();
		deck.clear();
		for(int i = 0; i < totalRepeats; i++)
		{
			for(int j = 1; j <= 10; j++)
			{
				deck.addCard(new Card(j, Card.Diamond));
			}
		}
		return deck;
	}

	public static void printBoard(Participant o, Participant p, Side8Board b)
	{
		printHand(o.getHand());
		System.out.println(b.getBoardView());
		printHand(p.getHand());
	}

	public static void printHand(ArrayList<Card> hand)
	{
		System.out.print("[");
		for(int i = 0; i < hand.size(); i++)
		{
			System.out.print(hand.get(i).getNumber());
			if(i != hand.size() - 1)
			{
				System.out.print("  ");
			}
		}
		System.out.println("]");
	}
}
