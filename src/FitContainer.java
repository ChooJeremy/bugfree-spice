import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class FitContainer implements ComponentListener
{
	public static int dx = -57;
	public static int dy = -78;

	private Container container;
	private JFrame source;

	public FitContainer(JFrame referenceToCheck, Container itemToChange)
	{
		source = referenceToCheck;
		container = itemToChange;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		container.setSize(source.getWidth() + dx, source.getHeight() + dy);
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
