package frontend;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A class that is used to add a counter to the screen between rounds so the player doesn't have to instantly react to newly-spawned enemies. This class has its own thread, so it must deal with some concurrency issues.
 * 
 * @author Owen Roszkowski
 */
public class CountdownDrawer extends ScreenDrawer {

	/**
	 * Dunno
	 */
	private static final long	serialVersionUID	= 4129532775556215921L;

	/**
	 * How long this CountdownDrawer counts down for, in seconds.
	 */
	private int					countdown;

	/**
	 * Creates a new CountdownTimer that counts down from the given time to 0.
	 * 
	 * @param time - The time, in seconds, that this CountdownTimer will count down
	 */
	public CountdownDrawer(int time) {
		countdown = time;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(countdown), 300, 300);
	}

	/**
	 * Repaints this component with the updated remaining time, then sleeps for 1 second.
	 */
	@Override
	public void run() {
		while (countdown > 0) {
			repaint();
			countdown--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
