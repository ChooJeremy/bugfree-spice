package GameCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CardShower extends JButton implements ActionListener, MouseListener
{
	private Container container;

	public CardShower(BaseCard card)
	{
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		this.addActionListener(this);
		this.setFont(new Font("Calibri", Font.PLAIN, 18));
		this.setText("<html>" + card.getName() + "<br />" + card.getDescription() + "</html>");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("You're playing a game card!");
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("MouseClick!");
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		System.out.println("MousePress!");
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		System.out.println("MouseRelease!");
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		System.out.println("MouseEnter!");
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		System.out.println("MouseExit!!");
	}
}
