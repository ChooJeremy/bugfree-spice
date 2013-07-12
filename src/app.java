import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class app extends JFrame implements ActionListener
{
	private Container container;

	public app()
	{
		System.out.println("App initialized!");
		setTitle("App");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		container = getContentPane();
		container.setLayout(new FlowLayout());
		container.setBackground(Color.BLACK);
	}

	@Override
	public void paint(Graphics g)
	{

		System.out.println("Paint method called! g: " +  g);
		g.setColor(Color.GREEN);
		for(int i = 0; i < 10; i++)
		{
			g.drawLine(getWidth(), getHeight(), i*getWidth()/10, 0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public static void main(String[] args)
	{
		System.out.println("Creating gui...");
		new app();
	}
}
