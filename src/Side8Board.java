import java.util.*;

public class Side8Board
{
	private int[][] board;

	public Side8Board()
	{
		board = new int[3][3];
	}

	public int[][] getBoard() {return board; }

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

	public void setBoardNumber(int location, int number)
	{
		switch(location)
		{
			case 0:
			case 1:
			case 2:
				board[0][location] = number;
				break;
			case 3:
			case 4:
			case 5:
				board[1][location - 3] = number;
				break;
			case 6:
			case 7:
			case 8:
				board[2][location - 6] = number;
				break;
			default:
				throw new RuntimeException("Invalid location " + location);
		}
	}

	public int getBoardNumber(int location)
	{
		switch(location)
		{
			case 0:
			case 1:
			case 2:
				return board[0][location];
			case 3:
			case 4:
			case 5:
				return board[1][location - 3];
			case 6:
			case 7:
			case 8:
				return board[2][location - 6];
			default:
				throw new RuntimeException("Invalid location " + location);
		}
	}

	public String getBoardView()
	{
		String result = "---------------------------------\n";
		for(int i = 0; i < board.length; i++)
		{
			result += "|\t";
			for(int j = 0; j < board[i].length; j++)
			{
				result += board[i][j] + "\t";
			}
			result += "|\n";
		}
		result += "---------------------------------";
		return result;
	}

	public String toString()
	{
		return "Board: " + Jeremy.arrayToString(board);
	}
}
