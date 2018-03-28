package backend.objects.npc;

import java.awt.Color;
import java.awt.Graphics;

import backend.objects.MovingObject;
import backend.user.Player;
import backend.utility.Vector;

/**
 * A circular projectile fired by instances of RangedEnemy.java.
 * 
 * @author Owen Roszkowski
 */
public class EnemyProjectile extends MovingObject {

	/**
	 * The width of all EnemyProjectiles.
	 */
	public static final int		WIDTH			= 10;

	/**
	 * The height of all EnemyProjectiles. Always equal to EnemyProjectile.WIDTH as this projectile is a circle.
	 */
	public static final int		HEIGHT			= WIDTH;

	/**
	 * The constant speed at which this projectile moves.
	 */
	public static final double	DEFAULT_SPEED	= Player.MAX_SPEED * 1.2;

	/**
	 * The amount of damage dealt to the player upon collision.
	 */
	public static final int		DAMAGE			= Player.MAX_HEALTH / 4;

	/**
	 * The color of this projectile.
	 */
	private static final Color	COLOR			= Color.BLACK;

	/**
	 * Creates a new projectile whose upper-left corner is at position (0, 0) which will constantly move in the given direction at a speed of EnemyProjectile.DEFAULT_SPEED.
	 * 
	 * @param direction - The direction in which this projectile will move
	 */
	public EnemyProjectile(Vector direction) {
		super();
		velocity = direction;
		velocity.setMagnitude(DEFAULT_SPEED);
	}

	/**
	 * Creates a new projectile at the given position with the given direction.
	 * 
	 * @param xPos - The x position of this EnemyProjectile's upper-left corner
	 * @param yPos - The y position of this EnemyProjectile's upper-left corner
	 * @param direction - The direction in which this projectile will move
	 */
	public EnemyProjectile(int xPos, int yPos, Vector direction) {
		super(xPos, yPos, WIDTH, HEIGHT);
		velocity = direction;
		velocity.setMagnitude(DEFAULT_SPEED);

	}

	/**
	 * Does nothing, as this MovingObject's velocity never changes.
	 */
	@Override
	public void updateVelocity() {
		return;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(COLOR);
		g.drawOval(getX(), getY(), width, height);
		g.fillOval(getX(), getY(), width, height);
	}

}
