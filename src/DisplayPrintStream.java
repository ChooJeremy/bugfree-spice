import java.io.*;
import java.util.*;

public class DisplayPrintStream extends PrintStream
{
	PrintDisplayer mainDisplayer;

	public DisplayPrintStream(PrintDisplayer caller) throws FileNotFoundException
	{
		super("temp.txt");
		mainDisplayer = caller;
	}

	@Override
	public void println()
	{
		mainDisplayer.addText("\n");
	}

	@Override
	public void print(String s)
	{
		mainDisplayer.addText(s);
	}

	@Override
	public void println(String s)
	{
		mainDisplayer.addText(s + "\n");
	}

	@Override
	public void print(int i)
	{
		mainDisplayer.addText("" + i);
	}

	@Override
	public void println(int i)
	{
		mainDisplayer.addText("" + i + "\n");
	}
}
