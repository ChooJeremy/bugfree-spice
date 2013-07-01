import java.util.*;

public class CTF
{
	static Random random = new Random();

	public static void main(String[] args)
	{
		for(int i = 0; i < 9999; i++)
		{
			System.out.print("\tpublic static String ");
			System.out.print(generateRandomNumbersAndLetters(6));
			System.out.println("() {return \"" + generateRandomNumbersAndLetters(10) + "\"; }");
		}
	}

	public static String generateRandomNumbersAndLetters(int length)
	{
		char[] values = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3',
				'4', '5', '6', '7', '8', '9'};
		String result = "";
		for(String randChar; result.length() < length;)
		{
			if(result.length() == 0)
			{
				randChar = "" + values[random.nextInt(values.length - 9)];
			}
			else
			{
				randChar = "" + values[random.nextInt(values.length)];
			}
			if(random.nextBoolean())
			{
				randChar = randChar.toUpperCase();
			}
			result += randChar;
		}
		return result;
	}
}
