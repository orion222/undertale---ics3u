import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class slash extends JPanel implements Runnable, KeyListener {
	public static BufferedImage[] slashImage = new BufferedImage[4];
	public static int[] x = new int[4];
	public static int[] y = new int[4];
	public static int num = 0;
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		slash panel = new slash();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
    public slash() {
        setPreferredSize(new Dimension(900, 900));
        setBackground(new Color(32, 55, 200));

        Thread thread = new Thread(this);
        thread.start();

        try {
        	slashImage[0] = ImageIO.read(new File("assets/battleImages/slash/slash1.png"));
        	slashImage[1] = ImageIO.read(new File("assets/battleImages/slash/slash2.png"));
        	slashImage[2] = ImageIO.read(new File("assets/battleImages/slash/slash3.png"));
        	slashImage[3] = ImageIO.read(new File("assets/battleImages/slash/slash4.png"));
        }
        catch (Exception e) {}

        addKeyListener(this);
        this.setFocusable(true);
        
        x[0] = 100;
        y[0] = 100;
        
        x[1] = 103;
        y[1] = 120;
        
        x[2] = 53;
        y[2] = 120;
        
        x[3] = 130;
        y[3] = 147;
    }

    // Bug when you initially click a movement button, the animation goes fast, then goes at the slower intended rate
    public void paintComponent(Graphics g) {
    	
    	super.paintComponent(g);
    	g.drawImage(slashImage[num], x[num], y[num], null);
    	
    	
 
        
        
//        g.drawImage(slashImage[0], 100, 100, null);
//        g.drawImage(slashImage[1], 103, 120, null);
//        g.drawImage(slashImage[2], 53, 120, null);
//        g.drawImage(slashImage[3], 130, 147, null);
        
        
        

    }

    // Determining which direction the
    // user is going at and figuring out
    // the correct pictures to draw
    public void run() {
    	while(true) {
    		if(num > 3) {
    			num = 0;
    		}
    		repaint();
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		num++;
    		
    	}
 
    
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}

