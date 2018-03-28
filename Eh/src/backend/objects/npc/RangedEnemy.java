package backend.objects.npc;

import java.awt.Color;
import java.awt.Graphics;

import backend.objects.MovingObject;
import backend.user.Player;
import backend.utility.Vector;
import world.World;

/**
 * An enemy that keeps its distance and fires at the player with round projectiles.
 * 
 * @author Owen Roszkowski
 */
public class RangedEnemy extends MovingObject {

	/**
	 * The constant number of pixels the RangedEnemy accelerates by per tick.
	 */
	private static final double	ACCELERATION	= Player.ACCELERATION * 0.5;

	/**
	 * The constant number of pixels moving counter to the acceleration of the RangedEnemy at any given time.
	 */
	private final static double	FRICTION		= ACCELERATION * 0.5;

	/**
	 * The constant maximum number of pixels a RangedEnemy may move per tick.
	 */
	private static final double	MAX_SPEED		= ACCELERATION * 4;

	/**
	 * The constant number of pixels a RangedEnemy is forced backwards by from each shot.
	 */
	private static final double	RECOIL			= ACCELERATION * 4;

	/**
	 * The largest distance, in pixels, from the player a RangedEnemy will accurately shoot from. RangedEnemies are essentially always trying to move to reach this point, then fire.
	 */
	public static final int		RANGE			= 300;

	/**
	 * The smallest distance, in pixels, from the player a RangedEnemy will get before fleeing and abandoning firing.
	 */
	public static final int		TOO_CLOSE		= RANGE - 100;

	/**
	 * The cooldown on the RangedEnemy's gun, in ticks.
	 */
	private static final int	COOLDOWN		= 75;

	/**
	 * A random shade of blue that is the color of this specific instance of RangedEnemy.
	 */
	private final Color			COLOR			= new Color((int) (66 + 89 * Math.random()), (int) (66 + 124 * Math.random()), 244);

	/**
	 * The width of the circle that this RangedEnemy's shape is.
	 */
	private static final int	WIDTH			= 20;

	/**
	 * The height of the circle that this RangedEnemy's shape is.
	 */
	private static final int	HEIGHT			= 20;

	/**
	 * The maximum health this enemy starts with and can have.
	 */
	public static final double	MAX_HEALTH		= 4;

	/**
	 * How many ticks this enemy cannot be hit by the player during after being hit. Used to get rid of player 'double taps'.
	 */
	public static final int		I_FRAMES		= 50;

	/**
	 * A Vector that always points at the player; where this enemy is aiming. Usually not visible to the user.
	 */
	private Vector				aim;

	/**
	 * A counter that keeps track of the cooldown on this enemy's gun, in ticks.
	 */
	private int					cdTick;

	/**
	 * A counter that keeps track of how long the enemy must stop moving before firing, in ticks.
	 */
	private int					freezeTick;

	/**
	 * How long the enemy is 'frozen' for. The maximum value <code>freezeTick</code> will assume in a firing cycle.
	 */
	private int					freezeLength;

	/**
	 * The current health of this enemy. Between 0 and RangedEnemy.MAX_HEALTH.
	 */
	private double				health;

	/**
	 * A counter that keeps track of how may frames of invulnerability this enemy has upon being hit.
	 */
	private int					iTicks;

	private short				colorTick;

	/**
	 * Creates a new RangedEnemy whose upper-left corner is at (0, 0) and whose width and height is 0.
	 */
	public RangedEnemy() {
		super();
		aim = new Vector();
		cdTick = 0;
		freezeTick = 0;
		freezeLength = 1;
		health = MAX_HEALTH;
		iTicks = 0;
		colorTick = 0;
	}

	/**
	 * Creates a new RangedEnemy whose upper-left corner is at the given position.
	 * 
	 * @param xPos - The x position of this RangedEnemy's upper-left corner.
	 * @param yPos - The y position of this RangedEnemy's upper-left corner.
	 */
	public RangedEnemy(double xPos, double yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
		aim = new Vector();
		cdTick = 0;
		freezeTick = 0;
		freezeLength = 1;
		health = MAX_HEALTH;
		iTicks = 0;
		colorTick = 0;
	}

