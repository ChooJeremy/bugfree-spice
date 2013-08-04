import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TextShower extends JFrame
{
	private JTextArea textArea;

	public TextShower(String title)
	{
		setTitle(title);
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		this.add(textArea);
	}

	public void setText(String text)
	{
		textArea.setText(text);
		this.pack();
	}
}
