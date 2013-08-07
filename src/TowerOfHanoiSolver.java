import java.util.*;

public class TowerOfHanoiSolver
{
	public static final int FIRST = 1;
	public static final int SECOND = 2;
	public static final int THIRD = 3;

	public static int totalPieces = 8;
	public static final long timeToPause = 10;
	public static boolean useNewLine;

	private static TowerOfHanoi towerToSolve;
	private static TextShower textShower;

	public static void main(String[] args)
	{
		useNewLine = args.length != 0;
		if(args.length >= 1)
		{
			useNewLine = !Jeremy.isInteger(args[0]);
			if(!useNewLine)
			{
				useNewLine = false;
				totalPieces = Integer.parseInt(args[0]);
			}
			if(args.length >= 2)
			{
				useNewLine = true;
			}
		}
		towerToSolve = new TowerOfHanoi(totalPieces);
		textShower = new TextShower("Tower of Hanoi");
		move(FIRST, THIRD, totalPieces, SECOND);
		System.out.println("Total movements: " + TowerOfHanoi.movements);
		Jeremy.pause(timeToPause * 50);
		textShower.dispose();
	}

	public static void move(int start, int end, int numOfPieces, int other)
	{
		if(numOfPieces == 1)
		{
			towerToSolve.move(start, end);
			afterMovement();
			return;
		}
		move(start, other, numOfPieces - 1, end);
		towerToSolve.move(start, end);
		afterMovement();
		move(other, end, numOfPieces - 1, start);
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
