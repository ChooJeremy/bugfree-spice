import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
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
					//Player has selected a board to use a game card on.
				}
			}
		}
	}

	public void performOpponentsTurn()
	{
		performOpponentsTurn(((GridLayout) playerBoard.getLayout()).getColumns() == 7);
	}

	public void print()
	{
		System.out.println("Print: ");
		System.out.println("Playerboard components: " + Jeremy.arrayToString(playerBoard.getComponents()).replace(", ", "\n"));
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
			//Make a move
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
						new AppTimer(this, AppTimer.performOpponentsTurn, 500);
					}
					return;
				}
				else
				{
					//Compare the cards to see who starts first
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

		}
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
