import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class PoeQuality
{
	public static ArrayList<PossibleQuality> rejections;
	public static boolean debug;

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		int option;
		do
		{
			System.out.print("Use quality list from PoeHelper [1] or create your own quality list [2]? ");
			option = Jeremy.getInteger("Please enter either 1 or 2. \n" +
					"Enter 1 to load files from PoeHelper and use those lists. + \n " +
					"Enter 2 to start a new quality check with no template.");
			if(option == 1)
			{
				ArrayList<PossibleQuality> qualities = new ArrayList<PossibleQuality>();
				//Load files
				try
				{
					FileInputStream fileIn = new FileInputStream("Quality.ser");
					ObjectInputStream in = new ObjectInputStream(fileIn);
					qualities = (ArrayList<PossibleQuality>) in.readObject();
					in.close();
					fileIn.close();
					System.out.println("Files were successfully loaded!");
				}
				catch(FileNotFoundException f)
				{
					System.out.println("Files were not found! The database was not loaded.");
				}
				catch(Exception e)
				{
					Jeremy.pause(2);
					System.out.println("File failed to load!");
					System.out.println("Details:");
					System.out.println("-----------------------------------------------------------------------------------------------------");
					e.printStackTrace();
					System.out.println("-----------------------------------------------------------------------------------------------------");
				}
				//Initialize values in PoeHelper
				PoeHelper.admin = false;
				PoeHelper.edited = false;
				PoeHelperInterface.quality(qualities);
				if(PoeHelper.edited)
				{
					System.out.print("Would you like to save the qualities? ");
					if(Jeremy.getBoolean())
					{
						try
						{
							FileOutputStream fileOut = new FileOutputStream("Quality.ser");
							ObjectOutputStream out = new ObjectOutputStream(fileOut);
							out.writeObject(qualities);
							fileOut.close();
							out.close();
							System.out.println("Files were successfully saved.");
						}
						catch(Exception e)
						{
							System.out.println("An error occurred!");
							System.out.println("Details:");
							System.out.println("-----------------------------------------------------------------------------------");
							Jeremy.pause(2);
							e.printStackTrace();
							System.out.println("-----------------------------------------------------------------------------------");
						}
					}
				}
				else
				{
					System.out.println("Files were not saved.");
				}
			}
			else if(option == 2)
			{
				System.out.print("Use debug mode? ");
				PoeHelperInterface.newQuality(Jeremy.getBoolean());
			}
			else
			{
				System.out.println("Please enter either a 1 or 2.");
				System.out.println("Enter 1 to load files from PoeHelper and use those lists.");
				System.out.println("Enter 2 to start a new quality check with no template.");
			}
		} while (option != 1 && option != 2);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> calculateQuality(ArrayList<Integer> qualities, boolean toDebug)
	{
		ArrayList<Integer> startingQuality = new ArrayList<Integer>();
		rejections = new ArrayList<PossibleQuality>();
		debug = toDebug;

		System.out.print("Would you like to use any specific qualities from this list? ");
		if(Jeremy.getBoolean())
		{
			for(int i = 1; ; i++)
			{
				System.out.print("[" + i + "] Please enter in the qualities you would like to use: ");
				int userInput = Jeremy.getInteger();
				if(Jeremy.searchInArrayList(qualities, userInput) != -1)
				{
					startingQuality.add(userInput);
				}
				else if(userInput <= 0 || userInput >= 20)
				{
					PoeHelperInterface.removeCertainQualities(qualities, startingQuality);
					System.out.println("The qualities you entered: " + startingQuality + " was removed from the main qualities.");
					break;
				}
				else
				{
					System.out.println("That quality was not found in the list of qualities!");
				}
			}
		}

		Collections.sort(qualities);
		//Stores 10+
		ArrayList<Integer> above10 = new ArrayList<Integer>();
		//Stores 5, 6, 7, 8, 9. Also, I'm using an Array! :O
		int[] amountsBelow10 = new int[5];
		for(int i = 0; i < amountsBelow10.length; i++)
		{
			amountsBelow10[i] = 0;                  //initialization
		}


		//Stores 4-. Should be 0.
		ArrayList<Integer> others = new ArrayList<Integer>();

		for(Integer aQuality : qualities)
		{
			if(aQuality >= 5 && aQuality <= 9)
			{
				amountsBelow10[aQuality - 5]++;
			}
			else if(aQuality >= 10)
			{
				above10.add(aQuality);
			}
			else
			{
				others.add(aQuality);
			}
		}

		//Get the list of all those big qualities
		ArrayList<PossibleQuality> bigQualities = new ArrayList<PossibleQuality>();
		//If the user specified starting qualities, add those in
		if(startingQuality.size() != 0)
		{
			bigQualities.add(new PossibleQuality(startingQuality));
		}
		createStarter(bigQualities, startingQuality, (ArrayList<Integer>) above10.clone());
		Collections.sort(bigQualities);

		for(int i = bigQualities.size() - 1; i >= 0; i--)
		{
			//calculate it
			ArrayList<Integer> result = finishQuality(bigQualities.get(i).getTheQuality(), amountsBelow10);
			if(result.size() != 0)
			{
				return result;
			}
		}

		//Attempt to finish by matching those below 10, but only if the user did not specify that he wants certain qualities.
		if(startingQuality.size() == 0)
		{
			return finishQuality(new ArrayList<Integer>(), amountsBelow10);
		}
		else
		{
			return new ArrayList<Integer>();
		}
	}

	public static ArrayList<Integer> finishQuality(ArrayList<Integer> currentsBackup, int[] possibles)
	{
		if(debug)
		{
			System.out.println("Finish Quality: Current: " + currentsBackup + "\tpossibles: " + arrayToString(possibles));
		}

		//Check if can finish or not
		if(getTotal(currentsBackup) == 40 && isNotRejected(currentsBackup))
		{
			//Ask if the user will accept it
			System.out.println("Final possibles: " + Jeremy.arrayToString(possibles));
			System.out.print("Possible combination identified: " + currentsBackup + ". Accept? ");
			if(Jeremy.getBoolean())
			{
				return currentsBackup;
			}
			else
			{
				rejections.add(new PossibleQuality(currentsBackup));
				return new ArrayList<Integer>();
			}
		}
		else if(getTotal(currentsBackup) >= 40)
		{
			return new ArrayList<Integer>();
		}
		@SuppressWarnings("unchecked")
		ArrayList<Integer> currents = (ArrayList<Integer>) currentsBackup.clone();
		int wanted = 40 - getTotal(currents);
		ArrayList<Integer> commons = new ArrayList<Integer>();
		ArrayList<Integer> result;
		int[] possiblesToSend = new int[5];
		System.arraycopy(possibles, 0, possiblesToSend, 0, possiblesToSend.length);
		//Scan through all the highest ones
		for(int i = 0; i < numOfHighest(possibles); i++)
		{
			int common = getHighestRelatedNumber(possiblesToSend);
			//If the highest is 0, we can't exactly use that...so we skip it. In this case, if its 0, then there is no way this will
			//match (highest is 0!!!) so we return nothing
			if(possibles[common - 5] == 0)
			{
				return new ArrayList<Integer>();
			}
			commons.add(common);
			wanted = wanted - common;
			possibles[common - 5]--;
			possiblesToSend[common - 5]--;
			currents.add(common);
			result = finishQuality(currents, possibles);
			if(result.size() != 0)
			{
				return result;
			}
			//Undo
			possibles[common - 5]++;
			wanted = wanted + common;
			currents.remove(currents.size() - 1);
		}
		//reset them back
		for(Integer aCommon : commons)
		{
			possiblesToSend[aCommon - 5]++;
		}
		commons.clear();

		//Next, try the second one
		for(int i = 0; i < numOfHighest2(possibles); i++)
		{
			int common = getHighestRelatedNumber2(possiblesToSend);
			//If the highest is 0, we can't exactly use that...so we skip it. In this case, if it's 0, the second highest are all 0
			//and are thus useless. So forget the for loop. And since we checked the first highest, it failed, second highest, it
			//is 0, so impossible...no reason to check the rest. So, return fail.
			if(possibles[common - 5] == 0)
			{
				return new ArrayList<Integer>();
			}
			commons.add(common);
			wanted = wanted - common;
			possibles[common - 5]--;
			possiblesToSend[common - 5]--;
			currents.add(common);
			result = finishQuality(currents, possibles);
			if(result.size() != 0)
			{
				return result;
			}
			//Undo
			wanted = wanted + common;
			possibles[common - 5]++;
			currents.remove(currents.size() - 1);
		}
		//reset them back
		for(Integer aCommon : commons)
		{
			possiblesToSend[aCommon - 5]++;
		}

		//Attempt to test from 9 to 5, then.
		for(int i = possibles.length - 1; i >= 0; i--)
		{
			int currentNum;
			if(possibles[i] > 0)
			{
				currentNum = i+5;
				possibles[i]--;
				wanted = wanted - currentNum;
				currents.add(currentNum);
				result = finishQuality(currents, possibles);
				if(result.size() != 0)
				{
					return result;
				}
				//Undo
				possibles[i]++;
				wanted = wanted + currentNum;
				currents.remove(currents.size() - 1);
			}
		}

		//Else, sorry.
		return new ArrayList<Integer>();
	}

	@SuppressWarnings("unchecked")
	public static void createStarter(ArrayList<PossibleQuality> qualities, ArrayList<Integer> currentQuality, ArrayList<Integer> above10s)
	{
		for(int i = above10s.size() - 1; i >= 0; i--)
		{
			currentQuality.add(above10s.get(i));
			if(getTotal(currentQuality) <= 40)
			{
				ArrayList<Integer> currentQuality2 = (ArrayList<Integer>) currentQuality.clone();
				if(!isAlreadyNoted(qualities, currentQuality2))
				{
					ArrayList<Integer> above10s2 = (ArrayList<Integer>) above10s.clone();
					above10s2.remove(i);
					qualities.add(new PossibleQuality((ArrayList<Integer>) currentQuality.clone()));
					createStarter(qualities, currentQuality2, above10s2);
				}
			}
			currentQuality.remove(currentQuality.size() - 1);
		}
	}

	//Gets the highest n's value in an arrayList
	public static int getHighestLocation(ArrayList<Integer> target, int number)
	{
		//if you are not asking for the highest, get the previous highest.
		boolean isHighest = true;
		int previousHighest = 9999;
		if(number != 1)
		{
			previousHighest = getHighestLocation(target, number - 1);
			isHighest = false;
		}
		int highest = target.get(0);
		for(Integer aTarget : target)
		{
			if(isHighest)
			{
				if(aTarget > highest)
				{
					highest = aTarget;
				}
			}
			else
			{
				if(aTarget < previousHighest && aTarget > highest)
				{
					highest = aTarget;
					if(previousHighest == 9999)
					{
						System.out.println("For some reason, isHighest was true and the previous highest was used!");
					}
				}
			}
		}
		for(int i = 0; i < target.size(); i++)
		{
			if(target.get(i) == highest)
			{
				return i;
			}
		}
		throw new RuntimeException("getHighestLocation found an integer in an arraylist once, but couldn't find it again!\n" +
				"integer was: " + highest + " and arraylist was: " + target);
	}

	public static boolean isAlreadyNoted(ArrayList<PossibleQuality> qualities, ArrayList<Integer> toCheck)
	{
		for(PossibleQuality aPossibleQuality : qualities)
		{
			if(aPossibleQuality.qualityEquals(toCheck))
			{
				return true;
			}
		}
		return false;
	}

	public static int getTotal(ArrayList<Integer> target)
	{
		int total = 0;
		for(int aTarget : target)
		{
			total += aTarget;
		}
		return total;
	}

	private static int getHighestRelatedNumber(int[] target)
	{
		if(target.length != 5)
		{
			throw new RuntimeException("getHighestRelatedNumber had an array that was more than 5!\n" +
					"It was: " + arrayToString(target));
		}
		int highest = 0;
		for(int aTarget : target)
		{
			if(highest < aTarget)
			{
				highest = aTarget;
			}
		}
		//get the location
		for(int i = 0; i < target.length; i++)
		{
			if(target[i] == highest)
			{
				return (i + 5);
			}
		}
		throw new RuntimeException("getHighestRelatedNumber found an integer in an array once, but couldn't find it again!\n" +
				"integer was: " + highest + " and array was: " + arrayToString(target));
	}

	private static int numOfHighest(int[] target)
	{
		int highest = 0;
		int times = 0;
		for(int aTarget : target)
		{
			if(highest < aTarget)
			{
				highest = aTarget;
				times = 1;
			}
			if(aTarget == highest)
			{
				times++;
			}
		}
		return times;
	}

	private static int getHighestRelatedNumber2(int[] target)
	{
		//Get the first highest
		int highest = target[getHighestRelatedNumber(target) - 5];
		//Get the second highest
		int secondHighest = 0;
		for(int aTarget : target)
		{
			if(secondHighest < aTarget && aTarget != highest)   //Cannot be the highest
			{
				secondHighest = aTarget;
			}
		}
		//get the location
		for(int i = 0; i < target.length; i++)
		{
			if(target[i] == secondHighest)
			{
				return (i + 5);
			}
		}
		throw new RuntimeException("getHighestRelatedNumber2 found an integer in an array once, but couldn't find it again!\n" +
				"integer was: " + secondHighest + " and array was: " + arrayToString(target));
	}

	private static int numOfHighest2(int[] target)
	{
		//Get the first highest
		int highest = target[getHighestRelatedNumber(target) - 5];
		int times = 0;
		int secondHighest = 0;
		for(int aTarget : target)
		{
			if(secondHighest < aTarget && aTarget != highest)
			{
				secondHighest = aTarget;
				times = 1;
			}
			if(aTarget == secondHighest)
			{
				times++;
			}
		}
		return times;
	}

	private static String arrayToString(int[] target)
	{
		String result = "[";
		for(int aTarget : target)
		{
			result = result + aTarget + ", ";
		}
		if(target.length > 0)
		{
			result = result.substring(0, result.length() - 2);
		}
		return result + "]";
	}

	private static boolean isNotRejected(ArrayList<Integer> possible)
	{
		for(PossibleQuality aRejection : rejections)
		{
			if(aRejection.qualityEquals(possible))
			{
				return false;
			}
		}
		return true;
	}
}
