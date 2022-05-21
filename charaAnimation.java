import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class charaAnimation extends JPanel implements Runnable, KeyListener {

    BufferedImage[] chara = new BufferedImage[10];

    // Indicates which picture should be drawn
    // from all the chara pictures
    public static int num = 0;

    public static int x = 100;
    public static int y = 100;

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
        g.drawImage(chara[num], x, y, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("animation");
        charaAnimation panel = new charaAnimation();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // Determining which direction the
    // user is going at and figuring out
    // the correct pictures to draw
    public void run() {
        if(key > 0)
        {
            repaint();
            // w
            if(key == 1){
                if(!legW){num = 4; legW = true;}
                else {num = 5; legW = false;}
            }
            // a
            else if(key == 2)
                if(!legA){num = 8; legA = true;}
                else {num = 1; legA = false;}
            // s
            else if(key == 3){
                if(!legS){num = 6; legS = true;}
                else {num = 7; legS = false;}
            }
            // d
            else{
                if(!legD){num = 9; legD = true;}
                else{num = 3; legD = false;}
            }
        }
    }

    public void keyTyped(KeyEvent e) {}

    // Declaring a value to key to indicate
    // which movement button has been pressed
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == 'w')
        {
            System.out.println("w");
            key = 1;
        }
        else if(e.getKeyChar() == 'a')
        {
            System.out.println("a");
            key = 2;
        }
        else if(e.getKeyChar() == 's')
        {
            System.out.println("s");
            key = 3;
        }
        else if(e.getKeyChar() == 'd')
        {
            System.out.println("d");
            key = 4;
        }
        run();
    }

    // When button is released, chara
    // should go back to default standing
    // position in whichever direction
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == 'w')
        {
            System.out.println("w");
            num = 0;
        }
        else if(e.getKeyChar() == 'a')
        {
            System.out.println("a");
            num = 1;
            legA = false;
        }
        else if(e.getKeyChar() == 's')
        {
            System.out.println("s");
            num = 2;
            legS = false;
        }
        else if(e.getKeyChar() == 'd')
        {
            System.out.println("d");
            num = 3;
        }
        repaint();
    }
}

