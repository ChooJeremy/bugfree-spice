////////////////////////////////////////////////////////////////////////////////////////////////////////
//By Jeremy Choo																					  //
//																									  //
//Simulates a game of Indian Poker (Like Blind man's Bluff)											  //
//																									  //
//The game is played like this: Imagine a deck of cards in front of you. I (a player) will take		  //
//a card from the deck, and without looking at it, place it at my forehead, with it's front facing you//
//Thus you can see my card and I can see yours. Indian Poker can be played with as many players		  //
//As you want. At the start, each player can see anybody else's cards...but not his/her own.		  //
//																									  //
//To win the game, you need to have the highest card. The highest is 'A' (Ace) and the smallest is 2  //
//																									  //
//The idea of the game is that if the other person has like a really high card, you must convince	  //
//him/her that the card is really low and that (s)he should change it. If the card is actually low,   //
//you need to convince him/her that the card is actually high.										  //
//																									  //
//A player's card is revealed to himself/herself once (s)he decides to change the card.				  //
//																									  //
//Version number: 4 (12/06/2012)																	  //
//																									  //
//Current version has 4 AIs completed. They are BeginnerAI, Human, AdvancedAI and FML. A simple		  //
//description is available in game for how the AIs work after they are selected.					  //
//However, you can try reading the codes to learn how any AI works, but be warned, learning how the   //
//AI works could cause the game to no longer be fun, where you begin to strategize based on the code. //
////////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Random;		//Required to use the Rand() function
public class IndianPoker
{
	static int randNum;											//Declaring global variable
	static int playerCardNumber, comCardNumber;			//Global so that the player's number can be accessed by anyone.
	static int change, comExpectedCard;
	static int changes;
	static int form;												//for HumanAI
	static int gameType;
	static String mode;
	static int[] CardsInDeck = new int[52];
	static int currentCard;
	static JTextAreaInputStreamScanner input;

	public static void setScanner(JTextAreaInputStreamScanner s)
	{
		input = s;
	}

