import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class app extends JFrame implements ActionListener
{
	private Container container;
	private JLabel gameStatus;
	private Container gameBoard, playerBoard;
	private Side8Wrapper s8w;
	private boolean isEnemyTurn;

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
		gameBoard.setLayout(new GridLayout(3, 3));
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
		playerBoard.setLayout(new GridLayout(1, 7));
		for(int i = 0; i < 7; i++)
		{
			jb = new JButton("" + i);
			jb.setFont(new Font("Calibri", Font.PLAIN, 18));
			jb.setBackground(Color.WHITE);
			jb.setActionCommand("" + i);
			jb.addActionListener(this);
			playerBoard.add(jb);
		}

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
		isEnemyTurn = false;
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
				//It is the start of the game, where players are setting the number cards
				s8w.setCardNo(selectedCardLocation);
				gameStatus.setText("Card selected: " + source.getText());
				System.out.println("Please select a place on the board to send your card to.");
			}
			else
			{
				//Game cards are being played
				System.out.println("Game cards are being played!");
			}
		}
		else
		{
			if(s8w.getCardNo() == null)
			{
				gameStatus.setText("Please select a card to use first!");
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
					//Or something tha thas already been selected
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
					new AppTimer(this, "performOpponentsTurn", 2000);
					isEnemyTurn = true;
				}
				else
				{

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
		else
		{
			//Make a move
		}
		isEnemyTurn = false;
		dump();
	}

	public void dump()
	{
		System.out.println(s8w.getEverythingInString());
		gameStatus.setText("<html><pre>" + s8w.getEverythingInString() + "</pre></html>");
	}

	public static void main(String[] args)
	{
		System.out.println("Creating gui...");
		new app();
	}
}
