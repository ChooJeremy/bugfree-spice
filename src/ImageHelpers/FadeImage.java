package ImageHelpers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class that reads in an image and displays it for a brief period of time. The image slowly fades out over the time specified.
 *
 * To use, pass in the image that you plan on using and the duration into the construction.
 * Add this JPanel to the component you want to show the image on.
 * Then, call (new Thread(var)).start();.
 *
 */
public class FadeImage extends JPanel implements Runnable
{
	public static int STEPS = 100;

	private Image image;
	private long duration;
	private float alpha;
	private boolean started;

	public FadeImage(String imagePath, long d)
	{
		try
		{
			image = ImageIO.read(new File(imagePath));
		}
		catch(IOException ex)
		{
			throw new RuntimeException(ex);
		}
		new BufferedImage()
		duration = d;
		started = false;
	}

	public int getImageHeight()
	{
		return image.getHeight(null);
	}

	public int getImageWidth()
	{
		return image.getWidth(null);
	}

	@Override
	public void run()
	{
		started = true;
		alpha = 0;
		long pauseTime = duration / STEPS;
		for(int i = 0; i < STEPS; i++)
		{
			alpha += 1.0 / STEPS;
			repaint();
			try
			{
				Thread.sleep(pauseTime);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		//Once done, remove this from the parent
		this.getParent().remove(this);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(started)
		{
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(ac);
			g2d.drawImage(image, 0, 0, null);
			Toolkit.getDefaultToolkit().sync();
		}
	}
}