	public static void main(String[] args)
	{
		Random generator = new Random();
		char startGame;
		init_CardsInDeck();
		shuffle_CardsInDeck();
		do
		{
			System.out.println("-------------------------------------------------------------------------------\n");
			System.out.println("What mode of difficulty would you like?");
			System.out.println("The choices are: \"BeginnerAI\", \"Human\", \"AdvancedAI\" and \"FML (Friendly Mother Loving mode)\"");
			System.out.print("Your decision: ");
			mode = (input.nextLine()).toUpperCase();
			System.out.println("-------------------------------------------------------------------------------\n");
			switch(mode)
			{
				case "BEGINNERAI":
					do
					{
						System.out.println("You have choosen BeginnerAI. BeginnerAI characteristics:");
						System.out.println("-> Extremely Predictable.\n-> Every response from the AI always mean a certain thing.");
						System.out.println("-> Easy to influence the AI to change or not.\n-> Only one randomness involved in the gameplay.");
						System.out.println("Are you sure? (Y/N): ");
						mode = (input.nextLine()).toUpperCase();
						startGame = mode.charAt(0);
						if(startGame == 'Y')
						{
							BeginnerAI();
						}
						else if(startGame == 'N')
						{
							mode = "Repeat";
						}
						else
						{
							System.out.println("You have entered an incorrect answer. Please try again");
							startGame = 'a';
						}
					}
					while(startGame == 'a');
					break;
				case "ADVANCEDAI":
					do
					{
						System.out.println("You have choosen AdvancedAI. AdvancedAI characteristics:");
						System.out.println("-> Unpredictable.\n-> Every response from the AI could mean a myraid of things.");
						System.out.println("-> Still possible to influence the AI to change.\n-> There is still one method that can cause a garaunteed victory.");
						System.out.println("Are you sure? (Y/N): ");
						mode = (input.nextLine()).toUpperCase();
						startGame = mode.charAt(0);
						if(startGame == 'Y')
						{
							AdvancedAI();
						}
						else if(startGame == 'N')
						{
							mode = "Repeat";
						}
						else
						{
							System.out.println("You have entered an incorrect answer. Please try again");
							startGame = 'a';
						}
					}
					while(startGame == 'a');
					break;
				case "HUMAN":
					do
					{
						System.out.println("You have choosen Human. Human characteristics:");
						System.out.println("-> 50/50 Predictable.\n-> All responses is either true or false.");
						System.out.println("-> Hard to influence the AI to change or not.\n-> No way for a guaranteed victory.");
						System.out.println("Are you sure? (Y/N): ");
						mode = (input.nextLine()).toUpperCase();
						startGame = mode.charAt(0);
						if(startGame == 'Y')
						{
							Human();
						}
						else if(startGame == 'N')
						{
							mode = "Repeat";
						}
						else
						{
							System.out.println("You have entered an incorrect answer. Please try again");
							startGame = 'a';
						}
					}
					while(startGame == 'a');
					break;
				case "FML":
					do
					{
						System.out.println("You have choosen FML mode. FML characteristics:");
						System.out.println("-> ...\n-> ...");
						System.out.println("-> ...\n-> GG.");
						System.out.println("Are you sure? (Y/N): ");
						mode = (input.nextLine()).toUpperCase();
						startGame = mode.charAt(0);
						if(startGame == 'Y')
						{
							FML();
						}
						else if(startGame == 'N')
						{
							mode = "Repeat";
						}
						else
						{
							System.out.println("You have entered an incorrect answer. Please try again");
							startGame = 'a';
						}
					}
					while(startGame == 'a');
					break;
				case "/ADMIN":
					CheckType(1);
					break;
				case "/DEBUG":
					CheckType(2);
					break;
				case "SNAFU":
				case "FUCK":
					CheckType(3);
					break;
				case "CMI":
					CheckType(4);
					break;
				case "/QUIT":
					System.out.println("WHAT? You're not even playing? God...why did you run this?");
					return;
				default:
					System.out.println("You have entered an incorrect answer that does not match either of the options. Please try again.");
					mode = "Repeat";
					break;
			}
		}
		while(mode.equals("Repeat"));
		System.out.println("Both players have decided against changing their cards");  //Game ended
		System.out.print("The game ends. Computer's card was ");    //And the game ends. Repeat what the computer's card is...
		AnnounceCard(comCardNumber);
		System.out.print("...and your card was ");       //And what the player's card is...
		AnnounceCard(playerCardNumber);
		comCardNumber = TrueValue(comCardNumber); //change the computer's card number so that A = highest, etc.
		playerCardNumber = TrueValue(playerCardNumber);//Now we do the same thing to the player.
		if(comCardNumber > playerCardNumber)  //Now if the computer's card was bigger,
		{
			System.out.println("Computer's Card is higher! You lose!");  //He wins.
		}
		else if(comCardNumber < playerCardNumber)   //If your card was higher,
		{
			System.out.println("Your Card is higher! Congratulations! You win!");  //You win!
		}
		else                                   //Oherwise,
		{
			System.out.println("It's a tie!");  //It must be a draw.
		}
		System.out.println("GAME OVER");  //And obviously, the game ends. GAME OVER.

	}

	public static void BeginnerAI()
	{
		Random generator = new Random();			//Allows usage of Random();
		do
		{
			Phase1();
			switch(playerCardNumber)
			{
				case 11:
					System.out.println("pretty low\"");
					break;
				case 12:
					System.out.println("really low\"");
					break;
				case 0:
					System.out.println("very low\"");
					break;
				case 1:
					System.out.println("...\nI'll be honest. It's 2. Please change it so that we can have a decent match.\"");//his reply
					break;
				default:
					System.out.println("ok, actually\"");
					break;
			}
			Phase2();
			if(comExpectedCard >= 2 && comExpectedCard <= 10)  //If you say his card is 2-10...
			{                                                   //he thinks it's low and decides to change it
				System.out.println("Computer says: \"I'm definitely changing my card\"");
				ComCardChange();
			}
			else if(comExpectedCard >= 11)   //If you tell him his card was J-K
			{
				System.out.println("Computer says: \"Well, I'm not changing my card\"");
			}
			else if(comExpectedCard == 1)                         //If you tell him his card is Ace...
			{
				randNum = generator.nextInt(3) + 1;      //The computer cannot always trust you. Thus a random number between
				if(randNum == 1)                         //1-3 is generated. If it's 1, the computer doesn't believe you...
				{
					System.out.println("Computer says: \"I don't believe you. I'm changing my card.\"");
					ComCardChange();
				}
				else                            //Otherwise, if the random number generated is 2 or 3, he doesn't change.
				{
					System.out.println("Computer says: \"Ok, I'm not changing my card\"");
				}
			}
		}                         //Way, WAAAAYYYY back at the for loop...between everything this holds the game. Now, if
		while(changes != 0);      //there had be no changes, end the game.
	}

