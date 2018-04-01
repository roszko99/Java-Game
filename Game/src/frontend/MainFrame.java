package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import backend.framework.Updator;
import backend.user.Player;
import world.World;

/**
 * This class basically sets everything that the user sees and doesn't see, then hands everything to World.java to handle and run.
 * 
 * @author Owen Roszkowski
 */
public class MainFrame extends JFrame {

	/**
	 * I don't know what this means, but it gets rid of the soft error symbol.
	 */
	private static final long	serialVersionUID	= -2687968734727141535L;

	/**
	 * A constant representing the length of a side of the JPanel displaying the game. The JPanel is always a square.
	 */
	public static final int		FRAME_SIZE			= 600;

	/**
	 * The ScreenDrawer in charge of drawing all objects on screen.
	 */
	private ScreenDrawer		drawer;

	/**
	 * The Updator in charge of constantly running at a fixed rate (Updator.RUN_PERIOD) and calculating the positions, velocities, etc. of every GameObject in the world.
	 */
	private Updator				updator;

	/**
	 * The player being controlled by the user.
	 */
	private Player				p;

	/**
	 * Creates a new MainFrame and initializes all variables. Does not actually start any instance of Updator.java or ScreenDrawer.java, instead leaving that to World.java.
	 */
	public MainFrame() {
		World.frame = this;
		p = new Player(300, 300);
		drawer = new ScreenDrawer();
		updator = new Updator();
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				p.kbInput(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				p.clearKbInput(e.getKeyCode());
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
	}

	/**
	 * Creates a new MainFrame, adds a start button, and leaves the rest to World.java.
	 * 
	 * @param args - Not implemented in this program.
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame();
				initFrame(frame);
				initButtons(frame);
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Called from pushing the start button. Starts World.java going.
	 * 
	 * @param frame - The MainFrame object which needs to have the game started in. (Necessary due to this method being made static).
	 */
	private static void startGame(MainFrame frame) {
		World.player = frame.p;
		World.updator = frame.updator;
		World.drawer = frame.drawer;
		frame.drawer.setSize(FRAME_SIZE, FRAME_SIZE);
		frame.getContentPane().add(frame.drawer, BorderLayout.CENTER);
		World.incrementRound();
	}

	/**
	 * Creates a start button that starts the game when pressed.
	 * 
	 * @param frame - The MainFrame object to which the button is being added. (Necessary due to this method being made static).
	 */
	private static void initButtons(MainFrame frame) {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);
		JButton button = new JButton("Start");
		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.getContentPane().remove(button);
				startGame(frame);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});
		frame.getContentPane().add(button, BorderLayout.CENTER);
	}

	/**
	 * A void that simply initialized some variables. Only used to not have a wall of text in the MainFrame constructor.
	 * 
	 * @param frame - The MainFrame whose variables are being initialized. (Necessary due to this method being made static).
	 */
	private static void initFrame(MainFrame frame) {
		frame.setTitle("Eh");
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
