
// INTRO COMMENTS
// the purpose of this file is to integrate text-file streaming
// most settings will have a script that will play once the player
// enters that setting. The following has a method to scroll through
// the dialogue, as we want to display messages 3 lines at a time 
// to not overload the player


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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class dialogue extends JPanel implements KeyListener{
	
	public static Scanner sc = new Scanner(System.in);
	
	public static BufferedImage dialogueBox;
	public static Font font;
	
	// max letters = 54
	
	public static boolean speaking = true;
	public static Scanner currentFile;
	public static ArrayList<String>[][] allScripts = new ArrayList[4][5];
	public static int startLine = 0;
	public static int maxLines = 0;
    

    
    
	public undertale game;
	public dialogue(undertale e){
		
		 game = e;
	}
	
	public dialogue(){
		try {
			dialogueBox = ImageIO.read(new File("assets/scripts/dialogueBox.png"));
		    font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);

		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(font);
		    
		} catch (IOException | FontFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("image does not exist");
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));
		addKeyListener(this);
		this.setFocusable(true);
		
		
		// import scripts
		for (int i = 1; i < 4; i++) {
			for (int x = 1; x < 5; x++) {
			
				try {
					allScripts[i][x] = new ArrayList<String>();
					currentFile = new Scanner(new File("assets/scripts/" + i + "_" + x + "_script.txt"));
					
					// put all lines of the text-file into an ArrayList that holds strings
					while (currentFile.hasNextLine()){
						allScripts[i][x].add(currentFile.nextLine());
					}
				} 
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block 
				}

			}
		}
	}
	
	
	/*
	 * The paint method component that will be implemented in the main file
	 * It draws the dialogue box and then 3 lines starting from the start line.
	 * The start line is an index to track which portion of the script we are on.
	 * 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (speaking) {
	   		g.setColor(new Color(255, 255, 255));
			g.setFont(font);
			g.drawImage(dialogueBox, 40, 420, null);
			for (int i = 0; i < 3; i++) {
				if (i + startLine < allScripts[1][1].size()) {
					g.drawString(allScripts[1][1].get(i + startLine), 300, 485 + 40 * i);
				}
				else {
					startLine = 0;
					speaking = false;
					break;
				}
			}
		}

	}
	public static void main(String[] args) throws IOException{
		JFrame frame = new JFrame("UNDERTALE");
		dialogue panel = new dialogue();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	
	/*
	 * keyPressed() will check if the player presses 'Z' or 'z' to 
	 * scroll through the dialogue. It adds 3 to start line since 
	 * we want to skip 3 lines after we displayed them
	 * The following if statements below are just for heals or if 
	 * the player has reached the trophy at the end of the game.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if (speaking) {
			if (e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
				if (undertale.grabbedHeal) {
					speaking = false;
					undertale.grabbedHeal = false;

					
				}
				else if (undertale.ended) {
					undertale.ended = false;
					speaking = false;
				}
				startLine += 3;
			}
			System.out.println(startLine);
		}
		repaint();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}