	public static void AdvancedAI()
	{
		Random generator = new Random();			//Allows usage of Random();
		do
		{
			Phase1();
			randNum = generator.nextInt(4);
			if(randNum == 0)									//Lie
			{
				switch(playerCardNumber)
				{
					case 11:                            //If your card was J, Q, K, A,
					case 12:
					case 0:
					case 1:
						System.out.println("3\""); 		  //3
						break;
					case 2:
					case 3:
					case 4:
					case 5:
						System.out.println("A\"");   //Your card is low, so he'll say it's A.
						break;
					case 6:
					case 7:
					case 8:
					case 9:
						System.out.println("J\"");			//Your card is so-so. J.
						break;
					default:
						System.out.println(playerCardNumber);
						break;
				}
			}
			else if(randNum == 1)			//Tell the truth
			{
				System.out.println(playerCardNumber);
			}
			else if(randNum == 2)			//Tell a version of the truth
			{
				switch(playerCardNumber)
				{
					case 1:
					case 0:
					case 12:
					case 11:
						System.out.println("Q\"");
						break;
					case 9:
					case 8:
					case 7:
					case 6:
						System.out.println("2\"");
						break;
					case 5:
					case 4:
					case 3:
					case 2:
						System.out.println(playerCardNumber + 4);
						break;
					case 10:
						System.out.println("10\"");
						break;
				}
			}
			else if(randNum == 3)			//Tell the number at the opposite end
			{
				switch(playerCardNumber)
				{
					case 1:
						System.out.println("2\"");
						break;
					case 0:
						System.out.println("3\"");
						break;
					case 12:
						System.out.println("4\"");
						break;
					case 11:
						System.out.println("5\"");
						break;
					case 2:
						System.out.println("A\"");
						break;
					case 3:
						System.out.println("K\"");
						break;
					case 4:
						System.out.println("Q\"");
						break;
					case 5:
						System.out.println("J\"");
						break;
					case 6:
					case 7:
					case 8:
					case 9:
						do
						{
							randNum = generator.nextInt(13) + 1; //The computer tells the user some random number...
						}
						while(randNum == 10);					//That isn't 10 (I want 10 to be GUARANTEED to be 10)
						AnnounceCard(randNum);
						break;
					case 10:
						System.out.println("10\"");
						break;
				}
			}
			Phase2();
			randNum = generator.nextInt(4);
			if(randNum == 0)		//Believe
			{
				if(comExpectedCard > 1 && comExpectedCard < 11)
				{
					System.out.println("Computer says: \"I believe you. I'm changing my card.\"");
					ComCardChange();
				}
				else
				{
					System.out.println("Computer says: \"I believe you. I'm not changing.\"");
				}
			}
			else if(randNum == 1)			//Don't believe
			{
				if(comExpectedCard > 1 && comExpectedCard < 11)
				{
					System.out.println("Computer says: \"I don't believe you. I'm not changing.\"");
				}
				else
				{
					System.out.println("Computer says: \"hmmm...I don't believe you. I'm changing!\"");
					ComCardChange();
				}
			}
			else if(randNum == 2)			//Believe only if answer is not extreme.
			{
				if(comExpectedCard > 3 && comExpectedCard < 11)
				{
					System.out.println("Computer says: \"I believe you. I'm changing my card.\"");
					ComCardChange();
				}
				else if((comExpectedCard > 10 && comExpectedCard < 13) || comExpectedCard == 0)
				{
					System.out.println("Computer says: \"I believe you. I'm not changing.\"");
				}
				else if(comExpectedCard == 1)
				{
					System.out.println("Computer says: \"hmmm...I don't believe you. I'm changing!\"");
					ComCardChange();
				}
				else
				{
					System.out.println("Computer says: \"I don't believe you. I'm not changing.\"");
				}
			}
			else				//Random
			{
				switch(comExpectedCard)
				{
					case 4:
					case 8:
					case 10:
					case 3:
					case 6:
						System.out.println("Computer says: \"I don't believe you. I'm not changing.\"");
						break;
					case 2:
					case 5:
					case 7:
					case 9:
						System.out.println("Computer says: \"I believe you. I'm changing my card.\"");
						ComCardChange();
						break;
					case 1:
					case 11:
						System.out.println("Computer says: \"I believe you. I'm not changing.\"");
						break;
					case 0:
					case 12:
						System.out.println("Computer says: \"hmmm...I don't believe you. I'm changing!\"");
						ComCardChange();
						break;
				}
			}
		}
		while(changes != 0);
	}

