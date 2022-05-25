import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class charaAnimation extends JPanel implements Runnable, KeyListener {

    BufferedImage[] chara = new BufferedImage[10];
    int x = 0;


    // Allows the program to know what
    // movement key has been pressed,
    // which determines specific
    // pictures to be drawn
    public static int key = 0;
    // 1 = w
    // 2 = a
    // 3 = s
    // 4 = d

    // Determining which leg should go
    // first when drawing animation
    // pictures going in the same direction
    public static boolean legW = false;
    public static boolean legA = false;
    public static boolean legS = false;
    public static boolean legD = false;

    // Creating a path to import the pictures
    File path = new File("assets/charaAnimation");
    File[] charaImages = path.listFiles();
    
	public undertale game;
	
	public charaAnimation(undertale e){
		
		 game = e;
	}
	
    public charaAnimation() {
        setPreferredSize(new Dimension(1000, 500));
        setBackground(new Color(32, 55, 200));

        Thread thread = new Thread(this);
        thread.start();

        try {
            for (int i = 0; i < chara.length; i++) {
                chara[i] = ImageIO.read(charaImages[i]);
            }
        }
        catch (Exception e) {}

        addKeyListener(this);
        this.setFocusable(true);
    }

    // Bug when you initially click a movement button, the animation goes fast, then goes at the slower intended rate
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {Thread.sleep(120);}
        catch (Exception e) {e.printStackTrace();}
    }

    // Determining which direction the
    // user is going at and figuring out
    // the correct pictures to draw
    public void run() {
        if(x >= 37 && x <= 40)
        {
            // w
            if(key == 1){
                if(!legW){game.curChara = 4; legW = true;}
                else {game.curChara = 5; legW = false;}
            }
            // a
            else if(key == 2)
                if(!legA){game.curChara = 8; legA = true;}
                else {game.curChara = 1; legA = false;}
            // s
            else if(key == 3){
                if(!legS){game.curChara = 6; legS = true;}
                else {game.curChara = 7; legS = false;}
            }
            // d
            else{
                if(!legD){game.curChara = 9; legD = true;}
                else{game.curChara = 3; legD = false;}
            }
            game.repaint();
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}

