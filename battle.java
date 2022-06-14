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
import javax.swing.JFrame;
import javax.swing.JPanel;



// implement runnable for constant update in main file
public class battle extends JPanel implements Runnable, KeyListener {


    public static int key;
    public static int pointerX;
    public static int playerX = 500;
    public static int playerY = 400;
    public static int playerSpeed = 5;
    public static BufferedImage player;
    public static BufferedImage brokenHeart;
    public static int bossHealth = 100;

    
    public static boolean up = false;
    public static boolean down = false;
    public static boolean right = false;
    public static boolean left = false;
    
    public static BufferedImage[] menuImages = new BufferedImage[4];
	public static File path1 = new File("assets/battleImages/menus");
	public static File[] menuFiles = path1.listFiles();
	
	
    public static BufferedImage[] selectionImages = new BufferedImage[4];
	public static File path3 = new File("assets/battleImages/options");
	public static File[] selectionFiles = path3.listFiles();
	
	public static ArrayList<dimension> gameBounds = new ArrayList<dimension>();
    
    public static boolean battling = true;
	public static int menuState = 1;
    public static int selectionState = 1;
    public static int health = 50;
    public static int textState = 1;
    public static int heals = 1;
    
    public static corner[] optionPos = new corner[4];
	public static Font font;
	public static Font damageFont;
	
	
	public static BufferedImage slider;
	public static BufferedImage bar;

	public static BufferedImage BD;
	public static BufferedImage gameOver;
	public static int attack = 1;
	public static int variation;

	public static int barX = 60;
	public int counter = 0;
	public static boolean stopped = false;
	public static int damage;
	
	
	// attacks
	
	public static int boneHeight = 100;
	public static int boneWidth = 15;
	public static BufferedImage bone;
	public static BufferedImage bone2;
	public static corner[] bonePositions = new corner[11];
	
	public static corner[] bonePositions2 = new corner[12];
	public static boolean[] bonesReleased;
	public static LinkedList<Integer> boneOrder = new LinkedList<Integer>();
	public static int startBone;
	public static int endBone;


    
    
	public undertale game;
	public battle(undertale e){
		
		 game = e;
	}
	
