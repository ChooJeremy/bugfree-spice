import java.awt.event.*;
import javax.swing.*;

public class AppTimer implements ActionListener
{
	private String methodToCallLater;
	private app reference;
	private Timer timer;
	public static String performOpponentsTurn = "performopponentsturn";

	public AppTimer(app referenceBack, String methodToCall, int timeToWait)
	{
		methodToCallLater = methodToCall;
		reference = referenceBack;
		timer = new Timer(100, this);
		timer.setInitialDelay(timeToWait);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(methodToCallLater.toLowerCase())
		{
			case "performopponentsturn":
				reference.performOpponentsTurn();
				break;
		}
		timer.stop();
		reference = null;
		timer = null;
	}
}
