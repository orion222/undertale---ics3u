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
public class dialogue extends JPanel implements Runnable, KeyListener {


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
    public static boolean speaking = false;

	public static Font font;
	public static BufferedImage box;
	
    public static ArrayList<dimension> interactionArea = new ArrayList<dimension>();
    
	public undertale game;
	public dialogue(undertale e){
		
		 game = e;
	}
	
    public dialogue() {
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));
		
		addKeyListener(this);
		this.setFocusable(true);
        Thread thread = new Thread(this);
        thread.start();
        
        
        
        
        try {
			player = ImageIO.read(new File("assets/battleImages/health/player.png"));
			box = ImageIO.read(new File("assets/story/dialogueBox.png"));
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
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(player, playerX, playerY, null);
		
		if (speaking) {
			g.drawImage(box, 55, 425, null);
		}
    			

    }
    
    public void keyPressed(KeyEvent e) 
    {
    	int xe = e.getKeyCode();
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


    public void run() {
    	while (true) {
			try {
				Thread.sleep(50);
				if (up) {
		    		playerY -= playerSpeed;
		    		
		    	}
		    	if (down) {
		    		playerY += playerSpeed;
		    		
		    	}
		    	if (left) {
		    		playerX -= playerSpeed;
		    	}
		    	if (right) {
		    		playerX += playerSpeed;
		    	}

			} 
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
	    	repaint();
	    	
    	}

	}

        	
       

    
    public static void main(String[] args) {
		JFrame frame = new JFrame("battle");
		dialogue panel = new dialogue();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

    }
    

    

    public void keyReleased(KeyEvent e)
    {
    	
    	
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
	
	public static int interact(int x, int y, ArrayList<dimension> q) {
		for (int i = 1; i < q.size(); i++) { // for every interaction area in the current setting
			// check if within top left corner
			dimension cur = q.get(i - 1);
			corner topL = cur.topLeft;
			corner bottomR = cur.bottomRight;
			if (topL.x <= x && x <= bottomR.x && topL.y <= y && y <= bottomR.y) {
				return i;
			}

		}
		return -1;
	}

	




    public void keyTyped(KeyEvent e) {    	
    }
}