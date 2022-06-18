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
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (speaking) {
			if (e.getKeyChar() == 'z') {
				
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
