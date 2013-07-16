import java.awt.event.*;
import javax.swing.*;

public class AppTimer implements ActionListener
{
	private String methodToCallLater;
	private app reference;
	private Timer timer;
	public static final String performOpponentsTurn = "performopponentsturn";
	public static final String allocateNextTurn = "allocatenextturn";
	public static final String performTieBreaker = "performtiebreaker";
	public static final String listenForInput = "listenforinput";
	public static boolean instant = false;

	public AppTimer(app referenceBack, String methodToCall, int timeToWait)
	{
		methodToCallLater = methodToCall;
		reference = referenceBack;
		timer = new Timer(100, this);
		timer.setInitialDelay(instant ? 1 : timeToWait);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(methodToCallLater.toLowerCase())
		{
			case AppTimer.performOpponentsTurn:
				reference.performOpponentsTurn();
				break;
			case AppTimer.allocateNextTurn:
				reference.allocateNextTurn();
				break;
			case AppTimer.performTieBreaker:
				reference.performTieBreaker();
				break;
			case AppTimer.listenForInput:
				reference.listenForInput();
				break;
			default:
				reference.dump();
				break;
		}
		timer.stop();
		reference = null;
		timer = null;
	}
}
