import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Item implements Serializable
{
    private String currentType;
	private Boolean identified;
	private String currentRarity;
	private String comment;
	private String currentLeague;
	private String currentAccount;
	private int quality;

    public static final String[] baseTypes =
	{ //This is the database. Sorted by type, then level.
		    //WEAPONS:
		    "Crude Bow", "Short Bow", "Long Bow", "Composite Bow", "Recurve Bow", "Bone Bow", "Royal Bow", "Death Bow", "Grove Bow", "Decurve Bow", "Compound Bow", "Sniper Bow", "Ivory Bow", "Highborn Bow", "Decimation Bow", "Thicket Bow", "Citadel Bow", "Ranger Bow", "Maraketh Bow", "Spine Bow", "Imperial Bow", "Harbinger Bow",
			"Nailed Fist", "Sharktooth Claw", "Awl", "Cat's Paw", "Blinder", "Timeworn Claw", "Sparkling Claw", "Fright Claw", "Thresher Claw", "Gouger", "Tiger's Paw", "Gut Ripper", "Prehistoric Claw", "Noble Claw", "Eagle Claw", "Great White Claw", "Throat Stabber", "Hellion's Paw", "Eye Gouger", "Vaal Claw", "Imperial Claw", "Terror Claw",
		    "Glass Shank", "Skinning Knife", "Carving Knife", "Stiletto", "Boot Knife", "Copper Kris", "Skean", "Imp Dagger", "Flaying Knife", "Butcher Knife", "Poignard", "Boot Blade", "Golden Kris", "Royal Skean", "Fiend Dagger", "Gutting Knife", "Slaughter Knife", "Ambusher", "Ezomyte Dagger", "Platinum Kris", "Imperial Skean", "Demon Dagger",
		    "Rusted Hatchet", "Jade Hatchet", "Boarding Axe", "Cleaver", "Broad Axe", "Arming Axe", "Decorative Axe", "Spectral Axe", "Jasper Axe", "Tomahawk", "Wrist Chopper", "War Axe", "Chest Splitter", "Ceremonial Axe", "Wraith Axe", "Karui Axe", "Siege Axe", "Reaver Axe", "Ezomyte Axe", "Vaal Hatchet", "Royal Axe", "Infernal Axe",
		    "Driftwood Club", "Tribal Club", "Spiked Club", "Stone Hammer", "War Hammer", "Bladed Mace", "Ceremonial Mace", "Dream Mace", "Petrified Club", "Barbed Club", "Rock Breaker", "Battle Hammer", "Flanged Mace", "Ornate Mace", "Phantom Mace", "Ancestral Club", "Tenderizer", "Gavel", "Legion Hammer", "Pernarch", "Auric Mace", "Nightmare Mace",
		    "Rusted Sword", "Copper Sword", "Sabre", "Broad Sword", "War Sword", "Ancient Sword", "Elegant Sword", "Dusk Blade", "Variscite Blade", "Cutlass", "Baselard", "Battle Sword", "Elder Sword", "Graceful Sword", "Twilight Blade", "Gemstone Sword", "Corsair Sword", "Gladius", "Legion Sword", "Vaal Blade", "Eternal Sword", "Midnight Blade",
		    "Driftwood Sceptre", "Darkwood Sceptre", "Bronze Sceptre", "Quartz Sceptre", "Iron Sceptre", "Ochre Sceptre", "Ritual Sceptre", "Shadow Sceptre", "Grinning Fetish", "Sekhem", "Crystal Sceptre", "Lead Sceptre", "Blood Sceptre", "Royal Sceptre", "Abyssal Sceptre", "Karui Sceptre", "Tyrant's Sekhem", "Opal Sceptre", "Platinum Sceptre", "Carnal Sceptre", "Vaal Sceptre", "Void Sceptre",
		    "Gnarled Branch", "Primitive Staff", "Long Staff", "Iron Staff", "Coiled Staff", "Royal Staff", "Vile Staff", "Woodful Staff", "Quarterstaff", "Military Staff", "Serpentine Staff", "Highborn Staff", "Foul Staff", "Primordial Staff", "Lathi", "Ezomyte Staff", "Maelström Staff", "Imperial Staff", "Judgement Staff",
		    "Rusted Spike", "Whalebone Rapier", "Battered Foil", "Basket Rapier", "Jagged Foil", "Antique Rapier", "Elegant Foil", "Thorn Rapier", "Wyrmbone Rapier", "Burnished Foil", "Estoc", "Serrated Foil", "Primeval Rapier", "Fancy Foil", "Apex Rapier", "Dragonbone Rapier", "Tempered Foil", "Pecoraro", "Spiraled Foil", "Vaal Rapier", "Jeweled Foil", "Harpy Rapier",
		    "Stone Axe", "Jade Chopper", "Woodsplitter", "Poleaxe", "Double Axe", "Gilded Axe", "Shadow Axe", "Jasper Chopper", "Timber Axe", "Headsman Axe", "Labrys", "Noble Axe", "Abyssal Axe", "Karui Chopper", "Sundering Axe", "Ezomyte Axe", "Vaal Axe", "Despot Axe", "Void Axe",
		    "Driftwood Maul", "Tribal Maul", "Mallet", "Sledgehammer", "Spiked Maul", "Brass Maul", "Fright Maul", "Totemic Maul", "Great Mallet", "Steelhead", "Spiny Maul", "Plated Maul", "Dread Maul", "Karui Maul", "Colossus Mallet", "Piledriver", "Meatgrinder", "Imperial Maul", "Terror Maul",
		    "Corroded Blade", "Longsword", "Bastard Sword", "Two-Handed Sword", "Etched Greatsword", "Ornate Sword", "Spectral Sword", "Butcher Sword", "Footman Sword", "Highland Blade", "Engraved Greatsword", "Tiger Sword", "Wraith Sword", "Headman's Sword", "Reaver Sword", "Ezomyte Blade", "Vaal Greatsword", "Lion Sword", "Infernal Sword",
		    "Driftwood Wand", "Goat's Horn", "Carved Wand", "Quartz Wand", "Spiraled Wand", "Sage Wand", "Faun's Horn", "Engraved Wand", "Crystal Wand", "Serpent Wand", "Omen Wand", "Demon's Horn", "Imbued Wand", "Opal Wand", "Tornado Wand", "Prophecy Wand",
		    //ARMOUR:
		    "Plate Vest", "Shabby Jerkin", "Simple Robe", "Chainmail Vest", "Scale Vest", "Padded Vest", "Chestplate", "Light Brigandine", "Chainmail Tunic", "Strapped Leather", "Oiled Vest", "Silken Vest", "Ringmail Coat", "Copper Plate", "Scale Doublet", "Buckskin Tunic", "Padded Jacket", "Scholar's Robe", "Infantry Brigandine", "War Plate", "Chainmail Doublet", "Oiled Coat", "Silken Garb", "Wild Leather", "Full Ringmail", "Full Scale Armor", "Mage's Vestment", "Full Leather", "Full Plate", "Scarlet Raiment", "Silk Robe", "Waxed Garb", "Sun Leather", "Soldier's Brigandine", "Arena Plate", "Full Chainmail", "Lordly Plate", "Bone Armor", "Holy Chainmail", "Thief's Garb", "Cabalist Regalia", "Field Lamellar", "Bronze Plate", "Eelskin Tunic", "Sage's Robe", "Wyrmscale Doublet", "Latticed Ringmail", "Quilted Jacket", "Silken Wrap", "Battle Plate", "Frontier Leather", "Hussar Brigandine", "Crusader Chainmail", "Sleek Coat", "Sun Plate", "Glorious Leather", "Conjurer's Vestment", "Full Wyrmscale", "Ornate Ringmail", "Crimson Raiment", "Colosseum Plate", "Spidersilk Robe", "Coronal Leather", "Commander's Brigandine", "Chain Hauberk", "Lacquered Garb", "Majestic Plate", "Cutthroat's Garb", "Destroyer Regalia", "Battle Lamellar", "Devout Chainmail", "Sharkskin Tunic", "Savant's Robe", "Golden Plate", "Crypt Armor", "Dragonscale Doublet", "Loricated Ringmail", "Necromancer Silks", "Sentinel Jacket", "Destiny Leather", "Crusader Plate", "Desert Brigandine", "Conquest Chainmail", "Astral Plate", "Exquisite Leather", "Varnished Coat", "Occultist's Vestment", "Full Dragonscale", "Elegant Ringmail", "Blood Raiment", "Zodiac Leather", "Widowsilk Robe", "Gladiator Plate", "General's Brigandine", "Saint's Hauberk", "Vaal Regalia", "Assassin's Garb", "Glorious Plate", "Sadist Garb", "Triumphant Lamellar", "Saintly Chainmail", "Carnal Armor",
		    "Iron Greaves", "Wool Shoes", "Rawhide Boots", "Chain Boots", "Wrapped Boots", "Leatherscale Boots", "Steel Greaves", "Velvet Slippers", "Goathide Boots", "Ringmail Boots", "Strapped Boots", "Ironscale Boots", "Silk Slippers", "Deerskin Boots", "Plated Greaves", "Clasped Boots", "Mesh Boots", "Bronzescale Boots", "Scholar Boots", "Reinforced Greaves", "Nubuck Boots", "Shackled Boots", "Steelscale Boots", "Riveted Boots", "Antique Greaves", "Satin Slippers", "Eelskin Boots", "Zealot Boots", "Trapper Boots", "Serpentscale Boots", "Samite Slippers", "Sharkskin Boots", "Ancient Greaves", "Ambush Boots", "Horseman's Boots", "Wyrmscale Boots", "Conjurer Boots", "Goliath Greaves", "Shagreen Boots", "Carnal Boots", "Desert Boots", "Hydrascale Boots", "Arcanist Slippers", "Vaal Greaves", "Stealth Boots", "Assassin's Boots", "Crusader Boots", "Dragonscale Boots", "Sorcerer Boots", "Titan Greaves", "Murder Boots", "Slink Boots",
			"Iron Gauntlets", "Wool Gloves", "Rawhide Gloves", "Fishscale Gauntlets", "Wrapped Mitts", "Chain Gloves", "Goathide Gloves", "Plated Gauntlets", "Velvet Gloves", "Ironscale Gauntlets", "Strapped Mitts", "Ringmail Gloves", "Deerskin Gloves", "Bronze Gauntlets", "Silk Gloves", "Bronzescale Gauntlets", "Clasped Mitts", "Mesh Gloves", "Nubuck Gloves", "Steel Gauntlets", "Trapper Mitts", "Steelscale Gauntlets", "Embroidered Gloves", "Riveted Gloves", "Eelskin Gloves", "Antique Gauntlets", "Satin Gloves", "Serpentscale Gauntlets", "Zealot Gloves", "Sharkskin Gloves", "Ambush Mitts", "Ancient Gauntlets", "Samite Gloves", "Wyrmscale Gauntlets", "Carnal Mitts", "Horseman's Gloves", "Goliath Gauntlets", "Shagreen Gloves", "Conjurer Gloves", "Cavalry Gloves", "Assassin's Mitts", "Hydrascale Gauntlets", "Arcanist Gloves", "Stealth Gloves", "Vaal Gauntlets", "Crusader Gloves", "Murder Mitts", "Dragonscale Gauntlets", "Titan Gauntlets", "Sorcerer Gloves", "Slink Gloves",
			"Rusted Casque", "Battered Cap", "Vine Circlet", "Rusted Helmet", "Tarnished Mask", "Corroded Chain Coif", "Footman's Casque", "Copper Circlet", "Leather Cap", "Wooden Mask", "Chainmail Coif", "Flanged Helmet", "Tribal Circlet", "Copper Mask", "Gladiator Casque", "Leather Hood", "Ringmail Coif", "Ribbed Helmet", "Executioner Casque", "Gemmed Circlet", "Ceremonial Mask", "Studded Hood", "Inlaid Chain Coif", "Gilded Helmet", "Gilded Circlet", "Bandit Mask", "Imperial Casque", "Coolus Helmet", "Woven Ring Coif", "Iron Mask", "Carved Circlet", "Reaver Casque", "Hunter Cap", "Finned Helmet", "Mesh Chain Coif", "Heathen Mask", "Forest Hood", "Mercenary Casque", "Encrusted Circlet", "Ornate Helmet", "Assassin Mask", "Zealot's Chain Coif", "Gleaming Circlet", "Vaal Casque", "Grove Hood", "Steel Mask", "Polished Helmet", "Commander's Ring Coif", "Petrified Circlet", "Huntress Cap", "Destroyer Casque", "Pagan Mask", "Bladed Helmet", "Crusader's Chain Coif", "Thicket Hood", "Annihilator Casque", "Royal Circlet", "Murder Mask", "Crested Helmet", "Imperial Chain Coif", "Ezomyte Circlet", "Armageddon Casque", "Jungle Hood",
			"Splintered Tower Shield", "Goathide Buckler", "Twig Spirit Shield", "Spiked Bundle", "Corroded Tower Shield", "Rotted Round Shield", "Plank Kite Shield", "Pine Buckler", "Yew Spirit Shield", "Rawhide Tower Shield", "Driftwood Spiked Shield", "Fir Round Shield", "Linden Kite Shield", "Bone Spirit Shield", "Painted Buckler", "Cedar Tower Shield", "Reinforced Kite Shield", "Studded Round Shield", "Alloyed Spike Shield", "Tarnished Spirit Shield", "Hammered Buckler", "Copper Tower Shield", "Scarlet Round Shield", "Layered Kite Shield", "Burnished Spike Shield", "Jingling Spirit Shield", "War Buckler", "Reinforced Tower Shield", "Splendid Round Shield", "Ornate Spiked Shield", "Brass Spirit Shield", "Ceremonial Kite Shield", "Gilded Buckler", "Painted Tower Shield", "Walnut Spirit Shield", "Oak Buckler", "Redwood Spiked Shield", "Maple Round Shield", "Buckskin Tower Shield", "Etched Shield", "Ivory Spirit Shield", "Enameled Buckler", "Mahogany Tower Shield", "Spiked Round Shield", "Compound Spiked Shield", "Ancient Spirit Shield", "Steel Kite Shield", "Corrugated Buckler", "Bronze Tower Shield", "Polished Spiked Shield", "Chiming Spirit Shield", "Crimson Round Shield", "Laminated Kite Shield", "Battle Buckler", "Girded Tower Shield", "Thorium Spirit Shield", "Golden Buckler", "Sovereign Spiked Shield", "Baroque Round Shield", "Angel Kite Shield", "Crested Tower Shield", "Lacewood Spirit Shield", "Ironwood Buckler", "Teak Round Shield", "Alder Spike Shield", "Shagreen Tower Shield", "Fossilized Spirit Shield", "Branded Kite Shield", "Lacquered Buckler", "Hunting Shield", "Ebony Tower Shield", "Champion Kite Shield", "Ezomyte Spiked Shield", "Spiny Round Shield", "Vaal Spirit Shield", "Vaal Buckler", "Ezomyte Tower Shield", "Harmonic Spirit Shield", "Mosaic Kite Shield", "Mirrored Spiked Shield", "Cardinal Round Shield", "Crusader Buckler", "Colossal Tower Shield", "Archon Kite Shield", "Titanium Spirit Shield", "Imperial Buckler", "Elegant Round Shield", "Supreme Spiked Shield", "Pinnacle Tower Shield",
			//OTHERS (Was not found in Item Data, instead found in the wiki.
			"Rugged Quiver", "Cured Quiver", "Light Quiver", "Heavy Quiver", "Conductive Quiver", //http://en.pathofexilewiki.com/wiki/Bows#Quivers
			"Chain Belt", "Rustic Sash", "Leather Belt", "Heavy Belt", "Studded Belt", "Cloth Belt", //http://en.pathofexilewiki.com/wiki/Belts
			"Coral Ring", "Paua Ring","Iron Ring", "Sapphire Ring", "Gold Ring", "Ruby Ring", "Topaz Ring", //http://en.pathofexilewiki.com/wiki/Ring
			"Paua Amulet", "Coral Amulet", "Jade Amulet", "Amber Amulet", "Lapis Amulet", "Gold Amulet", "Onyx Amulet" //http://en.pathofexilewiki.com/wiki/Amulet
	};

	public Item(String type, String rarity)
    {
	    setRarity(rarity);
        setType(type);
	    /*
	    if(currentRarity.equalsIgnoreCase("common"))
	    {
		    identified = true;
	    }
	    else
	    {
            identified = false;
	    }
	    is equivalent to the following:               */
	    identified = (currentRarity.equalsIgnoreCase("common"));
	    quality = 0;
	    currentLeague = PoeHelper.currentLeague;
	    currentAccount = PoeHelper.currentAccount;
    }

    private Boolean setType(String type)
    {
	    int result;
	    result = verifyType(type);
        if(result >= 0)
        {
            currentType = Item.baseTypes[result];
	        return true;
        }
        else
        {
            System.out.println("An error occurred! " + type + " was attempted to be set as it's base type, however there is no such base type found!");
	        return false;
        }
    }

	public String getType()
	{
		return currentType;
	}

	//verifies the base type, seeing if it can be used or not. If it can, returns the location of where the basetypes are in.
	//else, returns a negative number.
	public static int verifyType(String type)
	{
		for(int i = 0; i < baseTypes.length; i++)
		{
			if(baseTypes[i].equalsIgnoreCase(type))
			{
				return i;
			}
		}
		return -1;
	}

	public Boolean setRarity(String rarity)
	{
		if(verifyRarity(rarity))
		{
			currentRarity = rarity.toLowerCase();
			return true;
		}
		else
		{
			System.out.println("An error occurred! An item was attempted to set as rarity " + rarity);
			return false;
		}
	}

	public String getRarity()
	{
		return currentRarity;
	}

	public static Boolean verifyRarity(String rarity)
	{
		if(rarity.equalsIgnoreCase("common"))
		{
			return true;
		}
		else if(rarity.equalsIgnoreCase("rare"))
		{
			return true;
		}
		else if(rarity.equalsIgnoreCase("magical"))
		{
			return true;
		}
		else if(rarity.equalsIgnoreCase("unique"))
		{
			return true;
		}
		return false;
	}

	public Boolean improve()
	{
		if(quality == 20)
		{
			return false;
		}
		if(currentRarity.equalsIgnoreCase("common"))
		{
			quality = quality + 5;
		}
		else if(currentRarity.equalsIgnoreCase("magical"))
		{
			quality = quality + 2;
		}
		else if(currentRarity.equalsIgnoreCase("rare") || currentRarity.equalsIgnoreCase("unique"))
		{
			quality = quality + 1;
		}
		if(quality > 20)
		{
			quality = 20;
		}
		return true;
	}

	public int getQuality()
	{
		return quality;
	}

	public void setLeague(String league)
	{
		currentLeague = league;
	}

	public String getLeague()
	{
		return currentLeague;
	}

	public void setAccount(String account)
	{
		currentAccount = account;
	}

	public String getAccount()
	{
		return currentAccount;
	}

	public void comment(String playerComment)
	{
		comment = playerComment;
	}
	public String getComment()
	{
		return comment;
	}

	public void admin(ArrayList<String> leagues, ArrayList<String> accounts)
	{
		Scanner input = new Scanner(System.in);
		String userInput;
		int number;
		String userItemType;
		int[] matches;
		//used to check if the user request is successfully set or not. If not, then offer to set it manually, bypassing checks.
		Boolean set;

		if(PoeHelper.admin)
		{
			System.out.println("Admin mode successfully entered.");
			System.out.println("Warning: any changes done here could permanently destroy the database, rendering it unusable!");
			while(true)
			{
				set = false;
				System.out.print("Current status: ");
				trace();
				Item.adminMenu();
				userInput = input.nextLine().toUpperCase();
				switch(userInput)
				{
					//Set base type
					case "1":
						System.out.print("Enter the base type you wish to change it to: ");
						userItemType = input.nextLine();
						number = Item.verifyType(userItemType);
						if(number >= 0)
						{
							setType(userItemType);
							System.out.println("Base class " + Item.baseTypes[number] + " successfully set.");
							PoeHelper.edited = true;
							set = true;
						}
						else
						{
							System.out.println("No such base types found! Run checker? [Y/N]");
							userInput = input.nextLine().toUpperCase();
							if(userInput.equals("Y"))
							{
								matches = Jeremy.findSimilarString(Item.baseTypes, userItemType);
								if(matches.length > 0)
								{
									System.out.println("Matches found!");
									for(int i = 0; i < matches.length; i++)
									{
										System.out.println((i+1) + " : " + Item.baseTypes[matches[i]]);
									}
									System.out.print("Enter the number you meant, or enter a number out of range to exit: ");
									do
									{
										userInput = input.nextLine();
									} while(userInput.equals(""));
									number = PoeHelperInterface.validateString(userInput);
									number = number - 1;
									if(number >= 0 && number < matches.length)
									{
										setType(Item.baseTypes[matches[number]]);
										System.out.println("Base class " + Item.baseTypes[matches[number]] + " successfully set.");
										set = true;
										PoeHelper.edited = true;
									}
								}
								else
								{
									System.out.println("The checker did not find any similar item types.");
								}
							}
						}
						if(!set)
						{
							System.out.println("Item base class was not successfully set.");
							System.out.println("Would you like to ignore all checks, bypass all warnings and set it anyway? [Y/N]");
							userInput = input.nextLine().toUpperCase();
							if(userInput.equals("Y"))
							{
								currentType = userItemType;
								System.out.println("Item base class set.");
								PoeHelper.edited = true;
							}
						}
						break;
					//Set rarity
					case "2":
						System.out.println("Enter the new rarity: ");
						userInput = input.nextLine();
						if(verifyRarity(userInput))
						{
							setRarity(userInput);
							System.out.println("Rarity successfully set.");
							PoeHelper.edited = true;
						}
						else
						{
							userItemType = userInput;
							System.out.println("The rarity you entered is not a recognized possible rarity value.");
							System.out.println("Set anyway? [Y/N]");
							userInput = input.nextLine().toUpperCase();
							if(userInput.equals("Y"))
							{
								currentRarity = userItemType;
								System.out.println("Rarity set.");
								PoeHelper.edited = true;
							}
						}
						break;
					//Set quality
					case "3":
						System.out.println("Enter the new quality: ");
						do
						{
							userInput = input.nextLine();
						} while(userInput.equals(""));
						number = PoeHelperInterface.validateString(userInput);
						if(number >= 0 && number <= 20)
						{
							quality = number;
							System.out.println("Quality successfully set.");
							PoeHelper.edited = true;
						}
						else
						{
							System.out.println("The number you entered is not a viable quality.");
							System.out.println("Set anyway? [Y/N]");
							userInput = input.nextLine().toUpperCase();
							if(userInput.equals("Y"))
							{
								quality = number;
								System.out.println("Quality set.");
								PoeHelper.edited = true;
							}
						}
						break;
					//Identify or unidentify
					case "4":
						System.out.println("Current status: identified = " + identified);
						System.out.println("Set it to? [true/false]");
						userInput = input.nextLine().toUpperCase();
						switch (userInput)
						{
							case "TRUE":
								identified = true;
								System.out.println("Identified is now true.");
								PoeHelper.edited = true;
								break;
							case "FALSE":
								identified = false;
								System.out.println("Identified is now false.");
								PoeHelper.edited = true;
								break;
							default:
								System.out.println("Unrecognized command. Returning to menu...");
								break;
						}
						break;
					//set league
					case "5":
						System.out.println("Current league is " + currentLeague);
						PoeHelperInterface.printLeagues(leagues);
						System.out.print("Enter new league name: ");
						userInput = input.nextLine();
						for(String aLeague : leagues)
						{
							if(aLeague.equalsIgnoreCase(userInput))
							{
								currentLeague = aLeague;
								System.out.println("League was successfully set as " + currentLeague);
								PoeHelper.edited = true;
								set = true;
								break;
							}
						}
						if(!set)
						{
							System.out.println("League found was not an accepted league.");
							System.out.println("Set anyway? [Y/N]");
							userItemType = userInput;
							userInput = input.nextLine().toUpperCase();
							if(userInput.equals("Y"))
							{
								currentLeague = userItemType;
								PoeHelper.edited = true;
								System.out.println("League set.");
							}
						}
						break;
					//set account
					case "6":
						System.out.println("Current account is: " + currentAccount);
						PoeHelperInterface.printAccounts(accounts);
						System.out.print("Enter new account name: ");
						userInput = input.nextLine();
						for(String anAccount : accounts)
						{
							if(anAccount.equalsIgnoreCase(userInput))
							{
								currentAccount = anAccount;
								System.out.println("Account was successfully set as " + currentAccount);
								PoeHelper.edited = true;
								set = true;
								break;
							}
						}
						if(!set)
						{
							System.out.println("Account found was not an accepted account.");
							System.out.println("Set anyway? [Y/N]");
							userItemType = userInput;
							userInput = input.nextLine().toUpperCase();
							if(userInput.equals("Y"))
							{
								currentAccount = userItemType;
								System.out.println("Account set.");
								PoeHelper.edited = true;
							}
						}
						break;
					//set comment
					case "7":
						System.out.println("Current comment is: " + comment);
						System.out.println("Enter new comment, or nothing to cancel.");
						userInput = input.nextLine();
						if(userInput.equals(""))
						{
						}
						else if(userInput.equalsIgnoreCase("x"))
						{
							return;
						}
						else
						{
							comment = userInput;
							PoeHelper.edited = true;
						}
						break;
					case "X":
						return;
					default:
						System.out.println("Unrecognized command. Enter 'x' to exit.");
				}
			}
		}
		else
		{
			System.out.println("An attempt to enter admin mode in the Item class was called, but admin is not set to true!");
		}
	}

	private static void adminMenu()
	{
		Jeremy.menuStart(3);
		System.out.println("1\t\t\t\t-> Change base type");
		System.out.println("2\t\t\t\t-> Change rarity");
		System.out.println("3\t\t\t\t-> Change quality");
		System.out.println("4\t\t\t\t-> Identified or unidentify this.");
		System.out.println("5\t\t\t\t-> Change item's league.");
		System.out.println("6\t\t\t\t-> Change item's account.");
		System.out.println("7\t\t\t\t-> Change item's comment.");
		Jeremy.menuEnd();
	}

	public void trace()
	{
		System.out.printf("Base Type: %-24s Rarity: %-7s League: %-22s Account: %-15s Quality: %2s%% Identified: %s\n", currentType, currentRarity, currentLeague, currentAccount, quality, identified);
		if(comment != null)
		{
			System.out.println("Comment: " + comment);
		}
	}

	public Boolean identify()
	{
		if(identified)
		{
			System.out.println("You tried to identify an item in the database that was already identified!");
			return false;
		}
		else
		{
			identified = true;
			return true;
		}
	}
}
