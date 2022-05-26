import javax.swing.*;
import java.awt.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.*;




public class undertaleRework extends JPanel implements KeyListener, MouseListener, Runnable{
	
	public static int counter;
	public static int gameState = 0;
	public static int setting = 0;
	public static int mouseX;
	public static int mouseY;
	public static int mapX = 0;
	public static int mapY = 0;
	
	// chara variables
	public static int charaX = 478;
	public static int charaY = 222;
	public static int charaSpeed = 10;
	
    // Creating a path to import the pictures
    public static File path = new File("assets/charaAnimation");
    public static File[] charaFile = path.listFiles();
    public static BufferedImage[] charaImages = new BufferedImage[10];
	public static int curChara = 2;  // know which frame of chara to show
	
	//IMAGES
	
	public static BufferedImage titleScreen;

	// map images
    public static File path2 = new File("assets/maps/ruins");
    public static File[] ruinsFile = path2.listFiles();
    public static BufferedImage[] ruinsImages = new BufferedImage[4];
	public static int curRuins = 0;  // know which frame of ruins to show
	public static ArrayList<dimension>[] ruinsBounds = new ArrayList[4];
	public static ArrayList<dimension>[] ruinsExits = new ArrayList[4];
	
    public static File path3 = new File("assets/maps/snowden");
    public static File[] snowdenFile = path3.listFiles();
    public static BufferedImage[] snowdenImages = new BufferedImage[4];
	public static int curSnowden = 0;  // know which frame of snowden to show
	public static ArrayList<dimension>[] snowdenBounds = new ArrayList[4];
	public static ArrayList<dimension>[] snowdenExits = new ArrayList[4];

	
    public static File path4 = new File("assets/maps/temp");
    public static File[] tempFile = path4.listFiles();
    public static BufferedImage[] tempImages = new BufferedImage[4];
	public static int curTemp = 0;  // know which frame of temp to show
	public static ArrayList<dimension>[] tempBounds = new ArrayList[4];
	public static ArrayList<dimension>[] tempExits = new ArrayList[4];


	public static ArrayList<dimension>[][] allBounds;
	public static ArrayList<dimension>[][] allExits;
	
	BufferedImage fadeStart;
	BufferedImage fadeEnd;
	
	// audio
	public static Clip startSong;
	
	
	public static AudioInputStream startSongInput;
	
	// position


	
	// other
	animation animation = new animation(this);
	charaAnimation charaAnimation = new charaAnimation(this);

	public undertaleRework() {
		
		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		setPreferredSize(new Dimension(990, 615));  
		setBackground(new Color(0, 0, 0));
		
		// import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));
			
			for (int i = 0; i < 4; i++) {
                ruinsImages[i] = ImageIO.read(ruinsFile[i]);
                snowdenImages[i] = ImageIO.read(snowdenFile[i]);
                // tempImages[i] = ImageIO.read(tempFile[i]);


			}

            for (int i = 0; i < 10; i++) {
                charaImages[i] = ImageIO.read(charaFile[i]);
            }
            startSongInput = AudioSystem.getAudioInputStream(new File("audio/mus_musicbox.ogg"));
            startSong = AudioSystem.getClip();
            startSong.open(startSongInput);
	    
		}
		catch (Exception e) {
			System.out.println("Image does not exist");
		}
		
		// create thread
		Thread thread = new Thread(this);
		thread.start();
		
		// add listeners
		addKeyListener(this);
		this.setFocusable(true);
		addMouseListener(this);
		
		// create boundaries & exits
		for (int i = 0; i < 4; i++) {
			ruinsBounds[i] = new ArrayList<dimension>();
			ruinsExits[i] = new ArrayList<dimension>();
			snowdenBounds[i] = new ArrayList<dimension>();
			snowdenExits[i] = new ArrayList<dimension>();
			tempBounds[i] = new ArrayList<dimension>();
			tempExits[i] = new ArrayList<dimension>();
		}
		for (int i = 1; i < 4; i++) {
			for (int x = 0; x < 4; x++) {
				allBounds[i][x] = new ArrayList<dimension>();
				allExits[i][x] = new ArrayList<dimension>();
			}
		}
		
		
		// ruins1
		ruinsBounds[1].add(new dimension(new corner (135, -10), new corner (800, 375)));
		ruinsBounds[1].add(new dimension(new corner(-50, 260), new corner(135, 375)));
		
