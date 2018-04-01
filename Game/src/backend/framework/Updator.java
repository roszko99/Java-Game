package backend.framework;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.TimerTask;

import backend.objects.GameObject;
import backend.objects.ImmovableObject;
import backend.objects.MovingObject;
import backend.objects.npc.EnemyProjectile;
import backend.objects.npc.RangedEnemy;
import backend.user.MeleeAttack;
import backend.user.Player;
import backend.utility.Vector;
import world.World;

/**
 * Updates position, velocity, etc. data for every object. As it is a TimerTask, it needs a Timer object to run it periodically. Each execution of this class's <code>run()</code> method is one 'tick'. At a run period (Updator.RUN_PERIOD) of 10 ms,
 * the game runs at 100 ticks per second. This class has its own thread, so it must deal with some concurrency issues.
 * 
 * @author Owen Roszkowski
 */
public class Updator extends TimerTask {

	/**
	 * The length of time this class's <code>run()</code> method is allowed to run for, in milliseconds.
	 */
	public static final long RUN_PERIOD = (long) (10);

	/**
	 * Creates a new Updator. It is only necessary to instantiate this object to restart the Timer.
	 */
	public Updator() {
	}

	/**
	 * Goes through World.java's list of GameObjects and calls their <code>updatePosition()</code> and <code>updateVelocity()</code> functions, as well as deals with object interactions (running into walls, mostly, which is not at all perfect right
	 * now).
	 */
	@Override
	public void run() {
		synchronized (this) {
			ArrayList<GameObject> gameObjects = (ArrayList<GameObject>) World.getGameObjects();
			boolean allDead = true;
			for (GameObject gObj : gameObjects) {
				if (gObj.getClass().equals(RangedEnemy.class))
					allDead = false;
				if (!MovingObject.class.isInstance(gObj))
					continue;
				else {
					MovingObject mover = (MovingObject) gObj;
					mover.updateVelocity();
					for (GameObject obj : gameObjects) {
						if (obj.equals(gObj))
							continue;
						Rectangle intersection = obj.getBounds().intersection(mover.getBounds());
						if (intersection.width > 0 && intersection.height > 0) {
							if (ImmovableObject.class.isInstance(obj)) {
								// System.out.println("Intersection\t" + intersection.width + " " + intersection.height);

								/*
								 * This code just keeps you in one place when you hit a wall, regardless of input. Vector correctingV = mover.getVelocity().opposite(); correctingV.setMagnitude(intersection.width /
								 * Math.cos(Math.atan2(mover.getVelocity().getYComponent(), mover.getVelocity().getXComponent()))); mover.getVelocity().add(correctingV);
								 */
								if (EnemyProjectile.class.isInstance(mover)) {
									mover.terminate();
								} else if (intersection.width > intersection.height) {
									mover.getVelocity().add(new Vector(0, (mover.getVelocity().getYComponent() > 0) ? -intersection.height : intersection.height));
								} else {
									mover.getVelocity().add(new Vector((mover.getVelocity().getXComponent() > 0) ? -intersection.width : intersection.width, 0));
								}
							} else if (EnemyProjectile.class.isInstance(mover) && Player.class.isInstance(obj)) {
								mover.terminate();
								((Player) obj).hit(EnemyProjectile.DAMAGE);
							} else if (RangedEnemy.class.isInstance(mover) && MeleeAttack.class.isInstance(obj)) {
								// TODO: add knockback
								((RangedEnemy) mover).hit(MeleeAttack.DAMAGE);
							}
						}
						// System.out.println("Position\t" + mover.getTrueX() + " " + mover.getTrueY());
					}
					mover.updatePosition();
				}
			}
			if (allDead) {
				World.incrementRound();
			}
		}
	}

}
