import java.util.ArrayList;

public class Side8
{
	public static void main(String[] args)
	{
		startGame();
	}

	public static void startGame()
	{
		Side8Wrapper s8w = new Side8Wrapper();
		Participant player = s8w.getPlayer();
		Participant opponent = s8w.getOpponent();
		Side8Board board = s8w.getBoard();
		printBoard(s8w, true);
		playerTurn(s8w);
		printBoard(s8w, true);
	}

	public static void playerTurn(Side8Wrapper s8w)
	{
		int userInput;
		boolean repeat;
		Participant player = s8w.getPlayer();
		printBoard(s8w);
		System.out.println("Please enter the number that you wish to play: ");
		do
		{
			repeat = false;
			userInput = Jeremy.getInteger("That is not a number. Please try again.");
			if(userInput >= 1 && userInput <= 10)
			{
				Card card = new Card(userInput, Card.DIAMOND);
				int result = Jeremy.searchInArrayList(player.getHand(), new Card(userInput, Card.DIAMOND));
				if(result == -1)
				{
					repeat = true;
				}
				else
				{
					s8w.getPlayer().removeCard(card);
				}
			}
			else
			{
				repeat = true;
			}
			if(repeat)
			{
				System.out.println("That is not a valid card. Please try again.");
			}
		} while(repeat);
	}

	public static void printBoard(Side8Wrapper s8w)
	{
		printBoard(s8w, false);
	}

	public static void printBoard(Side8Wrapper s8w, boolean showOpponent)
	{
		if(showOpponent)
		{
			printHand(s8w.getOpponent().getHand());
		}
		System.out.println(s8w.getBoard().getBoardView());
		printHand(s8w.getPlayer().getHand());
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