	public void updateVelocity() {
		if (iTicks > 0) {
			iTicks--;
		}
		cdTick++;
		Vector distVector = this.getDistanceVector(World.player);
		if (frozen()) {
			;
		} else if (distVector.getMagnitude() > RANGE) {
			if (cdTick >= 1.33 * COOLDOWN) {
				freeze(COOLDOWN / 3);
			} else {
				Vector v = distVector.clone();
				v.setMagnitude(ACCELERATION);
				velocity.add(v);
			}
		} else if (distVector.getMagnitude() <= TOO_CLOSE) {
			if (cdTick > 2.33 * COOLDOWN) {
				freeze(COOLDOWN / 5);
			} else {
				Vector v = distVector.clone();
				v = v.opposite();
				v.setMagnitude(ACCELERATION);
				velocity.add(v);
			}
		} else if (cdTick >= 0.75 * COOLDOWN) {
			freeze(COOLDOWN / 4);
		}
		if (velocity.getMagnitude() > FRICTION)
			velocity.setMagnitude(velocity.getMagnitude() - FRICTION);
		else
			velocity.setMagnitude(0);
		if (velocity.getMagnitude() > MAX_SPEED)
			velocity.setMagnitude(MAX_SPEED);
	}

	/**
	 * Stops this enemy from moving for the given amount of ticks.
	 * 
	 * @param ticks - The amount of ticks for which this object is unable to move
	 */
	private void freeze(int ticks) {
		freezeTick = ticks;
		freezeLength = ticks;
	}

	/**
	 * Advances the freeze counter, unfreezing this enemy if it has been frozen long enough.
	 * 
	 * @return true if this RangedEnemy is still unable to move, false otherwise
	 */
	private boolean frozen() {
		freezeTick--;
		if (freezeTick == 0) {
			fire(0);
			cdTick = 0;
			return false;
		} else if (freezeTick <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Performs the appropriate set of actions to make this enemy fire a projectile.
	 * 
	 * @param angle - NOT IMPLEMENTED the magnitude of degrees from perfect aim the shot will deviate by
	 */
	private void fire(double angle) {
		aim = this.getDistanceVector(World.player);
		aim.setMagnitude(width * 0.5);
		aim.setDirection(aim.getDirection() + angle);
		World.addGameObject(new EnemyProjectile(getX() + width / 4, getY() + height / 4, aim.clone()));
		Vector v = aim.clone();
		v.setMagnitude(RECOIL);
		velocity.add(v.opposite());
	}

	@Override
	public void paint(Graphics g) {
		if (iTicks != 0 && iTicks % 10 == 0) {
			if (colorTick == 0) {
				g.setColor(new Color(255 - COLOR.getRed(), 255 - COLOR.getBlue(), 255 - COLOR.getGreen()));
				colorTick++;
			} else {
				g.setColor(COLOR);
				colorTick--;
			}
		} else {
			g.setColor(COLOR);
		}
		g.drawOval(getX(), getY(), width, height);
		g.fillOval(getX(), getY(), width, height);
		double progress = (double) freezeTick / freezeLength;
		if (progress != 1) {
			g.setColor(Color.BLACK);
			g.drawOval(getX() + (int) (width - width * progress) / 2, getY() + (int) (height - height * progress) / 2, (int) (width * progress), (int) (height * progress));
		}
		g.setColor(new Color(245, 20, 50));
	}

	/**
	 * Performs the appropriate actions (knockback, damage to health, etc.) when the player is hit by an enemy.
	 * 
	 * @param damage - The amount of damage to be subtracted from the player's current health
	 */
	public void hit(double damage) {
		if (iTicks > 0) {
			return;
		}
		health -= damage;
		if (health <= 0) {
			terminate();
		} else {
			iTicks = I_FRAMES;
		}
	}
}
