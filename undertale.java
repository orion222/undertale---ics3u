import javax.swing.*;

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




public class undertale extends JPanel implements KeyListener, MouseListener, Runnable{
	
	public static int counter;
	public static int gameState = 0;
	public static int eventState = 0;
	public static int mouseX;
	public static int mouseY;

	// images
	BufferedImage titleScreen;
	BufferedImage ruins1;
	
	
	BufferedImage fadeStart;
	BufferedImage fadeEnd;
	
	
	// position
	public static int ruinsX = 295;
	public static int ruinsY = 3603;

	
	// other
	animation animation = new animation(this);

	public undertale() {
		
		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		setPreferredSize(new Dimension(990, 615));  
		setBackground(new Color(0, 0, 0));
		
		// import images
		try {
			
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));
			ruins1 = ImageIO.read(new File("assets/ruins1.png"));
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
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animation.alpha));

		// fade
		
		if (animation.fading) {
			if (!animation.faded)
				g2d.drawImage(fadeStart, 0, 0, null);
			else
				g2d.drawImage(fadeEnd, 0, 0, null);

		}
		// the start menu
		else if (gameState == 0) {
			
			g2d.drawImage(titleScreen, 0, 0, null);

			
		}
		
		// exploration 
		else if (gameState == 1) {
			g2d.drawImage(ruins1, 0, 0, null);
			System.out.println("ruins rendering done");
			
		}				
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
				animation.fading = true;
				animation.wait = true;
				fadeStart = titleScreen;
				fadeEnd = ruins1;
				gameState = 1;
				animation.fade(fadeStart, fadeEnd, "slow");
				

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
