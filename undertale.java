/*
 * ICS3U - Final Culminating Project
 * By Orion Chen & Nicky Lam
 * For Mr. Chow
 * 
 * UNDERTALE
 * This project is a re-make of the famous indie game UNDERTALE.
 * It is a game about exploration, finding items, and talking to new
 * characters that you meet in the game. However, we obviously do not 
 * have the expertise to implement all the things in the original game.
 * In this re-make, you can explore 2 different maps of the game and 
 * find heals in each setting. You are then able to fight 1 boss
 * at the very last setting in the second map.
 * After that, you will be taken to a trophy room and the game ends there.
 * 
 */
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.*;


public class undertale extends JPanel implements KeyListener, MouseListener, Runnable{
	public static int gameState = 0;
	public static int setting = 1;
	public static int mouseX;
	public static int mouseY;
	public static int mapX = 0;
	public static int mapY = 0;
	public static int change = 1;

	// Chara variables
	public static int charaX = 475;
	public static int charaY = 220;
	public static int charaSpeed = 5;

	// Chara theoretical position when map is moving
	// and not herself
	public static int globalPos;

	public static int fps = 20;

	// Variables for movement
	public static boolean up = false;
	public static boolean left = false;
	public static boolean down = false;
	public static boolean right = false;

	// Chara starting positions when entering or exiting
	// [map][setting][1 = start, 2 = exit]
	public static corner[][][] allPos = new corner[4][5][3];

	// Creating a path to import the pictures
	public static BufferedImage[] charaImages = new BufferedImage[10];
	public static int curChara = 2;  // know which frame of chara to show

	// Images
	public static BufferedImage titleScreen;
	public static BufferedImage aboutScreen;
	public static boolean about = false;

	// Map images
	public static BufferedImage[] ruinsImages = new BufferedImage[5];

	public static ArrayList<dimension>[] ruinsBounds = new ArrayList[5];
	public static ArrayList<dimension>[] ruinsExits = new ArrayList[5];

	public static BufferedImage[] snowdenImages = new BufferedImage[5];
	public static ArrayList<dimension>[] snowdenBounds = new ArrayList[5];
	public static ArrayList<dimension>[] snowdenExits = new ArrayList[5];
	public static BufferedImage replace;

	public static BufferedImage[] flowey = new BufferedImage[2];
	public static ArrayList<dimension>[] floweyBounds = new ArrayList[2];
	public static ArrayList<dimension>[] floweyExits = new ArrayList[2];

	// gameState is the map, setting is the current place
	// [map][setting]
	public static ArrayList<dimension>[][] allBounds = new ArrayList[4][5];
	public static ArrayList<dimension>[][] allExits = new ArrayList[4][5];
	public static BufferedImage[][] allMaps = new BufferedImage[4][5];
	public static dimension[][] allInteractables = new dimension[4][5];
	public static boolean[][] visitedInteractables = new boolean[4][5];

	// Set of coords in specific maps where the map camera needs to move
	public static dimension[][] moveMap = new dimension[3][5];

	// Images that will be faded out and into
	BufferedImage fadeStart;
	BufferedImage fadeEnd;

	// Battle
	// Variables that let the program if user is currently
	// battling or has defeated the boss
	public static boolean battling = false;
	public static boolean winner = false;

	// Player images
	public static BufferedImage player;
	public static BufferedImage brokenHeart;

	// Menu images
	public static BufferedImage[] menuImages = new BufferedImage[4];

	// Menu options
	public static BufferedImage[] selectionImages = new BufferedImage[4];
	public static corner[] optionPos = new corner[4];

	// Slash animation
	public static BufferedImage[] slashImage = new BufferedImage[4];
	public static corner[] slashCoords = new corner[4];

	// Player boundaries in battle
	public static ArrayList<dimension> gameBounds = new ArrayList<dimension>();

	// Font
	public static Font damageFont;

	// Slider
	public static BufferedImage bar;

	// Number of heals
	public static int heals = 0;

	// Boss image
	public static BufferedImage BD;
	public static BufferedImage deadBoss;

	// When user runs out of hp
	public static BufferedImage gameOver;
	public static BufferedImage gameOver2;
	public static int z = 0;
	public static boolean over = false;

	// Attacks
	public static BufferedImage bone;
	public static BufferedImage bone2;
	public static corner[] bonePositions = new corner[11];
	public static corner[] bonePositions2 = new corner[12];

	// Keycode specifically for battle
	public static int xe;

	// To display winning text when claiming trophy
	public static boolean ended = false;

	// Textfile streaming
	public static Scanner sc = new Scanner(System.in);
	public static ArrayList<String>[][] allScripts = new ArrayList[4][5];

	// startScript must be 1-indexed
	public static boolean[][] startScript;
	public static boolean bossDefeated = false;
	public static ArrayList<String> healScript = new ArrayList<String>();
	public static Scanner healText;
	public static boolean grabbedHeal = false;
	public static Font speakingFont;
	// Character images when speaking
	public static BufferedImage dialogueBox;
	public static BufferedImage orion;
	public static BufferedImage nicky;
	public static BufferedImage sans;
	public static BufferedImage clarence;
	public static BufferedImage BDspeaking;

	public static Thread thread;
	
