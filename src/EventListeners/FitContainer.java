package EventListeners;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class FitContainer implements ComponentListener
{
	private Container container;
	private Container source;
	private int dx;
	private int dy;

	public FitContainer(Container referenceToCheck, Container itemToChange, int changeInX, int changeInY)
	{
		source = referenceToCheck;
		container = itemToChange;
		dx = changeInX;
		dy = changeInY;
		System.out.println(dx + " " + dy);
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		container.setSize(source.getWidth() + dx, source.getHeight() + dy);
		container.setPreferredSize(new Dimension(source.getWidth() + dx, source.getHeight() + dy));
		container.revalidate();
	}

	//We don't need the rest, they do nothing.
	@Override
	public void componentMoved(ComponentEvent e) { }

	@Override
	public void componentShown(ComponentEvent e) { }

	@Override
	public void componentHidden(ComponentEvent e) { }
}
