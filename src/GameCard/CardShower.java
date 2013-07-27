package GameCard;

import EventListeners.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CardShower extends JButton implements ActionListener, MouseListener
{
	public static final int LARGE_HEIGHT = 200;
	public static final int LARGE_WIDTH = 175;

	private JLayeredPane container;
	private JPanel largeCard;

	public CardShower(JLayeredPane gameScreen, BaseCard card)
	{
		container = gameScreen;
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		this.addActionListener(this);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));

		//Add the required stuff
		JLabel title = new JLabel(card.getName());
		title.setOpaque(true);
		title.setFont(new Font("Calibri", Font.BOLD, 18));
		this.add(title);

		JLabel picture = new JLabel();
		picture.setText("Picture");
		picture.setBackground(Color.CYAN);
		picture.setOpaque(true);
		//Set the size of the picture dynamically. As the size of the button increases, so does the picture
		//This will help to keep the title, pic and description in separate lines, since otherwise the flowlayout set
		//will cause items to be on the same line if possible.
		//Add the listener to this since it's this object that's being resized, not the picture.
		this.addComponentListener(new FitContainer(this, picture, -17, -120));
		this.add(picture);

		JLabel description = new JLabel("<html><pre>" + card.getDescription() + "</pre></html>");
		description.setFont(new Font("Calibri", Font.PLAIN, 11));
		description.setSize(200, 200);
		this.add(description);

		//The big card that appears on hover over
		largeCard = new JPanel();
		largeCard.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK, 1),
				card.getName(),
				TitledBorder.CENTER,
				TitledBorder.TOP
		));
		largeCard.setBackground(Color.WHITE);
		largeCard.add(new JPanel());
		largeCard.getComponent(0).setBackground(Color.CYAN);
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
		Point startingLocation = getShowLocation();
		largeCard.setBounds(startingLocation.x, startingLocation.y, LARGE_WIDTH, LARGE_HEIGHT);
		container.add(largeCard, JLayeredPane.POPUP_LAYER);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		container.remove(largeCard);
		container.repaint();
	}

	public Point getShowLocation()
	{
		Point result = this.getLocationOnScreen();
		//Location is based off the screen, change it so that it is now based off the container.
		//Alternatively, getLocation only works through the parent container, we would have to do a for loop to pass through every
		//container, check if it's not the final JFrame, and keep up doing point.translate.
		result.translate(
				container.getLocationOnScreen().x * -1,
				container.getLocationOnScreen().y * -1
		);

		//Increase height
		result.translate(0, LARGE_HEIGHT * -1);
		//Aggregate it so that it is never cut off.
		if(result.x > container.getWidth() / 2)
		{
			result.translate((LARGE_WIDTH - this.getWidth()) * -1, 0);
		}
		return result;
	}
}
