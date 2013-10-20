import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;


public class JTextAreaInputStream extends InputStream
{
	byte[] contents;
	int pointer = 0;
	boolean okToRead;
	public final PrintDisplayer caller;

	public JTextAreaInputStream(final JTextArea text, final PrintDisplayer c)
	{
		okToRead = false;
		caller = c;
		text.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				if(e.getKeyChar()==KeyEvent.VK_ENTER)
				{
					contents = text.getText().getBytes();
					caller.addText(text.getText());
					pointer = 0;
					text.setText("");
					e.consume();
					okToRead = true;
				}
				super.keyReleased(e);
			}
		});
	}

	@Override
	public int read() throws IOException
	{
		if(pointer >= contents.length)
		{
			okToRead = false;
			return -1;
		}
		return this.contents[pointer++];
	}

	public boolean isOkToRead()
	{
		return okToRead;
	}
}