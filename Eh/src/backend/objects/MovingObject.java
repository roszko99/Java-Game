package backend.objects;

import backend.utility.Vector;

/**
 * Describes how a typical object that can have a velocity should act in the game's envirnoment.
 * 
 * @author Owen Roszkowski
 */
public abstract class MovingObject extends GameObject {

	/**
	 * The 'true' x position of this object. This variable is of double precision, but GameObject.java's default x position is only of int precision.
	 */
	protected double	trueX;
	/**
	 * The 'true' y position of this object. This variable is of double precision, but GameObject.java's default x position is only of int precision.
	 */
	protected double	trueY;

	/**
	 * A Vector representing the change in position, in pixels, of this MovingObject per tick.
	 */
	protected Vector	velocity;

	/**
	 * Creates a new MovingObject whose upper-left corner is at (0, 0) and whose width and height is 0.
	 */
	public MovingObject() {
		super();
		trueX = trueY = 0;
		velocity = new Vector();
	}

	/**
	 * Creates a MovingObject with the given characteristics.
	 * 
	 * @param xPos - The x position of this MovingObject's upper-left corner
	 * @param yPos - The y position of this MovingObject's upper-left corner
	 * @param w - The width of this MovingObject
	 * @param h - The height of this MovingObject
	 */
	public MovingObject(double xPos, double yPos, int w, int h) {
		super((int) xPos, (int) yPos, w, h);
		trueX = xPos;
		trueY = yPos;
		velocity = new Vector();
	}

	/*
	 * GETTERS & SETTERS
	 */

	/**
	 * Similar to GameObject.java's <code>setX()</code>, but with double precision.
	 * 
	 * @param d - The new 'true' x position of this MovingObject
	 */
	public void setX(double d) {
		trueX = d;
	}

	/**
	 * Similar to GameObject.java's <code>setX()</code>, but with double precision.
	 * 
	 * @param d - The new 'true' x position of this MovingObject
	 */
	public void setY(double d) {
		trueX = d;
	}

	@Override
	public void setX(int newX) {
		trueX = newX;
	}

	@Override
	public void setY(int newY) {
		trueY = newY;
	}

	/**
	 * Updates this MovingObject's position based on its current velocity. Usually, this method is only called by Updator.java or by this class's <code>updateVelocity()</code> function.
	 */
	public void updatePosition() {
		trueX += velocity.getXComponent();
		trueY += velocity.getYComponent();
	}

	/**
	 * Gets this MovingObject's current velocity.
	 * 
	 * @return a Vector representing this MovingObject's current velocity
	 */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * Sets this MovingObject's velocity to be a given one.
	 * 
	 * @param v - the Vector to set as this MovingObject's new velocity.
	 */
	public void setVelocity(Vector v) {
		velocity = v;
	}

	/**
	 * Similar to GameObject.java's <code>getX()</code> but with double precision.
	 * 
	 * @return this MovingObject's 'true' x position
	 */
	public double getTrueX() {
		return trueX;
	}

	/**
	 * Similar to GameObject.java's <code>getY()</code> but with double precision.
	 * 
	 * @return this MovingObject's 'true' Y position
	 */
	public double getTrueY() {
		return trueY;
	}

	@Override
	public int getX() {
		return (int) trueX;
	}

	@Override
	public int getY() {
		return (int) trueY;
	}

	/**
	 * Updates this MovingObject's velocity in its own specific way.
	 */
	public abstract void updateVelocity();

}
