import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class PrintDisplayer extends JFrame implements KeyListener
{
	public final int INPUT_HEIGHT = 60;

	private JTextArea input;
	private JTextArea output;
	private JScrollPane outputScroller;

	public PrintDisplayer()
	{
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		revalidate();
		repaint();

		output = new JTextArea(){     /*
			@Override
			public void setText(String s)
			{
				//http://stackoverflow.com/questions/6366776/how-to-count-the-number-of-lines-in-a-jtextarea-including-those-caused-by-wrapp
				//Calculate a line's height
				FontMetrics fontMetrics = getFontMetrics(getFont());
				int lineHeight = fontMetrics.getAscent() + fontMetrics.getDescent();
				//http://stackoverflow.com/questions/17005336/count-number-of-lines-in-jtextarea-given-set-width
				//Calculate the total
			}                           */
		};
		input = new JTextArea();
		outputScroller = new JScrollPane();
		//outputScroller.createVerticalScrollBar();
		outputScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		outputScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		//Locations
		output.setSize(this.getSize().width - 20, this.getSize().height - INPUT_HEIGHT);
		outputScroller.setSize(this.getSize().width - 20, this.getSize().height - INPUT_HEIGHT);
		input.setSize(this.getSize().width - 20, INPUT_HEIGHT);
		output.setLocation(0, 0);
		outputScroller.setLocation(0, 0);
		input.setLocation(0, this.getSize().height - INPUT_HEIGHT);

		output.setFont(new Font("Dialog", Font.PLAIN, 15));
		input.setFont(new Font("DialogInput", Font.PLAIN, 15));

		output.setEditable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);

		input.setBackground(Color.LIGHT_GRAY);
		input.addKeyListener(this);

		Container c = new JLayeredPane();
		setContentPane(c);
		//outputScroller.add(output);
		c.add(output);
		c.add(input);

		revalidate();
		repaint();

		PrintStream printStream = null;
		try
		{
			printStream = new DisplayPrintStream(this);
		}
		catch(FileNotFoundException f)
		{
			System.err.println("An error occurred: " );
			System.err.print(f.getLocalizedMessage());
		}
		System.setOut(printStream);
		IndianPoker.main(new String[]{});
	}

	public void addText(String s)
	{
		output.setText(output.getText() + s);
		revalidate();
		repaint();
	}

	public static void main(String[] args)
	{
		new PrintDisplayer();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		//Listen for enter events, and if so, send input
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			try
			{
				addText(input.getDocument().getText(0, input.getDocument().getLength()) + "\n");
			}
			catch(BadLocationException b)
			{
				addText("\n" + b.getMessage() + "\n");
			}
			input.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		//Ignore enters
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			e.consume();
			input.setText("");
		}
	}
}
