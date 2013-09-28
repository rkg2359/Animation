import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class ComplexAnimationSwingApp extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4248154113117801830L;
	int x = 0;
	int y = 214;
	int velocityX;
	int velocityY;
	Timer timer = new Timer(10, this);
	DrawPanel draw = new DrawPanel();

	JButton walk = new JButton("Walk");
	JButton stop = new JButton("Stop");
	Boolean isWalking = false;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ComplexAnimationSwingApp gui = new ComplexAnimationSwingApp();

			}
		});
	}

	public ComplexAnimationSwingApp() {

		JFrame frame = new JFrame();
		frame.addKeyListener(new ListenToKeys());
		frame.setFocusable(true);
		frame.setFocusTraversalKeysEnabled(false);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(draw);

		frame.setSize(500, 530);
		frame.setVisible(true);
		playSound("Play");
	}

	public void jump() {
		y = y - velocityY;

		draw.repaint();
	}

	public void run() {

		x = x + velocityX;

		if (x > 500 && velocityX > 0) {
			x = -100;
			// velocityX = 2;
			x = x + velocityX;
		} else if (x < -30 && velocityX < 0) {
			x = 600;
			// velocityX = 2;
			x = x + velocityX;
		}
		draw.repaint();
		System.out.println("Hello");

	}

	public synchronized void playSound(final String url) {
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless
					// it blocks on the Clip finishing, see
					// comments
					public void run() {
						try {
							Clip clip = AudioSystem.getClip();
							AudioInputStream inputStream = AudioSystem
									.getAudioInputStream(ComplexAnimationSwingApp.class
											.getResourceAsStream("fonkdongo - plomo_2.mp3"
													+ url));
							clip.open(inputStream);
							clip.start();
						} catch (Exception e) {
							System.err.println(e.getMessage());
						}
					}
				}).start();
	}

	// public void stop() {
	// // velocityX = 0;
	// x = x + velocityX;
	// if (x > 500) {
	// x = -100;
	// // velocityX = 0;
	// x = x + velocityX;
	// }
	// }

	class DrawPanel extends JPanel {

		private Image[] images = new Image[3];

		private int current = 0;

		public Image getNextImage() {
			if (current == images.length) {
				current = 0;
			}
			return images[current++];
		}

		public void paintComponent(Graphics g) {

			timer.start();
			Color lightBlue = new Color(135, 206, 250);
			Color grassGreen = new Color(34, 139, 34);
			Color gold = new Color(255, 215, 0);

			g.setColor(lightBlue);
			g.fillRect(0, 0, this.getWidth(), 450);
			g.setColor(gold);
			g.fillOval(430, -20, 100, 100);
			g.setColor(grassGreen);
			g.fillRect(0, 320, this.getWidth(), 150);
			images[0] = new ImageIcon("walk1.png").getImage();
			images[1] = new ImageIcon("walk2.png").getImage();
			images[2] = new ImageIcon("walk3.png").getImage();
			Image manWalking = new ImageIcon(getNextImage()).getImage();
			Image cloud1 = new ImageIcon("cloud.png").getImage();
			Image cloud2 = new ImageIcon("cloud.png").getImage();
			Image manStill = new ImageIcon("walk2.png").getImage();
			g.drawImage(cloud1, 15, 10, this);

			// Image cloud1 = new ImageIcon("cloud.png").getImage();
			try {
				if (isWalking == true)
					g.drawImage(manWalking, x, y, this);
				else if (isWalking == false)
					g.drawImage(manStill, x, y, this);
			} catch (Exception e) {

			}

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		// velocityX = 10;
		// isWalking = true;
		// play();

	}

	class ListenToKeys implements KeyListener {
		private final Set<Integer> pressed = new HashSet<Integer>();

		public synchronized void keyPressed(KeyEvent e) {
			pressed.add(e.getKeyCode());

			if (pressed.contains(KeyEvent.VK_UP)
					&& pressed.contains(KeyEvent.VK_RIGHT)) {
				repaint();
				velocityX = 10;
				velocityY = -10;
				isWalking = true;
				run();
				jump();

			}

			if (pressed.contains(KeyEvent.VK_UP)
					&& pressed.contains(KeyEvent.VK_LEFT)) {
				repaint();
				velocityX = -10;
				velocityY = 10;
				isWalking = true;
				jump();
				run();

			}

			if (pressed.contains(KeyEvent.VK_RIGHT)) {
				repaint();
				velocityX = 10;
				isWalking = true;
				run();

			}

			if (pressed.contains(KeyEvent.VK_LEFT)) {
				velocityX = -10;
				isWalking = true;
				run();

			}
			if (pressed.contains(KeyEvent.VK_UP)) {
				velocityY = 10;
				isWalking = true;
				jump();

			}

		}

		public void keyReleased(KeyEvent e) {
			if (pressed.contains(KeyEvent.VK_UP)) {
				velocityY = 0;
				isWalking = true;
				jump();

			}
			pressed.remove(e.getKeyCode());
		}

		public void keyTyped(KeyEvent e) {
		}

	}
}