	public static void Human()
	{
		Random generator = new Random();
		int turns = 0, maxturn;	//How human AI works is that there are four forms. In form 1, humanAI lies and is more
		maxturn = generator.nextInt(3) + 3; //likely to believe you. In form 2, humanAI tells the truth and is more likely
		form = generator.nextInt(4) + 1; //to believe you. In form 3, it lies and is less likely to believe you. in 4, it
		do			//is truthful and is less likely to believe you. The form changes when certain conditions are fulfilled.
		{
			Phase1();
			turns = turns + 1;
			if(turns == maxturn)
			{
				FormChange();
				maxturn = generator.nextInt(3) + 3;
			}
			if(form % 2 == 1)
			{
				if(playerCardNumber > 10)
				{
					System.out.println("low\"");
				}
				else if(playerCardNumber <= 1)
				{
					System.out.println("very low. You should change it.\"");
				}
				else if(playerCardNumber >= 6)
				{
					System.out.println("high.\"");
				}
				else
				{
					System.out.println("very high. Keep it.\"");
				}
			}
			else
			{
				if(playerCardNumber > 10)
				{
					System.out.println("high\"");
				}
				else if(playerCardNumber <= 1)
				{
					System.out.println("very high. Keep it.\"");
				}
				else if(playerCardNumber >= 6)
				{
					System.out.println("low.\"");
				}
				else
				{
					System.out.println("very low. You should change it.\"");
				}
			}
			Phase2();
			if(change == 1)                          //Changes form if conditionsa are met
			{
				if(form % 2 == 1 && (playerCardNumber > 10 || playerCardNumber <= 1))
				{
					FormChange();
				}
				else if(form % 2 == 0 && (playerCardNumber < 10 || playerCardNumber > 1))
				{
					FormChange();
				}
			}
			randNum = generator.nextInt(3) + 1;
			if(form <= 2)				//more likely to believe
			{
				if(randNum == 1)
				{
					if(comExpectedCard <= 1 || comExpectedCard > 10)
					{
						System.out.println("Computer says: \"I don't believe you. I'm changing my card.\"");
						if(comCardNumber > 2 && comCardNumber < 11)
						{
							FormChange();
						}
						ComCardChange();

					}
					else
					{
						System.out.println("Computer says: \"I don't believe you. I'm not changing.\"");
					}
				}
				else
				{
					if(comExpectedCard <= 1 || comExpectedCard > 10)
					{
						System.out.println("Computer says: \"Ok then, I'm not changing.\"");
					}
					else
					{
						System.out.println("Computer says: \"I believe you. I'm changing my card.\"");
						if(comCardNumber <= 1 && comCardNumber > 10)
						{
							FormChange();
						}
						ComCardChange();
					}
				}
			}
			else
			{
				if(randNum == 1)
				{
					if(comExpectedCard <= 1 || comExpectedCard > 10)
					{
						System.out.println("Computer says: \"Ok then, I'm not changing.\"");
					}
					else
					{
						System.out.println("Computer says: \"I believe you. I'm changing my card.\"");
						if(comCardNumber <= 1 && comCardNumber > 10)
						{
							FormChange();
						}
						ComCardChange();
					}
				}
				else
				{
					if(comExpectedCard <= 1 || comExpectedCard > 10)
					{
						System.out.println("Computer says: \"I don't believe you. I'm changing my card.\"");
						if(comCardNumber > 2 && comCardNumber < 11)
						{
							FormChange();
						}
						ComCardChange();
					}
					else
					{
						System.out.println("Computer says: \"I don't believe you. I'm not changing.\"");
					}
				}
			}
		} while(changes != 0);
	}

