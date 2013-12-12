package Side8Items;

import Buffs.BaseBuff;
import Helper.JeremyCopy;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Side8BoardItem extends JButton
{
	public static final int ALLY = 1;
	public static final int ENEMY = 2;
	public static final int NEUTRAL = 3;
	public static final int TEXTWIDTH = 100;
	public static final int TEXTHEIGHT = 50;
	public static final int TRANSITIONSTEPS = 50;
	public static final Integer DAMAGENUMBERPANE = new Integer(250);

	private int currentNum;
	private int type;
	private int shield;
	private ArrayList<BaseBuff> buffs;
	private JLayeredPane mainPaneReference;

	public Side8BoardItem()
	{
		currentNum = 0;
		type = 3;
		shield = 0;
		buffs = new ArrayList<>();

		//Display
		this.setFont(new Font("Calibri", Font.PLAIN, 18));
		this.setText("0");
		updateValues(false);
	}

	public int getCurrentNum() { return currentNum; }
    public int getType() { return type; }
	public int getShield() { return shield; }
	public ArrayList<BaseBuff> getBuffs() { return buffs; }
	public void setCurrentNum(int currentNum) { this.currentNum = currentNum; }
	public void setType(int type) {	this.type = type; }
	public void setShield(int shield) {	this.shield = shield; }
	public void addBuff(BaseBuff buff) { buffs.add(buff); }
	public void setJLayeredPaneReference(JLayeredPane ref) {mainPaneReference = ref;}

	public void takeDamage(int damage)
	{
		takeDamage(damage, ALLY);
	}

	public void takeDamage(int damage, int currentSide)
	{
		for(BaseBuff buff : buffs)
		{
			damage = buff.actionPerformed(damage);
		}
		int finalDamage = damage;
		if(shield > 0 && damage > 0)
		{
			if(shield > damage)
			{
				shield -= damage;
				finalDamage = 0;
			}
			else
			{
				shield = 0;
				finalDamage = damage - shield;
			}
		}
		currentNum -= finalDamage;
		if(currentNum <= 0 && type != currentSide)
		{
			currentNum = 3;
			type = currentSide;
		}
		if(currentNum < 0)
		{
			currentNum = 0;
		}
	}

	public void updateValues()
	{
		updateValues(true);
	}

	public void updateValues(boolean doTransition)
	{
		int numberInUI = Integer.parseInt(getText());
		this.setText("" + currentNum);
		Color expectedColor = null;
		switch(type)
		{
			case ALLY:
				expectedColor = Color.GREEN;
				break;
			case ENEMY:
				expectedColor = Color.RED;
				break;
			case NEUTRAL:
				expectedColor = Color.LIGHT_GRAY;
				break;
		}
		if(doTransition)
		{
			if(this.getBackground() == expectedColor)
			{
				//No change in colour, check for change in number
				if(numberInUI != currentNum)
				{
					mainPaneReference.add(createLabelWithChange(currentNum - numberInUI));
				}
			}
			else
			{
				//There is a change
				//Firstly, create a new label that shows the previous number being set to 0.
				mainPaneReference.add(createLabelWithChange(numberInUI * -1));
				//Transition it to the new colout
				transition(this.getBackground(), expectedColor, 2000);
			}
		}
		else
		{
			this.setBackground(expectedColor);
		}
	}

	public JLabel createLabelWithChange(int change)
	{
		if(change == 0)
		{
			System.out.println("Change of 0 detected!");
		}
		//Get the middle location to find where to show the label.
		Point labelMiddle = this.getLocationOnScreen();
		labelMiddle.translate(
				mainPaneReference.getLocationOnScreen().x * -1,
				mainPaneReference.getLocationOnScreen().y * -1
		);
		labelMiddle.translate(
				this.getWidth(),
				this.getHeight()
		);
		JLabel result = new JLabel("" + change);
		if(change >= 0)
		{
			result.setText("+" + result.getText());
			result.setForeground(Color.GREEN);
		}
		else
		{
			result.setForeground(Color.RED);
		}
		labelMiddle.translate(
				TEXTWIDTH * -1,
				TEXTHEIGHT * -1
		);
		result.setBounds(labelMiddle.x, labelMiddle.y, TEXTWIDTH, TEXTHEIGHT);
		return result;
	}

	public void transition(Color beginColor, Color endColor, long time)
	{
		int pauseTime = (int) time / TRANSITIONSTEPS;
		double rChange = (beginColor.getRed() - endColor.getRed()) * 1.0 / TRANSITIONSTEPS;
		double bChange = (beginColor.getBlue() - endColor.getBlue()) * 1.0 / TRANSITIONSTEPS;
		double gChange = (beginColor.getGreen() - endColor.getGreen()) * 1.0 / TRANSITIONSTEPS;
		double r, g, b;
		r = beginColor.getRed();
		g = beginColor.getGreen();
		b = beginColor.getBlue();
		for(int i = 0; i < TRANSITIONSTEPS; i++)
		{
			r += rChange;
			g += gChange;
			b += bChange;
			this.setBackground(new Color((int) r, (int) g, (int) b));
			JeremyCopy.pause(pauseTime);
		}
	}
}
