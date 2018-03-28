package backend.utility;

/**
 * A utility class that represents a simple vector comprised of an x and y component.
 * 
 * @author Owen Roszkowski
 */
public class Vector {

	/**
	 * The x component of this vector.
	 */
	private double	xComponent;

	/**
	 * The y component of this vector.
	 */
	private double	yComponent;

	/**
	 * Creates a new vector with x and y components of 0.
	 */
	public Vector() {
		xComponent = yComponent = 0;
	}

	/**
	 * Creates a new vector with the given x and y components.
	 * 
	 * @param x - This vector's x component
	 * @param y - This vector's y component
	 */
	public Vector(double x, double y) {
		xComponent = x;
		yComponent = y;
	}

	/**
	 * Adds the given vector to this one.
	 * 
	 * @param v - The vector whose components are to be added to this one's.
	 */
	public void add(Vector v) {
		xComponent += v.getXComponent();
		yComponent += v.getYComponent();
	}

	/**
	 * Gets a vector whose direction is opposite of this one, but whose magnitude is the same.
	 * 
	 * @return a new Vector with opposite components of this one
	 */
	public Vector opposite() {
		return new Vector(-xComponent, -yComponent);
	}

	/*
	 * GETTERS & SETTERS
	 */

	/**
	 * Gets this vector's x component.
	 * 
	 * @return the x component of this vector
	 */
	public double getXComponent() {
		return xComponent;
	}

	/**
	 * Gets this vector's y component.
	 * 
	 * @return the y component of this vector
	 */
	public double getYComponent() {
		return yComponent;
	}

	/**
	 * Gets the magnitude of this vector.
	 * 
	 * @return the magnitude of this vector, found using Pythagoras' theorem
	 */
	public double getMagnitude() {
		return Math.sqrt(xComponent * xComponent + yComponent * yComponent);
	}

	/**
	 * Maintains the direction of this vector, yet changes its magnitude.
	 * 
	 * @param mag - The new magnitude of this vector
	 */
	public void setMagnitude(double mag) {
		double theta = Math.atan2(yComponent, xComponent);
		xComponent = mag * Math.cos(theta);
		yComponent = mag * Math.sin(theta);
	}

	/**
	 * Gets the direction of this vector.
	 * 
	 * @return a double representing
	 */
	public double getDirection() {
		return Math.atan2(yComponent, xComponent);
	}

	/**
	 * Maintains the magnitude of this vector, yet changes its direction.
	 * 
	 * @param theta - The new angle, in radians, of this vector
	 */
	public void setDirection(double theta) {
		double m = getMagnitude();
		xComponent = m * Math.cos(theta);
		yComponent = m * Math.sin(theta);
	}

	@Override
	public Vector clone() {
		return new Vector(xComponent, yComponent);
	}

}
