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



public class undertale extends JPanel implements KeyListener, MouseListener, Runnable{

	public static int counter;
	public static int gameState = 0;
	public static int setting = 1;
	public static int mouseX;
	public static int mouseY;
	public static int mapX = 0;
	public static int mapY = 0;
	public static int change = 1;

	// chara variables
	public static int charaX = 475;
	public static int charaY = 220;
	public static int charaSpeed = 5;

	public static int globalPos;
	public static int mapCharaY;


	// [map][setting][1 = start, 2 = exit]
	public static corner[][][] allPos = new corner[4][5][3];

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

	public static ArrayList<dimension>[] ruinsBounds = new ArrayList[5];
	public static ArrayList<dimension>[] ruinsExits = new ArrayList[5];

	public static File path3 = new File("assets/maps/snowden");
	public static File[] snowdenFile = path3.listFiles();
	public static BufferedImage[] snowdenImages = new BufferedImage[5];
	public static ArrayList<dimension>[] snowdenBounds = new ArrayList[5];
	public static ArrayList<dimension>[] snowdenExits = new ArrayList[5];


	public static File path4 = new File("assets/maps/temp");
	public static File[] tempFile = path4.listFiles();
	public static BufferedImage[] tempImages = new BufferedImage[5];
	public static ArrayList<dimension>[] tempBounds = new ArrayList[5];
	public static ArrayList<dimension>[] tempExits = new ArrayList[5];


	// gameState is the map, setting is the current place
	public static ArrayList<dimension>[][] allBounds = new ArrayList[4][5];
	public static ArrayList<dimension>[][] allExits = new ArrayList[4][5];
	public static BufferedImage[][] allMaps = new BufferedImage[4][5];

	// set of coords in specific maps where the map camera needs to move
	public static dimension[][] moveMap = new dimension[3][5];

	BufferedImage fadeStart;
	BufferedImage fadeEnd;

	// audio
	public static Clip startSong;

	public static AudioInputStream startSongInput;

	// position



	// other
	animation animation = new animation(this);
	charaAnimation charaAnimation = new charaAnimation(this);

