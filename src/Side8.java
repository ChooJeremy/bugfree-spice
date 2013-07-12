import java.util.*;

public class Side8
{
	public static void main(String[] args)
	{
		Side8Board board = new Side8Board();
		for(int i = 0; i < 5; i++)
		{
			board.createRandomBoard();
			System.out.println(board.getBoardView());
			System.out.println(board);
		}
	}
}
