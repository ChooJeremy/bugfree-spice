import java.util.*;

public class PoeHelperInterface
{
	public static void quality(ArrayList<PossibleQuality> qualities)
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		boolean rerun, debug = false;
		ArrayList<Integer> quality = new ArrayList<Integer>();

		do
		{
			rerun = true;
			qualityMenu(debug);
			userInput = scanner.nextLine().toUpperCase();
			switch(userInput)
			{
				case "QUALITY":
					newQuality(debug);
					rerun = false;
					break;
				case "ALL":
					printQualities(qualities);
					break;
				case "NEW":
					for(int i = 1;;i++)
					{
						System.out.print("Please enter in quality number " + i + ", or an invalid one to cancel: ");
						userInput = scanner.nextLine();
						if(userInput.equalsIgnoreCase("x"))
						{
							return;
						}
						else
						{
							int possibleQuality = validateString(userInput);
							if(possibleQuality >= 0 && possibleQuality <= 20)
							{
								quality.add(possibleQuality);
							}
							else
							{
								qualities.add(new PossibleQuality(quality));
								System.out.print("Enter in the name for the quality list " + quality  + ": ");
								qualities.get(qualities.size() - 1).setName(scanner.nextLine());
								PoeHelper.edited = true;
								quality.clear();
								break;
							}
						}
					}
					break;
				case "X":
					return;
				default:
					if(PoeHelper.admin)
					{
						if(userInput.equals("DEBUG"))
						{
							debug = true;
							System.out.println("You will now enter any quality calculations with debug mode on.");
							break;
						}
					}
					Boolean found = false;
					String possibleName = getTheName(userInput);
					for(int i = 0; i < qualities.size(); i++)
					{
						PossibleQuality aQuality = qualities.get(i);
						if(possibleName.equalsIgnoreCase(aQuality.getName()))
						{
							found = true;
							String possibleType = getTheType(userInput);
							if(possibleType == null)
							{
								editQuality(qualities, i, "", debug);
							}
							else if(possibleType.equalsIgnoreCase("-d"))
							{
								editQuality(qualities, i, "DELETE", debug);
							}
							else if(possibleType.equalsIgnoreCase("-s"))
							{
								editQuality(qualities, i, "SORT", debug);
							}
							else if(possibleType.equalsIgnoreCase("-u"))
							{
								editQuality(qualities, i, "USE", debug);
							}
							else if(possibleType.equalsIgnoreCase("-a"))
							{
								editQuality(qualities, i, "ADD", debug);
							}
							else if(possibleType.equalsIgnoreCase("-r"))
							{
								editQuality(qualities, i, "REMOVE", debug);
							}
							else if(possibleType.equalsIgnoreCase("-n"))
							{
								editQuality(qualities, i, "NAME", debug);
							}
							else
							{
								System.out.println("Unrecognized type. The only accepted types are <none>, -d, -u, -s, -a, -r and -n.");
								System.out.println("<none> means calculate quality for that type, d means delete it, " +
										"u means quick-use it, s means sort it, a means add new qualities, r means remove qualities " +
										"and n means change it's name.");
							}
						}
					}
					if(!found)
					{
						System.out.println("Unrecognized name of quality list. Please try again.");
					}
					break;
			}
		} while(rerun);
	}

	public static void qualityMenu(boolean isDebug)
	{
		Jeremy.menuStart(3);
		System.out.println("quality\t\t\t-> Begin a new quality check (no template)");
		System.out.println("all \t\t\t-> Print all the lists");
		System.out.println("new \t\t\t-> Add a new list of qualities");
		System.out.println("<name> \t\t\t-> Edit this quality list");
		System.out.println("<name> -d\t\t-> Delete this quality list");
		System.out.println("<name> -u\t\t-> Quick-use this quality list");
		System.out.println("<name> -s\t\t-> Sort this quality list");
		System.out.println("<name> -a\t\t-> Add new qualities to this list");
		System.out.println("<name> -r\t\t-> Remove qualities from this list");
		System.out.println("<name> -n\t\t-> Change the name of this list");
		if(PoeHelper.admin)
		{
			System.out.println("--------------ADMIN commands:----------------------");
			System.out.print("debug\t\t\t-> Enter Quality calculator with debug mode ");
			if(isDebug)
			{
				System.out.println("off.");
			}
			else
			{
				System.out.println("on.");
			}
		}
		Jeremy.menuEnd();
	}

	public static void newQuality(boolean toDebug)
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		ArrayList<Integer> qualityArr = new ArrayList<Integer>();
		int possibleQuality;
		System.out.println("Enter your quality values, in any order. To stop, enter a value out of range.");
		for(int i = 0; ; i++)
		{
			System.out.print("Item number " + (i+1) + " has quality: ");
			do
			{
				userInput = input.nextLine();
			} while(userInput.equals(""));
			if(userInput.equalsIgnoreCase("x"))
			{
				return;
			}
			possibleQuality = validateString(userInput);
			if(possibleQuality < 0 || possibleQuality > 20)
			{
				break;
			}
			else
			{
				qualityArr.add(possibleQuality);
			}
		}
		ArrayList<Integer> results = PoeQuality.calculateQuality(qualityArr, toDebug);
		if(results.size() == 0)
		{
			System.out.println("There is no way to add all of them up to 40!");
		}
		else
		{
			System.out.println("You will need the following quality: " + results);
		}
	}

	public static void printQualities(ArrayList<PossibleQuality> qualities)
	{
		if(qualities.size() == 0)
		{
			System.out.println("There are no qualities stored in the database!");
			return;
		}
		int maxLength = 0;
		//Get the maximum length of any name.
		for(PossibleQuality aQuality : qualities)
		{
			if(aQuality.getName().length() > maxLength)
			{
				maxLength = aQuality.getName().length();
			}
		}
		//Increase maxLength until it is a multiple of 4, for printf
		for(; maxLength % 4 != 0; maxLength++);
		System.out.print("Name");
		for(int i = 4; i < maxLength; i = i + 4)
		{
			System.out.print("\t");
		}
		System.out.println("\tQualities stored");
		for(PossibleQuality aQuality : qualities)
		{
			System.out.printf("%-" + maxLength + "s\t" + aQuality.getTheQuality() + "\n", aQuality.getName());
		}
	}

	public static String getTheName(String target)
	{
		if(target.length() < 3)     //prevent crashing if the name is like 1 and I just want to edit it.
		{
			return target;
		}
		String possibleRemoval = target.substring(target.length() - 2);
		if(possibleRemoval.equalsIgnoreCase("-d") || possibleRemoval.equalsIgnoreCase("-s") || possibleRemoval.equalsIgnoreCase("-u")
				|| possibleRemoval.equalsIgnoreCase("-a") || possibleRemoval.equalsIgnoreCase("-r") || possibleRemoval.equalsIgnoreCase("-n"))
		{
			return target.substring(0, target.length() - 3);
		}
		return target;
	}

	public static String getTheType(String target)
	{
		if(target.length() < 3)   //Adding anything behind, like " -t" requires at least 3 characters. So if there's less than 3,
		{                         //There is definitely no extra commands at all
			return null;
		}
		String possibleRemoval = target.substring(target.length() - 2);
		if(possibleRemoval.equalsIgnoreCase("-d") || possibleRemoval.equalsIgnoreCase("-s") || possibleRemoval.equalsIgnoreCase("-u")
				|| possibleRemoval.equalsIgnoreCase("-a") || possibleRemoval.equalsIgnoreCase("-r") || possibleRemoval.equalsIgnoreCase("-n"))
		{
			return target.substring(target.length() - 2);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void editQuality(ArrayList<PossibleQuality> arrayList, int location, String command, boolean toDebug)
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		boolean rerun;
		PossibleQuality possibleQuality = arrayList.get(location);
		ArrayList<Integer> qualities;

		do
		{
			if(command.equals(""))
			{
				editQualityMenu(possibleQuality);
				userInput = scanner.nextLine().toUpperCase();
				rerun = true;
			}
			else
			{
				userInput = command.toUpperCase();
				rerun = false;
			}
			switch(userInput)
			{
				case "SORT":
					Collections.sort(possibleQuality.getTheQuality());
					System.out.println(possibleQuality.getName() + " is successfully sorted.");
					PoeHelper.edited = true;
					break;
				case "DELETE":
					System.out.println("Are you sure you want to delete " + possibleQuality.getName() + "?");
					System.out.print("It contains: " + possibleQuality.getTheQuality() + ". ");
					if(Jeremy.getBoolean())
					{
						System.out.print(possibleQuality.toString());
						arrayList.remove(location);
						System.out.println(" deleted.");
						PoeHelper.edited = true;
					}
					else
					{
						System.out.println(possibleQuality.getName() + " was not deleted.");
					}
					rerun = false;
					break;
				case "USE":
					System.out.println("This is the current qualities of " + possibleQuality.getName() + ": " + possibleQuality.getTheQuality());
					ArrayList<Integer> result = PoeQuality.calculateQuality( (ArrayList<Integer>) possibleQuality.getTheQuality().clone(), toDebug);
					if(result.size() == 0)
					{
						System.out.println("There was no way to add them all up to 40!");
					}
					else
					{
						System.out.println("You will need the following qualities: " + result);
						System.out.print("Would you like to remove these qualities from the original list? ");
						if(Jeremy.getBoolean())
						{
							removeCertainQualities(possibleQuality.getTheQuality(), result);
							PoeHelper.edited = true;
							System.out.println("Qualities were removed.");
						}
						else
						{
							System.out.println("No qualities were removed.");
						}
					}
					rerun = false;
					break;
				case "REMOVE":
					System.out.print("These are the qualities: ");
					qualities = possibleQuality.getTheQuality();
					System.out.println(qualities);
					qualities = new ArrayList<Integer>();
					int toRemove;
					do
					{
						System.out.print("Please enter in the number of the quality you would like to remove: ");
						userInput = scanner.nextLine();
						if(userInput.equalsIgnoreCase("x"))
						{
							System.out.println("No numbers were removed.");
							return;
						}
						else
						{
							toRemove = validateString(userInput);
							if(toRemove >= 0 && toRemove <= 20)
							{
								if(Jeremy.searchInArrayList(possibleQuality.getTheQuality(), toRemove) != -1)
								{
									qualities.add(toRemove);
								}
								else
								{
									System.out.println("The quality you asked to be removed was not found!");
								}
							}
							else
							{
								removeCertainQualities(possibleQuality.getTheQuality(), qualities);
								System.out.println("The qualities are all successfully removed.");
								PoeHelper.edited = true;
							}
						}
					} while(toRemove >= 0 && toRemove <= 20);
					break;
				case "ADD":
					qualities = possibleQuality.getTheQuality();
					System.out.println("This quality list currently has " + qualities.size() + " number of qualities inside it.");
					for(int i = qualities.size() + 1;; i++)
					{
						System.out.print("Please enter in quality number " + i + ", or an invalid value to exit: ");
						do
						{
							userInput = scanner.nextLine();
						} while (userInput.equals(""));
						if(userInput.equalsIgnoreCase("X"))
						{
							System.out.println("The new qualities are all added.");
							return;
						}
						else
						{
							int aQuality = validateString(userInput);
							if(aQuality >= 0 && aQuality <= 20)
							{
								qualities.add(aQuality);
								PoeHelper.edited = true;
							}
							else
							{
								System.out.println("The new qualities are all successfully added.");
								break;
							}
						}
					}
					break;
				case "NAME":
					System.out.println("This quality list is currently called " + possibleQuality.getName());
					System.out.print("Please enter in the new name: ");
					possibleQuality.setName(scanner.nextLine());
					System.out.println("The quality list is now called " + possibleQuality.getName());
					PoeHelper.edited = true;
					break;
				case "X":
					return;
				default:
					if(PoeHelper.admin && userInput.equalsIgnoreCase("clear"))
					{
						if(PoeHelper.edited)
						{
							System.out.print("Are you sure you would like to clear this quality list? ");
							if(Jeremy.getBoolean())
							{
								possibleQuality.getTheQuality().clear();
								System.out.println("List of qualities cleared! To preserve changes, saving would be required!");
								PoeHelper.edited = true;
							}
							else
							{
								System.out.println("List of qualities was not cleared.");
							}
						}
						else
						{
							possibleQuality.getTheQuality().clear();
							System.out.println("List of qualities cleared! To preserve changes, saving would be required!");
							PoeHelper.edited = true;
						}
						break;
					}
					System.out.println("Unrecognized input. Please try again.");
					rerun = true;
			}
		} while(rerun);
	}

	public static void editQualityMenu(PossibleQuality possibleQuality)
	{
		System.out.println(possibleQuality + " {" + possibleQuality.getTheQuality().size() + "}");
		Jeremy.menuStart(3);
		System.out.println("sort\t\t\t-> Sort this quality list");
		System.out.println("delete\t\t\t-> Delete this quality list");
		System.out.println("use\t\t\t\t-> Use this quality list");
		System.out.println("remove\t\t\t-> Remove a number, or multiple number, in this quality list.");
		System.out.println("add\t\t\t\t-> Add a number, or multiple numbers, into this quality list.");
		System.out.println("name\t\t\t-> Change the name of this quality list.");
		System.out.println("x\t\t\t\t-> Return to the previous menu.");
		if(PoeHelper.admin)
		{
			System.out.println("--------------ADMIN commands:----------------------");
			System.out.println("clear\t\t\t-> Clear this quality list");
		}
		Jeremy.menuEnd();
	}

	public static void removeCertainQualities(ArrayList<Integer> arrayList, ArrayList<Integer> toRemove)
	{
		boolean isRemoved;
		for(Integer aToRemove : toRemove)
		{
			isRemoved = false;
			for(int j = 0; j < arrayList.size(); j++)
			{
				if(aToRemove.equals(arrayList.get(j)))
				{
					arrayList.remove(j);
					isRemoved = true;
					break;
				}
			}
			if(!isRemoved)
			{
				System.out.println("Oops, there was an error! " + aToRemove + " was not removed from the arraylist " + arrayList + "!");
			}
		}
	}

	public static void league(ArrayList<String> leagues)
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		String possibleLeague;
		int number;

		do
		{
			leagueMenu();
			userInput = input.nextLine();
			switch(userInput)
			{
				case "1":
					printLeagues(leagues);
					System.out.print("Enter the number of tha associated league you wish to switch to: ");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("X"))
					{
						return;
					}
					else if(!userInput.equals(""))
					{
						number = validateString(userInput);
						number = number - 1;
						if(number >= 0 && number < leagues.size())
						{
							PoeHelper.currentLeague = leagues.get(number);
							System.out.println("The current league is now " + PoeHelper.currentLeague);
							PoeHelper.edited = true;
						}
					}
					break;
				case "2":
					printLeagues(leagues);
					System.out.print("Enter the number of the league you wish to remove: ");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("X"))
					{
						return;
					}
					else if(!userInput.equals(""))
					{
						number = validateString(userInput);
						number = number - 1;
						if(number >= 0 && number < leagues.size())
						{
							if(PoeHelper.currentLeague.equals(leagues.get(number)))
							{
								System.out.println("The league you are removing is the current active league.");
								System.out.print("The new league will be set to ");
								if(number != 0)
								{
									System.out.println(leagues.get(0));
									PoeHelper.currentLeague = leagues.get(0);
								}
								else
								{
									System.out.println(leagues.get(1));
									PoeHelper.currentLeague = leagues.get(1);
								}
							}
							leagues.remove(number);
							System.out.println("League removed.");
							PoeHelper.edited = true;
						}
						else
						{
							System.out.println("Input entered does not match an associated league.");
						}
					}
					break;
				case "x":
					return;
				default:
					possibleLeague = userInput;
					System.out.println("Unrecognized command. Add as new league? ]Y/N]");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("Y"))
					{
						leagues.add(possibleLeague);
						System.out.println(possibleLeague + " added as a possible League.");
						PoeHelper.edited = true;
					}
					break;
			}
		} while(true);
	}

	public static void leagueMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("1\t\t\t\t-> Change current league");
		System.out.println("2\t\t\t\t-> Remove a league");
		System.out.println("<anything>\t\t-> add as new league");
		Jeremy.menuEnd();
	}

	public static void printLeagues(ArrayList<String> leagues)
	{
		System.out.println("Here are the list of leagues possible:");
		for(int i = 0; i < leagues.size(); i++)
		{
			System.out.println((i+1) + ": " + leagues.get(i));
		}
	}

	public static void accounts(ArrayList<String> accounts)
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		String possibleAccount;
		int number;

		do
		{
			accountsMenu();
			userInput = input.nextLine();
			switch(userInput)
			{
				case "1":
					printAccounts(accounts);
					System.out.print("Enter the number of tha associated account you wish to switch to: ");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("X"))
					{
						return;
					}
					else if(!userInput.equals(""))
					{
						number = validateString(userInput);
						number = number - 1;
						if(number >= 0 && number < accounts.size())
						{
							PoeHelper.currentAccount = accounts.get(number);
							System.out.println("The current account is now " + PoeHelper.currentAccount);
							PoeHelper.edited = true;
						}
					}
					break;
				case "2":
					printAccounts(accounts);
					System.out.print("Enter the number of the account you wish to remove: ");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("X"))
					{
						return;
					}
					else if(!userInput.equals(""))
					{
						number = validateString(userInput);
						number = number - 1;
						if(number >= 0 && number < accounts.size())
						{
							if(PoeHelper.currentAccount.equals(accounts.get(number)))
							{
								System.out.println("The account you are removing is the current active account.");
								System.out.print("The new account will be set to ");
								if(number != 0)
								{
									System.out.println(accounts.get(0));
									PoeHelper.currentAccount = accounts.get(0);
								}
								else
								{
									System.out.println(accounts.get(1));
									PoeHelper.currentAccount = accounts.get(1);
								}
							}
							accounts.remove(number);
							System.out.println("Account removed.");
							PoeHelper.edited = true;
						}
						else
						{
							System.out.println("Input entered does not match an associated account.");
						}
					}
					break;
				case "x":
					return;
				default:
					possibleAccount = userInput;
					System.out.println("Unrecognized command. Add as new Account? ]Y/N]");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("Y"))
					{
						accounts.add(possibleAccount);
						System.out.println(possibleAccount + " added as a possible Account.");
						PoeHelper.edited = true;
					}
					break;
			}
		} while(true);
	}

	public static void accountsMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("1\t\t\t\t-> Change current account");
		System.out.println("2\t\t\t\t-> Remove a account");
		System.out.println("<anything>\t\t-> add as new account");
		Jeremy.menuEnd();
	}

	public static void printAccounts(ArrayList<String> accounts)
	{
		System.out.println("Here are the list of accounts possible:");
		for(int i = 0; i < accounts.size(); i++)
		{
			System.out.println((i+1) + ": " + accounts.get(i));
		}
	}

	//This method is used to validate a String
	//It ensure that the string only contains integers, to parse to an integer data type
	//This is to ensure the program doesn't crash
	//returns the integer if correct, -1 if wrong.
	public static int validateString(String target)
	{
		//If the string contains nothing, then...
		if(target.length() == 0)
		{
			System.out.println("A string passed into validateString contains nothing! This should have be configured and prevented beforehand, but wasn't!");
			return -1;
		}
		char[] targetArr = target.toCharArray();
		int result;
		//check for negatives
		if(targetArr[0] == '-')
		{
			//set it to a value so that it doesn't crash with the system below
			targetArr[0] = 5;
		}
		for(int i = 0; i < targetArr.length; i++)
		{
			if(targetArr[i] < '0' || targetArr[i] > '9')
			{
				return -1;
			}
		}
		result = Integer.parseInt(target);
		return result;
	}
}