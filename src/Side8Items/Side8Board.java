package Side8Items;

import Helper.JeremyCopy;
import java.util.*;

public class Side8Board
{
	public static final int ALLY = 1;
	public static final int ENEMY = 2;
	public static final int NEUTRAL = 3;

	private Side8BoardItem[] board;

	public Side8Board()
	{
		board = new Side8BoardItem[9];
		for(int i = 0; i < board.length; i++)
		{
			board[i] = new Side8BoardItem();
		}
	}

	public Side8BoardItem[] getBoard() {return board; }

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
		board[4].setCurrentNum(random.nextInt(6));
		board[4].setType(NEUTRAL);
	}

	public void setBoardNumber(int location, int number)
	{
		board[location].setCurrentNum(number);
	}

	public void setStatus(int location, int newStatus)
	{
		board[location].setType(newStatus);
	}

	public int getBoardNumber(int location)
	{
		return board[location].getCurrentNum();
	}

	public int getStatus(int location)
	{
		return board[location].getType();
	}

	public Side8BoardItem getBoardItem(int location)
	{
		return board[location];
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

	public void setConverse()
	{
		for(Side8BoardItem i : board)
		{
			i.setConverse();
		}
	}

	public void clearConverse()
	{
		for(Side8BoardItem i : board)
		{
			i.removeConverse();
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
		for(int i = 0; i < 3; i++)
		{
			result += "|\t";
			for(int j = 0; j < 3; j++)
			{
				result += board[i*3+j].getCurrentNum() + getStatusInText(board[i*3+j].getType()).substring(0, 1) + "\t";
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

	public int getPlayerCount()
	{
		int total = 0;
		for(Side8BoardItem aBoard : board)
		{
			if(aBoard.getType() == ALLY)
			{
				total += aBoard.getCurrentNum();
			}
		}
		return total;
	}

	public int getEnemyCount()
	{
		int total = 0;
		for(Side8BoardItem aBoard : board)
		{
			if(aBoard.getType() == ENEMY)
			{
				total += aBoard.getCurrentNum();
			}
		}
		return total;
	}

	public void startPlayerTurn()
	{
	}

	public void endPlayerTurn()
	{
		//Calculate the total number of squares the player owns
		ArrayList<Integer> choices = new ArrayList<>();
		for(int i = 0; i < board.length; i++)
		{
			if(board[i].getType() == ALLY)
			{
				choices.add(i);
			}
		}
		//Nothing to increase
		if(choices.size() == 0)
		{
			return;
		}
		int numberToIncreaseBy = Math.max(0, choices.size() - 4) + 2;
		//Increase a random one by that
		int location = choices.get((int) (Math.random() * choices.size()));
		Side8BoardItem itemToIncrease = getBoardItem(location);
		System.out.println("End of player turn: " + location + " is healed by " + numberToIncreaseBy);
		itemToIncrease.takeDamage(numberToIncreaseBy * -1, ALLY);
		itemToIncrease.updateValues();
	}

	public void startEnemyTurn()
	{
	}

	public void endEnemyTurn()
	{
		//Calculate the total number of squares the player owns
		ArrayList<Integer> choices = new ArrayList<>();
		for(int i = 0; i < board.length; i++)
		{
			if(board[i].getType() == ENEMY)
			{
				choices.add(i);
			}
		}
		int numberToIncreaseBy = Math.max(0, choices.size() - 4) + 2;
		if(choices.size() == 0)
		{
			return;
		}
		//Increase a random one by that
		int location = choices.get((int) (Math.random() * choices.size()));
		Side8BoardItem itemToIncrease = getBoardItem(location);
		System.out.println("End of enemy turn: " + location + " is healed by " + numberToIncreaseBy);
		itemToIncrease.takeDamage(numberToIncreaseBy * -1, ENEMY);
		itemToIncrease.updateValues();
	}
}
