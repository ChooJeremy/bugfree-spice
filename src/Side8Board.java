import java.util.*;

public class Side8Board
{
	int[][] board;

	public Side8Board()
	{
		board = new int[3][3];
	}

	public void createRandomBoard()
	{
		Random random = new Random();
		int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7};
		Jeremy.randomize(numbers);
		for(int i = 0; i < numbers.length; i++)
		{
			int count = random.nextInt(10) + 1;
			if(i < 4)
			{
				count = count * -1;
			}
			switch(numbers[i])
			{
				case 0:
				case 1:
				case 2:
					board[numbers[i]][0] = count;
					break;
				case 3:
					board[0][1] = count;
					break;
				case 4:
					board[2][1] = count;
					break;
				case 5:
				case 6:
				case 7:
					board[numbers[i] - 5][2] = count;
					break;
			}
		}
		board[1][1] = random.nextInt(10) + random.nextInt(10);
	}

	public String getBoardView()
	{
		String result = "-----------------\n";
		for(int i = 0; i < board.length; i++)
		{
			result += "|\t";
			for(int j = 0; j < board[i].length; j++)
			{
				result += board[i][j] + "\t";
			}
			result += "|\n";
		}
		result += "-----------------";
		return result;
	}

	public String toString()
	{
		return "Board: " + Jeremy.arrayToString(board);
	}
}
