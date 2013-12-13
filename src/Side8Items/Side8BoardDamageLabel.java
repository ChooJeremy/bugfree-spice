package Side8Items;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Side8BoardDamageLabel extends JLabel implements Runnable
{
	public Side8BoardDamageLabel(int change)
	{
		//Space in between so easier to see
		if(change >= 0)
		{
			this.setText("+ " + change);
			this.setBackground(Color.CYAN);
			this.setForeground(Color.BLACK);
		}
		else
		{
			this.setText("- " + (change * -1));
			this.setBackground(Color.RED);
			this.setForeground(Color.WHITE);
		}

		this.setOpaque(true);
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		//Make it move up
		for(int i = 0; i < 25; i++)
		{
			this.setBounds(this.getX(), this.getY() - 2, this.getWidth(), this.getHeight());
			try
			{
				Thread.sleep(100);
			}
			catch(Exception e)
			{
				System.out.println("Side8BoardDamageLabel sleep failed!");
				System.out.println(e.getMessage());
			}
		}
		Component c = this.getParent();
		getParent().remove(this);
		c.repaint();
		c.revalidate();
	}
}
