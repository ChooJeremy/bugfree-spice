package Side8Items;

import Helper.JeremyCopy;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Side8BoardTransition implements Runnable
{
	public static final int TRANSITIONSTEPS = 50;

	Color beginColor;
	Color endColor;
	long time;
	JComponent user;

	public Side8BoardTransition(Color startingColor, Color endingColor, long t, JComponent u)
	{
		beginColor = startingColor;
		endColor = endingColor;
		user = u;
		time = t;
	}

	@Override
	public void run()
	{
		int pauseTime = (int) time / TRANSITIONSTEPS;
		double rChange = (endColor.getRed() - beginColor.getRed()) * 1.0 / TRANSITIONSTEPS;
		double bChange = (endColor.getBlue() - beginColor.getBlue()) * 1.0 / TRANSITIONSTEPS;
		double gChange = (endColor.getGreen() - beginColor.getGreen()) * 1.0 / TRANSITIONSTEPS;
		double r, g, b;
		r = beginColor.getRed();
		g = beginColor.getGreen();
		b = beginColor.getBlue();
		for(int i = 0; i < TRANSITIONSTEPS; i++)
		{
			r += rChange;
			g += gChange;
			b += bChange;
			user.setBackground(new Color((int) r, (int) g, (int) b));
			JeremyCopy.pause(pauseTime);
		}
	}
}
