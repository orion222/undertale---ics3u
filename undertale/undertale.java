import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.*;




public class undertale extends JPanel implements KeyListener, MouseListener, Runnable, ActionListener{
	
	public static int counter;
	public static int gameState = 0;
	public static int eventState = 0;
	public static int mouseX;
	public static int mouseY;
	public static float alpha = 1.0f;
	
	// -1 = no fade, 1 = fade in, 2 = fade out
	public static boolean fading = false;
	public static boolean faded = false;
	public static int fade = -1;
	public static int fadeSpeed = 1;
	
	// images
	BufferedImage titleScreen;
	BufferedImage ruins;
	
	// position
	public static int ruinsX = 295;
	public static int ruinsY = 3603;
	
	// time
	Timer timer = new Timer(20, this);
	
	public undertale() {
		
		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		setPreferredSize(new Dimension(990, 615));  
		setBackground(new Color(0, 0, 0));
		
		// import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));
			ruins = ImageIO.read(new File("assets/ruins.png"));
		}
		catch (Exception e) {
			System.out.println("Image does not exist");
		}
		
		// create thread
		Thread thread = new Thread(this);
		thread.start();
		
		// add listeners
		addKeyListener(this);
		this.setFocusable(true);
		addMouseListener(this);
		
		// other
			

	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		undertale panel = new undertale();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// the start menu
		if (gameState == 0) {
			
			// if the fade animation is finished
			if (alpha == 0) {
				try {
					Thread.sleep(3000);
					gameState = 1;
					repaint();
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if (fading) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
				g2d.drawImage(titleScreen, 0, 0, null);
			}
			
			else {
				g.drawImage(titleScreen, 0, 0, null);

			}
		}
		
		// exploration 
		else if (gameState == 1) {
			System.out.println(counter);
			counter++;
			if (!faded) { 
				System.out.println("start fade");
				faded = true;
				timer.start();
			}
			
			else if (fading) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
				g2d.drawImage(ruins, ruinsX, ruinsY, null);
				System.out.println("ruins");
			}
			
			else {
				g.drawImage(ruins, ruinsX, ruinsY, null);
				System.out.println("ruins rendering done");
				g.drawImage(titleScreen, 0, 0, null);
				
				// this is weird. it can draw the title screen but can't 
				// draw ruins.png
				// im pretty sure the coordinates for ruins.png gives a valid location tho
				// some fault with the picture

			}
		}
			
			
				
			
	}
	
		
			
	
	
	public void fadeOut() {
		alpha -= 0.01 * fadeSpeed;
		if (alpha < 0) {
			alpha = 0;
			timer.stop();
			fade = 1;
		}
		repaint();
	}
	
	public void fadeIn() {	
		alpha += 0.01 * fadeSpeed;
		if (alpha > 1) {
			alpha = 1;
			timer.stop();
			fade = -1;
			fading = false;
		}
		repaint();
	}


	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		mouseX = e.getX();
		mouseY = e.getY();
		if (gameState == 0) {
			
			// play game
			if (370 <= mouseX && mouseX <= 670 && 185 <= mouseY && mouseY <= 270) {
				System.out.println("play");
				fading = true;
				fade = 2;
				timer.start();  // the timer calls the actionListener method

			}
			
			// about 
			else if (370 <= mouseX && mouseX <= 670 && 300 <= mouseY && mouseY <= 390) {
				System.out.println("about");
				
				// change to about screen
				eventState = 2;


			}
			
			else if (370 <= mouseX && mouseX <= 670 && 420 <= mouseY && mouseY <= 505) {
				System.out.println("quit");
				System.exit(0); // terminates the program
			}
		}
		
		repaint();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (fade == 1) fadeIn();
		else if (fade == 2) fadeOut();
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void run() {
		
	}

	
	
	
	
///////////////////////////////////////////////////////	
	
	
	
	// useless methods
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
