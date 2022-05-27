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




public class orion extends JPanel implements KeyListener, MouseListener, Runnable{
	
	public static int counter;
	public static int gameState = 0;
	public static int setting = 1;
	public static int mouseX;
	public static int mouseY;
	public static int mapX = 0;
	public static int mapY = 0;
	
	// chara variables
	public static int charaSpeed = 5;
	public static dimension[][] charaPos = new dimension[5][2];
	
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
    public static BufferedImage[] ruinsImages = new BufferedImage[5];
	public static int curRuins = 0;  // know which frame of ruins to show

	public static ArrayList<dimension>[] ruinsBounds = new ArrayList[5];
	public static ArrayList<dimension>[] ruinsExits = new ArrayList[5];
	
    public static File path3 = new File("assets/maps/snowden");
    public static File[] snowdenFile = path3.listFiles();
    public static BufferedImage[] snowdenImages = new BufferedImage[5];
	public static int curSnowden = 0;  // know which frame of snowden to show
	public static ArrayList<dimension>[] snowdenBounds = new ArrayList[5];
	public static ArrayList<dimension>[] snowdenExits = new ArrayList[5];

	
    public static File path4 = new File("assets/maps/temp");
    public static File[] tempFile = path4.listFiles();
    public static BufferedImage[] tempImages = new BufferedImage[5];
	public static int curTemp = 0;  // know which frame of temp to show
	public static ArrayList<dimension>[] tempBounds = new ArrayList[5];
	public static ArrayList<dimension>[] tempExits = new ArrayList[5];

	
	public static ArrayList<dimension>[][] allBounds = new ArrayList[4][5];
	public static ArrayList<dimension>[][] allExits = new ArrayList[4][5];
	public static BufferedImage[][] allMaps = new BufferedImage[4][5];
	
	public static File path5 = new File("assets/battle");
	public static File[] battleFile = path5.listFiles();
	public static BufferedImage[] battleImages = new BufferedImage[1];
	
	BufferedImage fadeStart;
	BufferedImage fadeEnd;
	
	// audio
	public static Clip startSong;
	
	
	public static AudioInputStream startSongInput;
	
	// position


	
	// other
	animation animation = new animation(this);
	charaAnimation charaAnimation = new charaAnimation(this);

	public orion() {
		
		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		setPreferredSize(new Dimension(990, 615));  
		setBackground(new Color(0, 0, 0));
		
		// import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));
			
			for (int i = 1; i < 5; i++) {
                snowdenImages[i] = ImageIO.read(snowdenFile[i - 1]);
                // tempImages[i] = ImageIO.read(tempFile[i]);


			}
			
			battleImages[0] = ImageIO.read(new File("assets/battle/slider.png"));

			ruinsImages[1] = ImageIO.read(new File("assets/maps/ruins/ruins1.png"));
			ruinsImages[2] = ImageIO.read(new File("assets/maps/ruins/ruins2.png"));
			ruinsImages[3] = ImageIO.read(new File("assets/maps/ruins/ruins3.png"));
			ruinsImages[4] = ImageIO.read(new File("assets/maps/ruins/ruins4.png"));

			charaImages[0] = ImageIO.read(new File("assets/charaAnimation/AcharaB1.png"));
			charaImages[1] = ImageIO.read(new File("assets/charaAnimation/AcharaL1.png"));
			charaImages[2] = ImageIO.read(new File("assets/charaAnimation/BcharaF1.png"));
			charaImages[3] = ImageIO.read(new File("assets/charaAnimation/BcharaR1.png"));
			charaImages[4] = ImageIO.read(new File("assets/charaAnimation/charaB2.png"));
			charaImages[5] = ImageIO.read(new File("assets/charaAnimation/charaB3.png"));
			charaImages[6] = ImageIO.read(new File("assets/charaAnimation/charaF2.png"));
			charaImages[7] = ImageIO.read(new File("assets/charaAnimation/charaF3.png"));
			charaImages[8] = ImageIO.read(new File("assets/charaAnimation/charaL2.png"));
			charaImages[9] = ImageIO.read(new File("assets/charaAnimation/charaR2.png"));
            /*
            startSongInput = AudioSystem.getAudioInputStream(new File("audio/mus_musicbox.ogg"));
            startSong = AudioSystem.getClip();
            startSong.open(startSongInput);
	    	*/
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
		
		// Positions of Chara
		charaPos[1][0] = new dimension(new corner(480, 220), new corner(480, 220));
		
		// create boundaries & exits
		for (int i = 1; i < 5; i++) {
			ruinsBounds[i] = new ArrayList<dimension>();
			ruinsExits[i] = new ArrayList<dimension>();
			snowdenBounds[i] = new ArrayList<dimension>();
			snowdenExits[i] = new ArrayList<dimension>();
			tempBounds[i] = new ArrayList<dimension>();
			tempExits[i] = new ArrayList<dimension>();
		}
		
		for (int i = 1; i < 4; i++) { // map
			for (int x = 0; x < 4; x++) { // setting
				allBounds[i][x] = new ArrayList<dimension>();
				allExits[i][x] = new ArrayList<dimension>();
			}
		}
		
		
		// ruins1
		ruinsBounds[1].add(new dimension(new corner (135, -10), new corner (800, 375)));
		ruinsBounds[1].add(new dimension(new corner(-25, 260), new corner(135, 375)));
		
		// ruins2
		ruinsBounds[2].add(new dimension(new corner (55, 290), new corner (995, 415)));
		ruinsBounds[2].add(new dimension(new corner (120, 250), new corner (230, 290)));
	
		
		// entrance and exits
		ruinsExits[1].add(new dimension(new corner(1000, 625), new corner(1000, 625)));  // there is no entrance so set to max val
		ruinsExits[1].add(new dimension(new corner(-25, 260), new corner(-44, 375)));
		
		ruinsExits[2].add(new dimension(new corner(120,250), new corner(230, 250))); // exit
		ruinsExits[2].add(new dimension(new corner(995,290), new corner(995, 415))); // entrance
		
		
		// putting them all in a list
		allBounds[1] = ruinsBounds;
		allBounds[2] = snowdenBounds;
		allBounds[3] = tempBounds;
		
		allMaps[1] = ruinsImages;
		allMaps[2] = snowdenImages;
		allMaps[3] = tempImages;
		
		allExits[1] = ruinsExits;
		allExits[2] = snowdenExits;
		allExits[3] = tempExits;
		
		
		
		

	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		orion panel = new orion();
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
			// startSong.start();
			
		}
		
