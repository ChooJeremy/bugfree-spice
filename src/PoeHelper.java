import java.io.*;
import java.util.*;

public class PoeHelper
{
	protected static ArrayList<Item> database;
	protected static ArrayList<String> leagues;
	protected static ArrayList<String> accounts;
	protected static ArrayList<PossibleQuality> qualities;
	protected static ArrayList<Gem> gems;
	//states if the database has been edited since the last save or not
	public static Boolean edited;
	public static Boolean admin;
	public static String currentLeague;
	public static String currentAccount;
	public static String furtherIORequirements;

    public static void main(String[] args)
    {
	    Scanner input = new Scanner(System.in);	//Allows usage of Scanner
	    gems = new ArrayList<Gem>();

	    String userInput;
	    String itemType;
	    int[] baseTypesMatches;
	    int intLocation;
	    int count;
	    String[] magicalItemsArr;

	    System.out.println("Please enter where you are running from: cmd or Intellij");
	    userInput = input.nextLine().toLowerCase();
	    if(userInput.equals("cmd"))
	    {
		    furtherIORequirements = "../../../";
	    }
	    else if(userInput.equals("intellij"))
	    {
		    furtherIORequirements = "";
	    }
	    else
	    {
		    System.out.println("Intellij assumed.");
		    furtherIORequirements = "";
	    }

	    //initialize values
	    load();
	    edited = false;
	    admin = false;
	    intLocation = 0;

        do
        {
            showMenu();
	        do
	        {
                userInput = input.nextLine().toUpperCase();
		        if(userInput.equals("EDITED?"))
		        {
			        System.out.println(edited);
		        }
		        else if(userInput.equals("DATABASE?"))
		        {
			        if(admin)
			        {
			            System.out.println(database);
			        }
			        else
			        {
				        System.out.println("Admin access required to execute this command. To enter admin mode, type \"admin\".");
			        }
		        }
	        } while (userInput.equals("EDITED?") || userInput.equals("DATABASE?"));
            switch(userInput)
            {
                //Show everything in database
                case "1":
	                if(database.size() == 0)
	                {
		                System.out.println("No items found in database!");
	                }
	                else
	                {
		                System.out.println("Item\tType");
		                for(int i = 0; i < database.size(); i++)
		                {
			                System.out.print((i+1) + "\t-> " + database.get(i).getType());
			                if(database.get(i).getQuality() > 0)
			                {
				                System.out.print(" <superior>");
			                }
			                System.out.println();
		                }
	                }
	                break;
                //Show everything in database with detailed information
	            case "1 -ALL":
		            if(database.size() == 0)
		            {
			            System.out.println("No items found in database!");
		            }
		            else
		            {
			            System.out.println("Item");
			            for(int i = 0; i < database.size(); i++)
			            {
				            System.out.print((i+1) + "\t-> ");
				            database.get(i).trace();
			            }
		            }
		            break;
	            //Show all whites to find for
                case "2":
	                magicalItemsArr = new String[database.size()];
	                count = 0;
	                //get all magical items
	                for(int i = 0; i < magicalItemsArr.length; i++)
	                {
		                if(database.get(i).getRarity().equalsIgnoreCase("magical"))
		                {
			                magicalItemsArr[count] = database.get(i).getType();
			                count++;
		                }
	                }
	                //If you already have a common of that kind in the database, forget it.
	                for(Item anItem : database)
	                {
		                if(anItem.getRarity().equalsIgnoreCase("common"))
		                {
			                for(int j = 0; j < magicalItemsArr.length; j++)
			                {
				                if(anItem.getType().equals(magicalItemsArr[j]))
				                {
					                magicalItemsArr[j] = "";
					                break;
				                }
			                }
		                }
	                }
	                //next print out if there is a rare item for the corresponding magical item
	                if(count > 0)
	                {
		                //using intLocation temporarily
		                intLocation = 0;
		                for(int i = 0; i < count; i++)
		                {
			                for(Item anItem : database)
			                {
				                if(magicalItemsArr[i].equals(anItem.getType()))
				                {
					                if(anItem.getRarity().equalsIgnoreCase("rare"))
					                {
						                System.out.println((i + 1) + ": Watch out for: " + magicalItemsArr[i]);
						                intLocation = 1;
						                break;
					                }
				                }
			                }
		                }
		                if(intLocation == 0)
		                {
		                    System.out.println("No items needs to be looked out for, as no magical items match rare items!");
		                }
		                else
		                {
			                System.out.println("Enter a number to remove the corresponding rare and magical items from the database, or enter anything else to exit: ");
			                System.out.println("Alternatively, enter a base class to do stuff with that base class.");
			                userInput = input.nextLine();
			                if(userInput.equals(""))
			                {
				                break;
			                }
			                intLocation = Item.verifyType(userInput);
			                if(intLocation < 0)
			                {
				                intLocation = PoeHelperInterface.validateString(userInput);
				                intLocation = intLocation - 1;
				                if(intLocation >= 0 && intLocation < database.size())
				                {
					                //remove the rare
					                for(int i = 0; i < database.size(); i++)
					                {
						                if(database.get(i).getType().equals(magicalItemsArr[intLocation]))
						                {
							                if(database.get(i).getRarity().equalsIgnoreCase("rare"))
							                {
							                    database.remove(i);
								                System.out.println("The rare item was successfully removed.");
								                edited = true;
							                    break;
							                }
						                }
					                }
					                //magical
					                for(int i = 0; i < database.size(); i++)
					                {
						                if(database.get(i).getType().equals(magicalItemsArr[intLocation]))
						                {
							                if(database.get(i).getRarity().equals("magical"))
							                {
								                database.remove(i);
								                System.out.println("The magical item was successfully removed.");
								                edited = true;
								                break;
							                }
						                }
					                }
				                }
			                }
			                else
			                {
				                itemType = Item.baseTypes[intLocation];
				                item(itemType);
			                }
		                }
	                }
	                else
	                {
		                System.out.println("No items needs to be looked out for as there are no magical items!");
	                }
                    break;
	            case "3":
	            case "SAVE":
		            save();
		            break;
	            case "X":
		            break;
                case "EXIT":
	                if(edited)
	                {
	                    System.out.println("Are you sure? Your data have not been saved!");
		                Jeremy.menuStart(3);
		                System.out.println("Exit\t\t\t-> Exit without saving");
		                System.out.println("Save\t\t\t-> Save and exit");
		                System.out.println("<anything else>\t-> Return to menu");
		                Jeremy.menuEnd();
	                    userInput = input.nextLine().toUpperCase();
		                if(userInput.equals("SAVE"))
		                {
			                save();
		                }
		                else if(!userInput.equals("EXIT"))
		                {
			                break;
		                }
                    }
	                return;
	            //edit the database
	            case "4":
	            case "DATABASE":
		            if(database.size() > 0)
		            {
		                database();
		            }
		            else
		            {
			            System.out.println("There's no items in the database - so, there's no point entering database editing mode!");
		            }
		            break;
	            case "5":
	            case "QUALITY":
		            PoeHelperInterface.quality(qualities);
		            break;
	            case "6":
		            gem();
		            break;
	            case "7":
		            if(database.size() == 0)
		            {
			            System.out.println("There are no items in the database!");
		            }
		            else
		            {
			            databaseItem(database.size()-1);
		            }
		            break;
	            case "8":
		            magicalItemsArr = new String[database.size()];
		            count = 0;
		            //get all magical items
		            for(int i = 0; i < magicalItemsArr.length; i++)
		            {
			            if(database.get(i).getRarity().equalsIgnoreCase("magical"))
			            {
				            magicalItemsArr[count] = database.get(i).getType();
				            count++;
			            }
		            }
		            if(count > 0)
		            {
			            //common
			            for(int i = 0; i < magicalItemsArr.length; i++)
			            {
				            intLocation = 0;
				            for(Item anItem : database)
				            {
					            if(anItem.getType().equals(magicalItemsArr[i]))
					            {
						            if(anItem.getRarity().equalsIgnoreCase("common"))
						            {
							            intLocation = 1;
							            break;
						            }
					            }
				            }
				            if(intLocation == 0)
				            {
					            magicalItemsArr[i] = "";
				            }
			            }
		            }
		            else
		            {
			            System.out.println("There are no items to show as there are no magical items!");
			            break;
		            }
		            //if rares match, print.
		            if(count > 0)
		            {
			            //using intLocation temporarily
			            intLocation = 0;
			            for(int i = 0; i < count; i++)
			            {
				            for(Item anItem : database)
				            {
					            if(magicalItemsArr[i].equals(anItem.getType()))
					            {
						            if(anItem.getRarity().equalsIgnoreCase("rare"))
						            {
							            System.out.println((i + 1) + ": Item type: " + magicalItemsArr[i]);
							            intLocation = 1;
							            break;
						            }
					            }
				            }
			            }
			            if(intLocation == 0)
			            {
				            System.out.println("No items needs to be looked out for, as no rare items match the magical and common base types!");
			            }
			            else
			            {
				            System.out.println("Enter a number to remove the corresponding items from the database, or enter anything else to exit: ");
				            System.out.println("Alternatively, enter a base class to do stuff with that base class.");
				            userInput = input.nextLine();
				            if(userInput.equals(""))
				            {
					            break;
				            }
				            intLocation = Item.verifyType(userInput);
				            if(intLocation < 0)
				            {
					            intLocation = PoeHelperInterface.validateString(userInput);
					            intLocation = intLocation - 1;
					            if(intLocation >= 0 && intLocation < magicalItemsArr.length)
					            {
						            //remove the rare
						            for(int i = 0; i < database.size(); i++)
						            {
							            if(database.get(i).getType().equals(magicalItemsArr[intLocation]))
							            {
								            if(database.get(i).getRarity().equalsIgnoreCase("rare"))
								            {
									            database.remove(i);
									            System.out.println("The rare item was successfully removed.");
									            edited = true;
									            break;
								            }
							            }
						            }
						            //magical
						            for(int i = 0; i < database.size(); i++)
						            {
							            if(database.get(i).getType().equals(magicalItemsArr[intLocation]))
							            {
								            if(database.get(i).getRarity().equalsIgnoreCase("magical"))
								            {
									            database.remove(i);
									            System.out.println("The magical item was successfully removed.");
									            edited = true;
									            break;
								            }
							            }
						            }
						            //common
						            for(int i = 0; i < database.size(); i++)
						            {
							            if(database.get(i).getType().equals(magicalItemsArr[intLocation]))
							            {
								            if(database.get(i).getRarity().equalsIgnoreCase("common"))
								            {
									            database.remove(i);
									            System.out.println("The common item was successfully removed.");
									            edited = true;
									            break;
								            }
							            }
						            }
					            }
				            }
				            else
				            {
					            itemType = Item.baseTypes[intLocation];
					            item(itemType);
				            }
			            }
		            }
		            else
		            {
			            System.out.println("There are no items to show as there are no common and magical items that match!");
		            }
		            break;
	            case "9":
		            PoeHelperInterface.league(leagues);
		            break;
	            case "0":
		            PoeHelperInterface.accounts(accounts);
		            break;
	            case "ADMIN":
		            if(!admin)
		            {
			            System.out.print("Password: ");
			            userInput = input.nextLine();
			            if(userInput.equals("Path of Exile@GGG"))
			            {
				            System.out.println("Password accepted.");
				            System.out.println("Full database control granted.");
				            admin = true;
				            break;
			            }
			            else
			            {
				            System.out.println("Password declined.");
				            System.out.println("Password Hint: <game>@<company>");
			            }
		            }
		            else
		            {
			            System.out.println("You are an admin. Log off? [Y/N]");
			            userInput = input.nextLine().toUpperCase();
			            if(userInput.equals("Y"))
			            {
				            admin = false;
				            System.out.println("You have logged off.");
			            }
		            }
		            break;
                //Search for a class type or a base type
	            default:
	                itemType = userInput;
	                //Check if there are no problems
	                intLocation = Item.verifyType(itemType);
	                if(intLocation >= 0)
	                {
		                itemType = Item.baseTypes[intLocation];
		                item(itemType);
	                }
	                else
	                {
		                System.out.println("The string you input did not match any item type.");
		                System.out.println("Run checker? [Y/N]");
		                userInput = input.nextLine().toUpperCase();
		                if(userInput.equals("Y"))
		                {
			                baseTypesMatches = Jeremy.findSimilarString(Item.baseTypes, itemType);
			                if(baseTypesMatches.length == 0)
			                {
				                System.out.println("No similar item base type was found!");
			                }
			                else
			                {
				                System.out.println("The following matches were found:");
				                for(int i = 0; i < baseTypesMatches.length; i++)
				                {
					                System.out.println((i+1) + ": " + Item.baseTypes[baseTypesMatches[i]]);
				                }
				                System.out.print("Enter the number of the item you were expecting, or enter a value out of range to exit: ");
				                do
				                {
				                    userInput = input.nextLine();
				                }
				                while(userInput.length() == 0);
				                intLocation = PoeHelperInterface.validateString(userInput);
				                if(intLocation > 0 && intLocation <= baseTypesMatches.length)
				                {
					                item(Item.baseTypes[baseTypesMatches[intLocation - 1]]);
				                }
			                }
		                }
	                }
                    break;
            }
	        System.out.println("Returning to menu...");
	        Jeremy.pause(100);
        } while(true);
    }

