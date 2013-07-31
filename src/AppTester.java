import java.util.*;

public class AppTester
{
	//Uncomment line "listenForInput" in app's constructor (find at the end of the constructor) for best results.

	public static void main(String[] args)
	{
		for(double i = 0; i < 1; i += 0.0005)
		{
			app anApp = new app();
			anApp.dispose();
			Jeremy.createProgress(i);
		}
	}
}
