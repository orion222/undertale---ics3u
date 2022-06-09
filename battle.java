import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



// implement runnable for constant update in main file
public class battle extends JPanel implements Runnable, KeyListener {


    public static int key;
    public static int pointerX;
    public static int playerX = 500;
    public static int playerY = 400;
    public static BufferedImage character;

    
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
	
	
    
    public static boolean battling = true;
	public static int menuState = 1;
    public static boolean invulnerable = false;
    public static int selectionState = 1;
    public static int health = 10;
    public static int textState = 1;
    public static int heals;
    
    public static position[] optionPos = new position[4];
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
		
		optionPos[1] = new position(235, 539);
		optionPos[2] = new position(442, 539);
		optionPos[3] = new position(650, 539);
		
	
    
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(255, 255, 255));
		g.setFont(font);
    	if (battling) {

    		// menu background
    		g.drawImage(menuImages[menuState], 0, 0, null);
    		

    		// highlight selection
    		if (menuState == 1) {
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
    		
    		
    	}

    }
    
    public void keyPressed(KeyEvent e) 
    {
    	int xe = e.getKeyCode();
    	if (menuState == 1) {
    		if (textState == 1) {
	    		
	    		// left 
				if (xe == 37) { 
					System.out.println(selectionState);
					if (selectionState - 1 > 0) {
						selectionState --;
						System.out.println(selectionState);
	
					}
				}
				
				// right
				else if (xe == 39) {
					System.out.println(selectionState);
					if (selectionState + 1 < 4) {
						selectionState ++;
						System.out.println(selectionState);
	
					}
					
				}
	    		// if the key is z, then select
				else if (xe == 90) {
					if (selectionState == 1) {
						textState = 1;
						menuState ++;
						
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
				if (up) {
		    		playerY -= 5;
		    		
		    	}
		    	if (down) {
		    		playerY += 5;
		    		
		    	}
		    	if (left) {
		    		playerX -= 5;
		    	}
		    	if (right) {
		    		playerX += 5;
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
    	}
    }
    
    
    
    

	// create an objects for measurements
	public static class position{

		public int x;
		public int y;
		public position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}



    public void keyTyped(KeyEvent e) {    	
    }
}
