import java.io.InputStream;
import java.util.*;

public class JTextAreaInputStreamScanner
{
	Scanner scanner;
	JTextAreaInputStream jTextAreaInputStream;

	public JTextAreaInputStreamScanner(JTextAreaInputStream source)
	{
		scanner = new Scanner(source);
		jTextAreaInputStream = source;
	}

	public String nextLine()
	{
		waitForInput();
		return scanner.nextLine();
	}

	public int nextInt()
	{
		waitForInput();
		int answer = 0;
		try
		{
			answer = scanner.nextInt();
		}
		catch(Exception e)
		{
			jTextAreaInputStream.caller.addText(e.getMessage());
		}
		return answer;
	}

	public void waitForInput()
	{
		while(!jTextAreaInputStream.isOkToRead())
		{
			try{Thread.sleep(1000); } catch(Exception e) {}
		}
	}
}
