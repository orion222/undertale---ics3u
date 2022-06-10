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
    public static boolean invulnerable = false;
    public static int selectionState = 1;
    public static int health = 10;
    public static int textState = 1;
    public static int heals;
    
    public static corner[] optionPos = new corner[4];
	public static Font font;
	
    
    
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
			player = ImageIO.read(new File("assets/battleImages/health/player.png"));
		
        }
        
        catch (Exception e) {
        	System.out.println("image does not exist");
        }
        

		try {
		     font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);
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
    		
    		// stats
    		g.drawString(health + "", 350, 518);
    		g.drawString("10", 430, 518);
    		g.drawString(heals + "", 645, 518);
    		g.drawString("5", 710, 518);

		
    		

    		// highlight selection
    		if (menuState == 1) {
    			g.setFont(font);
    			if (textState == 1) {
    				g.drawString("What will Chara do?", 75, 360);
    			}
    			else if (textState == 2) {
    				g.drawString("You healed 2 HP! You have " + heals + " left", 75, 360);
    			}
    			else if (textState == 3) {
    				g.drawString("You fled the scene" , 75, 360);

    			}
    			g.drawImage(selectionImages[selectionState], optionPos[selectionState].x, optionPos[selectionState].y, null);
    		}
    		
    		else if (menuState == 2) {
    			
    		}
    		else if (menuState == 3) {
    			g.drawImage(player, playerX, playerY, null);
    			
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
						
					}
					else if (selectionState == 2) {
						textState = 2;
						
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
    		
    	}
    	
    	
    	else if (menuState == 3) {
			if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { // keycode 39 is the up arrow key
				System.out.println("up");
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
		    	if (right && withinBounds(playerX + playerSpeed, playerY - playerSpeed, gameBounds)) {
		    		playerX += playerSpeed;
		    	}

	    	}
	    	repaint();

	    	
    	}

	}

        	
       

    
    public static void main(String[] args) {
		JFrame frame = new JFrame("battle");
		battle panel = new battle();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

    }
    

    

    public void keyReleased(KeyEvent e)
    {
    	
    	if (menuState == 3) {
			if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { 
				System.out.println("up");
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

	




    public void keyTyped(KeyEvent e) {    	
    }
}