		// entrance and exits
		ruinsExits[1].add(new dimension(new corner(1000, 625), new corner(1000, 625)));
		ruinsExits[1].add(new dimension(new corner(-50, 260), new corner(-50, 375)));
		
		
		// putting them all in a list
		allBounds[1] = ruinsBounds;
		allBounds[2] = snowdenBounds;
		allBounds[3] = tempBounds;
		
		
		
		

	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		undertaleRework panel = new undertaleRework();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animation.alpha));

		// fade
		
		if (animation.fading) {
			if (!animation.faded) {
				g2d.drawImage(fadeStart, 0, 0, null);
				
				// fade out the character as well
				if (gameState - 1 != 0 && 1 <= gameState && gameState <= 3) {
			        g2d.drawImage(charaImages[curChara], charaX, charaY, null);
				}
			}
			else {
				g2d.drawImage(fadeEnd, 0, 0, null);
				// fade in the character as well
				if (1 <= gameState && gameState <= 3) {
			        g2d.drawImage(charaImages[curChara], charaX, charaY, null);
				}
			}

		}
		// the start menu
		else if (gameState == 0) {
			g2d.drawImage(titleScreen, 0, 0, null);
			startSong.start();
			
		}
		
		// exploration 
		else if (gameState == 1) {
			g2d.drawImage(ruinsImages[curRuins], mapX, mapY, null);
			
			try {
				Thread.sleep(60);
			    g2d.drawImage(charaImages[curChara], charaX, charaY, null);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}				
	}
	


	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		mouseX = e.getX();
		mouseY = e.getY();
		if (gameState == 0) {
			
			// play game
			if (370 <= mouseX && mouseX <= 670 && 185 <= mouseY && mouseY <= 270) {
				System.out.println("play");
				animation.fading = true;
				animation.wait = true;
				fadeStart = titleScreen;
				fadeEnd = ruinsImages[0];
				gameState = 1;
				setting = 0; 
				animation.fade(fadeStart, fadeEnd, "slow");
				

			}
			
			// about 
			else if (370 <= mouseX && mouseX <= 670 && 300 <= mouseY && mouseY <= 390) {
				System.out.println("about");
				
				// change to about screen


			}
			
			else if (370 <= mouseX && mouseX <= 670 && 420 <= mouseY && mouseY <= 505) {
				System.out.println("quit");
				System.exit(0); // terminates the program
			}
		}
		
		repaint();
		
	}


	@Override
    public void keyPressed(KeyEvent e) {
		if (!animation.fading && 1 <= gameState && gameState <= 3 ) {
	        if(e.getKeyCode() == 38 && withinBounds(charaX, charaY - charaSpeed, curBounds))
	        {
	            charaY -= charaSpeed;
	            charaAnimation.key = 1;
	        }
	        else if(e.getKeyCode() == 37 && withinBounds(charaX - charaSpeed, charaY, curBounds))
	        {
	            charaX -= charaSpeed;
	            charaAnimation.key = 2;
	        }
	        else if(e.getKeyCode() == 40 && withinBounds(charaX, charaY + charaSpeed, curBounds))
	        {
	            charaY += charaSpeed;
	            charaAnimation.key = 3;
	        }
	        else if(e.getKeyCode() == 39 && withinBounds(charaX + charaSpeed, charaY, curBounds))
	        {
	            charaX += charaSpeed;
	            charaAnimation.key = 4;
	        }
	        charaAnimation.run();
	        
	        // if (exiting(charaX, charaY, curExits))
		}
	
    }

	public static boolean withinBounds(int x, int y, ArrayList<dimension> q) {
		
		for (dimension i: q) {
			// check if within top left corner
			corner topL = i.topLeft;
			corner bottomR = i.bottomRight;
			if (topL.x < x && x < bottomR.x && topL.y < y && y < bottomR.y) {
				return true;
			}
			
		}
		
		
		return false;
	}
	
	
	// -1 equals go back, 0 equals we haven't exited, 1 equals go to next setting
	public static int exiting(int x, int y, ArrayList<dimension> exits) {
		dimension entrance = exits.get(0);
		dimension exit = exits.get(1);
		
		if (entrance.bottomRight.y <= y && y <= entrance.topLeft.y
				&& entrance.topLeft.x == x) {
			// we entered the entrance so we go back to the previous setting
			return -1;
		}
		else if (exit.bottomRight.y <= y && y <= exit.topLeft.y
				&& exit.topLeft.x == x) {
			// we entered the entrance so we go back to the previous setting
			return 1;
		}
		
		
		
		return 0; // we have not exited the map
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (!animation.fading && 1 <= gameState && gameState <= 3 ) {
			if(e.getKeyCode() == 38)
	        {
	            System.out.println("w");
	            System.out.println("w hsa been released");
	            curChara = 0;
	        }
	        else if(e.getKeyCode() == 37)
	        {
	            System.out.println("a");
	            curChara = 1;
	        }
	        else if(e.getKeyCode() == 40)
	        {
	            System.out.println("s");
	            curChara = 2;
	        }
	        else if(e.getKeyCode() == 39)
	        {
	            System.out.println("d");
	            curChara = 3;
	        }
	        repaint();
		}
		
	}
	
	@Override
	public void run() {
		
	}
	
	
	// create an objects to measure the boundaries
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

	
	
	
	
///////////////////////////////////////////////////////	
	
	
	
	// useless methods
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



}