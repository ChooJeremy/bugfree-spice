import java.util.*;

public class ProgrammerLife
{
	public static void main(String[] args)
	{
		int currentBugs = 99;
		while(currentBugs > 0)
		{
			currentBugs = printResult(currentBugs);
			Jeremy.pause(1000);
		}
	}

	public static int printResult(int currentBugs)
	{
		System.out.println(currentBugs + " little bugs in the code");
		System.out.println(currentBugs + " little bugs in the code");
		currentBugs += (int) (Math.random() * 20) - 10;
		currentBugs = currentBugs < 0 ? 0 : currentBugs;
		System.out.println("Take one down, patch it around");
		System.out.println(currentBugs + " little bugs in the code");
		return currentBugs;
	}
}
