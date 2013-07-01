import java.io.Serializable;

public class Gem implements Serializable, Comparable<Gem>
{
	public static final String[] gemTypes =
	{ //List of all the gems. Change in the array signifies a change (either in colour or type).
			//Strength skills:
			"Anger", "Cleave", "Decoy Totem", "Determination", "Devouring Totem", "Dominating Blow", "Enduring Cry", "Flame Totem", "Glacial Hammer", "Ground Slam", "Heavy Strike", "Immortal Call", "Infernal Blow", "Leap Slam", "Lightning Strike", "Molten Shell", "Punishment", "Rejuvenation Totem", "Shield Charge", "Shockwave Totem", "Sweep", "Vitality", "Warlord's Mark",
			"Change",
			//Strength support:
			"Added Fire Damage", "Blood Magic", "Cold to Fire", "Fire Penetration", "Increased Duration", "Iron Grip", "Iron Will", "Item Quantity", "Knockback", "Life Gain on Hit", "Life Leech", "Melee Damage on Full Life", "Melee Physical Damage", "Ranged Attack Totem", "Reduced Mana", "Spell Totem", "Stun", "Weapon Elemental Damage",
			"Change",
			//Dexterity  skills:
			"Bear Trap", "Blood Rage", "Burning Arrow", "Detonate Dead", "Double Strike", "Dual Strike", "Elemental Hit", "Ethereal Knives", "Explosive Arrow", "Fire Trap", "Flicker Strike", "Freeze Mine", "Frenzy", "Grace", "Haste", "Hatred", "Ice Shot", "Lightning Arrow", "Phase Run", "Poison Arrow", "Projectile Weakness", "Puncture", "Rain of Arrows", "Split Arrow", "Temporal Chains", "Viper Strike", "Whirling Blades",
			"Change",
			//Dexterity support:
			"Added Cold Damage", "Additional Accuracy", "Blind", "Chain", "Chance to Flee", "Cold Penetration", "Culling Strike", "Faster Attacks", "Faster Projectiles", "Fork", "Greater Multiple Projectiles", "Lesser Multiple Projectiles", "Mana Leech", "Pierce", "Point Blank", "Trap",
			"Change",
			//Intelligence skills:
			"Arc", "Arctic Breath", "Clarity", "Cold Snap", "Conductivity", "Conversion Trap", "Critical Weakness", "Discharge", "Discipline", "Elemental Weakness", "Enfeeble", "Fireball", "Firestorm", "Flammability", "Freezing Pulse", "Frostbite", "Frost Wall", "Ice Nova", "Ice Spear", "Incinerate", "Lightning Warp", "Power Siphon", "Purity", "Raise Spectre", "Raise Zombie", "Righteous Fire", "Shock Nova", "Spark", "Summon Skeletons", "Tempest Shield", "Vulnerability", "Wrath",
			"Change",
			//Intelligence support:
			"Added Chaos Damage", "Added Lightning Damage", "Chance to Ignite", "Concentrated Effect", "Elemental Proliferation", "Faster Casting", "Increased Area of Effect", "Increased Critical Damage", "Increased Critical Strikes", "Item Rarity", "Lightning Penetration", "Minion Damage", "Minion Life", "Minion Speed", "Remote Mine"
	};

	private String gemType;
	//private int level;

	public Gem(String type)
	{
		int gemNumber = verifyType(type);
		if(gemNumber != -1)
		{
			gemType = gemTypes[gemNumber];
		}
		else
		{
			System.out.println("Gem was attempted to be created with an incorrect gem type! type was: " + type);
		}
		//level = 1;
	}

	public String getGemType() {return gemType;}
	//public int getLevel() {return level;}

	@Override
	public String toString()
	{
		//return gemType + " [" + level + "] - " + getGemColour(gemType);
		return gemType + " - " + getGemColour(gemType);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Gem)
		{
			Gem gem = (Gem) o;
			return (gem.getGemType().equals(gemType)/* && gem.getLevel() == level*/);
		}
		else return super.equals(o);
	}

	@Override
	public int compareTo(Gem o)
	{
		return gemType.compareTo(o.getGemType());
	}

	public static int verifyType(String possibleGem)
	{
		for(int i = 0; i < gemTypes.length; i++)
		{
			if(possibleGem.equalsIgnoreCase(gemTypes[i]))
			{
				return i;
			}
		}
		return -1;
	}

	public static String getGemColour(String gem)
	{
		int number = verifyType(gem);
		//get the number of "Colour" before the gem
		int numberOfColour = 0;
		for(int i = 0; i < number; i++)
		{
			if(gemTypes[i].equals("Change"))
			{
				numberOfColour++;
			}
		}
		switch(numberOfColour)
		{
			case 0: return "Red (Skill)";
			case 1: return "Red (Support)";
			case 2: return "Green (Skill)";
			case 3: return "Green (Support)";
			case 4: return "Blue (Skill)";
			case 5: return "Blue (Support)";
			default:
				System.out.println("getGemColour was given an incorrect gem String! Gem is: " + gem + " and the number of colours before it was " + numberOfColour);
				return "";
		}
	}
}
