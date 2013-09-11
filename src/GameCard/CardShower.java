package GameCard;

import EventListeners.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class CardShower extends JButton implements MouseListener
{
	public static final int LARGE_HEIGHT = 200;
	public static final int LARGE_WIDTH = 175;

	private JLayeredPane container;
	private JPanel largeCard;
	private BaseCard cardBeingShown;

	public BaseCard getCardBeingShown() {return cardBeingShown; }

	public CardShower(JLayeredPane gameScreen, BaseCard card)
	{
		container = gameScreen;
		cardBeingShown = card;
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
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

		JTextArea description = new JTextArea(card.getShortDescription());
		description.setFont(new Font("Calibri", Font.PLAIN, 11));
		description.setWrapStyleWord(true);
		description.setLineWrap(true);
		description.setPreferredSize(new Dimension(130, 50));
		//Make the text's horizontal change dynamically.
		description.setPreferredSize(new Dimension(130, 74));
		this.addComponentListener(new FitContainer(this, description, -17, null));
		description.setEditable(false);
		//Mouse events from a jtextarea do not propagate down, add this as well.
		description.addMouseListener(this);
		//So do click events. Add a action command, check if it's this, if so, get it to propagates.

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

		//Add the picture
		JLabel largePicture = new JLabel();
		largePicture.setText("Picture");
		largePicture.setBackground(Color.CYAN);
		largePicture.setOpaque(true);
		largePicture.setPreferredSize(new Dimension(LARGE_WIDTH - 25, 50));
		largeCard.add(largePicture);

		//Description
		JTextArea longDescription = new JTextArea();
		longDescription.setText(card.getLongDescription());
		longDescription.setWrapStyleWord(true);
		longDescription.setLineWrap(true);
		longDescription.setEditable(false);
		longDescription.setPreferredSize(new Dimension(LARGE_WIDTH - 25, LARGE_HEIGHT - 110));
		largeCard.add(longDescription);

		//flavourText
		JTextArea flavourText = new JTextArea();
		flavourText.setText(card.getFlavourText());
		flavourText.setWrapStyleWord(true);
		flavourText.setLineWrap(true);
		flavourText.setEditable(false);
		flavourText.setFont(new Font("Dialog", Font.ITALIC, 8));
		flavourText.setPreferredSize(new Dimension(LARGE_WIDTH - 25, 20));
		largeCard.add(flavourText);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		//Propagate events from the text area down so that clicks on the text area are received as well.
		if(e.getSource() instanceof JTextArea)
		{
			this.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, this.getActionCommand()));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

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
