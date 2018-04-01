package backend.user;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import backend.objects.GameObject;

/**
 * A GameObject that represents the melee attack arc of the player. It is not a MovingObject, as it cannot really have a velocity, yet it has an <code>updatePosition()</code> function to keep it at the same position relative to the player from which
 * it originated.
 * 
 * @author Owen Roszkowski
 */
public class MeleeAttack extends GameObject {

	/**
	 * The amount of damage the melee attack deals.
	 */
	public static final double DAMAGE = 2.3;

	/**
	 * Creates a MeleeAttack object at the given position.
	 * 
	 * @param xPos - the x position of the point of the cone of this attack arc
	 * @param yPos - the y position of the point of the cone of this attack arc
	 */
	MeleeAttack(int xPos, int yPos) {
		super(xPos, yPos, 50, 10 + Player.HEIGHT);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawArc(getX() - 10, getY() - 15, 50, 10, 180, -180);
		g.fillArc(getX() - 10, getY() - 15, 50, 10, 180, -180);
		g.fillPolygon(new int[] { getX() - 10, getX() + Player.WIDTH / 2, getX() + Player.WIDTH + 10 }, new int[] { getY() - 10, getY() + Player.HEIGHT / 2, getY() - 10 }, 3);
	}

	/**
	 * Similar to MovingObject.java's <code>updatePosition()</code>, because this class is meant to be paired with the player instance.
	 * 
	 * @param xPos - The current x position of the player this attack originated from
	 * @param yPos - The current y position of the player this attack originated from
	 */
	public void updatePosition(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x - width / 4, y - 10, width, 10);
	}

}