	public static void FML()
	{
		Random generator = new Random();			//Allows usage of Random();
		int comTemp, playerTemp;
		int aceCount = 0;
		do
		{
			Phase1();
			randNum = generator.nextInt(13);
			switch(randNum)           					 //This is printing the card.
			{
				case 11:
					System.out.println("J\"");
					break;
				case 12:
					System.out.println("Q\"");
					break;
				case 0:
					System.out.println("K\"");
					break;
				case 1:
					System.out.println("A\"");
					break;
				default:
					System.out.println(randNum + "\"");
					break;
			}
			Phase2();
			if(changes == 1)
			{
				aceCount = 0;
			}
			comTemp = TrueValue(comCardNumber);
			playerTemp = TrueValue(playerCardNumber);
			if(playerTemp == 200)
			{
				if(aceCount >= 20 && comTemp == 200)		//let it be a tie
				{
					comTemp = 2;
					playerTemp = 1;
				}
				aceCount = aceCount + 1;
			}
			if(comTemp <= playerTemp)	//And ignored. The computer changes if his card is really lower (or same)...
			{
				if(comExpectedCard >= 11 || comExpectedCard <= 1)//We still need a response for the player's response, so...
				{
					System.out.println("Computer says: \"I don't believe you. I'm changing\"");
				}
				else if(comExpectedCard <= 10)
				{
					System.out.println("Computer says: \"Well, I'm definitely changing\"");
				}
				ComCardChange();
			}
			else													//Otherwise there's no need to change
			{
				if(comExpectedCard >= 11 || comExpectedCard <= 1)
				{
					System.out.println("Computer says: \"Ok, then, I'm not changing.\"");
				}
				else if(comExpectedCard <= 10)
				{
					System.out.println("Computer says: \"I don't believe you. I'm not changing\"");
				}
			}
		}
		while(changes != 0);
	}

	public static void AnnounceCard(int n)
	{
		switch(n)           					 //This is printing the card.
		{
			case 11:
				System.out.println("J");
				break;
			case 12:
				System.out.println("Q");
				break;
			case 0:
				System.out.println("K");
				break;
			case 1:
				System.out.println("A");
				break;
			default:
				System.out.println(n);
				break;
		}
	}

	public static void ComCardChange()
	{
		Random generator = new Random();			//Allows usage of Random();
		changes = changes + 1;
		System.out.print("Computer reveals to himself that his card was ");
		AnnounceCard(comCardNumber);
		if(comCardNumber >= 2 && comCardNumber <= 10) //Now for computer's reaction. If his card is 2-8...
		{
			randNum = generator.nextInt(6) + 1; //Generates a random happy response out of 6 given.
			switch(randNum)
			{
				case 1:
					System.out.println("Computer says: \"Well, it was low. Lucky I changed\"");
					break;
				case 2:
					System.out.println("Computer says: \"Yay! Good to have changed it\"");
					break;
				case 3:
					System.out.println("Computer says: \"Quite low. Changing FTW!\"");
					break;
				case 4:
					System.out.println("Computer says: \"Excellent!\"");
					break;
				case 5:
					System.out.println("Computer says: \"Yay!\"");
					break;
				case 6:
					System.out.println("Computer says: \"Great!\"");
					break;
			}
		}
		else if(comCardNumber >= 11 || comCardNumber == 0)  //If his card was J-K,
		{
			randNum = generator.nextInt(6) + 1; //Generates a random mild response.
			switch(randNum)
			{
				case 1:
					System.out.println("Computer says: \"...\"");
					break;
				case 2:
					System.out.println("Computer says: \"At least it wasn't that high\"");
					break;
				case 3:
					System.out.println("Computer says: \"Shouldn't have done that\"");
					break;
				case 4:
					System.out.println("Computer says: \"hmm...\"");
					break;
				case 5:
					System.out.println("Computer says: \"Ok...never mind. Changing is fine\"");
					break;
				case 6:
					System.out.println("Computer says: \"I can get an Ace!\"");
					break;
			}
		}
		else if(comCardNumber == 1)					//If the card was Ace though...
		{
			randNum = generator.nextInt(6) + 1; //Generates a random "Swearing" response.
			switch(randNum)
			{
				case 1:
					System.out.println("Computer says: \"*beep* *beep* *beeeeeeeeeeeeeeep*\"");
					break;
				case 2:
					System.out.println("Computer says: \"Damnit!\"");
					break;
				case 3:
					System.out.println("Computer says: \"NOOOOOOOOOOOOOOOOOOO!!!\"");
					break;
				case 4:
					System.out.println("Computer says: \"Sh*t\"");
					break;
				case 5:
					System.out.println("Computer says: \"SO shouldn't have changed\"");
					break;
				case 6:
					System.out.println("Computer says: \"Arrrgh\"");
					break;
			}
		}
		comCardNumber = draw_CardsInDeck() % 13;
	}

