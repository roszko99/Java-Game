package frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;

import backend.framework.Updator;
import backend.objects.GameObject;
import backend.user.Player;
import world.World;

/**
 * Calls the <code>paint()</code> method of every GameObject in World.java's list of GameObjects and also draws the HUD. Tries to run every 5 ms, but may well go slower. This class has its own thread, so it must deal with some concurrency issues.
 * 
 * @author Owen Roszkowski
 */
public class ScreenDrawer extends JComponent implements Runnable {

	/**
	 * Again, I don't know what this does, but it doesn't break anything.
	 */
	private static final long	serialVersionUID	= -3109529532964673088L;

	/**
	 * How long the ScreenDrawer sleeps for before running again.
	 */
	private final long			DOWNTIME			= 5;

	/**
	 * Creates a new ScreenDrawer.
	 */
	public ScreenDrawer() {
	}

	/**
	 * When this component is painted, it simply paints everything onscreen, including game objects and the HUD.
	 */
	@Override
	public void paintComponent(Graphics g) {
		paintGameObjects(g);
		paintOverlay(g);
	}

	/**
	 * Calls the <code>paint()</code> method of each game object in World.java's list of game objects.
	 * 
	 * @param g - The Graphics object on which to draw the GameObjects
	 */
	private void paintGameObjects(Graphics g) {
		synchronized (Updator.class) {
			for (GameObject obj : World.getGameObjects()) {
				obj.paint(g);
			}
		}
	}

	/**
	 * Draws the player's health in the lower-left corner.
	 * 
	 * @param g - The Graphics object on which to draw the GameObjects
	 */
	private void paintOverlay(Graphics g) {
		g.setColor(Color.RED);
		Polygon p = new Polygon(new int[] { 5, 7, 12, 10 }, new int[] { MainFrame.FRAME_SIZE - 2, MainFrame.FRAME_SIZE - 12, MainFrame.FRAME_SIZE - 12, MainFrame.FRAME_SIZE - 2 }, 4);
		synchronized (Updator.class) {
			for (int n = 0; n < Player.MAX_HEALTH; n++) {
				if (n > 0) {
					p.translate(10, 0);
				}
				g.drawPolygon(p);
				if (n < World.player.getHealth()) {
					g.fillPolygon(p);
				}
			}
		}
	}

	/**
	 * Simply tries to call this Component's <code>repaint()</code> method every 5 ms.
	 */
	@Override
	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(DOWNTIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
