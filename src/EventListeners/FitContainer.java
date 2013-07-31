package EventListeners;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FitContainer implements ComponentListener
{
	private Container container;
	private Container source;
	private Integer dx;
	private Integer dy;

	public FitContainer(Container referenceToCheck, Container itemToChange, Integer changeInX, Integer changeInY)
	{
		source = referenceToCheck;
		container = itemToChange;
		dx = changeInX;
		dy = changeInY;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		int expectedWidth = (dx == null ? container.getWidth() : source.getWidth() + dx);
		int expectedHeight = (dy == null ? container.getHeight() : source.getHeight() + dy);
		container.setSize(expectedWidth, expectedHeight);
		container.setPreferredSize(new Dimension(expectedWidth, expectedHeight));
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