    public battle() {
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));
		addKeyListener(this);
		this.setFocusable(true);
        Thread thread = new Thread(this);
        thread.start();
        
        
        
        
        try {
			menuImages[1] = ImageIO.read(new File("assets/battleImages/menus/menu1.png"));
			menuImages[2] = ImageIO.read(new File("assets/battleImages/menus/menu2.png"));
			menuImages[3] = ImageIO.read(new File("assets/battleImages/menus/menu3.png"));
			selectionImages[1] = ImageIO.read(new File("assets/battleImages/options/option1.png"));
			selectionImages[2] = ImageIO.read(new File("assets/battleImages/options/option2.png"));
			selectionImages[3] = ImageIO.read(new File("assets/battleImages/options/option3.png"));   
			player = ImageIO.read(new File("assets/battleImages/characters/player.png"));
			bar = ImageIO.read(new File("assets/battleImages/menus/bar.jpg"));
			bone = ImageIO.read(new File("assets/battleImages/attacks/bone.png"));
			bone2 = ImageIO.read(new File("assets/battleImages/attacks/bone2.png"));
			BD = ImageIO.read(new File("assets/battleImages/characters/boss.png"));
			brokenHeart = ImageIO.read(new File("assets/battleImages/characters/deadPlayer.png"));
			gameOver = ImageIO.read(new File("assets/battleImages/menus/menu4.png"));
        }
        
        catch (Exception e) {
        	System.out.println("image does not exist");
        }
        

		try {
		     font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);
		     damageFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(50f);

		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(font);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
	
		//use the font
		this.setFont(font);
		
		optionPos[1] = new corner(235, 539);
		optionPos[2] = new corner(442, 539);
		optionPos[3] = new corner(650, 539);
		
		gameBounds.add(new dimension(new corner(310, 295), new corner(655,445)));
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
    	if (battling) {
    		
    		g.setColor(new Color(255, 255, 255));
    		g.setFont(font);
    		
    		// menu background
    		g.drawImage(menuImages[menuState], 0, 0, null);
    		g.drawImage(BD, 430, 85, null);
    		
    		// stats
    		g.drawString(health + "", 345, 518);
    		g.drawString("50", 425, 518);
    		g.drawString(heals + "", 645, 518);
    		g.drawString("5", 710, 518);
    		g.drawString("BOSS HEALTH: ", 385, 75);
    		g.drawString(bossHealth + "", 575, 75);

  
    		// if u lose or win
    		if (menuState > 3) {
    			if (menuState == 4) {
    				super.paintComponent(g);
    				g.drawImage(brokenHeart, playerX, playerY, null);

        			try {
    					Thread.sleep(2000);
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}

        		}
    		
    			else if (menuState == 5) {
    				
    			}
    			
    			
    		}
    		else {

	    		// highlight selection
	    		if (menuState == 1) {
	    			g.setFont(font);
	    			if (textState == 1) {
	    				g.drawString("What will Chara do?", 75, 360);
	    			}
	    			else if (textState == 2) {
	    				if (health == 50)
		    				g.drawString("You are MAX HP!", 75, 360);

	    				else if (heals == 0) 
		    				g.drawString("You are out of heals!", 75, 360);
	    					
	    				else 
		    				g.drawString("You healed 10 HP! You have " + heals + " left", 75, 360);
	    				
	    	
	    			}
	    			else if (textState == 3) {
	    				g.drawString("You fled the scene" , 75, 360);

	
	    			}
	    			g.drawImage(selectionImages[selectionState], optionPos[selectionState].x, optionPos[selectionState].y, null);
	    		}
	    		
	    		else if (menuState == 2) {
	    			if(counter == 0) {
	    				counter = 1;
	    				barX = 60;
	    				damage = 0;
	    				stopped = false;
	    			}
	        		if(stopped) {
	        			try {
	        				System.out.println("damaged");
	        				Thread.sleep(1500);
	        			} 
	        			catch (InterruptedException e1) {
	        				e1.printStackTrace();
	        			}
	        			
	        			if (Math.random() >= 0.50)
	        				variation = 1;
	        			else variation = 2;
	        			System.out.println("variation = " + variation);
	        			
	        			if (attack == 1) {
	        				firstAttack();
	        			}
	        			else if (attack == 2) {
	        				secondAttack();
	        			}
	        			menuState = 3;
	        				

	        			
	        			
	    			}
	        		else {
		        		g.drawImage(bar, barX, 314, null);
		        		barX += 20;
		        		if(barX >= 940 || damage >= 1) {
		        			g.drawString("MINUS " + damage + " HP", 620, 150);
		        			stopped = true;
		        		}
	        		}
	    		}
	    		else if (menuState == 3) {
	    			g.drawImage(player, playerX, playerY, null);    			
	
	    			if (attack == 1) {
	    				for (int i = 1; i < 11; i++) {
	    					corner cur = bonePositions[i];
	    					if (297 < cur.x && cur.x < 670) {
	    						g.drawImage(bone, cur.x, cur.y, null);
	    					}
	    				}
	    			}
	    			else if (attack == 2) {
	    				for (int i = 1; i < 11; i++) {
	    					corner cur = bonePositions2[i];
	    					if (variation == 1) {
		    					if (cur.x < 680) {
		    						g.drawImage(bone2, cur.x, cur.y, null);
		    					}
	    					}
	    					else {
	    						if (cur.x > 297) {
		    						g.drawImage(bone2, cur.x, cur.y, null);
	    						}
	    					}
	    				}
	    			}
	    			//g.drawImage(bone, 680, 295, null);
	    		}
    		}
    		
    	
    		
    	}

    }
    
    public void keyPressed(KeyEvent e) 
    {
    	int xe = e.getKeyCode();

    	if (menuState == 1) {
    		
    		// start state
    		if (textState == 1) {
	    		
	    		// left 
				if (xe == 37) { 
					if (selectionState - 1 > 0) {
						selectionState --;
	
					}
				}
				
				// right
				else if (xe == 39) {
					if (selectionState + 1 < 4) {
						selectionState ++;
	
					}
					
				}
	    		// if the key is z, then select
				else if (xe == 90) {
					if (selectionState == 1) {
						textState = 1;
						menuState = 2;
						counter = 0;
						System.out.println("menu2");
						
					}
					else if (selectionState == 2 && health < 50 && heals > 0) {
						if (health + 10 > 50) {
							health += 50 - health;
						}
						else health += 10;
						textState = 2;
						heals--;
						
					}
					else if (selectionState == 3) {
						textState = 3;
						
					}
					
				}
				/*
				// if the key is x, then go back
				else if (x == 88) {
				}
				*/
    		}
    		
    		// if you picked to heal or flee, you must click z another time to progress
    		else {
    			if (xe == 90) {
    				if (selectionState == 2) {
    					textState = 1;
    					menuState++;
    				}
    				else if (selectionState == 3) {
    					battling = false;
    					
    				}
    			}
    		}
			repaint();
    	}
    	
    	
    	// slider
    	else if (menuState == 2) {
    		if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
    			stopped = false;
    			if(barX <= 275 || barX >= 705) {
    				damage = 5;
    			}
    			else if(barX <= 450 || barX >= 530) {
    				damage = 10;
    			}
    			else {
    				damage = 15;
    			}
    			bossHealth -= damage;
    			System.out.println("DAMAGE: " + damage);
    		}
    	}
    	
    	
    	else if (menuState == 3) {
			if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { // keycode 39 is the up arrow key
				up = true;
			}
			
			if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
				down = true;
			}
			
	
			if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
				left = true;
			}
			
	
			if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
				right = true;
			}
    	}
    	
    	else if (menuState == 4 || menuState == 5) {
    		if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
    			resetStats();
    			System.out.println("quit");
    		}

    	}

    }


    public void run() {
    	while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

    		if (menuState == 3) { //  if you are fighting then we need this 
       				
    			
				if (up && withinBounds(playerX, playerY - playerSpeed, gameBounds)) {
		    		playerY -= playerSpeed;
		    		
		    	}
		    	if (down && withinBounds(playerX, playerY + playerSpeed, gameBounds)) {
		    		playerY += playerSpeed;
		    		
		    	}
		    	if (left && withinBounds(playerX - playerSpeed, playerY, gameBounds)) {
		    		playerX -= playerSpeed;
		    	}
		    	if (right && withinBounds(playerX + playerSpeed, playerY, gameBounds)) {
		    		playerX += playerSpeed;
		    	}
		    	
		    	if (attack == 1) {
		    		for (int i = 1; i < 11; i++) {
		    			corner cur = bonePositions[i];
		    			if (collision(new corner(playerX, playerY), new corner(playerX + 25, playerY + 25), new corner(cur.x, cur.y), new corner(cur.x + boneWidth, cur.y + boneHeight))) {
		            		health -= 1;
		            		
		            		// player has died
		            		if (health == 0) {
		            			menuState = 4;
		            			
		            		}
		    			}
		    		}
		    		updateFirstAttack();
		    		if (variation == 1) {
			    		if (bonePositions[10].x < 150) {
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
		    			if (bonePositions[10].x > 850) {
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
		    		
		    	}
		    	else if (attack == 2) {
		    		for (int i = 1; i < 11; i++) {
		    			corner cur = bonePositions2[i];
		    			
		    			// the width for a horizontal bone is 70
		    			// the height for a horizontal one is the width of bone 1
		    			if (collision(new corner(playerX, playerY), new corner(playerX + 25, playerY + 25), new corner(cur.x, cur.y), new corner(cur.x + 70, cur.y + boneWidth))) {
		            		health -= 1;
		            		
		            		// player has died
		            		if (health == 0) {
		            			menuState = 4;
		            			
		            		}
		    			}
		    		}
		    		updateSecondAttack();
		    		
		    		if (variation == 1) {
		    			if (bonePositions2[endBone].x < 100) {
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
		    			if (bonePositions2[endBone].x > 900) {
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
    	
    	repaint();	
    	}
    		
	}

	


    public void keyReleased(KeyEvent e)
    {
    	
    	if (menuState == 3) {
			if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { 
				up = false;
			}
			
			if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
				down = false;
			}
			
	
			if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
				left = false;
			}
			
	
			if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
				right = false;
			}
			System.out.println(playerX + " " + playerY);
    	}
    }
    

	public void firstAttack() {
		int k = (variation == 1) ? 680: 290;
		bonePositions[0] = new corner(Integer.MIN_VALUE, 0);
		for (int i = 1; i < 11; i++) {
			if (i % 2 == 0)
				bonePositions[i] = new corner(k, 295);
			else
				bonePositions[i] = new corner(k, 370);
		}
	}
	
	public void updateFirstAttack() {
		int k = (variation == 1) ? 1: -1;
		for (int i = 1; i < 11; i++) {
			
			// if the gap is greater than 100 pixels
			// update the next bone
			if (Math.abs(bonePositions[i].x - bonePositions[i - 1].x) >= 100) {
				System.out.println("bone x" + i + " = " + bonePositions[i].x);
				System.out.println("bone y" + i + " = " + bonePositions[i].y);

				// speed must be odd number not equal to 5 for some logic reason
				if (bossHealth > 55)
					bonePositions[i].x -= 3 * k;
				else {
					bonePositions[i].x -= 7 * k;
				}
			}
		}
	}
    
	
	// bones are thrown leftwards
	public void secondAttack() {
		
		// if variation is 1, bones will come from the right
		// if variation is 2, bones will come from the left
		int k = (variation == 1) ? 680: 290 - 69; // 69 is the horizontal boneWidth for bone2
		System.out.println(variation);
		for (int i = 1; i < 12; i++) {
			System.out.println(k);
			bonePositions2[i] = new corner(k, 295 + (i - 1) * boneWidth);
			boneOrder.add(i);
		}
		
		bonesReleased = new boolean[12];
		
		Collections.shuffle(boneOrder);
		endBone = boneOrder.get(boneOrder.size() - 1);
		startBone = boneOrder.poll();
		bonesReleased[startBone] = true;
		
	}
	public void updateSecondAttack() {
		int k = (variation == 1) ? 1: -1;
		
		for (int i = 1; i < 12; i++) {
			
			// if the gap is greater than 100 pixels
			// update the next bone
			if (bonesReleased[i]) {
				// speed must be odd number not equal to 5 for some logic reason
				if (bossHealth > 50)
					bonePositions2[i].x -= 13 * k;
				else 
					bonePositions2[i].x -= 17 * k;
			}
		}
		
		// for every multiple of 100 that the first bone has traveled, we will release a next bone
		// System.out.println(bonePositions2[startBone].x);
		
		if (boneOrder.size() > 0) {
			if (variation == 1) {
				if (bonePositions2[startBone].x - 680 + (100 * (11 - boneOrder.size())) < 0) {
					System.out.println("yo");
					bonesReleased[boneOrder.poll()] = true;
					
				}
			}
			else if (variation == 2){
				// starting position is 228, but we do 229 since 0 % 100 == 0
				if (bonePositions2[startBone].x - 228 - (100 * (11 - boneOrder.size())) > 0) {
					System.out.println("yo");
					bonesReleased[boneOrder.poll()] = true;
					
				}
			}
		}
		
	}
	

	
    
    


	// create an objects for measurements
	public static class corner{

		public int x;
		public int y;
		public corner(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	public static class dimension {
		public corner topLeft;
		public corner bottomRight;
		public dimension(corner topLeft, corner bottomRight) {
			this.topLeft = topLeft;
			this.bottomRight = bottomRight;
		}
	}
	
	public static boolean withinBounds(int x, int y, ArrayList<dimension> q) {
		for (dimension cur: q) { // for every boundary in the current setting
			// check if within top left corner
			corner topL = cur.topLeft;
			corner bottomR = cur.bottomRight;
			if (topL.x <= x && x <= bottomR.x && topL.y <= y && y <= bottomR.y) {
				return true;
			}

		}
		return false;
	}
    public boolean collision(corner playerTopLeft, corner playerBottomRight, corner objectTopLeft, corner objectBottomRight) {
		//System.out.println(topL1.x  + " < " + topL2.x + " < " + bottomR1.x + "  OR  " + topL1.x + " < " + bottomR2.x + " < " + bottomR1.x);
		//System.out.println(topL1.y  + " < " + topL2.y + " < " + bottomR1.y + "  OR  " + topL1.y + " < " + bottomR2.y + " < " + bottomR1.y);

    	if (playerTopLeft.y < objectBottomRight.y && playerBottomRight.y > objectTopLeft.y) {
        	if (playerTopLeft.x < objectBottomRight.x && playerBottomRight.x > objectTopLeft.x) {
        		return true;
        	}

    	}
    	  	
    	
    	
    	return false;
    }
    
    public static void main(String[] args) {
		JFrame frame = new JFrame("battle");
		battle panel = new battle();
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

    }
    

	public void resetStats() {
		health = 50;
		bossHealth = 100;
		playerX = 500;
		playerY = 400;
		menuState = 1;
		selectionState = 1;
		textState = 1;
		attack = 1;
		
	}




    public void keyTyped(KeyEvent e) {    	
    }
}
