//OOP 2012 Practical ALL
//Student ID: S10132161H	
//Student Name: Jeremy Choo	
//Module Group: IT08 
//Date: 10/29/12, Time: 3:26 PM
//Version number: 3.141592653589793
//	
//Features:
//1. findSimilarString
//2. menu Start and End
//3. Interactions with getting anything. Method names are: getInteger, isInteger, getDouble, isDouble, getBoolean. Optional parameter of Error message.
//4. Generate random name.
//5. Randomize the values in an array and arraylist. Note: array must be an array of objects.
//6. Converts an array to a String.
//
//Description:
//Base class for popular methods
import java.util.*;

public abstract class Jeremy
{
	/**
	 * Main method for testing/doing random stuff
	 *
	 * @param args arguments passed in from the command line.
	 */
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		Queue<String> userInputs = new LinkedList<>();
		String userInput;
		System.out.println("Copy and paste attributes: ");
		do
		{
			userInput = scanner.nextLine().trim();
			if(!userInput.isEmpty() && !userInput.equals("exit"))
			{
				userInputs.add(userInput);
			}
		} while(!userInput.equals("exit"));
		Queue<String> TODO = new LinkedList<>();
		String[] inputs;
		System.out.println("#region uselessCrap");
		while(!userInputs.isEmpty())
		{
			userInput = userInputs.remove();
			inputs = userInput.split(" ");
			System.out.println("private " + inputs[1] + " _" + inputs[2] + ";");
			TODO.add(userInput);
		}
		System.out.println();
		while(!TODO.isEmpty())
		{
			inputs = TODO.remove().split(" ");
			System.out.println(inputs[0] + " " + inputs[1] + " " + inputs[2]);
			System.out.println("{");
			System.out.println("\tget { return _" + inputs[2] + "; }");
			System.out.println("\tset { _" + inputs[2] + " = value; }");
			System.out.println("}");
			System.out.println();
		}
		System.out.println("#endregion");
	}

	/**
	 * Gets the standard deviation of all the numbers in an array. An optional parameter states the mean of the values in the array.
	 * If left empty, then the mean will be calculated.
	 *
	 * @param array the array of integers
	 * @return the standard deviation
	 */
	public static double getSd(int[] array)
	{
		int mean = 0;
		for(int aNumber : array)
		{
			mean += aNumber;
		}
		mean = mean / array.length;
		return getSd(array, mean);
	}

	/**
	 * Gets the standard deviation of all the numbers in an array. An optional parameter states the mean of the values in the array.
	 * If left empty, then the mean will be calculated.
	 *
	 * @param array the array of integers
	 * @param mean the mean of all the integers in the array
	 * @return the standard deviation
	 */
	public static double getSd(int[] array, int mean)
	{
		double SD = 0;
		for(int aNumber : array)
		{
			SD += Math.pow(aNumber - mean, 2);
		}
		SD = SD / array.length;
		return( Math.sqrt(SD) );
	}

	/**
	 * Generates the beginning of any menu.
	 *
	 * @param  numberOfTabs  the number of tabs you want to have between "Command" and "Result"
	 */
	public static void menuStart(int numberOfTabs)
	{
		System.out.println("========================== Menu ==========================");
		System.out.print("Command ");
		for(int i = 0; i < numberOfTabs; i++)
		{
			System.out.print("\t");
		}
		System.out.println("Result");
	}

	/**
	 * Generates the end for any menu
	 */
	public static void menuEnd()
	{
		System.out.println("==========================================================");
		System.out.print("Your input: ");
	}

	/**
	 * Uses the scanner object to get a boolean from the user.
	 * Prints [Y/N] to output and asks for a Yes or No.
	 * Prints the default error message "Please enter a yes or a no." if the user did not enter an integer.
	 *
	 * @return the boolean that the user has entered in.
	 */
	public static boolean getBoolean()
	{
		return getBoolean("Please enter a yes or a no");
	}

	/**
	 * Uses the scanner object to gets a boolean. Message is the string shown if there is an error.
	 * Prints [Y/N] to output and asks for a Yes or No.
	 *
	 * @param message The string shown if there is any error.
	 * @return a boolean based on what the user has entered.
	 */
	public static boolean getBoolean(String message)
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("[Y/N}");
		String userInput;
		do
		{
			userInput = scanner.nextLine();
			if(isYesNo(userInput))
			{
				return userInput.substring(0, 1).toUpperCase().equals("Y");
			}
			else
			{
				System.out.println(message);
			}
		} while(true);
	}

	//Private method
	private static boolean isYesNo(String target)
	{
		return target.equalsIgnoreCase("Y") || target.equalsIgnoreCase("N") || target.equalsIgnoreCase("Yes") || target.equalsIgnoreCase("No");
	}

	/**
	 * Uses the scanner object to get an integer from the user.
	 *
	 * @param message The string shown if there is any error.
	 * @return the integer that the user has entered in.
	 */
	public static int getInteger(String message)
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		Boolean isInteger;
		do
		{
			userInput = scanner.nextLine();
			isInteger = isInteger(userInput);
			if(!isInteger)
			{
				//If it fails, try doing calculation. Maybe the user entered a calculation? (i.e. 2+2). Try decoding.
				if(Calculator.validateString(userInput).equals("OK"))
				{
					//Then, try decoding and return the result.
					userInput = Calculator.removeSpacesAndComma(userInput);
					double possibleResult = Calculator.calculate(userInput);
					if(possibleResult > Integer.MAX_VALUE || possibleResult < Integer.MIN_VALUE)
					{   //If the calculation ended up more than max value (999999999999999) or less than min value
						//then this is wrong (cannot be accepted). Otherwise the user can just enter 99999999999999999
						System.out.println(message);
					}
					else
					{
						//Ask if the user wants to or not
						System.out.println("You entered an input that can be calculated (i.e. 2+2).  The result for your input is " + (int) possibleResult + ".");
						System.out.print("Would you like to accept this input? ");
						if(Jeremy.getBoolean())
						{
							return (int) possibleResult;
						}
						else
						{
							System.out.println("Well then, " + message.substring(0, 1).toLowerCase() + message.substring(1));
						}
					}
				}
				else
				{
					System.out.println(message);
				}
			}
		} while (!isInteger || !Calculator.validateString(userInput).equals("OK"));
		return Integer.parseInt(userInput);
	}

	/**
	 * Uses the scanner object to get an integer from the user.
	 * Prints the default error message "That is not an integer. Please try again." if the user did not enter an integer.
	 *
	 * @return the integer that the user has entered in.
	 */
	public static int getInteger()
	{
		return getInteger("That is not an integer. Please try again.");
	}

	/**
	 * uses the scanner object to get a double from the user.
	 *
	 * @param message The string shown if there is any error.
	 * @return the double that the user has entered in.
	 */
	public static Double getDouble(String message)
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		Boolean isDouble;
		do
		{
			userInput = scanner.nextLine();
			isDouble = isDouble(userInput);
			if(!isDouble)
			{
				//If it fails, try doing calculation. Maybe the user entered a calculation? (i.e. 2+2). Try decoding.
				if(Calculator.validateString(userInput).equals("OK"))
				{
					//Then, try decoding and return the result.
					userInput = Calculator.removeSpacesAndComma(userInput);
					double possibleResult = Calculator.calculate(userInput);
					//Ask if the user wants to or not
					System.out.println("You entered an input that can be calculated (i.e. 2+2).  The result for your input is " + possibleResult + ".");
					System.out.print("Would you like to accept this input? ");
					if(Jeremy.getBoolean())
					{
						return possibleResult;
					}
					else
					{
						System.out.println("Well then, " + message);
					}
				}
				else
				{
					System.out.println(message);
				}
			}
		} while (!isDouble || !Calculator.validateString(userInput).equals("OK"));
		return Double.parseDouble(userInput);
	}

	/**
	 * uses the scanner object to get a double from the user.
	 * Prints the default error message "That is not a double. Please try again." if the user did not enter a double.
	 *
	 * @return the double that the user has entered in.
	 */
	public static double getDouble()
	{
		return getDouble("That is not a double. Please try again.");
	}

	/**
	 * Checks if the string passed in is an integer or not.
	 *
	 * @param target the String to check
	 * @return true if it is an integer, false if not.
	 */
	public static Boolean isInteger(String target)
	{
		//If the string contains nothing, then...
		if(target.length() == 0)
		{
			return false;
		}
		char[] targetArr = target.toCharArray();
		int maxLength = 10;
		//check for negatives
		if(targetArr[0] == '-')
		{
			//set it to a value so that it doesn't crash with the system below
			targetArr[0] = '5';
			//if the only character was negative, return false
			if(target.length() == 1)
			{
				return false;
			}
			maxLength++;
		}
		for(int aTarget : targetArr)
		{
			if(aTarget < '0' || aTarget > '9')
			{
				return false;
			}
		}
		if(target.length() > maxLength)
		{
			return false;
		}
		else if(target.length() == maxLength) //if it's the maxLength, it might throw an error if it is more than max or less than min.
		{
			if(target.compareTo("" + Integer.MAX_VALUE) > 0 && maxLength == 10)
			{
				return false;
			}
			else if(target.compareTo("" + Integer.MIN_VALUE) > 0 && maxLength == 11)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the string passed in is a double or not.
	 *
	 * @param target the String to check
	 * @return true if it is an double, false if not.
	 */
	public static Boolean isDouble(String target)
	{
		//If the string contains nothing, then...
		if(target.length() == 0)
		{
			return false;
		}
		int dotNum = 0;
		char[] targetArr = target.toCharArray();
		//check for negatives
		if(targetArr[0] == '-')
		{
			//set it to a value so that it doesn't crash with the system below
			targetArr[0] = '5';
			//if the only character was negative, return false
			if(target.length() == 1)
			{
				return false;
			}
		}
		for(int aTarget : targetArr)
		{
			if(aTarget == '.')
			{
				dotNum++;
				//max 1 dot
				if(dotNum > 1)
				{
					return false;
				}
			}
			else if(aTarget < '0' || aTarget > '9')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the string passed in is an integer or not. This method uses a try catch and thus takes more time to execute.
	 *
	 * @param target the String to check
	 * @return true if it is an integer, false if not.
	 */
	public static Boolean isInteger2(String target)
	{
		try
		{
			Integer.parseInt(target);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Checks if the string passed in is a double or not. This method uses a try catch and thus takes more time to excepted.
	 *
	 * @param target the String to check
	 * @return true if it is a double, false if not.
	 */
	public static Boolean isDouble2(String target)
	{
		try
		{
			Double.parseDouble(target);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Generates a random name for you to use. The random name generator is simple and will usually come out with weird names
	 * Names generated contain a first and last name and will never contain a 'q', 'v', 'w', 'x', 'y' and 'z'.
	 *
	 *
	 * @return the random name generated.
	 */
	public static String generateRandomName()
	{
		char[] consonants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't'};
		char[] vowels = {'a', 'e', 'i', 'o', 'u'};
		char[] partName = new char[5];
		Random random = new Random();
		//details if the name will contain 4 or 5 letters
		boolean rerun = false;
		int randNum;
		String firstName, lastName;
		randNum = random.nextInt(consonants.length);
		partName[0] = consonants[randNum];
		randNum = random.nextInt(vowels.length);
		partName[1] = vowels[randNum];
		randNum = random.nextInt(consonants.length);
		partName[2] = consonants[randNum];
		randNum = random.nextInt(100);
		//50 | 50 chance between consonant and vowel
		if(randNum < 50)
		{
			randNum = random.nextInt(consonants.length);
			partName[3] = consonants[randNum];
		}
		else
		{
			randNum = random.nextInt(vowels.length);
			partName[3] = vowels[randNum];
			randNum = random.nextInt(100);
			if(randNum < 50)
			{
				rerun = true;
			}
		}
		if(rerun)
		{
			randNum = random.nextInt(consonants.length);
			partName[4] = consonants[randNum];
			//Add all the part names together
			firstName = "" + partName[0] + partName[1] + partName[2] + partName[3] + partName[4];
		}
		else
		{
			firstName = "" + partName[0] + partName[1] + partName[2] + partName[3];
		}
		//Capitalize the first letter
		firstName = makeFirstUppercase(firstName);
		//Now the last name is created
		randNum = random.nextInt(consonants.length);
		partName[0] = consonants[randNum];
		randNum = random.nextInt(vowels.length);
		partName[1] = vowels[randNum];
		randNum = random.nextInt(consonants.length);
		partName[2] = consonants[randNum];
		lastName = "" + partName[0] + partName[1] + partName[2];
		lastName = makeFirstUppercase(lastName);
		//Add them together
		return "" + firstName + " " + lastName;
	}

	/**This method attempts to find similar strings for a string entered.
	 * By "similar", the method basically will find strings that fit the following criteria:
	 * 1) Have a difference of 2 or less characters (i.e. Jonathan, Jonethon)
	 * 2) Have one less character (i.e. Plastic, Platic)
	 * 3) Have one more character (i.e. Pencil, Peancil)
	 *
	 * @param source The first input is the array of strings to compare
	 * @param target The string that you wish to find similar strings to.
	 * @return an int[] array of the matches location
	 */
	public static int[] findSimilarString (String[] source, String target)
	{
		//Generate two char arrays, used to store the strings to compare
		char[] targetArr;
		char[] sourceArr;
		//locations to store the result
		ArrayList<Integer> locations = new ArrayList<Integer>();
		int matchesBefore, matchesAfter;

		//Split target into multiple characters, assigning them into the first char array
		//It is converted to upper case first so that capitalization won't matter
		targetArr = (target.toUpperCase()).toCharArray();

		//run through every single string in the array
		for(int i = 0; i < source.length; i++)
		{
			matchesBefore = 0;
			matchesAfter = 0;

			//split source[i] into multiple characters and assign them into the second char array.
			sourceArr = (source[i].toUpperCase()).toCharArray();

			//for every character in the char array/String
			for(int j = 0; j < source[i].length() && j < target.length(); j++)
			{
				//if the characters match, increase matchesBefore by 1
				if(targetArr[j] == sourceArr[j])
				{
					matchesBefore++;
				}
			}

			//Now it checks if there were only two or one different character(s)
			//It checks against both because otherwise if let's say I enter "lwuegnleiur" and it is checked against "a",
			//"a" will always be TRUE because there will only be one mistake - the rest aren't checked
			if(matchesBefore >= sourceArr.length - 2 && matchesBefore >= targetArr.length - 2)
			{
				//This is a possible suggestion, so add it in
				locations.add(i);
			}
			else	//if there were more than 2 different characters, it's possible that only a letter was missing
			{		//i.e. chalie and charlie. That way, every single letter afterward would be wrong and destroy the code
				for(int j = 1; j < targetArr.length && j < sourceArr.length; j++)	//thus, now we add a "null" character to the
				{				//start to anticipate missing characters - or pretends to. Instead, [1] is checked with [0]...etc.
					//check if the character in [j] and [j-1] is equal, and increase matchesAfter by 1 if so
					if(targetArr[j-1] == sourceArr[j])
					{
						matchesAfter++;
					}
				}
				//if the two matches, added together, is equal to the length of both strings - 2 (theoretically, only the longer
				if(matchesAfter + matchesBefore >= sourceArr.length - 2 && matchesAfter + matchesBefore >= targetArr.length - 2)
				{		//one matters, if the longer one is true, the shorter one will also be true.)
					// This is "more than" since there is a possibility of double letters. (i.e. Joon)
					locations.add(i);
				}
				else	//If the for loop failed, it's also possible that there was an extra letter (i.e. charelie) Thus, now...
				{
					matchesAfter = 0;			//reset matchesAfter (It was probably 0 anyway, but just in case...)
					for(int j = 1; j < targetArr.length && j < sourceArr.length; j++)	//we will measure for [0] against [1], and
					{										//so forth. Repeat and rinse...
						//check if the character in [j] and [j-1] is equal, and if so, increase matchesAfter by 1
						if(targetArr[j] == sourceArr[j-1])
						{
							matchesAfter++;
						}
					}
					//if the two matches, added together, is equal to the length of both strings - 2 (theoretically, only the longer
					if(matchesAfter + matchesBefore >= sourceArr.length - 2 && matchesAfter + matchesBefore >= targetArr.length - 2)
					{		//one matters, if the longer one is true, the shorter one will also be true.)
						locations.add(i);
					}
				}	//end of if-else
			}	//otherwise, this is not a suggestion. Continue on with the next name...
		}	//end of for statement, all matches have been found.
		//initialize a new array to store the required search terms
		int[] results = new int[locations.size()];
		for(int i = 0; i < locations.size(); i++)
		{
			results[i] = locations.get(i);
		}
		return results;
	}	//end of method.

	/**This method attempts to find similar strings for a string entered.
	 * By "similar", the method basically will find strings that fit the following criteria:
	 * 1) Have a difference of 2 or less characters (i.e. Jonathan, Jonethon)
	 * 2) Have one less character (i.e. Plastic, Platic)
	 * 3) Have one more character (i.e. Pencil, Peancil)
	 *
	 * @param source The first input is the array of strings to compare
	 * @param target The string that you wish to find similar strings to.
	 * @return an ArrayList<Integer>() array of the matches location
	 */
	public static ArrayList<Integer> findSimilarString (ArrayList<String> source, String target)
	{
		//Generate two char arrays, used to store the strings to compare
		char[] targetArr;
		char[] sourceArr;
		//locations to store the result
		ArrayList<Integer> locations = new ArrayList<Integer>();
		int matchesBefore, matchesAfter;

		//Split target into multiple characters, assigning them into the first char array
		//It is converted to upper case first so that capitalization won't matter
		targetArr = (target.toUpperCase()).toCharArray();

		//run through every single string in the array
		for(int i = 0; i < source.size(); i++)
		{
			matchesBefore = 0;
			matchesAfter = 0;

			//split source[i] into multiple characters and assign them into the second char array.
			sourceArr = (source.get(i).toUpperCase()).toCharArray();

			//for every character in the char array/String
			for(int j = 0; j < source.get(i).length() && j < target.length(); j++)
			{
				//if the characters match, increase matchesBefore by 1
				if(targetArr[j] == sourceArr[j])
				{
					matchesBefore++;
				}
			}

			//Now it checks if there were only two or one different character(s)
			//It checks against both because otherwise if let's say I enter "lwuegnleiur" and it is checked against "a",
			//"a" will always be TRUE because there will only be one mistake - the rest aren't checked
			if(matchesBefore >= sourceArr.length - 2 && matchesBefore >= targetArr.length - 2)
			{
				//This is a possible suggestion, so add it in
				locations.add(i);
			}
			else	//if there were more than 2 different characters, it's possible that only a letter was missing
			{		//i.e. chalie and charlie. That way, every single letter afterward would be wrong and destroy the code
				for(int j = 1; j < targetArr.length && j < sourceArr.length; j++)	//thus, now we add a "null" character to the
				{				//start to anticipate missing characters - or pretends to. Instead, [1] is checked with [0]...etc.
					//check if the character in [j] and [j-1] is equal, and increase matchesAfter by 1 if so
					if(targetArr[j-1] == sourceArr[j])
					{
						matchesAfter++;
					}
				}
				//if the two matches, added together, is equal to the length of both strings - 2 (theoretically, only the longer
				if(matchesAfter + matchesBefore >= sourceArr.length - 2 && matchesAfter + matchesBefore >= targetArr.length - 2)
				{		//one matters, if the longer one is true, the shorter one will also be true.)
					// This is "more than" since there is a possibility of double letters. (i.e. Joon)
					locations.add(i);
				}
				else	//If the for loop failed, it's also possible that there was an extra letter (i.e. charelie) Thus, now...
				{
					matchesAfter = 0;			//reset matchesAfter (It was probably 0 anyway, but just in case...)
					for(int j = 1; j < targetArr.length && j < sourceArr.length; j++)	//we will measure for [0] against [1], and
					{										//so forth. Repeat and rinse...
						//check if the character in [j] and [j-1] is equal, and if so, increase matchesAfter by 1
						if(targetArr[j] == sourceArr[j-1])
						{
							matchesAfter++;
						}
					}
					//if the two matches, added together, is equal to the length of both strings - 2 (theoretically, only the longer
					if(matchesAfter + matchesBefore >= sourceArr.length - 2 && matchesAfter + matchesBefore >= targetArr.length - 2)
					{		//one matters, if the longer one is true, the shorter one will also be true.)
						locations.add(i);
					}
				}	//end of if-else
			}	//otherwise, this is not a suggestion. Continue on with the next name...
		}	//end of for statement, all matches have been found.
		return locations;
	}	//end of method.

	/**
	 * makes the first letter of a string uppercase
	 *
	 * @param target the String where you want to turn the first letter to uppercase
	 * @return the String in target with the first letter uppercase.
	 */
	public static String makeFirstUppercase(String target)
	{
		return target.substring(0, 1).toUpperCase() + target.substring(1);
	}

	/**
	 * randomizes the objects in an array of objects.
	 * For arrays where it is an array of primitive data type, this method is overloaded with a method that takes in an object for
	 * primitive data types.
	 *
	 * @param array the array which you want to randomize
	 */
	public static void randomize(Object[] array)
	{
		Random random = new Random();
		int randNum;
		Object temp;
		for(int i = array.length; i > 0; i--)
		{
			randNum = random.nextInt(i);
			temp = array[randNum];
			array[randNum] = array[i-1];
			array[i - 1] = temp;
		}
	}

	/**
	 * randomizes the objects in an array of primitive data types.
	 * It does not matter whether it is an int[], double[], char[] etc, the program will find out the type and cast it automatically.
	 * For arrays where it is an array of Objects, this method is overloaded with a method that takes in an object[]
	 *
	 * @param array the array which you want to randomize
	 */
	public static void randomize(Object array)
	{
		Random random = new Random();
		int randNum;
		if(!array.getClass().isArray())
		{
			System.out.println("Oops! The object submitted in randomize is not really an array! It's " + array.toString());
		}
		//boolean, byte, char, short, int, long, float, and double
		switch(array.getClass().getComponentType().toString())
		{
			case "boolean":
				System.out.println("boolean");
				boolean[] booleans = (boolean[]) array;
				boolean temp1;
				for(int i = booleans.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp1 = booleans[randNum];
					booleans[randNum] = booleans[i-1];
					booleans[i - 1] = temp1;
				}
				break;
			case "byte":
				System.out.println("byte");
				byte[] bytes = (byte[]) array;
				byte temp2;
				for(int i = bytes.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp2 = bytes[randNum];
					bytes[randNum] = bytes[i-1];
					bytes[i - 1] = temp2;
				}
				break;
			case "char":
				System.out.println("char");
				char[] chars = (char[]) array;
				char temp3;
				for(int i = chars.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp3 = chars[randNum];
					chars[randNum] = chars[i-1];
					chars[i - 1] = temp3;
				}
				break;
			case "short":
				System.out.println("short");
				short[] shorts = (short[]) array;
				short temp4;
				for(int i = shorts.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp4 = shorts[randNum];
					shorts[randNum] = shorts[i-1];
					shorts[i - 1] = temp4;
				}
				break;
			case "int":
				System.out.println("int");
				int[] ints = (int[]) array;
				int temp5;
				for(int i = ints.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp5 = ints[randNum];
					ints[randNum] = ints[i-1];
					ints[i - 1] = temp5;
				}
				break;
			case "long":
				System.out.println("long");
				long[] longs = (long[]) array;
				long temp6;
				for(int i = longs.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp6 = longs[randNum];
					longs[randNum] = longs[i-1];
					longs[i - 1] = temp6;
				}
				break;
			case "float":
				System.out.println("float");
				float[] floats = (float[]) array;
				float temp7;
				for(int i = floats.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp7 = floats[randNum];
					floats[randNum] = floats[i-1];
					floats[i - 1] = temp7;
				}
				break;
			case "double":
				System.out.println("double");
				double[] doubles = (double[]) array;
				double temp8;
				for(int i = doubles.length; i > 0; i--)
				{
					randNum = random.nextInt(i);
					temp8 = doubles[randNum];
					doubles[randNum] = doubles[i-1];
					doubles[i - 1] = temp8;
				}
				break;
			default:
				System.out.println("Oops! arrayToString noted that the object was an array, " +
						"but it was neither a boolean, byte, char, short, int, long, float, and double array! Not too sure how to handle this...");
		}
	}

	/**
	 * randomize the objects in an arraylist. Arraylist can contain any object(s)
	 *
	 * @param arrayList the arraylist which you want to randomize
	 */
	@SuppressWarnings("unchecked")
	//unchecked warning is OK here, the warning occurs at the line "arrayList.add(arrayList.get(randNum));", which will always
	//be ok because you're adding an Object to an arraylist of Objects.
	public static void randomize(ArrayList arrayList)
	{
		Random random = new Random();
		int randNum;
		for(int i = arrayList.size(); i > 0; i--)
		{
			randNum = random.nextInt(i);
			arrayList.add(arrayList.get(randNum));
			arrayList.remove(randNum);
		}
	}

	/**
	 * Pauses the thread for a predefined amount of time.
	 */
	public static void pause(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{
			System.out.println("Thread.sleep(" + time + ") failed.");
			e.printStackTrace();
		}
	}

	/**
	 * Checks if an array list contains a specified object or not.
	 *
	 * @param arrayList The arraylist in which the object will be searched for
	 * @param o The object to find
	 * @return the location in the arraylist if found, -1 if not.
	 */
	public static int searchInArrayList(ArrayList arrayList, Object o)
	{
		for(int i = 0; i < arrayList.size(); i++)
		{
			if(arrayList.get(i).equals(o))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Converts an array of Objects to a String for printing. String is in the following format:
	 * "[" + array[0] + ", " + array[1] + ", " + array[2] + ", " + array[3] + ... + "]"
	 * For an array of primitive objects, there is an overloaded method that takes in an object.
	 *
	 * @param array The array to be converted into a String
	 * @return the result, String.
	 */
	public static String arrayToString(Object[] array)
	{
		String result = "[";
		for(Object anArray : array)
		{
			result = result + anArray + ", ";
		}
		if(array.length != 0)
		{
			result = result.substring(0, result.length() - 2);
		}
		result = result + "]";
		return result;
	}

	/**
	 * Converts a primitive array to a String for printing. String is in the following format:
	 * "[" + array[0] + ", " + array[1] + ", " + array[2] + ", " + array[3] + ... + "]"
	 * It does not matter whether it is an int[], double[], char[] etc, the program will find out the type and cast it automatically.
	 * For an array of primitive objects, there is an overloaded method that takes in an object.
	 *
	 * @param array The array to be converted into a String
	 * @return the result, String.
	 */
	public static String arrayToString(Object array)
	{
		if(!array.getClass().isArray())
		{
			System.out.println("Oops! The object submitted in arrayToString is not really an array! It's " + array.toString());
			return array.toString();
		}
		//boolean, byte, char, short, int, long, float, and double
		String result = "[";
		int lengthOfArray;
		switch(array.getClass().getComponentType().toString())
		{
			case "boolean":
				boolean[] booleans = (boolean[]) array;
				for(boolean aBoolean : booleans)
				{
					result = result + aBoolean + ", ";
				}
				lengthOfArray = booleans.length;
				break;
			case "byte":
				byte[] bytes = (byte[]) array;
				for(byte aByte : bytes)
				{
					result = result + aByte + ", ";
				}
				lengthOfArray = bytes.length;
				break;
			case "char":
				char[] chars = (char[]) array;
				for(char aChar : chars)
				{
					result = result + aChar + ", ";
				}
				lengthOfArray = chars.length;
				break;
			case "short":
				short[] shorts = (short[]) array;
				for(short aShort : shorts)
				{
					result = result + aShort + ", ";
				}
				lengthOfArray = shorts.length;
				break;
			case "int":
				int[] ints = (int[]) array;
				for(int aInt : ints)
				{
					result = result + aInt + ", ";
				}
				lengthOfArray = ints.length;
				break;
			case "long":
				long[] longs = (long[]) array;
				for(long aLong : longs)
				{
					result = result + aLong + ", ";
				}
				lengthOfArray = longs.length;
				break;
			case "float":
				float[] floats = (float[]) array;
				for(float aFloat : floats)
				{
					result = result + aFloat + ", ";
				}
				lengthOfArray = floats.length;
				break;
			case "double":
				double[] doubles = (double[]) array;
				for(double aDouble : doubles)
				{
					result = result + aDouble + ", ";
				}
				lengthOfArray = doubles.length;
				break;
			default:
				System.out.println("Oops! arrayToString noted that the object was an array, " +
						"but it was neither a boolean, byte, char, short, int, long, float, and double array! Not too sure how to handle this...");
				return array.toString();
		}
		if(lengthOfArray != 0)
		{
			result = result.substring(0, result.length() - 2);
		}
		result = result + "]";
		return result;
	}

	/**
	 * Method to encode a String with a given seed. Remember the seed: it will be needed to decode it!
	 *
	 * @param target The String to be encoded
	 * @param seed The seed to be used to encode the String. The longer, the better, the more different the letters are, the better.
	 * @return The String that is encoded.
	 */
	public static String encode(String target,String seed)
	{
		char[] targetArr = target.toCharArray();
		char[] seedArr = seed.toCharArray();
		int seedNum = 0;
		String result = "";
		for(char aTargetArr : targetArr)
		{
			result = result + (char) (aTargetArr + seedArr[seedNum] - 'A');
			seedNum++;
			if(seedNum == seed.length())
			{
				seedNum = 0;
			}
		}
		return result;
	}

	/**
	 * Method to decode a String with a given seed. This complements the decode method.
	 *
	 * @param target The String to be decoded
	 * @param seed The seed to be used to decode the String.
	 * @return The String that is decoded.
	 */
	public static String decode(String target,String seed)
	{
		char[] targetArr = target.toCharArray();
		char[] seedArr = seed.toCharArray();
		int seedNum = 0;
		String result = "";
		for(char aTargetArr : targetArr)
		{
			result = result + (char) (aTargetArr - seedArr[seedNum] + 'A');
			seedNum++;
			if(seedNum == seed.length())
			{
				seedNum = 0;
			}
		}
		return result;
	}
}