import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class PrintDisplayer extends JFrame
{
	public final int INPUT_HEIGHT = 60;

	private JTextArea input;
	private JTextArea output;
	private JScrollPane outputScroller;
	private JTextAreaInputStream inputStream;

	public PrintDisplayer()
	{
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		revalidate();
		repaint();

		output = new JTextArea(){
			@Override
			public void setText(String s)
			{
				//http://stackoverflow.com/questions/6366776/how-to-count-the-number-of-lines-in-a-jtextarea-including-those-caused-by-wrapp
				//Calculate a line's height
				FontMetrics fontMetrics = getFontMetrics(getFont());
				int lineHeight = fontMetrics.getAscent() + fontMetrics.getDescent();
				//http://stackoverflow.com/questions/17005336/count-number-of-lines-in-jtextarea-given-set-width
				//For every line, Split the string into words, check if it exceeds the allowed width, add 1 if so
				String[] lines = s.split("\n");
				String[] words;
				boolean firstWord;
				String sentence;
				int totalLines = 0;
				for(String aLine : lines)
				{
					totalLines++;
					words = aLine.split(" ");
					sentence = "";
					firstWord = true;
					for(String aWord : words)
					{
						sentence += aWord + (firstWord ? "" : " ");
						firstWord = false;
						if(fontMetrics.stringWidth(sentence) > this.getWidth())
						{
							totalLines++;
							sentence = "";
							firstWord = true;
						}
					}
				}
				int finalTextHeight = lineHeight * totalLines;
				if(finalTextHeight > this.getHeight())
				{
					//Remove the first line and call this method again
					int firstNewLine = s.indexOf("\n");
					setText(s.substring(firstNewLine + 1, s.length()));
				}
				else
				{
					super.setText(s);
				}
			}
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
		//Key listener for input is added in the creation code
		inputStream = new JTextAreaInputStream(input, this);

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
		JTextAreaInputStreamScanner scanner = new JTextAreaInputStreamScanner(inputStream);

		IndianPoker.setScanner(scanner);
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
}
