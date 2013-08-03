import java.util.*;

public class TowerOfHanoiSolver
{
	public static final int FIRST = 1;
	public static final int SECOND = 2;
	public static final int THIRD = 3;

	public static final int totalPieces = 8;
	public static final long timeToPause = 10;
	public static boolean useNewLine;

	private static TowerOfHanoi towerToSolve;
	private static TextShower textShower;

	public static void main(String[] args)
	{
		useNewLine = args.length != 0;
		towerToSolve = new TowerOfHanoi(totalPieces);
		textShower = new TextShower("Tower of Hanoi");
		move(FIRST, THIRD, totalPieces);
		System.out.println("Total movements: " + TowerOfHanoi.movements);
		Jeremy.pause(timeToPause * 50);
		textShower.dispose();
	}

	public static void move(int start, int end, int numOfPieces)
	{
		if(numOfPieces == 1)
		{
			towerToSolve.move(start, end);
			afterMovement();
			return;
		}
		int other = 0;
		do {
			other++;
		} while(start + end + other < FIRST + SECOND + THIRD);
		move(start, other, numOfPieces - 1);
		towerToSolve.move(start, end);
		afterMovement();
		move(other, end, numOfPieces - 1);
	}

	public static void afterMovement()
	{
		textShower.setText(towerToSolve.toString());
		if(useNewLine)
		{
			new Scanner(System.in).nextLine();
		}
		else
		{
			Jeremy.pause(timeToPause);
		}
	}
}
