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
	}
}