	// other
	animation animation = new animation(this);
	charaAnimation charaAnimation = new charaAnimation(this);
	dialogue dialogue = new dialogue(this);
	audio audio = new audio(this);
	battle battle = new battle(this);
	public undertale() {

		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));

		// Import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));
			aboutScreen = ImageIO.read(new File("assets/aboutScreen.png"));

			// Maps
			snowdenImages[1] = ImageIO.read(new File("assets/maps/snowden/snowden1.png"));
			snowdenImages[2] = ImageIO.read(new File("assets/maps/snowden/snowden2.png"));
			snowdenImages[3] = ImageIO.read(new File("assets/maps/snowden/snowden3.png"));
			snowdenImages[4] = ImageIO.read(new File("assets/maps/snowden/snowden4.png"));
			replace = ImageIO.read(new File("assets/maps/snowden/realsnowden4.png"));

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

			flowey[1] = ImageIO.read(new File("assets/story/Flowey2.png"));

			// Dialogue
			dialogueBox = ImageIO.read(new File("assets/scripts/dialogueBox.png"));
			speakingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);
			orion = ImageIO.read(new File("assets/scripts/orion.png"));
			nicky = ImageIO.read(new File("assets/scripts/nicky.png"));
			clarence = ImageIO.read(new File("assets/scripts/clarence.png"));
			sans = ImageIO.read(new File("assets/scripts/sans.png"));
			BDspeaking = ImageIO.read(new File("assets/scripts/BDspeaking.png"));
			healText = new Scanner(new File("assets/scripts/heal_script.txt"));

			// BATTLE.JAVA
			// Menu / Buttons
			menuImages[1] = ImageIO.read(new File("assets/battleImages/menus/menu1.png"));
			menuImages[2] = ImageIO.read(new File("assets/battleImages/menus/menu2.png"));
			menuImages[3] = ImageIO.read(new File("assets/battleImages/menus/menu3.png"));

			selectionImages[1] = ImageIO.read(new File("assets/battleImages/options/option1.png"));
			selectionImages[2] = ImageIO.read(new File("assets/battleImages/options/option2.png"));
			selectionImages[3] = ImageIO.read(new File("assets/battleImages/options/option3.png"));

			player = ImageIO.read(new File("assets/battleImages/characters/player.png"));
			bar = ImageIO.read(new File("assets/battleImages/menus/bar.jpg"));
			bone = ImageIO.read(new File("assets/battleImages/attacks/bone.png"));
			bone2 = ImageIO.read(new File("assets/battleImages/attacks/bone2.png"));
			BD = ImageIO.read(new File("assets/battleImages/characters/boss.png"));

			brokenHeart = ImageIO.read(new File("assets/battleImages/characters/deadPlayer.png"));
			gameOver = ImageIO.read(new File("assets/battleImages/menus/gameOver.png"));
			gameOver2 = ImageIO.read(new File("assets/battleImages/menus/gameOver2.png"));
			deadBoss = ImageIO.read(new File("assets/battleImages/menus/deadBoss.png"));

			// slash animation
			slashImage[0] = ImageIO.read(new File("assets/battleImages/slash/slash1.1.png"));
			slashImage[1] = ImageIO.read(new File("assets/battleImages/slash/slash2.1.png"));
			slashImage[2] = ImageIO.read(new File("assets/battleImages/slash/slash3.1.png"));
			slashImage[3] = ImageIO.read(new File("assets/battleImages/slash/slash4.1.png"));

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// Register the font
			ge.registerFont(speakingFont);
		}
		catch (Exception e) {
			System.out.println("Image does not exist");
		}

		// Create thread
		thread = new Thread(this);
		thread.start();

		// Add listeners
		addKeyListener(this);
		this.setFocusable(true);
		addMouseListener(this);

		// Create boundaries & exits
		for (int i = 1; i < 5; i++) {
			ruinsBounds[i] = new ArrayList<dimension>();
			ruinsExits[i] = new ArrayList<dimension>();
			snowdenBounds[i] = new ArrayList<dimension>();
			snowdenExits[i] = new ArrayList<dimension>();
		}
		floweyBounds[1] = new ArrayList<dimension>();
		floweyExits[1] = new ArrayList<dimension>();

		for (int i = 1; i < 3; i++) { // map
			for (int x = 0; x < 3; x++) { // setting
				allBounds[i][x] = new ArrayList<dimension>();
				allExits[i][x] = new ArrayList<dimension>();
			}
		}

		// Positions of Chara when entering a new setting
		// ruins1
		allPos[1][1][1] = new corner(480, 220); // Initial position after menu
		allPos[1][1][2] = new corner(15, 340); // In front of exit

		// ruins2
		allPos[1][2][1] = new corner(900, 255); // In front of entrance
		allPos[1][2][2] = new corner(130, 185); // In front of exit

		// ruins3
		allPos[1][3][1] = new corner(85, 240); // In front of entrance
		allPos[1][3][2] = new corner(870, 415); // In front of exit

		// ruins4
		allPos[1][4][1] = new corner(475, 450); // In front of entrance
		allPos[1][4][2] = new corner(470, 80); // In front of exit

		// snowden1
		allPos[2][1][1] = new corner(140, 400); // In front of entrance
		allPos[2][1][2] = new corner(905, 400); // In front of exit

		// snowden2
		allPos[2][2][1] = new corner(10, 360); // In front of entrance
		allPos[2][2][2] = new corner(930, 345); // In front of exit

		// snowden3
		allPos[2][3][1] = new corner(920, 380); // In front of entrance
		allPos[2][3][2] = new corner(60, 155); // In front of exit

		// snowden4
		allPos[2][4][1] = new corner(0, 265); // In front of entrance
		allPos[2][4][2] = new corner(865, 140); // In front of exit

		// Flowey
		allPos[3][1][1] = new corner(450, 120); // In front of entrance
		allPos[3][1][2] = new corner(0,0); // There no other settings beyond this, so no exit

		// Adding boundaries
		// ruins1
		ruinsBounds[1].add(new dimension(new corner (135, -10), new corner (800, 375)));
		ruinsBounds[1].add(new dimension(new corner(-35, 260), new corner(135, 375)));
		ruinsExits[1].add(new dimension(new corner(1000, 625), new corner(1000, 625)));  // there is no entrance so set to max val
		ruinsExits[1].add(new dimension(new corner(-35, 260), new corner(-35, 375))); // exit

		// ruins2
		ruinsBounds[2].add(new dimension(new corner (45, 175), new corner (940, 300)));
		ruinsBounds[2].add(new dimension(new corner (100, 150), new corner (170, 175)));
		ruinsExits[2].add(new dimension(new corner(940,175), new corner(940, 300))); // entrance
		ruinsExits[2].add(new dimension(new corner(100,150), new corner(170, 150))); // exit

		// ruins3
		ruinsBounds[3].add(new dimension(new corner(55, 180), new corner(135,285)));
		ruinsBounds[3].add(new dimension(new corner(135, 125), new corner(765,460)));
		ruinsBounds[3].add(new dimension(new corner(765, 355), new corner(900,460)));
		ruinsExits[3].add(new dimension(new corner(55,180), new corner(55,285))); // entrance
		ruinsExits[3].add(new dimension(new corner(900,355), new corner(900,460))); // exit

		// ruins4
		ruinsBounds[4].add(new dimension(new corner(450, 275), new corner(495,480)));
		ruinsBounds[4].add(new dimension(new corner(250, 270), new corner(695,280)));
		ruinsBounds[4].add(new dimension(new corner(395, 60), new corner(550,270)));
		ruinsBounds[4].add(new dimension(new corner(465,40), new corner(475,60)));
		ruinsExits[4].add(new dimension(new corner(445,480), new corner(500,480))); // entrance
		ruinsExits[4].add(new dimension(new corner(465,40), new corner(475, 40))); // exit

		// snowden1
		snowdenBounds[1].add(new dimension(new corner(210, 265), new corner(365, 320)));
		snowdenBounds[1].add(new dimension(new corner(210, 320), new corner(945, 470)));
		snowdenBounds[1].add(new dimension(new corner(90, 370), new corner(210, 470)));
		snowdenExits[1].add(new dimension(new corner(90, 370), new corner (90, 415))); // entrance
		snowdenExits[1].add(new dimension(new corner(945, 320), new corner(945, 470))); // exit

		// snowden2
		snowdenBounds[2].add(new dimension(new corner(-25, 250), new corner(950, 445)));
		snowdenBounds[2].add(new dimension(new corner(70, 0), new corner(605, 250)));
		snowdenBounds[2].add(new dimension(new corner(605, 180), new corner(820, 250)));
		snowdenExits[2].add(new dimension(new corner(-25, 250), new corner(-25, 445))); // entrance
		snowdenExits[2].add(new dimension(new corner(950, 250), new corner(950, 445))); // exit

		// snowden3
		snowdenBounds[3].add(new dimension(new corner(805, 310), new corner(945, 400)));
		snowdenBounds[3].add(new dimension(new corner(525, 160), new corner(805, 400)));
		snowdenBounds[3].add(new dimension(new corner(125, 250), new corner(525, 400)));
		snowdenBounds[3].add(new dimension(new corner(0, 160), new corner(390, 250)));
		snowdenBounds[3].add(new dimension(new corner(45, 130), new corner(75, 160)));
		snowdenExits[3].add(new dimension(new corner(945, 310), new corner(945, 400))); // entrance
		snowdenExits[3].add(new dimension(new corner(45, 130), new corner(75, 130))); // exit

		// snowden4
		snowdenBounds[4].add(new dimension(new corner(-40, 185), new corner(355,290)));
		snowdenBounds[4].add(new dimension(new corner(65, 10), new corner(355,290)));
		snowdenBounds[4].add(new dimension(new corner(355, 130), new corner(450,140)));
		snowdenBounds[4].add(new dimension(new corner(455, 130), new corner(495, 140)));

		// add this boundary below when the boss is beat so the player can progress
		//snowdenBounds[4].add(new dimension(new corner(495, 130), new corner(880, 140)));

		snowdenBounds[4].add(new dimension(new corner(880, 130), new corner(910,155)));
		snowdenBounds[4].add(new dimension(new corner(880, 155), new corner(895,175)));
		snowdenBounds[4].add(new dimension(new corner(880, 175), new corner(910,280)));
		snowdenExits[4].add(new dimension(new corner(-40, 190), new corner(-40, 305))); // entrance
		snowdenExits[4].add(new dimension(new corner(910, 130), new corner(910, 155))); // exit

		// Flowey
		floweyBounds[1].add(new dimension(new corner(10, 15), new corner(300, 495)));
		
		floweyBounds[1].add(new dimension(new corner(300, 110), new corner(615, 245)));
		floweyBounds[1].add(new dimension(new corner(300, 245), new corner(380, 430)));
		floweyBounds[1].add(new dimension(new corner(535, 245), new corner(615, 430)));
		floweyBounds[1].add(new dimension(new corner(300, 430), new corner(615, 495)));

		floweyBounds[1].add(new dimension(new corner(615, 15), new corner(910, 495)));
		floweyBounds[1].add(new dimension(new corner(440, 75), new corner(470, 110)));
		floweyExits[1].add(new dimension(new corner(1, 1), new corner(1,1)));
		floweyExits[1].add(new dimension(new corner(440, 75), new corner(470,75)));

		// Putting them all in a list
		allBounds[1] = ruinsBounds;
		allBounds[2] = snowdenBounds;
		allBounds[3] = floweyBounds;

		allMaps[1] = ruinsImages;
		allMaps[2] = snowdenImages;
		allMaps[3] = flowey;

		allExits[1] = ruinsExits;
		allExits[2] = snowdenExits;
		allExits[3] = floweyExits;

		// Coords to move maps
		moveMap[1][3] = new dimension(new corner(0, 290), new corner(0, 900));
		moveMap[1][4] = new dimension(new corner(0, 435), new corner(0, 900));
		moveMap[2][1] = new dimension(new corner(370, 0), new corner(1200, 0));
		moveMap[2][4] = new dimension(new corner(445, 0), new corner(1115, 0));

		// Slash animation coords
		slashCoords[0] = new corner(410, 0);
		slashCoords[1] = new corner(410, 40);
		slashCoords[2] = new corner(400, 70);
		slashCoords[3] = new corner(410, 70);

		// Interactables
		// ruins
		allInteractables[1][3] = new dimension(new corner(230, 860), new corner(355, 970));  // the bottom red bush, use globalPos

		// snowden
		allInteractables[2][1] = new dimension(new corner(210, 265), new corner(345, 265));  // the bush
		allInteractables[2][2] = new dimension(new corner(705, 180), new corner(770, 180));  // sans
		allInteractables[2][3] = new dimension(new corner(390, 195), new corner(525, 250));  // clarence (the snowman)
		allInteractables[2][4] = new dimension(new corner(1225, 130), new corner(1225, 140));  // the boss, must use globalPos
		allInteractables[3][1] = new dimension(new corner(380, 245), new corner(535, 430)); // the trophy

		// scripts (one indexed)
		startScript = new boolean[4][5];
		startScript[1] = new boolean[] {false, true, false, true, false};
		startScript[2] = new boolean[] {false, true, true, true, true};
		startScript[3] = new boolean[] {false, true, false, false, false};

		for (int i = 1; i < 4; i++) {
			for (int x = 1; x < 5; x++) {

				try {
					allScripts[i][x] = new ArrayList<String>();
					Scanner currentFile = new Scanner(new File("assets/scripts/" + i + "_" + x + "_script.txt"));
					while (currentFile.hasNextLine()){
						allScripts[i][x].add(currentFile.nextLine());
					}
				}
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				}

			}
		}

		while (healText.hasNextLine()) {
			healScript.add(healText.nextLine());
		}

		// Font
		try {
			damageFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(50f);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			//register the font
			ge.registerFont(damageFont); }
		catch (IOException | FontFormatException e) {
			e.printStackTrace(); }

		// Use the font
		this.setFont(damageFont);

		// Menu options
		optionPos[1] = new corner(235, 539);
		optionPos[2] = new corner(442, 539);
		optionPos[3] = new corner(650, 539);

		// Boundaries when in battle
		gameBounds.add(new dimension(new corner(310, 295), new corner(655,445)));
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		undertale panel = new undertale();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);


	}

	
	/*
	 * The method that paints the screen accordingly to each game state or
	 * if we are in battle mode. It also paints the dialogue for the game.
	 * 
	 * 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animation.alpha));

		if (!battling) {
			// When the boss has been defeated, the program should sleep for two seconds
			// as an image is drawn with text
			if (bossDefeated) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				audio.x = 1;
				bossDefeated = false;
			}
			
			// Playing the music of each map
			try {
				audio.playMusic(gameState);
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
				e1.printStackTrace();
			}
			
			// Whenever interacting with an object or person, the footstep sound should stop
			// **When going through the gate to the last room, the footsteps continuously play,
			// **so this is needed
			if(gameState == 3 && audio.footP || (grabbedHeal && audio.footP)) {audio.clip2.stop(); audio.footP = false; System.out.println("lionel x roman");}

			// Fade
			if (animation.fading) {
				if (!animation.faded) {
					g2d.drawImage(fadeStart, mapX, mapY, null);

					// fade out the character as well
					if (gameState - 1 != 0 && 1 <= gameState && gameState <= 3) {
						g2d.drawImage(charaImages[curChara], charaX, charaY, null);
					}


				} else {
					// change chara position
					if(gameState != 0) {
						charaX = allPos[gameState][setting][change].x;
						charaY = allPos[gameState][setting][change].y;
					}
					
					// Used to determine if map needs to move at a certain point
					// if Chara spawns at the bottom
					if (charaY > 312 && gameState == 1 && setting == 3) {
						globalPos = 1025;

					// Globalpos in ruins3 should be tracking her Y-value
					} else if (charaY < 312 && gameState == 1 && setting == 3) {
						globalPos = charaY;
					} 
					// if Chara spawns in the lower half of ruins4
					else if (charaY > 312 && gameState == 1 && setting == 4) {
						globalPos = 1075;
					} 
					// if Chara spawns in the upper half of ruins4
					else if (charaY < 312 && gameState == 1 && setting == 4) {
						globalPos = 240;
					} 
					// if Chara spawns on the right side of any snowden map
					// besides the fourth
					else if (gameState == 2 && charaX > 625 && setting != 4) {
						globalPos = (1250 - (640 - charaX));
					} 
					// if Chara spawns on the left side of any snowden map
					// besides the fourth
					else if (gameState == 2 && charaX < 625 && setting != 4) {
						globalPos = charaX;
					}

					g2d.drawImage(fadeEnd, mapX, mapY, null);
					// fade in the character as well
					if (1 <= gameState && gameState <= 3) {
						g2d.drawImage(charaImages[curChara], charaX, charaY, null);
					}
				}
			}
			// draw about screen
			else if(about) {
				g2d.drawImage(aboutScreen, 0, 0, null);
			}

			// the start menu
			else if (gameState == 0 && !about) {
				g2d.drawImage(titleScreen, 0, 0, null);
			}

			// exploration
			else if (1 <= gameState && gameState <= 3) {
				// snowden4 has the boss in it, so he should disappear when defeated
				if(winner) {allMaps[2][4] = replace;}
				g2d.drawImage(allMaps[gameState][setting], mapX, mapY, null);

				// animation
				try {
					Thread.sleep(60);
					g2d.drawImage(charaImages[curChara], charaX, charaY, null);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		else if (battling) {
			// font
			g.setColor(new Color(255, 255, 255));
			g.setFont(speakingFont);

			// menu background
			if (battle.menuState <= 3 || (battle.menuState == 5 && !winner)) {
				if (battle.menuState != 5) {
					g.drawImage(menuImages[battle.menuState], 0, 0, null);
				} 
				// needed so screen does not flicker when the boss is attacked
				else {
					g.drawImage(menuImages[2], 0, 0, null);
				}
				g.drawImage(BD, 430, 85, null);
			}

			// stats
			if (!(battle.menuState > 3) || (battle.menuState == 5 && !winner)) {
				g.drawString(battle.health + "", 345, 518);
				g.drawString("50", 425, 518);
				g.drawString(heals + "", 645, 518);
				g.drawString("5", 710, 518);
				g.drawString("BOSS HEALTH: ", 385, 75);
				g.drawString(battle.bossHealth + "", 575, 75);
			}

			// if u lose or win
			if (battle.menuState > 3 || battle.slashDraw) {
				// lose
				if (battle.menuState == 4) {
					// We use if statements to ensure that the order of
					// execution is correct as the thread.sleep causes
					// issues with the order. Menus / Text need to be redrawn
					// as the text for HP freezes at "1", then the sleep runs.
					if (z == 0) {
						if (battle.x == 0) {
							// redrawing all the background images
							g.drawImage(menuImages[3], 0, 0, null);
							g.drawImage(BD, 430, 85, null);
							g.drawString(battle.health + "", 345, 518);
							g.drawString("50", 425, 518);
							g.drawString(heals + "", 645, 518);
							g.drawString("5", 710, 518);
							g.drawString("BOSS HEALTH: ", 385, 75);
							g.drawString(battle.bossHealth + "", 575, 75);
							battle.x++;
							System.out.println("runed");
						} 
						else if (battle.x == 1) {
							// clearing the screen for heart crack (dying screen)
							super.paintComponent(g);
							g.drawImage(brokenHeart, battle.playerX, battle.playerY, null);
							battle.x++;
							System.out.println("runed2");
						} 
						else if (battle.x == 2) {
							battle.x++;
							battle.dead = true;
							System.out.println("runed3");
						}
					}
					// when player dies
					if (battle.dead) {
						// over makes sures that no key inputs work until the text
						// "press z to continue" appears
						over = true;
						try {
							audio.playMusic(gameState);
						} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							e1.printStackTrace();
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						battle.dead = false;
					} 
					// z controls when inputs work
					else if (battle.x == 3) {
						z++;
						if (z < 30) {
							g.drawImage(gameOver2, 0, 0, null);
						}
						if (z >= 30) {
							g.drawImage(gameOver, 0, 0, null);
							over = false;
						}
					}
				}

				// When dealing damage to the boss, it will create
				// a slash animation. To do so, you have to redraw
				// all the images and the slash images
				else if (battle.slashDraw) {
					if (battle.num <= 3) {
						g.drawImage(menuImages[2], 0, 0, null);
						g.drawImage(bar, battle.barX, 314, null);
						g.drawImage(BD, 430, 85, null);
						g.drawString(battle.health + "", 345, 518);
						g.drawString("50", 425, 518);
						g.drawString(heals + "", 645, 518);
						g.drawString("5", 710, 518);
						g.drawString("BOSS HEALTH: ", 385, 75);
						g.drawString(battle.bossHealth + "", 575, 75);
						g.drawString("MINUS " + battle.damage + " HP", 620, 150);
						// hit sound
						if (battle.playHit) {
							try {
								audio.hitSound(battle.damage);
							} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
								e1.printStackTrace();
							}
							battle.playHit = false;
						}
						g.drawImage(slashImage[battle.num], slashCoords[battle.num].x, slashCoords[battle.num].y, null);
					}
					// Reset variable values to ensure
					// that the animation can be reused
					else {
						if (battle.menuState != 5) {
							g.drawImage(bar, battle.barX, 314, null);
						}
						battle.slashDraw = false;
						battle.slash = false;
						battle.num = 0;
						System.out.println("bared: " + battle.barX);
					}

					// If the boss is defeated
					if (battle.menuState == 5 && !battle.slashDraw) {
						// specific image of boss dead
						g.drawImage(deadBoss, 0, 0, null);

						// make sure that you cannot fight the boss again
						allInteractables[2][4] = new dimension(new corner(-1, -1), new corner(-1, -1));
						snowdenBounds[4].add(new dimension(new corner(495, 130), new corner(880, 140)));
						battling = false;
						
						// both variables indicate that the user has won
						// winner is for interactive purposes and ensuring
						// that certain images are not drawn
						winner = true;
						// bossDefeated is specifically used for making the 
						// program sleep at a certain point
						bossDefeated = true;
					}
				}
			} 
			else {
				// highlight selection
				if (battle.menuState == 1) {
					g.setFont(speakingFont);
					if (battle.textState == 1) {
						g.drawString("What will Chara do?", 75, 360);
					} 
					// text that will be displayed when using a heal
					else if (battle.textState == 2) {
						if (battle.health == 50) {
							g.drawString("You are MAX HP!", 75, 360);
							g.drawString("Press 'Z' again to continue", 75, 390);
						} 
						else if (heals == 0) {
							g.drawString("You are out of heals!", 75, 360);
							g.drawString("Press 'Z' again to continue", 75, 390);
						} 
						else {
							g.drawString("You healed 10 HP! You have " + heals + " left", 75, 360);
							g.drawString("Press 'Z' again to continue", 75, 390);
						}
					} 
					// fleeing
					else if (battle.textState == 3) {
						g.drawString("You fled the scene", 75, 360);
						g.drawString("Press 'Z' again to continue", 75, 390);
					}
					g.drawImage(selectionImages[battle.selectionState], optionPos[battle.selectionState].x, optionPos[battle.selectionState].y, null);
				}
				// screen with the slider
				else if (battle.menuState == 2) {
					// Reset bar values
					if (battle.count == 0) {
						battle.count = 1;
						battle.barX = 60;
						battle.damage = 0;
						battle.stopBar = false;
					}
					// when bar has been stopped
					if (battle.stopBar) {
						try {
							System.out.println("damaged");
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}

						// deciding the variation of the attack (coming from left or right)
						if (Math.random() >= 0.50)
							battle.variation = 1;
						else battle.variation = 2;
						System.out.println("variation = " + battle.variation);

						// deciding which attack
						if (battle.attack == 1) {
							firstAttack();
						} 
						else if (battle.attack == 2) {
							secondAttack();
						}
						
						battle.menuState = 3;
						battle.stopBar = false;
					} 
					// condition is to ensure that the bar is not drawn
					// when using a heal
					else if (battle.selectionState != 2) {
						g.drawImage(bar, battle.barX, 314, null);
						// stopping the bar when it has reached the end
						// or user has clicked space
						if (battle.barX >= 920 || battle.damage >= 1) {
							battle.stopBar = true;
						}
						// if bar has not been stopped, add 20 to its x-value
						if (!battle.stopBar) {
							battle.barX += 20;
						}
					}
				} 
				// the battle itself
				else if (battle.menuState == 3) {
					g.drawImage(player, battle.playerX, battle.playerY, null);

					// drawing the attacks
					if (battle.attack == 1) {
						for (int i = 1; i < 11; i++) {
							corner cur = bonePositions[i];
							if (297 < cur.x && cur.x < 670) {
								g.drawImage(bone, cur.x, cur.y, null);
							}
						}
					} 
					else if (battle.attack == 2) {
						for (int i = 1; i < 11; i++) {
							corner cur = bonePositions2[i];
							if (battle.variation == 1) {
								if (cur.x < 680) {
									g.drawImage(bone2, cur.x, cur.y, null);
								}
							} 
							else {
								if (cur.x > 297) {
									g.drawImage(bone2, cur.x, cur.y, null);
								}
							}
						}
					}

				}
			}
		}
		
		
		// put this at the bottom because game must draw first and then dialogue
		/*
		 * This if statement checks if there is dialogue on the screen.
		 * If there is, it will draw a box and the dialogue from the appropriate text-file
		 * from gameState and setting.
		 */
		if (dialogue.speaking && !animation.fading && 1 <= gameState && gameState <= 3) {
			g.setColor(new Color(255, 255, 255));
			g.setFont(speakingFont);
			g.drawImage(dialogueBox, 40, 420, null);

			// orion appears every time gameState is odd
			// nicky appears every time gameState is even
			// they appear everywhere except for
			// gameState 2, settings 2-4
			if (!(gameState == 2 && (setting != 1))) {
				if (setting == 1) g.drawImage(orion, 53, 430, null);
				else if (setting == 3 || setting == 4) g.drawImage(nicky, 53, 430, null);
			} 
			else if (gameState == 2 && setting == 2)
				g.drawImage(sans, 100, 430, null);
			else if (gameState == 2 && setting == 3)
				g.drawImage(clarence, 100, 450, null);
			else if (gameState == 2 && setting == 4)
				g.drawImage(BDspeaking, 53, 430, null);

			for (int i = 0; i < 3; i++) {
				// text when grabbing a heal
				if (grabbedHeal) {
					g.drawString(healScript.get(i), 300, 485);
					g.drawString("You now have " + heals + ((heals > 1) ? " heals." : " heal."), 300, 485 + 40);

				} 
				// text when grabbing the trophy
				else if (ended) {
					g.drawString("You got a #1 Trophy!", 300, 485);
					g.drawString("Thank you for playing our game.", 300, 525);


				}
				else if (i + dialogue.startLine < allScripts[gameState][setting].size()) {
					g.drawString(allScripts[gameState][setting].get(i + dialogue.startLine), 300, 485 + 40 * i);
				} 
				else {
					dialogue.startLine = 0;
					dialogue.speaking = false;
					break;
				}
			}

			// turn starting scripts off
			if (startScript[gameState][setting]) {
				startScript[gameState][setting] = false;
			}
		}
	}
	
	
	/*
	 * The keyboard method that updates the player's position during exploration.
	 * Or if battle, it will call a keyboard method in battle.java that pretty much
	 * does the same thing but it adjusted to battle conditions
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		xe = 0;
		// Over is only true when the player dies in the boss fight
		// The losing screen makes the program freeze, so no inputs
		// should work during this time
		if(!over) {xe = e.getKeyCode();}

		// Key code for Chara's animation
		charaAnimation.x = e.getKeyCode();
	
		if(!battling) {
			System.out.println("key has pressed");
			if (!animation.fading && 1 <= gameState && gameState <= 3) {
				// change animation to run method as it depends on each click
				// for animation to change directions
				if (dialogue.speaking) {
					System.out.println("dialg");
					dialogue.keyPressed(e);
				}

				// when playing clicks the interact button on a interactable that has not been used
				else if ((e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') && !visitedInteractables[gameState][setting]) {
					// stop footstep audio if its playing
					if(audio.footP) {audio.footS = true;}
					try {
						audio.footstep(gameState);
					}
					catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
						e1.printStackTrace();
					}
					
					if (allInteractables[gameState][setting] != null) {
						int newX = charaX;
						int newY = charaY;
						if (gameState == 1 && setting == 3) {
							newY = globalPos;
						}
						else if (gameState == 2 && setting == 4) {
							newX = globalPos;
						}

						if (interact(newX, newY, allInteractables[gameState][setting])) {
							System.out.println("interacted");
							if(!(!winner && gameState == 2 && setting == 4)){
								visitedInteractables[gameState][setting] = true;
							}
							if (!battling)
								dialogue.speaking = true;
						}
					}
				}

				else {
					// movement
					if(e.getKeyCode() == 38)
					{
						up = true;
						System.out.println("orion");
					}
					if(e.getKeyCode() == 37)
					{
						left = true;
						System.out.println("orion2");
					}
					if(e.getKeyCode() == 40)
					{
						down = true;
						System.out.println("orion3");
					}
					if(e.getKeyCode() == 39)
					{
						right = true;
						System.out.println("orion4");
					}
					System.out.println();
					System.out.println("globalPos: " + globalPos);
					System.out.println("mapX: " + mapX + " mapY: " + mapY);

					// Deciding if Chara is going to the next or previous map
					// Exit array was specifically organized for index 1 to be the exit
					// and index 2 to be the entrance
					change = withinExit(charaX, charaY, allExits[gameState][setting]);
					System.out.println("chara x = " + charaX + " " + "charaY = " + charaY);

					if (change != 0) {
						// stop footstep audio
						audio.footS = true;
						try {
							audio.footstep(gameState);
						}
						catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							e1.printStackTrace();
						}
						// save the current map
						fadeStart = allMaps[gameState][setting];
						System.out.println(gameState + " " + setting + " change " + change);

						// go to next map
						if (change == 1) {
							System.out.println("next setting");
							// if there are no more maps
							if (gameState + 1 == 4) {
								setting = 4;
								gameState--;
								change = 2;
							}

							// going to next map
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

						// put this right after change
						// if the next setting has a starting script
						if (startScript[gameState][setting]) {
							dialogue.speaking = true;
							System.out.println("start script for " + gameState + " " + setting + " is now " + startScript[gameState][setting]);
						}

						System.out.println("before: " + gameState + " " + setting);
						fadeEnd = allMaps[gameState][setting];
						System.out.println("new: " + gameState + " " + setting);
						dialogue.startLine = 0;
						animation.fade(fadeStart, fadeEnd, "fast");
					}
				}
			}
		}
		// battling key inputs
		else if(battling && xe != 0) {
			battle.keyPressed(e);
		}
	}
	
	
	/*
	 * A method that tracks mouse interaction ONLY for the 
	 * title screen, since our game is keyboard based for everything else.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

		mouseX = e.getX();
		mouseY = e.getY();
		System.out.println(mouseX + " " + mouseY);
		if (gameState == 0 && !animation.fading) {

			// play game
			if (!about) {
				if (370 <= mouseX && mouseX <= 670 && 185 <= mouseY && mouseY <= 270) {
					animation.wait = false;
					fadeStart = titleScreen;
					fadeEnd = ruinsImages[1];
					gameState = 2;
					setting = 4;
					dialogue.speaking = true;
					animation.fade(fadeStart, fadeEnd, "fast");


				}

				// about
				else if (370 <= mouseX && mouseX <= 670 && 300 <= mouseY && mouseY <= 390) {
					System.out.println("about");
					about = true;

					// change to about screen
					animation.wait = false;
					fadeStart = titleScreen;
					fadeEnd = aboutScreen;
					animation.fade(fadeStart, fadeEnd, "fast");


				}
				// quit
				else if (370 <= mouseX && mouseX <= 670 && 420 <= mouseY && mouseY <= 505) {
					System.out.println("quit");
					System.exit(0); // terminates the program
				}
			}
			// back button in the about screen
			else {
				if(30 <= mouseX && mouseX <= 93 && 571 <= mouseY && mouseY <= 600) {
					animation.wait = false;
					fadeStart = aboutScreen;
					fadeEnd = titleScreen;
					animation.fade(fadeStart, fadeEnd, "fast");
					about = false;
				}
			}
		}
	}
	
	
	/*
	// Takes in character's current position and the array of boundaries for that
	// specific setting. Then, it checks if the character's current position is within
	// the X and Y values of any of the boundaries. Within those values, she will be
	// able to move freely. Otherwise, if her values go beyond the boundaries, the array
	// will return false and she will not be able to move.
	 * 
	 */

	public static boolean withinBounds(int x, int y, ArrayList<dimension> q) {
		for (dimension cur: q) { // for every boundary in the current setting
			// check if within top left corner
			corner topL = cur.topLeft;
			corner bottomR = cur.bottomRight;
			if (topL.x <= x && x <= bottomR.x && topL.y <= y && y <= bottomR.y) {
				return true;
			}

		}
		return false;
	}

	
	/*
	 *	For each exit in the setting, the method checks if you
		have entered a exit/entrance vertically or horizontally using
		the two boolean variables. If any of those are true, we can tell
		if it is an exit or entrance through the value of i. When i is 1,
		you have entered an entrance, and when i is 2, you entered an exit
		
		1 equals you go to the entrance of the next setting
		0 equals we haven't exited
		2 equals go to the exit of the previous setting
	 */
	public static int withinExit(int x, int y, ArrayList<dimension> exits) {

		for (int i = 0; i < 2; i++) {
			dimension cur = exits.get(i);

			boolean exitingVertically = cur.topLeft.x <= x && x <= cur.bottomRight.x && cur.topLeft.y == y;
			boolean exitingHorizontally = cur.topLeft.y <= y && y <= cur.bottomRight.y && cur.topLeft.x == x;
			if(exitingVertically) {
				System.out.println("VERTICAL");
			}
			else if(exitingHorizontally) {
				System.out.println("HORIZONTAL");
			}


			if (exitingVertically || exitingHorizontally) {

				// resetting movement
				up = false;
				down = false;
				right = false;
				left = false;

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

	/*
	 * This method resets chara's default standing position when a key is released
	 * so that he doesn't have one foot up when he is standing still.
	 */
	public void keyReleased(KeyEvent e) {
		int x = e.getKeyCode();
		if(!animation.fading && 1 <= gameState && gameState <= 3 && !dialogue.speaking) {
			// stop footstep audio on release
			if(x >= 37 && x <= 40 && audio.footP && !battling) {
				audio.footS = true;
				try {
					audio.footstep(gameState);
				} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			// movement in battle and out of battle works the same
			if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) {
				System.out.println("up");
				up = false;
				battle.up = false;
				if(!battling) {curChara = 0;}
			}

			if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
				down = false;
				battle.down = false;
				if(!battling) {curChara = 2; charaAnimation.legS = false;}
			}


			if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
				left = false;
				battle.left = false;
				if(!battling) {curChara = 1; charaAnimation.legA = false;}
			}


			if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
				right = false;
				battle.right = false;
				if(!battling) {curChara = 3;}
			}

		}
	}

	/*
	 * This thread is needed since our game is dynamic.
	 * It repaints the screen constantly.
	 * There 2 applications of this thread are to update the screen
	 * during exploration and in battle.
	 */
	public void run() {
		while(true) {
			if(!battling) {
				try {
					Thread.sleep(1000 / fps);
					// movement
					if (up && withinBounds(charaX, charaY - charaSpeed, allBounds[gameState][setting])) {
						charaAnimation.key = 1;
						if(gameState == 1) {
							// moving map vertically
							if(setting > 2 && moveMap[gameState][setting].topLeft.y < globalPos && globalPos <= moveMap[gameState][setting].bottomRight.y) {
								mapY += charaSpeed;
								globalPos -= charaSpeed;
							}
							// moving chara if there is no move map
							else {
								charaY -= charaSpeed;
								globalPos -= charaSpeed;
							}
						}
						// move chara
						else {
							charaY -= charaSpeed;
						}
						charaAnimation.run();

					}
					if (down && withinBounds(charaX, charaY + charaSpeed, allBounds[gameState][setting])) {
						charaAnimation.key = 3;
						if(gameState == 1) {
							// moving map vertically
							if(setting > 2 && moveMap[gameState][setting].topLeft.y <= globalPos && globalPos < moveMap[gameState][setting].bottomRight.y) {
								mapY -= charaSpeed;
								globalPos += charaSpeed;
							}
							// moving chara if there is no move map
							else {
								charaY += charaSpeed;
								globalPos += charaSpeed;
							}
						}
						// move chara
						else {
							charaY += charaSpeed;
						}
						charaAnimation.run();


					}
					if (left && withinBounds(charaX - charaSpeed, charaY, allBounds[gameState][setting])) {
						charaAnimation.key = 2;
						// moving map horizontally
						if(gameState == 2 && (setting == 1 || setting == 4)) {
							if(moveMap[gameState][setting].topLeft.x < globalPos && globalPos <= moveMap[gameState][setting].bottomRight.x) {
								mapX += charaSpeed;
							}
							// move chara when there is no move map
							else {
								charaX -= charaSpeed;
							}
							globalPos -= charaSpeed;
						}
						// move chara
						else {
							charaX -= charaSpeed;
						}
						charaAnimation.run();

					}
					if (right && withinBounds(charaX + charaSpeed, charaY, allBounds[gameState][setting])) {
						charaAnimation.key = 4;
						// moving map horizontally
						if(gameState == 2 && (setting == 1 || setting == 4)) {
							if(moveMap[gameState][setting].topLeft.x <= globalPos && globalPos < moveMap[gameState][setting].bottomRight.x) {
								mapX -= charaSpeed;
							}
							// moving chara when there is no move map
							else {
								charaX += charaSpeed;
							}
							globalPos += charaSpeed;
						}
						// move chara
						else {
							charaX += charaSpeed;
						}
						charaAnimation.run();
					}

					// Whenever you do any movement, the footstep audio should playing
					// when you are in snowden and in the snow
					if((up || down || left || right) && gameState == 2) {
						audio.footS = false;
						try {
							audio.footstep(gameState);
						} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint();
			}

			else if(battling){
				// play boss music
				try {audio.playMusic(gameState);}
				catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();}
				// use the run() in the battle file
				battle.run();
			}

		}

	}

	
	/*
	 * The interact method is run whenever the user presses 'Z' or 'z'.
	 * It is used to approach objects and check if they can find a heal.
	 * The logic is basically the same as withinBounds, where the code
	 * checks if the user is within a certain area when they press the interact
	 * key.
	 */
	public static boolean interact(int x, int y, dimension object) {
		up = false;
		down = false;
		right = false;
		left = false;

		System.out.println("x: " + object.topLeft.x + " < " + x + " < " + object.bottomRight.x);
		System.out.println("y: " + object.topLeft.y + " < " + y + " < " + object.bottomRight.y);


		if (object.topLeft.x <= x && x <= object.bottomRight.x && object.topLeft.y <= y && y <= object.bottomRight.y) {
			if (gameState == 2 && setting == 4) {
				battling = true;
			}

			else if (gameState == 3 && setting == 1) {
				ended = true;
			}
			else {
				grabbedHeal = true;
				heals++;
			}
			return true;
		}

		return false;
	}
	// Attacks
	
	/*
	 * This method initializes the first Attack (where bones appear
	 * top and bottom only). It creates an array that stores the positions
	 * depending on variation of each bone. 
	 */
	public void firstAttack() {
		int k = (battle.variation == 1) ? 680: 290;
		bonePositions[0] = new corner(Integer.MIN_VALUE, 0);
		for (int i = 1; i < 11; i++) {
			if (i % 2 == 0)
				bonePositions[i] = new corner(k, 295);
			else
				bonePositions[i] = new corner(k, 370);
		}
	}
	
	/*
	 * This method is to update the bone positions for the first 
	 * attack (where bones appear top and bottom only). It loops
	 * through the array that stores the position of each bone 
	 * and updates accordingly to the variation.
	 * 
	 */
	public void updateFirstAttack() {
		int k = (battle.variation == 1) ? 1: -1;
		for (int i = 1; i < 11; i++) {

			// if the gap is greater than 100 pixels
			// update the next bone
			if (Math.abs(bonePositions[i].x - bonePositions[i - 1].x) >= 100) {
//				System.out.println("bone x" + i + " = " + bonePositions[i].x);
//				System.out.println("bone y" + i + " = " + bonePositions[i].y);

				// speed must be odd number not equal to 5 for some logic reason
				if (battle.bossHealth > 55)
					bonePositions[i].x -= 3 * k;
				else {
					bonePositions[i].x -= 7 * k;
				}
			}
		}
	}
	
	/*
	 * This method is to initialize the second attack (where bones are thrown)
	 * It creates the starting positions of every bone depending on the 
	 * variation. It then stores some important variables such as startBone and 
	 * endBone to determine when the attack is over.
	 */
	public void secondAttack() {

		// if variation is 1, bones will come from the right
		// if variation is 2, bones will come from the left
		int k = (battle.variation == 1) ? 680: 290 - 69; // 69 is the horizontal boneWidth for bone2
		System.out.println(battle.variation);
		for (int i = 1; i < 12; i++) {
			System.out.println(k);
			bonePositions2[i] = new corner(k, 295 + (i - 1) * battle.boneWidth);
			battle.boneOrder.add(i);
		}

		battle.bonesReleased = new boolean[12];

		Collections.shuffle(battle.boneOrder);
		battle.endBone = battle.boneOrder.get(battle.boneOrder.size() - 1);
		battle.startBone = battle.boneOrder.poll();
		battle.bonesReleased[battle.startBone] = true;

	}
	
	/*
	 * This method is to update the second attack (where bones are thrown)
	 * of the boss during the battle. It loops through the coordinates of 
	 * each bone and sees if it is their turn to launch by measuring the gap
	 * between it and the previous bone launched. 
	 */
	
	public void updateSecondAttack() {
		
		// if the variation is 1, the bones will go from right to left
		// if the variation is 2, the bones will go from left to right
		// k represents the direction; 1 is right to left, -1 is left to right
		int k = (battle.variation == 1) ? 1: -1;
		

		for (int i = 1; i < 12; i++) {

			// if the gap is greater than 100 pixels
			// update the next bone
			if (battle.bonesReleased[i]) {
				// speed must be odd number not equal to 5 for some logic reason
				if (battle.bossHealth > 50)
					bonePositions2[i].x -= 13 * k;
				else
					bonePositions2[i].x -= 17 * k;
			}
		}

		// for every multiple of 100 that the first bone has traveled, we will release a next bone
		// System.out.println(bonePositions2[startBone].x);

		if (battle.boneOrder.size() > 0) {
			if (battle.variation == 1) {
				if (bonePositions2[battle.startBone].x - 680 + (100 * (11 - battle.boneOrder.size())) < 0) {
					System.out.println("yo");
					battle.bonesReleased[battle.boneOrder.poll()] = true;

				}
			}
			else if (battle.variation == 2){
				// starting position is 228, but we do 229 since 0 % 100 == 0
				if (bonePositions2[battle.startBone].x - 228 - (100 * (11 - battle.boneOrder.size())) > 0) {
					System.out.println("yo");
					battle.bonesReleased[battle.boneOrder.poll()] = true;

				}
			}
		}
	}




	// create an objects to measure the boundaries
	
	/*
	 * The corner class is meant to represent a corner of a rectangle in
	 * form of (x, y). It is also used in arrays that store the position
	 * of the player because the position is also represented in terms of (x, y)
	 */
	public static class corner{

		public int x;
		public int y;
		public corner(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	/*
	 * The dimension class is meant to represent a theoretical rectangle given
	 * 2 corners. The 2 corners are the top left and the bottom right, which you can
	 * draw a rectangle out of.
	 */
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