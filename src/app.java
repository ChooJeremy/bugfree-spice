import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class app extends JFrame implements ActionListener
{
	private Container container;
	private JLabel gameStatus;
	private Container gameBoard, playerBoard;
	private Side8Wrapper s8w;
	private boolean isEnemyTurn;
	private Border originalBorder;
	private boolean playerStartFirst;

	public app()
	{
		//Set the essentials
		setTitle("App");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//We wrap all our stuff in a JComponent with an empty border of 20px. This makes it such that the app has a margin.
		JComponent jc = (JComponent) this.getContentPane();
		jc.setLayout(new GridLayout(1, 1));
		jc.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		//Create the main container that will hold all our stuff
		container = new JPanel();
		jc.add(container);
		container.setLayout(new GridLayout(3, 1, 0, 5));

		//Top part: The status
		gameStatus = new JLabel("Status stuff goes here", SwingConstants.CENTER);
		gameStatus.setFont(new Font("Calibri", Font.PLAIN, 20));
		gameStatus.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		gameStatus.setToolTipText("Status message");

		//Middle part: The game board. We create a border so that it is sqare, and populate it with 9 buttons
		gameBoard = new JPanel();
		((JComponent) gameBoard).setBorder(BorderFactory.createEmptyBorder(0, 188, 0, 188));
		gameBoard.setLayout(new GridLayout(3, 3, 1, 1));
		JButton jb;
		for(int i = 0; i < 9; i++)
		{
			jb = new JButton();
			jb.setFont(new Font("Calibri", Font.PLAIN, 18));
			jb.setActionCommand("" + i);
			jb.addActionListener(this);
			gameBoard.add(jb);
		}

		//Bottom part: the player's hand. The starting is 7 cards. We populate it with 7 buttons
		playerBoard = new JPanel();
		playerBoard.setLayout(new GridLayout(1, 7, 1, 0));
		for(int i = 0; i < 7; i++)
		{
			jb = new JButton("" + i);
			jb.setFont(new Font("Calibri", Font.PLAIN, 18));
			jb.setBackground(Color.WHITE);
			jb.setActionCommand("" + i);
			jb.addActionListener(this);
			playerBoard.add(jb);
		}

		//Cache the border of the original buttons so we can set it back if we ever change it
		originalBorder = ((JComponent) playerBoard.getComponent(0)).getBorder();

		container.add(gameStatus);
		container.add(gameBoard);
		container.add(playerBoard);

		//Fill the board
		s8w = new Side8Wrapper();
		fillBoard();

		//Fill the player's hands
		for(int i = 0; i < s8w.getPlayer().handSize(); i++)
		{
			((JButton) playerBoard.getComponent(i)).setText("" + s8w.getPlayer().getHand().get(i).getNumber());
		}

		//Player starts first
		playerStartFirst = true;
		allocateNextTurn();
		//new AppTimer(this, AppTimer.listenForInput, 500);
		listenForInput();
	}

	public void fillBoard()
	{
		int[][] board = s8w.getBoard().getBoard();
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				JButton jb = (JButton) gameBoard.getComponent(i*3+j);
				if(board[i][j] < 0)
				{
					jb.setText("" + (board[i][j] * -1));
					jb.setBackground(Color.RED);
				}
				else if(board[i][j] > 0)
				{
					jb.setText("" + board[i][j]);
					jb.setBackground(Color.GREEN);
				}
				if((i==1&&j==1)||board[i][j] == 0)
				{
					jb.setText("" + board[i][j]);
					jb.setBackground(Color.LIGHT_GRAY);
				}
			}
		}
		gameStatus.setText("<html><pre>" + s8w.getBoard().getBoardView() + "</pre></html>");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(isEnemyTurn)
		{
			gameStatus.setText("It's the opponent's turn! Please hold on...");
			return;
		}
		/*
		System.out.println("Action performed!");
		System.out.println("Source text: " + ((JButton) e.getSource()).getText());
		System.out.println("Source: " + e.getSource());
		System.out.println("Background: " + ((JButton) e.getSource()).getBackground());
		System.out.println("Action command: " + e.getActionCommand());
		*/
		JButton source = (JButton) e.getSource();
		int selectedCardLocation = Integer.parseInt(e.getActionCommand());
		boolean startOfGame = ((GridLayout) playerBoard.getLayout()).getColumns() == 7;

		if(source.getBackground() == Color.WHITE) //Player cards clicked
		{
			if(startOfGame)
			{
				//It is the start of the game, where players are setting the numbers on the board

				//Set the card and show the user that is has been selected
				s8w.setCardNo(selectedCardLocation);
				((JButton) e.getSource()).setBorder(BorderFactory.createLoweredBevelBorder());
				deselectEverything();
				if(s8w.getPlayer().getHand().size() > 3)
				{
					//The player is selecting something to send to the board
					gameStatus.setText("Card selected: " + source.getText());
					//Wait for the player to choose which position to send to
				}
				else
				{
					//Neutral decision
					gameStatus.setText("<html><pre>Card chosen: " + source.getText() + "\nWaiting for opponent...</pre></html>");
					allocateNextTurn();
				}
			}
			else
			{
				//Game cards are being played
				System.out.println("Game cards are being played!");
				JButton btn = new JButton("This is a new button!");
				container.add(btn);
			}
		}
		else
		{
			//Player has not selected a card
			if(s8w.getCardNo() == null)
			{
				//If setting neutrals
				int playerMoves = 0, opponentMoves = 0;
				for(Component c : gameBoard.getComponents())
				{
					if(c.getBackground() == Color.RED)
					{
						opponentMoves++;
					}
					else if(c.getBackground() == Color.GREEN)
					{
						playerMoves++;
					}
				}
				if(playerMoves == 4 && opponentMoves == 4)
				{
					gameStatus.setText("Please select a card to compare!");
				}
				else
				{
					gameStatus.setText("Please select a card to use first!");
				}
			}
			else
			{
				if(startOfGame)
				{
					//Ensure the player is not setting the middle
					if(selectedCardLocation == 4)
					{
						gameStatus.setText("You can't send a card to the middle square, that square must be neutral!");
						return;
					}
					//Or something that has already been selected
					else if(Integer.parseInt(((JButton) gameBoard.getComponent(selectedCardLocation)).getText()) != 0)
					{
						gameStatus.setText("That place has already been set. Please set another square.");
						return;
					}
					//Get the card
					int playerCardLocation = s8w.getCardNo();
					int playerCardNo = Integer.parseInt(((JButton) playerBoard.getComponent(s8w.getCardNo())).getText());

					//Set to location
					((JButton) gameBoard.getComponent(selectedCardLocation)).setText("" + playerCardNo);
					gameBoard.getComponent(selectedCardLocation).setBackground(Color.GREEN);
					s8w.getBoard().setBoardNumber(selectedCardLocation, playerCardNo);

					//Remove the card
					s8w.getPlayer().removeCard(new Card(playerCardNo, Card.DIAMOND));
					playerBoard.remove(playerCardLocation);
					playerBoard.add(new JPanel(), playerCardLocation);

					//Update the final stuff
					gameStatus.setText(playerCardNo + " sent to board.");
					s8w.finishSelection();
					deselectEverything();
					allocateNextTurn();
				}
				else
				{
					//Set the required values
					s8w.setCardNo(selectedCardLocation);

					//Run the card

					//Update final stuff
					deselectEverything();
					allocateNextTurn();
				}
			}
		}
	}

	public void performOpponentsTurn()
	{
		performOpponentsTurn(((GridLayout) playerBoard.getLayout()).getColumns() == 7);
	}

	public void performOpponentsTurn(boolean isStartOfGame)
	{
		if(isStartOfGame)
		{
			int cardToUse = s8w.getOpponent().getHand().get(0).getNumber();

			//Find out how many moves the player has made, and how many moves the opponent has made
			int playerMoves = 0, opponentMoves = 0;
			for(Component c : gameBoard.getComponents())
			{
				if(c.getBackground() == Color.RED)
				{
					opponentMoves++;
				}
				else if(c.getBackground() == Color.GREEN)
				{
					playerMoves++;
				}
			}

			if(playerMoves == 4 && opponentMoves == 4)           //Neutral setting time
			{
				//Use the card already decided above (highest card in opponent's hand).
				int playerCardNo = Integer.parseInt(((JButton) playerBoard.getComponent(s8w.getCardNo())).getText());
				int neutrals = Math.abs(playerCardNo - cardToUse);
				s8w.getBoard().setBoardNumber(4, s8w.getBoard().getBoardNumber(4) + neutrals);
				((JButton) gameBoard.getComponent(4)).setText("" + s8w.getBoard().getBoardNumber(4));

				//Remove the cards
				s8w.getPlayer().removeCard(new Card(playerCardNo, Card.DIAMOND));
				playerBoard.remove(s8w.getCardNo());
				playerBoard.add(new JPanel(), (int) s8w.getCardNo());
				s8w.getOpponent().removeCard(new Card(cardToUse, Card.DIAMOND));
				s8w.finishSelection();

				gameStatus.setText("<html><pre>Opponent chose card " + cardToUse + ", your card was " + playerCardNo + "\nNeutrals added: " + neutrals + "</pre></html>");
			}
			else
			{
				//Set the cards on the board.
				//Run through until you find a square that's available. Flip a coin. If heads, pick that. Otherwise, continue
				Random random = new Random();
				boolean hasSetACard = false;
				while(!hasSetACard)
				{
					for(int i = 0; i < gameBoard.getComponentCount(); i++)
					{
						if(i != 4 && s8w.getBoard().getBoardNumber(i) == 0)
						{
							if(random.nextBoolean())
							{
								hasSetACard = true;
								//Set to location
								((JButton) gameBoard.getComponent(i)).setText("" + cardToUse);
								gameBoard.getComponent(i).setBackground(Color.RED);
								s8w.getBoard().setBoardNumber(i, cardToUse * -1);

								//Remove the card
								s8w.getOpponent().removeCard(new Card(cardToUse, Card.DIAMOND));
								break;
							}
						}
					}
				}
			}
		}
		else
		{
			//Opponent should now play a card.
			System.out.println("Enemy does stuff!");
			isEnemyTurn = false;
		}
		allocateNextTurn();
	}

	public void allocateNextTurn()
	{
		boolean startOfGame = ((GridLayout) playerBoard.getLayout()).getColumns() == 7;
		if(startOfGame)
		{
			//Find out how many moves the player has made, and how many moves the opponent has made
			int playerMoves = 0, opponentMoves = 0;
			for(Component c : gameBoard.getComponents())
			{
				if(c.getBackground() == Color.RED)
				{
					opponentMoves++;
				}
				else if(c.getBackground() == Color.GREEN)
				{
					playerMoves++;
				}
			}

			if(playerMoves == 4 && opponentMoves == 4)
			{
				if(s8w.getPlayer().getHand().size() > 1)
				{
					//Time to set the neutrals!
					if(s8w.getCardNo() == null)
					{
						isEnemyTurn = false;
						//Don't immediately change, append
						if(s8w.getPlayer().getHand().size() == 2)
						{
							gameStatus.setText(gameStatus.getText().substring(0, gameStatus.getText().length() - 13) +
									"\nSelect a card to compare with the opponent's card.</pre></html>");
						}
						else
						{
							gameStatus.setText("Select a card to compare with the opponent's card.");
						}
					}
					else
					{
						isEnemyTurn = true;
						new AppTimer(this, AppTimer.performOpponentsTurn, 500);
					}
					return;
				}
				else
				{
					int playerCard = s8w.getPlayer().getHand().get(0).getNumber();
					int opponentCard = s8w.getOpponent().getHand().get(0).getNumber();
					if(playerCard > opponentCard)
					{
						gameStatus.setText(gameStatus.getText().substring(0, gameStatus.getText().length() - 13) +
								"\nOpponent's card: " + opponentCard + " Yours: " + playerCard + ". You start first.</pre></html>");
						isEnemyTurn = false;
					}
					else if(playerCard < opponentCard)
					{
						gameStatus.setText(gameStatus.getText().substring(0, gameStatus.getText().length() - 13) +
								"\nOpponent's card: " + opponentCard + " Yours: " + playerCard + ". You start second.</pre></html>");
						isEnemyTurn = true;
					}
					else
					{
						//If check is already done before
						if(!gameStatus.getText().substring(0, 5).equals("Right"))
						{
							gameStatus.setText(gameStatus.getText().substring(0, gameStatus.getText().length() - 13) +
									"\nOpponent's card: " + opponentCard + " Yours: " + playerCard + ". It's a tie!</pre></html>");
							new AppTimer(this, AppTimer.performTieBreaker, 1500);
							return;
						}
					}
					//Remove the cards from the player's hands
					s8w.getPlayer().removeCard(new Card(playerCard, Card.DIAMOND));
					s8w.getOpponent().removeCard(new Card(opponentCard, Card.DIAMOND));
					//Create the game cards and give them to the players

					//Perform the required layout changes (Changing the player's board to 5, etc, dealing the cards)
					playerBoard.removeAll();
					playerBoard.setLayout(new GridLayout(1, 5, 1, 0));
					JButton jb;
					for(int i = 0; i < 5; i++)
					{
						jb = new JButton("<html>Card " + (i+1) + "<br />Description Description Description Description Description Description</html>");
						jb.setFont(new Font("Calibri", Font.PLAIN, 18));
						jb.setBackground(Color.WHITE);
						jb.setActionCommand("" + i);
						jb.addActionListener(this);
						playerBoard.add(jb);
					}
					//Run opponent's move if it's the opponent's turn, otherwise wait for user input.
					if(isEnemyTurn)
					{
						new AppTimer(this, AppTimer.allocateNextTurn, 1000);
					}
					repaint();
					revalidate();
				}
			}

			if(playerStartFirst)
			{
				//1P, 2O, 2P, 2O, 1P
				switch(playerMoves)
				{
					//In these cases, it's definitely the player's turn
					case 0:
					case 2:
						isEnemyTurn = false;
						gameStatus.setText("<html><pre>It's your turn! Pick a card to send to the board!</pre></html>");
						break;
					//Unsure, must check: May be after or before an opponent's move
					case 1:
					case 3:
						isEnemyTurn = true;
						switch(opponentMoves)
						{
							//Player has made 1 move, opponent's turn
							case 0:
								new AppTimer(this, AppTimer.performOpponentsTurn, 2000);
								break;
							//Player has made 1 move, opponent 1 move, opponent's 2nd turn (in a row)
							//or player has made 3 moves, opponent 3 moves, opponent's 2nd turn (in a row)
							case 1:
							case 3:
								//Let's not force the player to wait a long time
								new AppTimer(this, AppTimer.performOpponentsTurn, 200);
								break;
							//Player has made 1-3 moves, opponent 2 moves.
							case 2:
								if(playerMoves == 1)
								{
									isEnemyTurn = false;
									gameStatus.setText("<html><pre>It's your turn! Pick a card to send to the board!</pre></html>");
								}
								else
								{
									new AppTimer(this, AppTimer.performOpponentsTurn, 2000);
								}
								break;
							//Opponent has finished placing everything, player's last move.
							case 4:
								isEnemyTurn = false;
								gameStatus.setText("<html><pre>It's your turn! Pick a card to send to the board!</pre></html>");
								break;
						}
						break;
				}
			}
			else        //enemy starts first
			{
				//Essentially the same as player, but the playerMoves and opponent moves swapped.
				switch(opponentMoves)
				{
					//In these cases, it's definitely the opponent's turn
					case 0:
						isEnemyTurn = true;
						new AppTimer(this, AppTimer.performOpponentsTurn, 2000);
						break;
					case 2:
						isEnemyTurn = true;
						//Let's not force the player to wait a long time
						new AppTimer(this, AppTimer.performOpponentsTurn, 200);
						break;
					//Unsure, must check: May be after or before a player's move
					case 1:
					case 3:
						isEnemyTurn = false;
						switch(playerMoves)
						{
							//Opponent has made 1 move, player's turn
							case 0:
								gameStatus.setText("<html><pre>It's your turn! Pick a card to send to the board!</pre></html>");
								break;
							//Opponent has made 1 move, player 1 move, player's 2nd turn (in a row)
							//or opponent has made 3 moves, player 3 moves, player's 2nd turn (in a row)
							case 1:
							case 3:
								gameStatus.setText("<html><pre>It's your turn! Pick a card to send to the board!</pre></html>");
								break;
							//Opponent has made 1-3 moves, player 2 moves.
							case 2:
								if(opponentMoves == 1)
								{
									isEnemyTurn = true;
									new AppTimer(this, AppTimer.performOpponentsTurn, 2000);
								}
								else
								{
									gameStatus.setText("<html><pre>It's your turn! Pick a card to send to the board!</pre></html>");
								}
								break;
							//Player has finished placing everything, opponent's last move.
							case 4:
								isEnemyTurn = true;
								new AppTimer(this, AppTimer.performOpponentsTurn, 2000);
								break;
						}
						break;
				}
			}
		}
		else
		{
			//Give the next turn to the appropriate player.
		}
	}

	public void listenForInput()
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		boolean exit = false;
		boolean isDisposed = false;
		JButton jb;
		//newgame, Randomboard, (un)instant, repaint/revalidate, dispose, new, restart/reboot, exit
		System.out.println("Ready.");
		do
		{
			userInput = scanner.nextLine().toLowerCase();
			switch(userInput)
			{
				case "newgame":
					if(isDisposed)
					{
						System.out.println("lol");
						break;
					}
					s8w.restart();
					fillBoard();
					//Fill the player's hands
					//Act as if it's the start of the game by ensuring that the playerboard has 7 components
					playerBoard.removeAll();
					playerBoard.setLayout(new GridLayout(1, 7, 1, 0));
					for(int i = 0; i < 7; i++)
					{
						jb = new JButton("" + s8w.getPlayer().getHand().get(i).getNumber());
						jb.setFont(new Font("Calibri", Font.PLAIN, 18));
						jb.setBackground(Color.WHITE);
						jb.setActionCommand("" + i);
						jb.addActionListener(this);
						playerBoard.add(jb);
					}
					System.out.println("Done.");
					//Decide who starts first
					isEnemyTurn = false;
					allocateNextTurn();
					break;
				case "randomboard":
					if(isDisposed)
					{
						System.out.println("lol");
						break;
					}
					s8w.restart();
					s8w.getBoard().createRandomBoard();
					fillBoard();
					while(s8w.getPlayer().handSize() > 1)
					{
						s8w.getPlayer().removeCard(s8w.getPlayer().getHand().get(0));
					}
					while(s8w.getOpponent().handSize() > 1)
					{
						s8w.getOpponent().removeCard(s8w.getOpponent().getHand().get(0));
					}

					//Act as if it's the start of the game by ensuring that the playerboard has 7 components
					playerBoard.removeAll();
					playerBoard.setLayout(new GridLayout(1, 7, 1, 0));
					for(int i = 0; i < 6; i++)
					{
						playerBoard.add(new JPanel());
					}
					jb = new JButton("" + s8w.getPlayer().getHand().get(0).getNumber());
					jb.setFont(new Font("Calibri", Font.PLAIN, 18));
					jb.setBackground(Color.WHITE);
					jb.setActionCommand("" + 7);
					jb.addActionListener(this);
					playerBoard.add(jb);

					System.out.println("Done.");
					allocateNextTurn();
					break;
				case "instant":
					AppTimer.instant = true;
					System.out.println("Set.");
					break;
				case "uninstant":
					AppTimer.instant = false;
					System.out.println("Unset");
					break;
				case "repaint":
				case "revalidate":
					if(isDisposed)
					{
						System.out.println("lol");
						break;
					}
					container.revalidate();
					container.repaint();
					System.out.println(userInput + " request sent.");
					break;
				case "dispose":
					if(isDisposed)
					{
						System.out.println("lol");
						break;
					}
					this.dispose();
					isDisposed = true;
					System.out.println("Done.");
					break;
				case "new":
					if(!isDisposed)
					{
						System.out.print("The new input scanner will take over this. Continue? ");
						if(!Jeremy.getBoolean())
						{
							System.out.println("Ready.");
							break;
						}
					}
					new app();
					if(!isDisposed)
					{
						System.out.println("Old input scanner back. Ready.");
					}
					else
					{
						exit = true;
					}
					break;
				// ------------------------------ Fixed due to reliance on lack of break; statements. --------------------
				case "restart":
				case "reboot":
					this.dispose();
					isDisposed = true;
					new app();
				case "exit":
					exit = true;
					if(!isDisposed)
					{
						this.dispose();
					}
					break;
				// ------------------------------ Fixed due to reliance on lack of break; statements. --------------------
				default:
					System.out.println("Unrecognized input. Please try again.");
					System.out.println("Accepted input: newgame, Randomboard, (un)instant, repaint/revalidate, dispose, new, restart/reboot, exit");
					break;
			}
		} while (!exit);
	}

	public void performTieBreaker()
	{
		//Right now, player always wins!
		isEnemyTurn = false;
		gameStatus.setText("Right now, player always wins!");
		//If you change this gameStatus, make sure you change the one at the AllocateNextTurn decision when the condition
		//opponent card == player card is true
		new AppTimer(this, AppTimer.allocateNextTurn, 1000);
	}

	public void dump()
	{
		System.out.println(s8w.getEverythingInString());
	}

	public void deselectEverything()
	{
		//Deselect all previous cards
		for(Component c : playerBoard.getComponents())
		{
			if(c instanceof JButton)
			{
				((JButton) c).setBorder(originalBorder);
			}
		}

		//Deselect all the board stuff
		for(Component c : gameBoard.getComponents())
		{
			if(c instanceof JButton)
			{
				((JButton) c).setBorder(originalBorder);
			}
		}
	}

	public static void main(String[] args)
	{
		new app();
	}
}
