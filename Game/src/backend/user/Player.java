package backend.user;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import backend.objects.MovingObject;
import backend.utility.Vector;
import world.World;

/**
 * This class represents the character controlled by the user. It handles keyboard input and basically has the whole game running around it, so it's a somewhat hefty class.
 * 
 * @author Owen Roszkowski
 */
public class Player extends MovingObject {

	/**
	 * The constant number of pixels the player accelerates by per tick.
	 */
	public final static double	ACCELERATION		= 0.7;
	/**
	 * The constant number of pixels moving counter to the acceleration of the player at any given time.
	 */
	public final static double	FRICTION			= ACCELERATION * 0.5;

	/**
	 * The constant maximum number of pixels a player may move per tick.
	 */
	public final static double	MAX_SPEED			= ACCELERATION * 4;
	/**
	 * A reduced MAX_SPEED. It is a constant representing how fast the player moves during its attack animation, in pixels.
	 */
	public final static double	REDUCED_MAX_SPEED	= MAX_SPEED * 0.66;
	/**
	 * The maximum health this character has and starts with.
	 */
	public final static int		MAX_HEALTH			= 4;

	/**
	 * The width of this character's model.
	 */
	public final static int		WIDTH				= 30;

	/**
	 * The height of this character's model.
	 */
	public final static int		HEIGHT				= 30;

	/**
	 * The number of ticks which an enemy can intersect with the player's melee attack and take damage. AKA the lifetime of the player's melee attack.
	 */
	private final int			MELEE_HIT_LENGTH	= 10;

	/**
	 * An array of 1's and 0's which represent if a button is depressed. It follows the format: { Up, Down, Left, Right, Melee Attack, Ranged Atttack (NOT IMPLEMENTED) }
	 */
	private short[]				input;

	/**
	 * The current health of the player. Always less than or equal to MAX_HEALTH.
	 */
	private int					health;

	/**
	 * A boolean representing if the player is currently in its attack animation or not.
	 */
	private boolean				meleeAttacking;

	/**
	 * A counter that keeps track of the cooldown on the player's melee attack.
	 */
	private int					melTick;

	/**
	 * A reference to the MeleeAttack object of this player. It is null for the most part, and only points to an object when meleeAttacking is true.
	 */
	private MeleeAttack			meleeAttack;

	/**
	 * Creates a new player with its upper-left corner at 0, 0 of width and height Player.WIDTH and Player.HEIGHT.
	 */
	public Player() {
		super(0, 0, WIDTH, HEIGHT);
		input = new short[6];
		health = MAX_HEALTH;
		meleeAttacking = false;
		meleeAttack = null;
	}

	/**
	 * Creates a new player with its upper-left corner at the given x and y coordinates, of width and height Player.WIDTH and Player.HEIGHT.
	 * 
	 * @param xPos - The x position the player should be placed at.
	 * @param yPos - The y position the player should be placed at.
	 */
	public Player(double xPos, double yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
		input = new short[4];
		health = MAX_HEALTH;
		meleeAttacking = false;
		meleeAttack = null;

	}

	/**
	 * Updates the player's velocity based on current directional keyboard input, as well as melee attacking status.
	 */
	public void updateVelocity() {
		if (meleeAttacking) {
			melTick--;
			if (melTick > 0) {
				velocity.add(new Vector(((int) input[3] - (int) input[2]) * ACCELERATION, ((int) input[1] - (int) input[0]) * ACCELERATION));
				if (velocity.getMagnitude() > REDUCED_MAX_SPEED)
					velocity.setMagnitude(REDUCED_MAX_SPEED);
			} else {
				meleeAttacking = false;
				World.removeGameObject(meleeAttack);
				meleeAttack = null;
			}
		} else {
			velocity.add(new Vector(((int) input[3] - (int) input[2]) * ACCELERATION, ((int) input[1] - (int) input[0]) * ACCELERATION));
			if (velocity.getMagnitude() > MAX_SPEED)
				velocity.setMagnitude(MAX_SPEED);
		}

		if (velocity.getMagnitude() > FRICTION)
			velocity.setMagnitude(velocity.getMagnitude() - FRICTION);
		else
			velocity.setMagnitude(0.0);
	}

	/**
	 * Sets the status of the given key to 'released' thus removing it from consideration when updating the player's status.
	 * 
	 * @param key - The key code of the newly-released key (from KeyEvent.getKeyCode())
	 */
	public void clearKbInput(int key) {
		switch (key) {
		case KeyEvent.VK_W:
			input[0] = 0;
			break;
		case KeyEvent.VK_A:
			input[2] = 0;
			break;
		case KeyEvent.VK_S:
			input[1] = 0;
			break;
		case KeyEvent.VK_D:
			input[3] = 0;
			break;
		}
	}

	/**
	 * Sets the status of the given key to 'pressed' thus making it be considered when updating the player's status.
	 * 
	 * @param key - The key code of the newly-released key (from KeyEvent.getKeyCode())
	 */
	public void kbInput(int key) {
		switch (key) {
		case KeyEvent.VK_W:
			input[0] = 1;
			break;
		case KeyEvent.VK_A:
			input[2] = 1;
			break;
		case KeyEvent.VK_S:
			input[1] = 1;
			break;
		case KeyEvent.VK_D:
			input[3] = 1;
			break;
		case KeyEvent.VK_J:
			if (meleeAttacking == false) {
				meleeAttacking = true;
				meleeAttack = new MeleeAttack(getX(), getY());
				World.addGameObject(meleeAttack);
				melTick = MELEE_HIT_LENGTH;
			}
			break;
		}
	}

	/**
	 * NOT IMPLEMENTED
	 */
	public void mouseInput(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON2:
			break;
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect(getX(), getY(), width, height);
		g.fillRect(getX(), getY(), width, height);
	}

	/**
	 * Gets the current health of the player.
	 * 
	 * @return the player's current health. Between 0 and Player.MAX_HEALTH
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Performs the appropriate actions (knockback, damage to health, etc.) when the player is hit by an enemy.
	 * 
	 * @param damage - The amount of damage to be subtracted from the player's current health
	 */
	public void hit(int damage) {
		health -= damage;
		if (health <= 0)
			terminate();
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
		if (meleeAttacking)
			meleeAttack.updatePosition(getX(), getY());
	}

}
