package world;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Timer;

import backend.framework.Updator;
import backend.objects.GameObject;
import backend.objects.ImmovableObject;
import backend.objects.npc.RangedEnemy;
import backend.user.Player;
import frontend.CountdownDrawer;
import frontend.MainFrame;
import frontend.ScreenDrawer;

/**
 * This class should not be instantiated. All methods needed are static, to ensure that only one World ever exists. This class basically takes control of everything from MainFrame.java and runs things by incrementing rounds, spawning enemies, and
 * starting and stopping the game engine's updator. This class has its own thread, so it must deal with some concurrency issues.
 * 
 * @author Owen Roszkowski
 */
public class World {
	/**
	 * A list of all GameObjects currently in the world.
	 */
	private static ArrayList<GameObject>	gameObjects	= new ArrayList<GameObject>();

	/**
	 * An lock object meant solely to deal with concurrency issues of the gameObjects list.
	 */
	private static final Object				lock1		= new Object();

	/**
	 * A reference to the Player object in the world. There should only be one ever.
	 */
	public static Player					player;

	/**
	 * A reference to the Updator currently being run repeatedly. Since it is a TimerTask, this reference will change many times to different instances of Updator.java.
	 */
	public static Updator					updator;

	/**
	 * A reference to the ScreenDrawer object which is in charge of drawing everything onscreen (aka everything in gameObjects).
	 */
	public static ScreenDrawer				drawer;

	/**
	 * A reference to the MainFrame class. Needed for adding and removing CountdownTimers
	 */
	public static MainFrame					frame;

	/**
	 * A number representing what round the game is currently on. Right now, it just spawns <code>round + 1</code> enemies every new round.
	 */
	public static int						round		= 0;

	/**
	 * A reference to the Timer object which continually runs an Updator instance. Necessary for stopping and starting the Updator's looping.
	 */
	public static Timer						timer;

	/**
	 * Gets all GameObjects in the world at the moment. This includes things like walls, the player, enemies, and projectiles.
	 * 
	 * @return an ArrayList of type GameObject which contains references to all onscreen objects
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<GameObject> getGameObjects() {
		synchronized (lock1) {
			return (ArrayList<GameObject>) gameObjects.clone();
		}
	}

	/**
	 * Removes an object from existence in the world.
	 * 
	 * @param obj - The GameObject to be removed
	 */
	public static void removeGameObject(GameObject obj) {
		synchronized (lock1) {
			gameObjects.remove(obj);
		}
	}

	/**
	 * Adds an object to the world to be painted onscreen.
	 * 
	 * @param obj - The GameObject to be added
	 */
	public static synchronized void addGameObject(GameObject obj) {
		synchronized (lock1) {
			gameObjects.add(obj);
		}
	}

	/**
	 * Changes the current round from <code>round</code> to <code>round + 1</code>, and spawns <code>round + 1</code> enemies. It then stops the game's refreshing and starts a five-second countdown until resuming play.
	 */
	public static void incrementRound() {
		if (round == 0) {
			addGameObject(player);
			addGameObject(new ImmovableObject(0, 0, MainFrame.FRAME_SIZE, 20));
			addGameObject(new ImmovableObject(0, MainFrame.FRAME_SIZE - 20, MainFrame.FRAME_SIZE, 20));
			addGameObject(new ImmovableObject(MainFrame.FRAME_SIZE - 20, 0, 20, MainFrame.FRAME_SIZE));
			addGameObject(new ImmovableObject(0, 0, 20, MainFrame.FRAME_SIZE));
			CountdownDrawer cd = new CountdownDrawer(5);
			spawn("rangedEnemy", round + 1);
			drawer.update(drawer.getGraphics());
			frame.getContentPane().add(cd, BorderLayout.CENTER);
			frame.getContentPane().setComponentZOrder(cd, 0);
			frame.getContentPane().setComponentZOrder(drawer, 1);
			frame.validate();
			frame.pack();

			Thread cdThread = new Thread(cd);
			try {
				cdThread.start();
				cdThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frame.getContentPane().remove(cd);
			new Thread(drawer).start();
			timer = new Timer("Updator Timer");
			timer.scheduleAtFixedRate(updator, 0, Updator.RUN_PERIOD);

		} else {
			timer.cancel();
			timer = new Timer("Updator Timer");
			CountdownDrawer cd = new CountdownDrawer(5);
			frame.getContentPane().add(cd, BorderLayout.CENTER);
			frame.getContentPane().setComponentZOrder(cd, 0);
			frame.pack();

			new Thread(drawer).start();
			Thread cdThread = new Thread(cd);
			try {
				cdThread.start();
				cdThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frame.getContentPane().remove(cd);
			spawn("rangedEnemy", round + 1);
			updator = new Updator();
			timer.schedule(updator, 0, (long) (10));
		}
		round++;
	}

	/**
	 * Adds a new object to the world.
	 * 
	 * @param type - A String representing the type of GameObject to be added
	 * @param number - The number of this type of GameObject to add.
	 */
	public static synchronized void spawn(String type, int number) {
		// TODO: make this spawn a specific type of thing
		for (int i = 0; i < number; i++) {
			if (type.equals("rangedEnemy")) {
				boolean flag = false;
				RangedEnemy enemy = null;
				while (flag == false) {
					enemy = new RangedEnemy(50 + Math.random() * 500, 50 + Math.random() * 500);
					flag = true;
					for (GameObject gObj : gameObjects) {
						if (RangedEnemy.class.isInstance(gObj) && gObj.getDistanceVector(enemy).getMagnitude() < 50) {
							flag = false;
						}
					}
				}
				addGameObject(enemy);
			}
		}
	}
}
