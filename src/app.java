import EventListeners.*;
import GameCard.*;
import Side8Items.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class app extends JFrame implements ActionListener
{
	private JLayeredPane mainPane;
	private JLabel gameStatus;
	private Container gameBoard, playerBoard;
	private Side8Wrapper s8w;
	private boolean isEnemyTurn;
	private Border originalBorder;
	private boolean playerStartFirst;
	private boolean isStartOfGame;
	private boolean isPlayAgain;
	private int turnCounter;
	private boolean playerHasPlayed;
	private boolean enemyHasPlayed;
	private boolean gameIsOver;

	public app()
	{
		//Set the essentials
		setTitle("App");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//Provide a margin of 20 by 20 pixels.
		((JComponent) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		//Create a layered panel to hold the layout so that we can use absolute values to show certain cards.
		mainPane = new JLayeredPane();
		mainPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.getContentPane().add(mainPane);

		//Create the main container that will hold all our stuff
		Container container = new JPanel();
		container.setLayout(new GridLayout(3, 1, 0, 5));
		container.setLocation(0, 0);
		//Size will be set in the component listener.
		mainPane.add(container, JLayeredPane.DEFAULT_LAYER);

		//However, we want the container to be able to set it's size based on the window's size, and resize accordingly.
		//Thus, add a component listener that implements component resized and do stuff accordingly.
		this.addComponentListener(new FitContainer(this, container, -57, -78));

		//Top part: The status
		gameStatus = new JLabel("Status stuff goes here", SwingConstants.CENTER);
		gameStatus.setFont(new Font("Calibri", Font.PLAIN, 20));
		gameStatus.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		gameStatus.setToolTipText("Status message");

		//Middle part: The game board. We create a border so that it is square, and populate it with 9 buttons
		gameBoard = new JPanel();
		((JComponent) gameBoard).setBorder(BorderFactory.createEmptyBorder(0, 188, 0, 188));
		gameBoard.setLayout(new GridLayout(3, 3, 1, 1));
		//gameBoard will be populated later in fillBoard

		//Bottom part: the player's hand. The starting is 7 cards. We populate it with 7 buttons
		playerBoard = new JPanel();
		playerBoard.setLayout(new GridLayout(1, 7, 1, 0));
		JButton jb;
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
		s8w.setContentPane(mainPane);
		fillBoard();

		//Fill the player's hands
		for(int i = 0; i < s8w.getPlayer().getHand().size(); i++)
		{
			int theNum = ((StartCard) s8w.getPlayer().getHand().get(i)).getThisNum();
			((JButton) playerBoard.getComponent(i)).setText("" + theNum);
		}

		//Player starts first
		playerStartFirst = true;
		isStartOfGame = true;
		gameIsOver = false;
		allocateNextTurn();
		listenForInput();
	}

	public void fillBoard()
	{
		Side8Board board = s8w.getBoard();
		gameBoard.removeAll();
		for(int i = 0; i < 9; i++)
		{
			Side8BoardItem o = s8w.getBoard().getBoardItem(i);
			o.updateValues(false);
			o.setActionCommand("" + i);
			o.addActionListener(this);
			o.setJLayeredPaneReference(mainPane);
			gameBoard.add(o);
		}
	}

	public void fillPlayerHand()
	{
		playerBoard.removeAll();
		playerBoard.setLayout(new GridLayout(1, s8w.getPlayer().getHand().size(), 1, 0));
		JButton jb;
		for(int i = 0; i < s8w.getPlayer().getHand().size(); i++)
		{
			jb = new CardShower(mainPane, s8w.getPlayer().getHand().get(i));
			jb.setActionCommand("" + i);
			jb.addActionListener(this);
			playerBoard.add(jb);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(gameIsOver)
		{
			return;
		}
		if(isEnemyTurn)
		{
			//It might be the time for the player to discard his card. Do the check, and if so, discard it.
			if(!isPlayAgain && s8w.getPlayer().needsToDiscard() && !isStartOfGame && ((JButton) e.getSource()).getBackground() == Color.WHITE)
			{
				int selectedCardLocation = Integer.parseInt(e.getActionCommand());

				//Discard the card from the player's hands - it shouldn't be shown on the board
				s8w.getPlayer().removeCardFromHand(
					((CardShower) playerBoard.getComponent(selectedCardLocation)).getCardBeingShown()
				);
				//Also, now the cardNo selections and the targets are now invalid, invalidate them
				s8w.finishSelection();

				//Update the hand of the player
				updatePlayerHand(selectedCardLocation);

				//full update
				removeHoverOverCard();
				deselectEverything();
				fillBoard();
				repaint();
				revalidate();
				allocateNextTurn();
			}
			else
			{
				if(((JButton) e.getSource()).getBackground() == Color.WHITE)
				{
					gameStatus.setText("Please select a card to discard.");
				}
				if(s8w.getPlayer().needsToDiscard())
				{
					gameStatus.setText("Your turn has ended! Please discard cards as required...");
				}
				else
				{
					gameStatus.setText("It's the opponent's turn! Please hold on...");
				}
			}
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

		if(source.getBackground() == Color.WHITE) //Player cards clicked
		{
			if(isStartOfGame)
			{
				//It is the start of the game, where players are setting the numbers on the board

				//Set the card and show the user that is has been selected
				deselectEverything();

				s8w.setCardNo(selectedCardLocation);
				((JButton) e.getSource()).setBorder(BorderFactory.createLoweredBevelBorder());
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

				//Set the required stuff
				s8w.setCardNo(s8w.getPlayer().getIndexOfCardInHand(((CardShower) playerBoard.getComponent(selectedCardLocation)).getCardBeingShown()));
				((JButton) e.getSource()).setBorder(BorderFactory.createLoweredBevelBorder());
				BaseCard cardBeingPlayed = s8w.getPlayer().getHand().get(s8w.getCardNo());

				//Run the card if possible
				if(cardBeingPlayed.getTotalTargetsRequired() == 0)
				{
					performCardAction(cardBeingPlayed);

					//Discard the card from the player's hands - it shouldn't be shown on the board
					s8w.getPlayer().removeCardFromHand(cardBeingPlayed);
					//Also, now the cardNo selections and the targets are now invalid, invalidate them
					s8w.finishSelection();

					//Update the hand of the player
					updatePlayerHand(selectedCardLocation);

					//full update
					removeHoverOverCard();
					deselectEverything();
					fillBoard();
					repaint();
					revalidate();

					if(isEnemyTurn)
					{
						new AppTimer(this, AppTimer.allocateNextTurn, 1000);
					}
					System.out.println("Player played card: " + cardBeingPlayed);
				}
			}
		}
		else
		{
			//Player has not selected a card
			if(s8w.getCardNo() == -1)
			{
				if(isStartOfGame)
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
					gameStatus.setText("Please select a card to play first!");
				}
			}
			else
			{
				if(isStartOfGame)
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
					int playerCardNo = Integer.parseInt(((JButton) playerBoard.getComponent(playerCardLocation)).getText());

					//Set to location
					((JButton) gameBoard.getComponent(selectedCardLocation)).setText("" + playerCardNo);
					gameBoard.getComponent(selectedCardLocation).setBackground(Color.GREEN);
					s8w.getBoard().setBoardNumber(selectedCardLocation, playerCardNo);
					s8w.getBoard().setStatus(selectedCardLocation, Side8Board.ALLY);

					//Remove the card
					s8w.getPlayer().getHand().remove(new StartCard(playerCardNo));
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
					s8w.addTargetSelection(selectedCardLocation);

					//Run the card if possible
					BaseCard cardBeingPlayed = s8w.getPlayer().getHand().get(s8w.getCardNo());
					if(cardBeingPlayed.getTotalTargetsRequired() == s8w.getTargetSelection().size())
					{
						if(!cardBeingPlayed.isValidTarget(s8w, s8w.getTargetSelection()))
						{
							gameStatus.setText("Invalid targets selected! Please try again.");
							s8w.clearSelection();
							return;
						}
						performCardAction(s8w.getPlayer().getHand().get(s8w.getCardNo()));
						//Discard the card from the player's hands - it shouldn't be shown on the board
						s8w.getPlayer().removeCardFromHand(cardBeingPlayed);
						//Also, now the cardNo selections and the targets are now invalid, invalidate them
						s8w.finishSelection();

						//Remove the card from the player's hand so that it's not shown
						//Find the location in terms of the playerBoard.
						int cardLocation = -1;
						for(int i = 0; i < playerBoard.getComponentCount(); i++)
						{
							if(playerBoard.getComponent(i) instanceof CardShower)
							{
								if(((CardShower) playerBoard.getComponent(i)).isCard(cardBeingPlayed))
								{
									cardLocation = i;
									break;
								}
							}
						}
						updatePlayerHand(cardLocation);

						//Update remaining stuff
						removeHoverOverCard();
						fillBoard();
						repaint();
						revalidate();
					}

					//Update final stuff
					deselectEverything();
					allocateNextTurn();
				}
			}
		}
	}

	public void performOpponentsTurn()
	{
		if(isStartOfGame)
		{
			int cardToUse = ((StartCard) s8w.getOpponent().getHand().get(0)).getThisNum();

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
				s8w.getBoard().setStatus(4, Side8Board.NEUTRAL);
				((JButton) gameBoard.getComponent(4)).setText("" + s8w.getBoard().getBoardNumber(4));

				//Remove the cards
				s8w.getPlayer().getHand().remove(new StartCard(playerCardNo));
				playerBoard.remove(s8w.getCardNo());
				playerBoard.add(new JPanel(), (int) s8w.getCardNo());
				s8w.getOpponent().getHand().remove(0);
				s8w.finishSelection();
				deselectEverything();

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
								s8w.getBoard().setBoardNumber(i, cardToUse);
								s8w.getBoard().setStatus(i, Side8Board.ENEMY);

								//Remove the card
								s8w.getOpponent().getHand().remove(0);
								break;
							}
						}
					}
				}
			}
			allocateNextTurn();
		}
		else
		{
			//Opponent should now play a card.

			//Play a random card
			Random random = new Random();
			int cardLocation = random.nextInt(s8w.getOpponent().getHand().size());
			BaseCard cardBeingPlayed = s8w.getOpponent().getHand().get(cardLocation);
			s8w.setCardNo(cardLocation);

			System.out.println("Opponent played card: " + cardBeingPlayed);

			//Play the card.
			ArrayList<Integer> targets = cardBeingPlayed.getAISelectionOfTargets(s8w);
			for(int aTarget : targets)
			{
				s8w.addTargetSelection(aTarget);
			}
			System.out.println("Targets: " + targets);
			performCardAction(cardBeingPlayed);
			//Now invalidate the targets
			s8w.finishSelection();

			//Discard the card from the opponent's hands - it shouldn't be shown on the board
			s8w.getOpponent().removeCardFromHand(cardBeingPlayed);

			//full update
			removeHoverOverCard();
			deselectEverything();
			fillBoard();
			repaint();
			revalidate();

			new AppTimer(this, AppTimer.allocateNextTurn, 1000);
		}
	}

	public void allocateNextTurn()
	{
		if(isStartOfGame)
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
					if(s8w.getCardNo() == -1)
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
					int playerCard = ((StartCard) s8w.getPlayer().getHand().get(0)).getThisNum();
					int opponentCard = ((StartCard) s8w.getOpponent().getHand().get(0)).getThisNum();
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
					isStartOfGame = false;
					//Remove the cards from the player's hands
					s8w.getPlayer().getHand().remove(0);
					s8w.getOpponent().getHand().remove(0);
					//Create the game cards and give them to the players

					//Perform the required layout changes (Changing the player's board to 5, etc, dealing the cards)
					playerBoard.removeAll();
					playerBoard.setLayout(new GridLayout(1, 5, 1, 0));

					//Deal out the cards to each player
					s8w.getPlayer().initDeck();
					s8w.getOpponent().initDeck();
					for(int i = 0; i < 5; i++)
					{
						s8w.getPlayer().draw();
						s8w.getOpponent().draw();
					}

					//Display this information on the board.
					JButton jb;
					for(int i = 0; i < 5; i++)
					{
						jb = new CardShower(mainPane, s8w.getPlayer().getHand().get(i));
						jb.setActionCommand("" + i);
						jb.addActionListener(this);
						playerBoard.add(jb);
					}

					//Run opponent's move if it's the opponent's turn, otherwise wait for user input.
					if(isEnemyTurn)
					{
						new AppTimer(this, AppTimer.performOpponentsTurn, 2000);
					}
					turnCounter = 1;
					playerHasPlayed = false;
					enemyHasPlayed = false;
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
			//Find out if it's the end of the player's turn, and force discard if it is
			if(!isPlayAgain)
			{
				if(isEnemyTurn)
				{
					//Get the player to discard a card
					if(s8w.getPlayer().needsToDiscard() && turnCounter < 10) //Don't discard cards on the last turn.
					{
						gameStatus.setText("Your turn has ended! Please discard cards as required...");
						//Do not allocate the enemy's turn.
						return;
					}
					else
					{
						s8w.endPlayerTurn();
						playerHasPlayed = true;
						if(!enemyHasPlayed || turnCounter < 10)
						{
							//Do not draw cards for the opponent if this is the very first time the opponent is playing;
							//since cards are already drawn at the start
							if(turnCounter != 1 || enemyHasPlayed)
							{
								s8w.startEnemyTurn();
							}
						}
					}
				}
				//End of enemy turn, does he need to discard?
				else
				{
					while(s8w.getOpponent().needsToDiscard())
					{
						s8w.getOpponent().getHand().remove(0);
					}
					s8w.endEnemyTurn();
					enemyHasPlayed = true;
					//Don't draw cards on the first turn.
					if(!playerHasPlayed || turnCounter < 10)
					{
						if(turnCounter != 1 || playerHasPlayed)
						{
							s8w.startPlayerTurn();
							fillPlayerHand();
							revalidate();
						}
					}
				}

				if(enemyHasPlayed && playerHasPlayed)
				{
					if(turnCounter == 10)
					{
						gameIsOver = true;
						//GAME ENDS!
						int playerCount = s8w.getBoard().getPlayerCount();
						int enemyCount = s8w.getBoard().getEnemyCount();
						if(playerCount - enemyCount > 0)
						{
							gameStatus.setText("<html><pre>The game is over!\nYour total: " + playerCount + "\tEnemy total: " + enemyCount + "\nYou win!</pre></html>");
						}
						else if(playerCount == enemyCount)
						{
							gameStatus.setText("<html><pre>The game is over!\nYour total: " + playerCount + "\tEnemy total: " + enemyCount + "\nIt's a draw!</pre></html>");
						}
						else
						{
							gameStatus.setText("<html><pre>The game is over!\nYour total: " + playerCount + "\tEnemy total: " + enemyCount + "\nYou lose!</pre></html>");
						}
						return;
					}
					else
					{
						System.out.println("Turn " + ++turnCounter + "/10");
						System.out.println("Cards left in deck: " + s8w.getPlayer().getDeck().size() + "(Player) " + s8w.getOpponent().getDeck().size() + "(Opponent)");
						enemyHasPlayed = false;
						playerHasPlayed = false;
					}
				}
			}
			//Gameplay, allocate the turn correctly.
			if(isEnemyTurn)
			{
				new AppTimer(this, AppTimer.performOpponentsTurn, 1000);
			}
			else
			{
				gameStatus.setText("It's now your turn! Pick a card to play...");
			}
		}
	}

	public void performCardAction(BaseCard cardToPlay)
	{
		//Format the board accordingly if required
		if(isEnemyTurn)
		{
			s8w.getBoard().setConverse();
		}

		isPlayAgain = cardToPlay.performAction(s8w, s8w.getTargetSelection());
		//Start the animations
		new Thread(cardToPlay).start();

		//Reset it back if required
		if(isEnemyTurn)
		{
			s8w.getBoard().clearConverse();
		}

		if(!isPlayAgain)
		{
			//False, the user cannot play again.
			isEnemyTurn = !isEnemyTurn;
		}
	}

	public void removeHoverOverCard()
	{
		//Remove the hover-over card (if any)
		for(Component aComponent : mainPane.getComponentsInLayer(JLayeredPane.POPUP_LAYER))
		{
			mainPane.remove(aComponent);
		}
	}

	public void updatePlayerHand(int cardUserPlayed)
	{
		//We want to update the hand. We want to leave a hole in the card the user played so that the ui looks nice and is smooth.
		//Firstly, get all the locations where it is a JPanel and not a real card
		ArrayList<Integer> locationsWhereJPanel = new ArrayList<>();
		for(int i = 0; i < playerBoard.getComponentCount(); i++)
		{
			if(playerBoard.getComponent(i) instanceof JPanel)
			{
				locationsWhereJPanel.add(i);
			}
		}

		//Also, the current one will be a JPanel
		locationsWhereJPanel.add(cardUserPlayed);
		Collections.sort(locationsWhereJPanel);

		//Then, remove everything. Set the new layout, and add the JPanel at the relevant locations, including the new one.
		playerBoard.removeAll();
		playerBoard.setLayout(new GridLayout(1, s8w.getPlayer().getHand().size() + locationsWhereJPanel.size(), 1, 0));
		int currentPlayerHandPointer = 0;
		int currentJPanelPointer = 0;
		JButton jb;
		for(int i = 0; i < ((GridLayout) playerBoard.getLayout()).getColumns(); i++)
		{
			if(i == locationsWhereJPanel.get(currentJPanelPointer))
			{
				playerBoard.add(new JPanel());
				//Only increment if it's not the last
				if(locationsWhereJPanel.size() - 1 != currentJPanelPointer)
				{
					currentJPanelPointer++;
				}
			}
			else
			{
				jb = new CardShower(mainPane, s8w.getPlayer().getHand().get(currentPlayerHandPointer++));
				jb.setActionCommand("" + i);
				jb.addActionListener(this);
				playerBoard.add(jb);
			}
		}
	}

	public void listenForInput()
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		boolean exit = false;
		boolean isDisposed = false;
		JButton jb;
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
						jb = new JButton("" + ((StartCard) s8w.getPlayer().getHand().get(i)).getThisNum());
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
					while(s8w.getPlayer().getHand().size() > 1)
					{
						s8w.getPlayer().getHand().remove(0);
					}
					while(s8w.getOpponent().getHand().size() > 1)
					{
						s8w.getOpponent().getHand().remove(0);
					}

					//Act as if it's the start of the game by ensuring that the playerboard has 7 components
					playerBoard.removeAll();
					playerBoard.setLayout(new GridLayout(1, 7, 1, 0));
					for(int i = 0; i < 6; i++)
					{
						playerBoard.add(new JPanel());
					}
					jb = new JButton("" + ((StartCard) s8w.getPlayer().getHand().get(0)).getThisNum());
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
					if(isDisposed)
					{
						System.out.println("lol");
						break;
					}
					this.getContentPane().repaint();
					System.out.println(userInput + " request sent.");
					break;
				case "revalidate":
					if(isDisposed)
					{
						System.out.println("lol");
						break;
					}
					this.getContentPane().revalidate();
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
				case "dump":
					dump();
					break;
				case "updateui":
					fillBoard();
					fillPlayerHand();
					System.out.println("Done.");
					break;
				case "test":
					JPanel jp = new JPanel();
					jp.setBackground(Color.RED);
					jp.setOpaque(true);
					jp.setBounds(100, 100, 100, 100);
					mainPane.add(jp, JLayeredPane.POPUP_LAYER);
					System.out.println("Done.");
					System.out.println(MouseInfo.getPointerInfo().getLocation());
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
					System.out.println("Accepted input: newgame, Randomboard, (un)instant, repaint/revalidate, dispose, dump, updateUI, new, restart/reboot, exit");
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
