import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class battle extends JPanel implements Runnable, KeyListener {


    public static int key;
    public static int pointerX;
    public static int playerX = 500;
    public static int playerY = 400;
    
    public static boolean up = false;
    public static boolean down = false;
    public static boolean right = false;
    public static boolean left = false;
    
    public static BufferedImage character;
    public static BufferedImage menu;
    
    
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
			character = ImageIO.read(new File("assets/battleImages/heart.png"));

            
        }
        catch (Exception e) {}
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(character, playerX, playerY, null);

    }

    public void run() {
    	while (true) {
	    	
    		try {
				Thread.sleep(50);
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
    
    public void keyPressed(KeyEvent e) 
    {
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

    public void keyTyped(KeyEvent e) {    	
    }
    public void keyReleased(KeyEvent e)
    {
		if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { // keycode 39 is the up arrow key
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