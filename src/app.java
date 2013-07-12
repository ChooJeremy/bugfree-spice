import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class app extends JFrame implements ActionListener
{
	private Container container;
	private JLabel gameStatus;
	private Container gameBoard, playerBoard;

	public app()
	{
		//Set the essentials
		setTitle("App");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//We wrap all our stuff in a JComponent with an empty border of 20px. This makes it such that the app has a magin.
		JComponent jc = (JComponent) this.getContentPane();
		jc.setLayout(new GridLayout(1, 1));
		jc.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		//Create the main container that will hold all our stuff
		container = new JPanel();
		jc.add(container);
		container.setLayout(new GridLayout(3, 1, 0, 5));

		//Top part, middle part, and bottom part
		gameStatus = new JLabel("Status stuff goes here", SwingConstants.CENTER);
		gameStatus.setFont(new Font("Calibri", Font.PLAIN, 20));
		gameStatus.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		gameStatus.setToolTipText("Status message");
		gameBoard = new JPanel();
		gameBoard.setLayout(new GridLayout(3, 3));
		JButton jb;
		for(int i = 0; i < 9; i++)
		{
			jb = new JButton("" + i);
			jb.setFont(new Font("Calibri", Font.PLAIN, 18));
			jb.addActionListener(this);
			gameBoard.add(jb);
		}
		gameBoard.setBackground(Color.CYAN);
		playerBoard = new JPanel();
		playerBoard.setLayout(new GridLayout(1, 7));
		for(int i = 0; i < 7; i++)
		{
			jb = new JButton("" + i);
			jb.setFont(new Font("Calibri", Font.PLAIN, 18));
			jb.setBackground(Color.WHITE);
			jb.addActionListener(this);
			playerBoard.add(jb);
		}
		playerBoard.setBackground(Color.MAGENTA);

		container.add(gameStatus);
		container.add(gameBoard);
		container.add(playerBoard);

		//Fill the board
		Side8Wrapper s8w = new Side8Wrapper();
		s8w.getBoard().createRandomBoard();
		fillBoard(s8w.getBoard());
	}

	public void fillBoard(Side8Board s8b)
	{
		int[][] board = s8b.getBoard();
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
		gameStatus.setText("<html><pre>" + s8b.getBoardView() + "</pre></html>");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("Action performed!");
		Side8Board sb = new Side8Board();
		sb.createRandomBoard();
		fillBoard(sb);
	}

	public static void main(String[] args)
	{
		System.out.println("Creating gui...");
		new app();
	}
}
