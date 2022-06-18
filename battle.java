import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;



// implement runnable for constant update in main file
public class battle extends JPanel implements Runnable {


    public static int key;
    public static int pointerX;
    public static int playerX = 500;
    public static int playerY = 400;
    public static int playerSpeed = 5;
    public static int bossHealth = 100;

    public static boolean up = false;
    public static boolean down = false;
    public static boolean right = false;
    public static boolean left = false;
    
	public static File path1 = new File("assets/battleImages/menus");
	public static File[] menuFiles = path1.listFiles();
	
	public static File path3 = new File("assets/battleImages/options");
	public static File[] selectionFiles = path3.listFiles();
	
	public static int num = 0;
	public static undertale.corner[] slashCoords = new undertale.corner[4];
	public static boolean slash = false;
	public static boolean slashDraw = false;
	
	public static ArrayList<dimension> gameBounds = new ArrayList<dimension>();
    
    public static boolean battling = true;
	public static int menuState = 1;
    public static int selectionState = 1;
    public static int health = 50;
    public static int textState = 1;
    public static int heals = 1;
    
    public static undertale.corner[] optionPos = new undertale.corner[4];
	public static Font font;
	public static Font damageFont;
	
	// When user runs out of hp
	public static int x = 0;
	public static boolean dead = false;

	// Bar
	public static int barX = 60;
	public int count = 0;
	public static boolean stopBar = false;
	
	// Attacks
	public static int damage;
	public static boolean playHit = false;
	
	public static int attack = 1;
	public static int variation;
	
	public static int boneHeight = 100;
	public static int boneWidth = 15;
	public static undertale.corner[] bonePositions = new undertale.corner[11];
	
	public static undertale.corner[] bonePositions2 = new undertale.corner[12];
	public static boolean[] bonesReleased;
	public static LinkedList<Integer> boneOrder = new LinkedList<Integer>();
	public static int startBone;
	public static int endBone;
    
    
	public undertale game;
	public battle(undertale e){
		 game = e;
	}
	
	audio audio = new audio(this);
	
