package backend.objects;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Defines an ImmovableObject that MovingObjects cannot intersect with.
 * 
 * @author Owen Roszkowski
 *
 */
public class ImmovableObject extends GameObject {

	
	/**
	 * Creates a new rectangular ImmovableObject with its upper-left corner at 0, 0 of length and width 0.
	 */
	public ImmovableObject() {
		super();
	}

	/**
	 * Creates a new rectangular ImmovableObject with the given characteristics.
	 * @param xPos - The x position of the upper-left corner of this ImmovableObject
	 * @param yPos - The y position of the upper-left corner of this ImmovableObject
	 * @param width - The width of this ImmovableObject
	 * @param height - The height of this ImmovableObject
	 */
	public ImmovableObject(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.drawRect(x, y, width, height);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
	}

}
