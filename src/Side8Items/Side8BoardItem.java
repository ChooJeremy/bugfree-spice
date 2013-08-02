package Side8Items;

import Buffs.BaseBuff;
import java.util.*;

public class Side8BoardItem
{
	public static final int ALLY = 1;
	public static final int ENEMY = 2;
	public static final int NEUTRAL = 3;

	private int currentNum;
	private int type;
	private int shield;
	private ArrayList<BaseBuff> buffs;

	public Side8BoardItem()
	{
		currentNum = 0;
		type = 3;
		shield = 0;
	}

	public int getCurrentNum() { return currentNum; }
    public int getType() { return type; }
	public int getShield() { return shield; }
	public ArrayList<BaseBuff> getBuffs() { return buffs; }
	public void setCurrentNum(int currentNum) { this.currentNum = currentNum; }
	public void setType(int type) {	this.type = type; }
	public void setShield(int shield) {	this.shield = shield; }
	public void addBuff(BaseBuff buff) { buffs.add(buff); }

	public void takeDamage(int damage)
	{
		takeDamage(damage, ALLY);
	}

	public void takeDamage(int damage, int currentSide)
	{
		int finalDamage = damage;
		if(shield > 0)
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
}
