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
	public static final int TEXTWIDTH = 20;
	public static final int TEXTHEIGHT = 20;
	public static final Integer DAMAGENUMBERPANE = new Integer(250);

	private int currentNum;
	private int type;
	private int shield;
	private boolean isConverse;
	private ArrayList<BaseBuff> buffs;
	private JLayeredPane mainPaneReference;

	public Side8BoardItem()
	{
		currentNum = 0;
		type = 3;
		shield = 0;
		isConverse = false;
		buffs = new ArrayList<>();

		//Display
		this.setFont(new Font("Calibri", Font.PLAIN, 18));
		this.setText("0");
		updateValues(false);
	}

	public int getCurrentNum() { return currentNum; }
    public int getType()
    {
	    if(!isConverse)
	    {
		    return type;
	    }
	    else
	    {
		    return getConverseType(type);
	    }
    }
	public int getShield() { return shield; }
	public ArrayList<BaseBuff> getBuffs() { return buffs; }
	public void setCurrentNum(int currentNum) { this.currentNum = currentNum; }
	public void setType(int t)
	{
		if(!isConverse)
		{
			type = t;
		}
		else
		{
			type = getConverseType(t);
		}
	}
	public void setShield(int shield) {	this.shield = shield; }
	public void addBuff(BaseBuff buff) { buffs.add(buff); }
	public void setJLayeredPaneReference(JLayeredPane ref) {mainPaneReference = ref;}

	public static int getConverseType(int type)
	{
		if(type == ALLY)
		{
			return ENEMY;
		}
		else if(type == ENEMY)
		{
			return ALLY;
		}
		else
		{
			return type;
		}
	}

	public void setConverse()
	{
		isConverse = true;
	}

	public void removeConverse()
	{
		isConverse = false;
	}

	public void takeDamage(int damage)
	{
		if(isConverse)
		{
			takeDamage(damage, ENEMY);
		}
		else
		{
			takeDamage(damage, ALLY);
		}
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
					mainPaneReference.add(createLabelWithChange(currentNum - numberInUI), DAMAGENUMBERPANE);
				}
			}
			else
			{
				//There is a change
				//Firstly, create a new label that shows the previous number being set to 0.
				mainPaneReference.add(createLabelWithChange(numberInUI * -1), DAMAGENUMBERPANE);
				//Transition it to the new colour
				transition(this.getBackground(), expectedColor, 1000);
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
				this.getWidth() / 2,
				this.getHeight() / 2
		);
		JLabel result = new Side8BoardDamageLabel(change);
		//Now the label's top left is the exact same as the center of this button.
		//Change the point to the bottom left.
		labelMiddle.translate(
				0,
				TEXTHEIGHT * -1
		);
		result.setBounds(labelMiddle.x, labelMiddle.y, TEXTWIDTH, TEXTHEIGHT);
		result.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		return result;
	}

	public void transition(Color beginColor, Color endColor, long time)
	{
		new Thread(new Side8BoardTransition(beginColor, endColor, time, this)).start();
	}
}