	public undertale() {

		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		// 615
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));

		// import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));

			snowdenImages[1] = ImageIO.read(new File("assets/maps/snowden/snowden1.png"));

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

		// Positions of Chara
		// ruins1
		allPos[1][1][1] = new corner(480, 220); // Initial position after menu
		allPos[1][1][2] = new corner(15, 340); // In front of exit

		// ruins2
		allPos[1][2][1] = new corner(920, 255); // In front of entrance
		allPos[1][2][2] = new corner(130, 185); // In front of exit

		// ruins3
		allPos[1][3][1] = new corner(85, 240); // In front of entrance
		allPos[1][3][2] = new corner(870, 415); // In front of exit

		// ruins4
		allPos[1][4][1] = new corner(475, 450); // In front of entrance
		allPos[1][4][2] = new corner(470, 80); // In front of exit

		//snowden 1
		allPos[2][1][1] = new corner(140, 400); // In front of entrance
		allPos[2][1][2] = new corner(1960, 620); // In front of exit


		// ruins1
		ruinsBounds[1].add(new dimension(new corner (135, -10), new corner (800, 375)));
		ruinsBounds[1].add(new dimension(new corner(-35, 260), new corner(135, 375)));
		ruinsExits[1].add(new dimension(new corner(1000, 625), new corner(1000, 625)));  // there is no entrance so set to max val
		ruinsExits[1].add(new dimension(new corner(-35, 260), new corner(-35, 375))); // exit

		// ruins2
		ruinsBounds[2].add(new dimension(new corner (45, 175), new corner (1050, 300)));
		ruinsBounds[2].add(new dimension(new corner (100, 150), new corner (170, 175)));
		ruinsExits[2].add(new dimension(new corner(940,175), new corner(940, 300))); // entrance
		ruinsExits[2].add(new dimension(new corner(100,150), new corner(170, 150))); // exit

		// ruins3
		ruinsBounds[3].add(new dimension(new corner(55, 180), new corner(135,285)));
		ruinsBounds[3].add(new dimension(new corner(135, 125), new corner(765,460)));
		// boundary needs to move
		ruinsBounds[3].add(new dimension(new corner(765, 355), new corner(915,460)));
		ruinsExits[3].add(new dimension(new corner(55,180), new corner(55,285))); // entrance
		ruinsExits[3].add(new dimension(new corner(915,355), new corner(915,460))); // exit

		// ruins4
		ruinsBounds[4].add(new dimension(new corner(450, 275), new corner(495,480)));
		ruinsBounds[4].add(new dimension(new corner(250, 270), new corner(695,280)));
		ruinsBounds[4].add(new dimension(new corner(395, 60), new corner(550,270)));
		ruinsBounds[4].add(new dimension(new corner(465,40), new corner(475,60)));
		ruinsExits[4].add(new dimension(new corner(445,480), new corner(500,480))); // entrance
		ruinsExits[4].add(new dimension(new corner(465,40), new corner(475, 40))); // exit

		//snowden1
		snowdenBounds[1].add(new dimension(new corner(210, 265), new corner(365, 320)));
		snowdenBounds[1].add(new dimension(new corner(210, 320), new corner(2000, 470)));
		snowdenBounds[1].add(new dimension(new corner(80, 370), new corner(210, 470)));
		snowdenExits[1].add(new dimension(new corner(80, 370), new corner (80, 415)));
		snowdenExits[1].add(new dimension(new corner(1970, 530), new corner(1970, 705))); // exit IS BROKEN


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

		// Coords to move map
		moveMap[1][3] = new dimension(new corner(0, 290), new corner(0, 900));
		moveMap[1][4] = new dimension(new corner(0, 435), new corner(0, 900));





	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		undertale panel = new undertale();
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
				g2d.drawImage(fadeStart, mapX, mapY, null);

				// fade out the character as well
				if (gameState - 1 != 0 && 1 <= gameState && gameState <= 3) {
					g2d.drawImage(charaImages[curChara], charaX, charaY, null);
				}
				

			}
			else {
				// change chara position
				charaX = allPos[gameState][setting][change].x;
				charaY = allPos[gameState][setting][change].y;

				// if chara spawns at the bottom
				if (charaY > 312 && gameState == 1) {
					globalPos = 1250 - (625 - charaY);

				}
				else if (gameState == 1) {
					globalPos = charaY;
				}

				g2d.drawImage(fadeEnd, mapX, mapY, null);
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
	public void keyPressed(KeyEvent e) {
		charaAnimation.x = e.getKeyCode();
		if (!animation.fading && 1 <= gameState && gameState <= 3 ) {
			System.out.println("orion chen: " + charaY);
			if(e.getKeyCode() == 38 && withinBounds(charaX, charaY - charaSpeed, allBounds[gameState][setting]))
			{
				charaAnimation.key = 1;

				// test > 290 and test <= 900 is temporarily there specifically for ruins3, put values in
				// an array or smth

				// when test is at value 290, the next click should subtract from charaY and run through
				// the else statement, but since test is test >= 290, it goes through the first if statement
				// and the value of charaY will be off by 5
				if(setting > 2 && moveMap[gameState][setting].topLeft.y < globalPos && globalPos <= moveMap[gameState][setting].bottomRight.y) {
					mapY += charaSpeed;
					globalPos -= charaSpeed;
				}
				else {
					charaY -= charaSpeed;
					globalPos -= charaSpeed;
				}
				System.out.println("CHARA Y POSITION: " + charaY);
			}
			else if(e.getKeyCode() == 37 && withinBounds(charaX - charaSpeed, charaY, allBounds[gameState][setting]))
			{
				charaX -= charaSpeed;
				charaAnimation.key = 2;
			}
			else if(e.getKeyCode() == 40 && withinBounds(charaX, charaY + charaSpeed, allBounds[gameState][setting]))
			{
				charaAnimation.key = 3;
				// when test is at value 900, the next click should add to charaY and run through the else statement,
				// but since test is <= 900, it will run through the first if statement and the value of charaY will
				// be off by 5 and will not run
				if(setting > 2 && moveMap[gameState][setting].topLeft.y <= globalPos && globalPos < moveMap[gameState][setting].bottomRight.y) {
					mapY -= charaSpeed;
					globalPos += charaSpeed;
				}
				else {
					charaY += charaSpeed;
					globalPos += charaSpeed;
				}
			}
			else if(e.getKeyCode() == 39 && withinBounds(charaX + charaSpeed, charaY, allBounds[gameState][setting]))
			{
				charaX += charaSpeed;
				charaAnimation.key = 4;
			}
			charaAnimation.run();
			System.out.println("globalPos: " + globalPos);
			System.out.println("mapX: " + mapX + " mapY: " + mapY);

			change = withinExit(charaX, charaY, allExits[gameState][setting]);
			System.out.println("chara x = " + charaX + " " + "charaY = " + charaY);

			if (change != 0) {
				// save the current map
				fadeStart = allMaps[gameState][setting];
				System.out.println(gameState + " " + setting);

				// go to next map
				if (change == 1) {
					System.out.println("next setting");
					// if there are no more maps
					if (gameState + 1 == 4) {
						setting = 1;
						gameState++;
					}

					else if (setting + 1 == 5) {
						gameState++;
						setting = 1;
					}
					else setting++;
				}

				else if (change == 2) {
					// go to the previous MAP
					if (setting - 1 == 0) {
						gameState--;
						setting = 4;
					}
					// go to the previous setting but still in same map
					else setting--;
				}


				fadeEnd = allMaps[gameState][setting];
				System.out.println("new: " + gameState + " " + setting);
				System.out.println("anti orion: " + mapY);
	

				animation.fade(fadeStart, fadeEnd, "fast");


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
				animation.wait = false;
				fadeStart = titleScreen;
				fadeEnd = ruinsImages[1];
				gameState = 1;
				setting = 3;
				animation.fade(fadeStart, fadeEnd, "fast");



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
		for (dimension cur: q) { // for every boundary in the current setting
			// check if within top left corner
			corner topL = cur.topLeft;
			corner bottomR = cur.bottomRight;
			System.out.println(topL.x + " <= " + x + " <= " + bottomR.x);
			System.out.println(topL.y + " <= " + y + " <= " + bottomR.y + "\n");
			if (topL.x <= x && x <= bottomR.x && topL.y <= y && y <= bottomR.y) {
				return true;
			}

		}


		return false;
	}


	// 1 equals you go to the entrance of the next setting
	// 0 equals we haven't exited
	// 2 equals go to the exit of the previous setting
	public static int withinExit(int x, int y, ArrayList<dimension> exits) {
		for (int i = 0; i < 2; i++) {
			dimension cur = exits.get(i);



			System.out.println(cur.topLeft.y + " <= " + y + " <= " + cur.bottomRight.y + "  x == " + cur.topLeft.x);
			boolean exitingVertically = cur.topLeft.x <= x && x <= cur.bottomRight.x && cur.topLeft.y == y;
			boolean exitingHorizontally = cur.topLeft.y <= y && y <= cur.bottomRight.y && cur.topLeft.x == x;


			if (exitingVertically || exitingHorizontally) {

				// you entered the entrance so u go back
				if (i == 0) return 2;
					// go to the exit of the previous setting

					// you entered the exit so you progress to next map
				else if (i == 1) {
					return 1;
					// go to the entrance of the next setting
				}
			}
		}
		return 0;
	}

	// in ruins3, when y > 500, move camera


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
