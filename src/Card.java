public class Card implements Comparable<Card>
{
	public static final int Diamond = 1;
	public static final int Club = 2;
	public static final int Heart = 3;
	public static final int Spade = 4;

	private int type;
	private int number;

	public Card(int num, int t)
	{
		if(num < 1 || num > 13)
		{
			throw new InvalidCardNumberException(num, "a new object of Card failed to initialize!");
		}
		number = num;
		if(t >= Card.Diamond && t <= Card.Spade)
		{
			type = t;
		}
		else
		{
			throw new InvalidCardTypeException(t, "a new object of Card failed to initialize!");
		}
	}

	public int getType() {return type; }
	public int getNumber() {return number; }

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Card)
		{
			return equals((Card) o);
		}
		else return super.equals(o);
	}

	public boolean equals(Card card)
	{
		return card.getNumber() == number && card.getType() == type;
	}

	public static String getType(int type)
	{
		switch (type)
		{
			case Diamond: {return "♦";}
			case Club: {return "♣";}
			case Heart: {return "♥";}
			case Spade: {return "♠";}
			default:
				throw new InvalidCardTypeException(type, "getType method in Card failed - unrecognized type!");
		}
	}

	public static String getNumber(int number)
	{
		switch(number)
		{
			case 1: {return "A";}
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				return "" +  number;
			case 11: {return "J"; }
			case 12: {return "Q"; }
			case 13: {return "K"; }
			default:
				throw new InvalidCardNumberException(number, "getNumber in Card failed - unrecognized number!");
		}
	}


	public static int isCardType(String target)
	{
		switch(target)
		{
			case "A": return 1;
			case "2":
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
			case "8":
			case "9":
			case "10":
				return Integer.parseInt(target);
			case "J": return 11;
			case "Q": return 12;
			case "K": return 13;
			default: return -1;
		}
	}

	@Override
	public String toString()
	{
		return getNumber(number) + Card.getType(type);
	}

	@Override
	public int compareTo(Card other)
	{
		int numberCompare = other.getNumber() - number;
		if(numberCompare != 0)
		{
			return numberCompare;
		}
		else
		{
			numberCompare = other.getType() - type;
			return numberCompare;
		}
	}
}