    public static void showMenu()
    {
	    Jeremy.menuStart(3);
        System.out.println("1\t\t\t\t-> Show all items currently in the database (" + database.size() + ")");
        System.out.println("1 -all\t\t\t-> Show all items currently in the database and details about each item");
        System.out.println("2\t\t\t\t-> Show all white items that can be used to get an orb.");
	    System.out.println("3\t\t\t\t-> Save database to the computer");
	    System.out.println("4\t\t\t\t-> Edit your current database");
	    System.out.println("5\t\t\t\t-> Quality calculator");
	    System.out.println("6\t\t\t\t-> Gem Checker");
	    System.out.println("7\t\t\t\t-> Edit last item in the database.");
	    System.out.println("8\t\t\t\t-> Show all items that have a common, magical and rare type in the database.");
	    System.out.println("9\t\t\t\t-> League edit mode.");
	    System.out.println("0\t\t\t\t-> Account edit mode.");
        System.out.println("<Item Type>\t\t-> Do stuff with that item");
	    System.out.println("x (anytime)\t\t-> Return to menu");
        System.out.println("Exit\t\t\t-> Exit the program");
	    System.out.println("Note: current league is: \"" + currentLeague + "\" and your current account is: \"" + currentAccount + "\"");
	    Jeremy.menuEnd();
    }

