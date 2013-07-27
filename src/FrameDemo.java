import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FrameDemo extends JFrame
{
	public FrameDemo()
	{
		//Set the essentials
		setTitle("App");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		JPanel jp = new JPanel();
		jp.setBackground(Color.RED);
		jp.setLayout(null);
		jp.setOpaque(true);
		this.getContentPane().add(jp);
		jp.setBounds(100, 100, 100, 100);

		JButton jb = new JButton("A Button");
		jb.setLayout(null);
		jb.setOpaque(true);
		this.getContentPane().add(jb);
		jb.setBounds(300, 100, 100, 100);
	}

	public static void main(String[] args)
	{
		new FrameDemo();
	}
}