	public static void FormChange()
	{
		if(form == 4)
		{
			form = 0;
		}
		else
		{
			form++;
		}
	}

	public static int TrueValue(int n)		//Changes card so that Ace is truly highest and King is 2nd highest
	{
		if(n == 1)
		{
			n = 200;
		}
		else if(n == 0)
		{
			n = 100;
		}
		return(n);
	}

	public static void Phase1()
	{
		Random generator = new Random();
		changes = 0;   //No one has changed their card
		if(gameType == 3)											//SNAfU mode
		{
			randNum = generator.nextInt(10);
			if(randNum < 3)
			{
				playerCardNumber = (generator.nextInt(52) + 1) % 13;
			}
			else if(randNum > 6)
			{
				comCardNumber = (generator.nextInt(52) + 1) % 13;
			}
			else
			{
				comCardNumber = (comCardNumber + 1) % 13;
			}
		}
		System.out.print("Computer's card is "); //Announce computer's card
		AnnounceCard(comCardNumber);
		System.out.print("Computer says: \"Also, your card is ");   //Computer says about YOUR card.
	}

	public static void Phase2()
	{
		Random generator = new Random();
		do
		{
			System.out.println("Would you like to change your card? 1 for yes and 2 for no.");  //Ask to change
			change = input.nextInt();
			if(change == 1)                          //If you decide to change,
			{
				changes = changes + 1;
				System.out.print("Your card actually was ");
				AnnounceCard(playerCardNumber);
				System.out.println("You change your card");
				if(gameType == 4)
				{
					do
					{
						playerCardNumber = draw_CardsInDeck() % 13;
					} while (TrueValue(playerCardNumber) > TrueValue(comCardNumber));
				}
				else
				{
					playerCardNumber = draw_CardsInDeck() % 13;
				}
			}
			else if (change == 2)                          //Else if you decide not to change...
			{
				System.out.println("You decide against changing your card");
			}
			else                                              //If you do type in a wrong number (-1, 0, 3, 4, 5...)
			{
				System.out.println("You have entered an invalid number. Please try again.");
			}
		} while (change < 1 || change > 2);
		do
		{
			System.out.println("Computer says: \"Tell me about my card\nMy Card is...\" (Enter a number, let J be 11, Q be 12 and K be 13.)");
			comExpectedCard = input.nextInt();
			if(comExpectedCard >= 14 || comExpectedCard < 0)
			{
				System.out.println("You have entered an incorrect number. Please try again.");
			}
		} while(comExpectedCard >= 14 || comExpectedCard < 0);
	}

	public static void CheckType(int i)
	{
		if(gameType != 0 && gameType != i)
		{
			ReadType(gameType);
			System.out.println("disabled.");
		}
		if(gameType == i)
		{
			ReadType(i);
			System.out.println("is already enabled.");
		}
		else
		{
			ReadType(i);
			System.out.println("is now enabled.");
		}
		mode = "Repeat";
		gameType = i;
	}

	public static void ReadType(int i)
	{
		switch(i)
		{
			case 1:
				System.out.print("Admin");
				break;
			case 2:
				System.out.print("Debug");
				break;
			case 3:
				System.out.print("Situation Normal: All Fucked Up");
				break;
			case 4:
				System.out.print("Cannot Make It");
				break;
			default:
				System.out.println("Error: ReadType method failed: i does not match any value.");
				break;
		}
		System.out.print(" mode ");
	}

	public static void init_CardsInDeck()			//initalizes the deck
	{
		for(int i = 0; i < CardsInDeck.length; i++)
		{
			CardsInDeck[i] = i + 1;
			currentCard = 0;
		}
	}

	public static void shuffle_CardsInDeck()		//Shuffles the deck
	{
		Random generator = new Random();
		for(int i = 0; i < CardsInDeck.length; i++)
		{
			int temp = CardsInDeck[i];
			int j = i + generator.nextInt(CardsInDeck.length - i);
			CardsInDeck[i] = CardsInDeck[j];
			CardsInDeck[j] = temp;
			currentCard = 0;
		}
	}

	public static int draw_CardsInDeck() //draws a card
	{
		int result;
		result = CardsInDeck[currentCard];
		currentCard++;
		return result;
	}
}