package Side8Items;

import Helper.JeremyCopy;
import java.util.*;

public class Side8Board
{
	public static final int ALLY = 1;
	public static final int ENEMY = 2;
	public static final int NEUTRAL = 3;

	private int[][] board;
	private int[][] status;

	public Side8Board()
	{
		board = new int[3][3];
		status = new int[3][3];
		//Start everything off as neutral
		for(int i = 0; i < 9; i++)
		{
			setStatus(i, NEUTRAL);
		}
	}

	public int[][] getBoard() {return board; }

	public void createRandomBoard()
	{
		Random random = new Random();
		int[] numbers = {0, 1, 2, 3, 5, 6, 7, 8};
		JeremyCopy.randomize(numbers);
		for(int i = 0; i < numbers.length; i++)
		{
			int count = random.nextInt(5) + random.nextInt(5) + 2;
			setBoardNumber(numbers[i], count);
			if(i < 4)
			{
				setStatus(numbers[i], ENEMY);
			}
			else
			{
				setStatus(numbers[i], ALLY);
			}
		}
		board[1][1] = random.nextInt(6);
		status[1][1] = NEUTRAL;
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

	public void setStatus(int location, int newStatus)
	{
		switch(location)
		{
			case 0:
			case 1:
			case 2:
				status[0][location] = newStatus;
				break;
			case 3:
			case 4:
			case 5:
				status[1][location - 3] = newStatus;
				break;
			case 6:
			case 7:
			case 8:
				status[2][location - 6] = newStatus;
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

	public int getStatus(int location)
	{
		switch(location)
		{
			case 0:
			case 1:
			case 2:
				return status[0][location];
			case 3:
			case 4:
			case 5:
				return status[1][location - 3];
			case 6:
			case 7:
			case 8:
				return status[2][location - 6];
			default:
				throw new RuntimeException("Invalid location " + location);
		}
	}

	public static String getStatusInText(int status)
	{
		switch(status)
		{
			case ALLY:
				return "Ally";
			case ENEMY:
				return "Enemy";
			case NEUTRAL:
				return "Neutral";
			default:
				throw new IllegalArgumentException("Status: " + status);
		}
	}

	public Side8Board performConversion()
	{
		Side8Board result = new Side8Board();
		for(int i = 0; i < 9; i++)
		{
			result.setBoardNumber(i, this.getBoardNumber(i));
			switch(this.getStatus(i))
			{
				case ENEMY:
					result.setStatus(i, ALLY);
					break;
				case ALLY:
					result.setStatus(i, ENEMY);
					break;
				default:
					result.setStatus(i, this.getStatus(i));
					break;
			}
		}
		return result;
	}

	public void copyBoard(Side8Board boardToCopy)
	{
		for(int i = 0; i < 9; i++)
		{
			this.setBoardNumber(i, boardToCopy.getBoardNumber(i));
			this.setStatus(i, boardToCopy.getStatus(i));
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
				result += board[i][j] + getStatusInText(status[i][j]).substring(0, 1) + "\t";
			}
			result += "|\n";
		}
		result += "---------------------------------";
		return result;
	}

	public String toString()
	{
		String result = "";
		for(int i = 0; i < 9; i = i + 3)
		{
			result += "" + getBoardNumber(i) + getStatusInText(getStatus(i)) + "\t"
					+ getBoardNumber(i+1) + getStatusInText(getStatus(i+1)) + "\t"
					+ getBoardNumber(i+2) + getStatusInText(getStatus(i+2)) + "\n";
		}
		return result;
	}
}