		// exploration 
		else if (1 <= gameState && gameState <= 3) {
			g2d.drawImage(allMaps[gameState][setting], mapX, mapY, null);
			
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
				fadeEnd = ruinsImages[1];
				gameState = 1;
				setting = 1; 
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
	public static int withinExit(int x, int y, ArrayList<dimension> exits) {
		dimension entrance = exits.get(0);
		dimension exit = exits.get(1);
		
		if (entrance.bottomRight.y <= y && y <= entrance.topLeft.y
				&& entrance.topLeft.x > x) {
			// we entered the entrance so we go back to the previous setting
			return -1;
		}
		else if (exit.bottomRight.y <= y && y <= exit.topLeft.y
				&& exit.topLeft.x < x) {
			// we entered the entrance so we go back to the previous setting
			return 1;
		}
		
		
		
		return 0; // we have not exited the map
	}
	// When button is released, chara
	// should go back to default standing
	// position in whichever direction
	public void keyReleased(KeyEvent e) {
		int x = e.getKeyCode();
		if(!animation.fading && 1 <= gameState && gameState <= 3) {
			if (x == 38) {
				curChara = 0;
			} else if (x == 37) {
				curChara = 1;
				charaAnimation.legA = false;
			} else if (x == 40) {
				curChara = 2;
				charaAnimation.legS = false;
			} else if (x == 39) {
				System.out.println("d");
				curChara = 3;
			}
			repaint();
		}
	}
	@Override
    public void keyPressed(KeyEvent e) {
		int x = e.getKeyCode();
		charaAnimation.x = e.getKeyCode();
		if (!animation.fading && 1 <= gameState && gameState <= 3 ) {
	        if(e.getKeyCode() == 38 && withinBounds(charaX, charaY - charaSpeed, allBounds[gameState][setting]))
	        {
	            charaY -= charaSpeed;
	            charaAnimation.key = 1;
	        }
	        else if(e.getKeyCode() == 37 && withinBounds(charaX - charaSpeed, charaY, allBounds[gameState][setting]))
	        {
	            charaX -= charaSpeed;
	            charaAnimation.key = 2;
	        }
	        else if(e.getKeyCode() == 40 && withinBounds(charaX, charaY + charaSpeed, allBounds[gameState][setting]))
	        {
	            charaY += charaSpeed;
	            charaAnimation.key = 3;
	        }
	        else if(e.getKeyCode() == 39 && withinBounds(charaX + charaSpeed, charaY, allBounds[gameState][setting]))
	        {
	            charaX += charaSpeed;
	            charaAnimation.key = 4;
	        }
	        charaAnimation.run();
	        
			int change = withinExit(charaX, charaY, allExits[gameState][setting]);
	        System.out.println(charaX + " " + charaY + " " + change);
			if (change != 0) {
				// save the current map
				fadeStart = allMaps[gameState][setting];
				
				
				// go to next map
				if (change == 1) {
					System.out.println("next setting");
					// if there are no more maps
					if (gameState + 1 == 4) {
						setting = 0;
					}
					gameState++;
				}
				
				else if (change == -1) {
					// go to the previous MAP
					if (setting - 1 < 0) {
						gameState--;
						setting = 4;
					}
					// go to the previous setting but still in same map
					else {
						setting --;
					}
				}
				fadeEnd = allMaps[gameState][setting];
				animation.fade(fadeStart, fadeEnd, "fast");


			}
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