    public battle() {
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));
        Thread thread = new Thread(this);
        thread.start();
        
        slashCoords[0] = new undertale.corner(350, 0);
        slashCoords[1] = new undertale.corner(350, 40);
        slashCoords[2] = new undertale.corner(340, 70);
        slashCoords[3] = new undertale.corner(350, 70);

		try {
		     font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);
		     damageFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(50f);

		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(font);
		} catch (IOException | FontFormatException e) {
		    e.printStackTrace();
		}
	
		//use the font
		this.setFont(font);
		
		optionPos[1] = new undertale.corner(235, 539);
		optionPos[2] = new undertale.corner(442, 539);
		optionPos[3] = new undertale.corner(650, 539);
		
		gameBounds.add(new dimension(new undertale.corner(310, 295), new undertale.corner(655,445)));
    }

    public void paintComponent(Graphics g) {}


    public void run() {
    	while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// bug where if u hold down a movement key and ur hp drops to 0
			// the next time u continue, it will automatically move ur character
			// in that direction in ur next run
    		if (menuState == 3) { //  if you are fighting then we need this 
				if (up && withinBounds(playerX, playerY - playerSpeed, game.gameBounds)) {
		    		playerY -= playerSpeed;
		    		System.out.println("roman bukd");
		    		
		    	}
		    	if (down && withinBounds(playerX, playerY + playerSpeed, game.gameBounds)) {
		    		playerY += playerSpeed;
		    		System.out.println("roman bukd2");
		    		
		    	}
		    	if (left && withinBounds(playerX - playerSpeed, playerY, game.gameBounds)) {
		    		playerX -= playerSpeed;
		    		System.out.println("roman bukd3");
		    	}
		    	if (right && withinBounds(playerX + playerSpeed, playerY, game.gameBounds)) {
		    		playerX += playerSpeed;
		    		System.out.println("roman bukd4");
		    	}
		    	System.out.println("PLAYERX : " + playerX + " PLAYERY : " + playerY);
		    	if (attack == 1) {
		    		for (int i = 1; i < 11; i++) {
		    			undertale.corner cur = game.bonePositions[i];
		    			if (collision(new undertale.corner(playerX, playerY), new undertale.corner(playerX + 25, playerY + 25), new undertale.corner(cur.x, cur.y), new undertale.corner(cur.x + boneWidth, cur.y + boneHeight))) {
		            		health -= 1;
		            		
		            		// player has died
		            		if (health == 0) {
		            			menuState = 4;
		            			
		            		}
		    			}
		    		}
		    		game.updateFirstAttack();
		    		if (variation == 1) {
			    		if (game.bonePositions[10].x < 150) {
			    			menuState = 1;
			    			up = false;
			    			down = false;
			    			left = false;
			    			right = false;
			    			playerX = 500;
			    			playerY = 400;
			    			attack ++;
			    		}
		    		}
		    		
		    		else if (variation == 2) {
		    			if (game.bonePositions[10].x > 850) {
		    				menuState = 1;
		    				up = false;
			    			down = false;
			    			left = false;
			    			right = false;
			    			playerX = 500;
			    			playerY = 400;
			    			attack++;
		    			}
		    		}
		    		
		    	}
		    	else if (attack == 2) {
		    		for (int i = 1; i < 11; i++) {
		    			undertale.corner cur = game.bonePositions2[i];
		    			
		    			// the width for a horizontal bone is 70
		    			// the height for a horizontal one is the width of bone 1
		    			if (collision(new undertale.corner(playerX, playerY), new undertale.corner(playerX + 25, playerY + 25), new undertale.corner(cur.x, cur.y), new undertale.corner(cur.x + 70, cur.y + boneWidth))) {
		            		health -= 1;
		            		
		            		// player has died
		            		if (health == 0) {
		            			menuState = 4;
		            			
		            		}
		    			}
		    		}
		    		game.updateSecondAttack();
		    		
		    		if (variation == 1) {
		    			if (game.bonePositions2[endBone].x < 100) {
			    			menuState = 1;
			    			up = false;
			    			down = false;
			    			left = false;
			    			right = false;
			    			playerX = 500;
			    			playerY = 400;
			    			attack --;
			    		}
		    		}
		    		
		    		else if (variation == 2){
		    			if (game.bonePositions2[endBone].x > 900) {
			    			menuState = 1;
			    			up = false;
			    			down = false;
			    			left = false;
			    			right = false;
			    			playerX = 500;
			    			playerY = 400;
			    			attack --;
			    		}
		    		}
		    		
		    	}
		    	
		    	
		    	
		    	
    		}
    		if(slash) {
        		game.repaint();
        		try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		num++;
        		System.out.println("yo");
        		slashDraw = true;
    			
    		}
    	
    	game.repaint();	
    	}
    }
    	
	public static class dimension {
		public undertale.corner topLeft;
		public undertale.corner bottomRight;
		public dimension(undertale.corner topLeft, undertale.corner bottomRight) {
			this.topLeft = topLeft;
			this.bottomRight = bottomRight;
		}
	}
	
	public static boolean withinBounds(int x, int y, ArrayList<undertale.dimension> gameBounds2) {
		for (undertale.dimension cur: gameBounds2) { // for every boundary in the current setting
			// check if within top left undertale.corner
			undertale.corner topL = cur.topLeft;
			undertale.corner bottomR = cur.bottomRight;
			if (topL.x <= x && x <= bottomR.x && topL.y <= y && y <= bottomR.y) {
				return true;
			}

		}
		return false;
	}
	
    public boolean collision(undertale.corner playerTopLeft, undertale.corner playerBottomRight, undertale.corner objectTopLeft, undertale.corner objectBottomRight) {
		//System.out.println(topL1.x  + " < " + topL2.x + " < " + bottomR1.x + "  OR  " + topL1.x + " < " + bottomR2.x + " < " + bottomR1.x);
		//System.out.println(topL1.y  + " < " + topL2.y + " < " + bottomR1.y + "  OR  " + topL1.y + " < " + bottomR2.y + " < " + bottomR1.y);

    	if (playerTopLeft.y < objectBottomRight.y && playerBottomRight.y > objectTopLeft.y) {
        	if (playerTopLeft.x < objectBottomRight.x && playerBottomRight.x > objectTopLeft.x) {
        		return true;
        	}

    	}
    	return false;
    }
    
    public static void main(String[] args) {}

	public void resetStats() {
		health = 50;
		bossHealth = 100;
		playerX = 500;
		playerY = 400;
		menuState = 1;
		selectionState = 1;
		textState = 1;
		attack = 1;
		damage = 0;
		x = 0;
		up = false;
		down = false;
		left = false;
		right = false;
	}
}