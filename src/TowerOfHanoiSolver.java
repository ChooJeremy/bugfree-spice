import javax.swing.*;
import java.util.*;

public class TowerOfHanoiSolver
{
	public static final int FIRST = 1;
	public static final int SECOND = 2;
	public static final int THIRD = 3;

	public static final int totalPieces = 8;

	private static TowerOfHanoi towerToSolve;
	private static TextShower textShower;

	public static void main(String[] args)
	{
		towerToSolve = new TowerOfHanoi(totalPieces);
		textShower = new TextShower("Tower of Hanoi");
		move(FIRST, THIRD, totalPieces);
		System.out.println("Total movements: " + TowerOfHanoi.movements);
		textShower.dispose();
	}

	public static void move(int start, int end, int numOfPieces)
	{
		if(numOfPieces == 1)
		{
			towerToSolve.move(start, end);
			textShower.setText(towerToSolve.toString());
			Jeremy.pause(100);
			return;
		}
		int other = 0;
		do {
			other++;
		} while(start + end + other < 6);
		move(start, other, numOfPieces - 1);
		towerToSolve.move(start, end);
		textShower.setText(towerToSolve.toString());
		Jeremy.pause(100);
		move(other, end, numOfPieces - 1);
	}
}
