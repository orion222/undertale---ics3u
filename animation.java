import java.awt.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import javax.swing.Timer;

public class animation implements ActionListener{
	public static float alpha = 1.0f;
	
	// -1 = no fade, 1 = fade in, 2 = fade out
	public static boolean fading = false;
	public static boolean faded = false;
	public static int fade = -1;
	public static double fadeSpeed;
	public static boolean wait = false;
	
	// time
	Timer timer = new Timer(20, this);
	public undertale game;
	
	public animation(undertale e){
		
		 game = e;
	}
	
	public void fade(BufferedImage start, BufferedImage end, String speed) {
		if (speed.equals("fast")) {
			fadeSpeed = 0.05;
		}
		else if (speed.equals("slow")){
			fadeSpeed = 0.01;
		}
		timer.start();

	}
	public void fadeOut() {
		alpha -= fadeSpeed;
		if (alpha < 0) {
			alpha = 0;
			faded = true;
			if (wait) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void fadeIn() {	
		alpha += fadeSpeed;
		if (alpha > 1) {
			alpha = 1;
			fading = false;
			faded = false;
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (!faded) { // if we are currently fading out
			fadeOut();
		}
		
		else {
			fadeIn();
		}
		game.repaint();
		
	}
	
	
	
}