	//Handles the usage of items
	public static void item(String itemType)
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		Boolean repeat;
		ArrayList<Integer> similarItemLocation = new ArrayList<Integer>();
		int number;

		System.out.println(itemType + " item type verified.");
		for(int i = 0; i < database.size(); i++)
		{
			if(database.get(i).getType().equals(itemType))
			{
				if(similarItemLocation.size() == 0)
				{
					System.out.println("----------------------------------------------------------------------------------------------------");
				}
				System.out.println("You have an item of this type in the database, position " + (i+1) + ", with rarity: " + database.get(i).getRarity());
				similarItemLocation.add(i);
			}
		}
		if(similarItemLocation.size() != 0)
		{
			System.out.println("----------------------------------------------------------------------------------------------------");
		}
		do
		{
			repeat = false;
			itemMenu();
			userInput = input.nextLine().toUpperCase();
			switch(userInput)
			{
				case "X":
					break;
				case "1":
					if(similarItemLocation.size() == 0)
					{
						System.out.println("There are no items with the same base class in the database!");
					}
					else
					{
						System.out.println("Item");
						for(int aSimilarItem : similarItemLocation)
						{
							System.out.print((aSimilarItem+1) + "\t-> ");
							database.get(aSimilarItem).trace();
                		}
					}
					repeat = true;
					break;
				case "2":
					if(similarItemLocation.size() == 1)
					{
						databaseItem(similarItemLocation.get(0));
						return;
					}
					else if(similarItemLocation.size() == 0)
					{
						System.out.println("There are no such items in the database!");
						repeat = true;
					}
					else
					{
						System.out.println("There are multiple instances of this item in the database!");
						for(int i = 0; i < similarItemLocation.size(); i++)
						{
							System.out.print("Instance " + (i+1) + ": ");
							database.get(similarItemLocation.get(i)).trace();
						}
						System.out.println("Please enter the instance number you wish to edit, or anything else to cancel: ");
						do
						{
							userInput = input.nextLine().toUpperCase();
						} while(userInput.equals(""));
						if(userInput.equals("X"))
						{
							break;
						}
						else
						{
							number = PoeHelperInterface.validateString(userInput);
							number--;
							if(number >= 0 && number < similarItemLocation.size())
							{
								databaseItem(similarItemLocation.get(number));
							}
							else
							{
								System.out.println("Unrecognized instance.");
								repeat = true;
							}
						}
					}
					break;
				default:
					//if the user is talking about an item rarity
					if(Item.verifyRarity(userInput))
					{
						Item item = new Item(itemType, userInput);
						database.add(item);
						System.out.print("The following item was added to the database:\nItem ");
						item.trace();
						//an edit has been done
						edited = true;
					}
					else
					{
						if(admin)
						{
							if(userInput.equals("CYCLE"))
							{
								if(similarItemLocation.size() == 0)
								{
									System.out.println("There are no instances of this item in the database!");
								}
								else
								{
									for(int i = similarItemLocation.size() - 1; i >= 0; i--)
									{
										databaseItem(similarItemLocation.get(i));
									}
									return;
								}
								break;
							}
						}
						System.out.println("Unrecognized command.");
						repeat = true;
					}
					break;
			}
		} while(repeat);
	}

	public static void itemMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("1\t\t\t\t-> Search the database for these items, giving all information about the found items");
		System.out.println("2\t\t\t\t-> Edit this item in the database.");
		System.out.println("<rarity>\t\t-> Add this item to the database");
		System.out.println("x\t\t\t\t-> Return to menu");
		Jeremy.menuEnd();
	}

	public static void database()
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		Boolean repeat;
		int number;
		String leagueFirst;

		do
		{
			repeat = false;
			System.out.println("-------------------------------------------------------------------------------------------------------");
			databaseMenu();
			do
			{
				userInput = input.nextLine().toUpperCase();
			} while(userInput.equals(""));
			switch(userInput)
			{
				case "ALL":
					number = 0;
					//calculate number of spaces that the first integer should have
					for(double i = database.size(); i >= 1;)
					{
						i = i / 10;
						number++;
					}
					for(int i = 0; i < database.size(); i++)
					{
						System.out.printf("%0" + number + "d: %-24s Rarity: %-7s", i+1, database.get(i).getType(), database.get(i).getRarity());
						if(database.get(i).getQuality() > 0)
						{
							System.out.print(" <Superior>");
						}
						System.out.println();
						repeat = true;
					}
					break;
				case "LAST":
					databaseItem(database.size()-1);
					break;
				case "BASECLASS":
					ArrayList<String> itemType = new ArrayList<String>();
					for(Item anItem : database)
					{
						if(anItem.getRarity().equalsIgnoreCase("rare"))
						{
							itemType.add(anItem.getType());
						}
					}
					//sort the array
					Collections.sort(itemType);
					System.out.println("------------------------------");
					for(String aRareItemType : itemType)
					{
						System.out.println(aRareItemType);
					}
					System.out.println("------------------------------");
					break;
				case "LEAGUE":
					PoeHelperInterface.printLeagues(leagues);
					System.out.println("Enter the number of the league in which the items to be moved are contained in:");
					do
					{
						userInput = input.nextLine().toUpperCase();
					} while(userInput.equals(""));
					if(userInput.equals("X"))
					{
						return;
					}
					number = PoeHelperInterface.validateString(userInput);
					number = number - 1;
					if(number >= 0 && number < leagues.size())
					{
						leagueFirst = leagues.get(number);
					}
					else
					{
						break;
					}
					System.out.println("Enter the league to transfer to:");
					do
					{
						userInput = input.nextLine().toUpperCase();
					} while(userInput.equals(""));
					if(userInput.equals("X"))
					{
						return;
					}
					number = PoeHelperInterface.validateString(userInput);
					number = number - 1;
					if(number >= 0 && number < leagues.size())
					{
						userInput = leagues.get(number);
					}
					else
					{
						break;
					}
					number = 0;
					for(Item anItem : database)
					{
						if(anItem.getLeague().equalsIgnoreCase(leagueFirst))
						{
							anItem.setLeague(userInput);
							number++;
							edited = true;
						}
					}
					System.out.println(number + " items were moved from " + leagueFirst + " to " + userInput);
					break;
				case "X":
					return;
				default:
					number = PoeHelperInterface.validateString(userInput);
					number = number - 1;
					if(number >= 0 && number < database.size())
					{
						databaseItem(number);
						break;
					}
					if(admin)
					{
						switch(userInput)     //NOTE: remember to return after every case!
						{
							case "CLEAR":
							{
								if(edited)
								{
									System.out.println("Are you sure? This CANNOT be undone! [Y/N]");
									userInput = input.nextLine().toUpperCase();
									if(!userInput.equals("Y"))
									{
										return;
									}
								}
								database.clear();
								System.out.println("Database CLEARED! To preserve changes over applications, saving would be required.");
								System.out.println("Once saved, information is permanently gone!");
								edited = true;
								return;
							}
							case "CYCLE":
							for(int i = database.size() - 1; i >= 0; i--)
							{
								databaseItem(i);
							}
							return;
						}
					}
					System.out.println("Unrecognized command.");
					repeat = true;
					break;
			} //end of switch statement
		} while(repeat);
	}

	public static void databaseMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("<Item No.>\t\t-> Edit the specified item in the database");
		System.out.println("All\t\t\t\t-> Show all items in the database with their number");
		System.out.println("Last\t\t\t-> Edit the last item in the database (the most recently added item)");
		System.out.println("BaseClass\t\t-> Print out all the rare items in the database, sorted by item type");
		System.out.println("League\t\t\t-> Move all items from a league to another league");
		if(admin)
		{
			System.out.println("--------------ADMIN commands:----------------------");
			System.out.println("Clear\t\t\t-> Clears the database of all items");
			System.out.println("Cycle\t\t\t-> Cycle through the database and edit each item");
		}
		Jeremy.menuEnd();
	}

	public static void databaseItem(int itemLocation)
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		int number;
		//Create a reference to that object for easy usage
		Item item;
		item = database.get(itemLocation);
		System.out.println("You are about to edit item number " + (itemLocation + 1));
		System.out.print("Which has: ");
		item.trace();
		do
		{
			databaseItemMenu();
			userInput = input.nextLine();
			switch(userInput)
			{
				case "1":
					item.trace();
					break;
				case "2":
					if(item.identify())
					{
						System.out.println("Item identified.");
						edited = true;
					}
					break;
				case "3":
					if(item.improve())
					{
						System.out.println("Item improved. New quality: " + item.getQuality());
						edited = true;
					}
					else
					{
						System.out.println("Item already has maximum quality and can longer be improved.");
					}
					break;
				case "4":
					System.out.println("Are you sure? [Y/N]");
					userInput = input.nextLine().toUpperCase();
					if(userInput.equals("Y"))
					{
						database.remove(itemLocation);
						edited = true;
						System.out.println("Item removed.");
						return;
					}
					break;
				case "5":
					PoeHelperInterface.printLeagues(leagues);
					System.out.println("Enter the number of the league you wish to set to: ");
					do
					{
						userInput = input.nextLine();
					} while(userInput.equals(""));
					if(userInput.equalsIgnoreCase("x"))
					{
						return;
					}
					number = PoeHelperInterface.validateString(userInput);
					number =number - 1;
					if(number >= 0 && number < leagues.size())
					{
						item.setLeague(leagues.get(number));
						System.out.println("The item has been set as league " + item.getLeague());
						edited = true;
					}
					break;
				case "6":
					PoeHelperInterface.printAccounts(accounts);
					System.out.println("Enter the number of the account you wish to est to: ");
					do
					{
						userInput = input.nextLine();
					} while(userInput.equals(""));
					if(userInput.equalsIgnoreCase("x"))
					{
						return;
					}
					number = PoeHelperInterface.validateString(userInput);
					number = number - 1;
					if(number >= 0 && number < accounts.size())
					{
						item.setAccount(accounts.get(number));
						System.out.println("The item has been set as account " + item.getAccount());
						edited = true;
					}
					break;
				case "7":
					if(item.getComment() != null)
					{
						System.out.println("This item currently has the comment: " + item.getComment());
					}
					else
					{
						System.out.println("This item currently has no comment!");
					}
					System.out.println("Enter your new comment below.");
					userInput = input.nextLine();
					if(userInput.equalsIgnoreCase("x"))
					{
						return;
					}
					else if(userInput.equals(""))
					{
						break;
					}
					item.comment(userInput);
					System.out.println("Comment set.");
					edited = true;
					break;
				case "x":
					return;
				case "admin":
					if(admin)
					{
						System.out.println("Entering program's internal levels...");
						item.admin(leagues, accounts);
						return;
					}
				default:
					if(Item.verifyRarity(userInput))
					{
						item.setRarity(userInput);
						System.out.println("Item set as rarity " + userInput);
					}
					else
					{
						System.out.println("Unrecognized command. Please try again.");
					}
			}
		} while(true);
	}

	public static void databaseItemMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("1\t\t\t\t-> Re-print information about that item");
		System.out.println("2\t\t\t\t-> Identify that item");
		System.out.println("3\t\t\t\t-> Use a quality-increasing orb on that item, increasing it's quality");
		System.out.println("4\t\t\t\t-> Remove this item from the database.");
		System.out.println("5\t\t\t\t-> Change this item's league");
		System.out.println("6\t\t\t\t-> Change this item's account");
		System.out.println("7\t\t\t\t-> Add/change the comment");
		System.out.println("<rarity>\t\t-> Change it's rarity");
		if(admin)
		{
			System.out.println("--------------ADMIN commands:----------------------");
			System.out.println("admin\t\t\t-> Enter admin editing mode.");
		}
		Jeremy.menuEnd();
	}

	public static void gem()
	{
		Scanner scanner = new Scanner(System.in);
		String option, userInput;
		int number;
		do
		{
			gemMenu();
			option = scanner.nextLine().toUpperCase();
			switch(option)
			{
				case "ALL":
					if(gems.size() == 0)
					{
						System.out.println("There are no gems in the database!");
					}
					else
					{
						number = 0;
						//calculate number of spaces that the first integer should have
						for(double i = gems.size(); i >= 1;)
						{
							i = i / 10;
							number++;
						}
						for(int i = 0; i < gems.size(); i++)
						{
							System.out.printf("%" + number + "d %s \n", (i + 1), gems.get(i).toString());
						}
					}
					break;
				case "SORT":
					Collections.sort(gems);
					edited = true;
					System.out.println("The list of gems is successfully sorted!");
					break;
				case "X":
					return;
				default:
					if(admin)
					{
						if(option.equals("CLEAR"))
						{
							if(edited)
							{
								System.out.println("Are you sure? This CANNOT be undone! [Y/N]");
								userInput = scanner.nextLine().toUpperCase();
								if(!userInput.equals("Y"))
								{
									break;
								}
							}
							gems.clear();
							edited = true;
							System.out.println("List of gems cleared. To preserve changes, saving would be required.");
							break;
						}
					}
					//Check if there are no problems
					int gemNumber = Gem.verifyType(option);
					if(gemNumber == -1)
					{
						System.out.println("The string you input did not match any gem type.");
						System.out.println("Run checker? [Y/N]");
						userInput = scanner.nextLine().toUpperCase();
						if(userInput.equals("Y"))
						{
							int[] gemTypeMatches = Jeremy.findSimilarString(Gem.gemTypes, option);
							if(gemTypeMatches.length == 0)
							{
								System.out.println("No similar gem type was found!");
							}
							else
							{
								System.out.println("The following matches were found:");
								for(int i = 0; i < gemTypeMatches.length; i++)
								{
									System.out.println((i+1) + ": " + Gem.gemTypes[gemTypeMatches[i]]);
								}
								System.out.print("Enter the number of the item you were expecting, or enter a value out of range to exit: ");
								do
								{
									userInput = scanner.nextLine();
								} while(userInput.length() == 0);
								if(userInput.equalsIgnoreCase("x"))
								{
									return;
								}
								int result = PoeHelperInterface.validateString(userInput) - 1;
								if(result >= 0 && result < gemTypeMatches.length)
								{
									gemNumber = gemTypeMatches[PoeHelperInterface.validateString(userInput)-1];
								}
							}
						}
					}
					//If a gem match was found (before or after)
					if(gemNumber >= 0)
					{
						boolean wasRemoved = false;
						//See if you have that gem type or not
						int gemLocation = Jeremy.searchInArrayList(gems, new Gem(Gem.gemTypes[gemNumber]));
						if(gemLocation == -1)
						{
							System.out.println("You do not have that " + Gem.getGemColour(Gem.gemTypes[gemNumber]) + " gem.");
						}
						else
						{
							System.out.println("You DO have that gem! It's: " + gems.get(gemLocation));
							System.out.print("Would you like to remove the gem? ");
							if(Jeremy.getBoolean())
							{
								gems.remove(gemLocation);
								wasRemoved = true;
								edited = true;
								System.out.println("The gem was removed.");
							}
						}
						if(!wasRemoved)
						{
							System.out.print("Would you like to add a gem of this type? ");
							if(Jeremy.getBoolean())
							{
								gems.add(new Gem(Gem.gemTypes[gemNumber]));
								edited = true;
								System.out.println("A new gem of this type is added");
							}
						}
					}
					break;
			}
		} while (true);
	}

	public static void gemMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("All\t\t\t\t-> Print all gems in the database");
		System.out.println("Sort\t\t\t-> Sort the list of gems in the database");
		System.out.println("<gem type>\t\t-> Search for this gem in the database. You can also add or remove it.");
		if(admin)
		{
			System.out.println("--------------ADMIN commands:----------------------");
			System.out.println("Clear\t\t\t-> Clear the list of gems.");
		}
		Jeremy.menuEnd();
	}

	public static void saveObject(String name, Object o) throws Exception
	{
		FileOutputStream fileOut = new FileOutputStream(furtherIORequirements + name);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(o);
		fileOut.close();
		out.close();
	}

	public static void save()
	{
		try
		{
			saveObject("PoeHelper.ser", database);

			saveObject("Quality.ser", qualities);

			saveObject("Gem.ser", gems);

			saveObject("Leagues.ser", leagues);

			saveObject("Leagues2.ser", currentLeague);

			saveObject("Accounts.ser", accounts);

			saveObject("Accounts2.ser", currentAccount);

			System.out.println("Save successful!");
			edited = false;
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

	public static Object loadObject(String fileName)
	{
		try
		{
			FileInputStream fileIn = new FileInputStream(furtherIORequirements + fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object o = in.readObject();
			in.close();
			fileIn.close();
			return o;
		}
		catch(FileNotFoundException f)
		{
			//do nothing, the full load method will handle it.
		}
		catch(Exception e)
		{
			Jeremy.pause(2);
			System.out.println("File failed to load!");
			System.out.println("Details:");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void load()
	{
		Scanner input = new Scanner(System.in);

		boolean fileNotFound = false;

		//Attempt to load data
		database = (ArrayList<Item>) loadObject("PoeHelper.ser");
		qualities = (ArrayList<PossibleQuality>) loadObject("Quality.ser");
       	gems = (ArrayList<Gem>)	loadObject("Gem.ser");
		leagues = (ArrayList<String>) loadObject("Leagues.ser");
		currentLeague = (String) loadObject("Leagues2.ser");
		accounts = (ArrayList<String>) loadObject("Accounts.ser");
		currentAccount = (String) loadObject("Accounts2.ser");

		if(database == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The database was not found, and thus will begin empty...");
			database = new ArrayList<Item>();
		}
		if(qualities == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The saved qualities were not found, and thus will begin empty...");
			qualities = new ArrayList<PossibleQuality>();
		}
		else
		{
			if(fileNotFound)
			{
				System.out.println("The qualities were successfully loaded!");
			}
		}
		if(gems == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The saved gems were not found, and thus will begin empty...");
			gems = new ArrayList<Gem>();
		}
		else
		{
			if(fileNotFound)
			{
				System.out.println("The gems were successfully loaded!");
			}
		}
		if(leagues == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The stored leagues was not found, and thus you will need to initialize them!");
			System.out.print("Please enter a league that you will be using.\n" +
					"This league will be entered into the possible leagues and will be set as your current league: ");
			String league = input.nextLine();
			leagues = new ArrayList<String>();
			leagues.add(league);
			currentLeague = league;
		}
		else
		{
			if(fileNotFound)
			{
				System.out.println("The stored leagues was found and will be loaded!");
			}
		}
		while (currentLeague == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The current league was not found, and you will need to set it.");
			PoeHelperInterface.league(leagues);
		}
		if(accounts == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The stored accounts were not found, and thus you will need to initialize them!");
			System.out.print("Please enter an account that you will be using.\n" +
					"This account will be entered into the possible accounts and will be set as your current account: ");
			String account = input.nextLine();
			accounts = new ArrayList<String>();
			accounts.add(account);
			currentAccount = account;
		}
		else
		{
			if(fileNotFound)
			{
				System.out.println("The stored accounts were found and will be loaded!");
			}
		}
		while (currentAccount == null)
		{
			if(!fileNotFound)
			{
				System.out.println("Save files was not found! Beginning initialization...");
				fileNotFound = true;
			}
			System.out.println("The current account was not found, and you will need to set it.");
			PoeHelperInterface.accounts(accounts);
		}
		if(fileNotFound)
		{
			System.out.println("Initialization successful! Saving results...");
			save();
		}
		else
		{
			System.out.println("The saved files were successfully loaded!");
		}
	}
}