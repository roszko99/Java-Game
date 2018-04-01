package backend.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import backend.utility.Vector;
import world.World;

/**
 * Describes how a typical object in the game environment should look and act.
 * 
 * @author Owen Roszkowski
 */
public abstract class GameObject {

	/**
	 * X position of upper left corner
	 */
	protected int			x;

	/**
	 * Y position of upper left corner
	 */
	protected int			y;

	/**
	 * The width of this GameObject's hitbox.
	 */
	protected int			width;

	/**
	 * The height of this GameObject's hitbox.
	 */
	protected int			height;

	/**
	 * NOT IMPLEMENTED
	 */
	protected BufferedImage	sprite;

	/**
	 * Creates a new GameObject of width, height, x position, and y position all of 0.
	 */
	public GameObject() {
		x = y = width = height = 0;
		sprite = null;
	}

	/**
	 * Creates a new GameObject with the given characteristics.
	 * 
	 * @param xPos - The x position of this GameObject's upper-left corner.
	 * @param yPos - The y position of this GameObject's upper-left corner.
	 * @param width - The width of this GameObject's hitbox.
	 * @param height - The height of this GameObject's hitbox.
	 */
	public GameObject(int xPos, int yPos, int width, int height) {
		x = xPos;
		y = yPos;
		this.width = width;
		this.height = height;
		sprite = null;
	}

	/**
	 * Draws this GameObject in it's own specific way on the given Graphics context.
	 * 
	 * @param g - The Graphics upon which this GameObject is to be drawn
	 */
	public abstract void paint(Graphics g);

	/**
	 * Kills this GameObject and removes it from the world.
	 */
	public void terminate() {
		World.removeGameObject(this);
	}

	/*
	 * GETTERS & SETTERS
	 */

	/**
	 * Gets x position of upper left corner
	 * 
	 * @return this object's x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets y position of upper left corner
	 * 
	 * @return this object's y position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets this GameObject's upper-left corner's coordinate to the given x value.
	 * 
	 * @param newX - The x position to move this object to
	 */
	public void setX(int newX) {
		x = newX;
	}

	/**
	 * Sets this GameObject's upper-left corner's coordinate to the given y value.
	 * 
	 * @param newX - The y position to move this object to
	 */
	public void setY(int newY) {
		y = newY;
	}

	/**
	 * NOT IMPLEMENTED
	 */
	public BufferedImage getSprite() {
		return sprite;
	}

	/**
	 * Gets the rectanglular hitbox of this GameObject.
	 * 
	 * @return a new java.awt.Rectangle which has this GameObject's width and height
	 */
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), width, height);
	}

	/**
	 * Gets this GameObject's width.
	 * 
	 * @return this GameObject's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets this GameObject's height.
	 * 
	 * @return this GameObject's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the distance from this GameObject to another.
	 * 
	 * @param obj - The other object to find this GameObject's distance from
	 * @return a Vector pointing from the other object to this one
	 */
	public Vector getDistanceVector(GameObject obj) {
		return new Vector(obj.getX() + obj.width / 2 - this.getX() - this.width / 2, obj.getY() + obj.height / 2 - this.getY() - this.height / 2);
	}
}
