import javax.swing.*;
import java.awt.*;
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

	public static int fps = 20;

	public static boolean up = false;
	public static boolean left = false;
	public static boolean down = false;
	public static boolean right = false;
	public static boolean test = false;


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

	public static BufferedImage[] snowdenImages = new BufferedImage[5];
	public static ArrayList<dimension>[] snowdenBounds = new ArrayList[5];
	public static ArrayList<dimension>[] snowdenExits = new ArrayList[5];

	public static BufferedImage[] flowey = new BufferedImage[2];
	public static ArrayList<dimension>[] floweyBounds = new ArrayList[2];
	public static ArrayList<dimension>[] floweyExits = new ArrayList[2];

	// gameState is the map, setting is the current place
	public static ArrayList<dimension>[][] allBounds = new ArrayList[4][5];
	public static ArrayList<dimension>[][] allExits = new ArrayList[4][5];
	public static BufferedImage[][] allMaps = new BufferedImage[4][5];
	public static dimension[][] allInteractables = new dimension[4][5];
	public static boolean[][] visitedInteractables = new boolean[4][5];

	// set of coords in specific maps where the map camera needs to move
	public static dimension[][] moveMap = new dimension[3][5];

	BufferedImage fadeStart;
	BufferedImage fadeEnd;

	// audios
	
	// Stops the music when going through exits / entrances of snowden
	public static boolean stop = false;
	// battle
	public static boolean battling = false;
	public static boolean winner = false;
	
    public static BufferedImage player;
    public static BufferedImage brokenHeart;
    
    // menu images
    public static BufferedImage[] menuImages = new BufferedImage[4];
	public static File path1 = new File("assets/battleImages/menus");
	public static File[] menuFiles = path1.listFiles();
	
	// menu options
    public static BufferedImage[] selectionImages = new BufferedImage[4];
	public static File path3 = new File("assets/battleImages/options");
	public static File[] selectionFiles = path3.listFiles();
	public static corner[] optionPos = new corner[4];
	
	// slash animation
	public static BufferedImage[] slashImage = new BufferedImage[4];
	public static corner[] slashCoords = new corner[4];
	
	// player boundaries in battle
	public static ArrayList<dimension> gameBounds = new ArrayList<dimension>();
    
    // font
	public static Font damageFont;
	
	// slider
	public static BufferedImage slider;
	public static BufferedImage bar;

	// boss image
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

	public static int tester = 0;
	
	public static int xe;
	
	
	
	
	// textfile streaming
	public static Scanner sc = new Scanner(System.in);
	public static ArrayList<String>[][] allScripts = new ArrayList[4][5];
	
	// startScript must be 1-indexed
	public static boolean[][] startScript;
	public static ArrayList<String> healScript = new ArrayList<String>();
	public static Scanner healText;
	public static Font speakingFont;
	public static BufferedImage dialogueBox;
	public static BufferedImage orion;
	public static BufferedImage nicky;
	public static BufferedImage sans;
	public static BufferedImage clarence;
	public static BufferedImage BDspeaking;
	
	public static boolean grabbedHeal = false;
	
	

	

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
		// 615
		setPreferredSize(new Dimension(990, 615));
		setBackground(new Color(0, 0, 0));
		
		
		
		// import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));

			snowdenImages[1] = ImageIO.read(new File("assets/maps/snowden/snowden1.png"));
			snowdenImages[2] = ImageIO.read(new File("assets/maps/snowden/snowden2.png"));
			snowdenImages[3] = ImageIO.read(new File("assets/maps/snowden/snowden3.png"));
			snowdenImages[4] = ImageIO.read(new File("assets/maps/snowden/snowden4.png"));


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

            /*
            startSongInput = AudioSystem.getAudioInputStream(new File("audio/mus_musicbox.ogg"));
            startSong = AudioSystem.getClip();
            startSong.open(startSongInput);
	    	*/
			
			
			// DIALOGUE
			dialogueBox = ImageIO.read(new File("assets/scripts/dialogueBox.png"));
		    speakingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);
		    orion = ImageIO.read(new File("assets/scripts/orion.png"));
		    nicky = ImageIO.read(new File("assets/scripts/nicky.png"));
		    clarence = ImageIO.read(new File("assets/scripts/clarence.png"));
		    sans = ImageIO.read(new File("assets/scripts/sans.png"));
		    BDspeaking = ImageIO.read(new File("assets/scripts/BDspeaking.png"));
			healText = new Scanner(new File("assets/scripts/heal_script.txt"));
			
			// BATTLE.JAVA
			// menu / buttons
			
			
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
		    //register the font
		    ge.registerFont(speakingFont);
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
		}
		floweyBounds[1] = new ArrayList<dimension>();
		floweyExits[1] = new ArrayList<dimension>();

		for (int i = 1; i < 3; i++) { // map
			for (int x = 0; x < 3; x++) { // setting
				allBounds[i][x] = new ArrayList<dimension>();
				allExits[i][x] = new ArrayList<dimension>();
			}
		}

		// Positions of Chara
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
		allPos[3][1][1] = new corner(450, 120);
		allPos[3][1][2] = new corner(0,0);






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
		floweyBounds[1].add(new dimension(new corner(300, 110), new corner(615, 495)));
		floweyBounds[1].add(new dimension(new corner(615, 15), new corner(910, 495)));
		floweyBounds[1].add(new dimension(new corner(440, 75), new corner(470, 110)));
		floweyExits[1].add(new dimension(new corner(1, 1), new corner(1,1)));
		floweyExits[1].add(new dimension(new corner(440, 75), new corner(470,75)));


		// putting them all in a list
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
		slashCoords[0] = new corner(350, 0);
        slashCoords[1] = new corner(350, 40);
        slashCoords[2] = new corner(340, 70);
        slashCoords[3] = new corner(350, 70);
        
		// interactables
		
		// ruins
		allInteractables[1][3] = new dimension(new corner(230, 860), new corner(355, 970));  // the bottom red bush, use globalPos
		
		// snowden
		allInteractables[2][1] = new dimension(new corner(210, 265), new corner(345, 265));  // the bush
		allInteractables[2][2] = new dimension(new corner(705, 180), new corner(770, 180));  // sans
		allInteractables[2][3] = new dimension(new corner(390, 195), new corner(525, 250));  // clarence (the snowman)
		allInteractables[2][4] = new dimension(new corner(1165, 130), new corner(1165, 140));  // the boss, must use globalPos
		
		// 
		startScript = new boolean[][] {{false, false, false, false, false}, {false, true, false, true, false}, {false, true, true, true, true}};
		// import scripts
		
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

		//use the font
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animation.alpha));

		// fade
		if (!battling) {
			try {
				audio.playMusic(gameState);
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
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
	
					// Used to determine if map needs to move
					// at a certain point
	
					// if chara spawns at the bottom
					if (charaY > 312 && gameState == 1 && setting == 3) {
						globalPos = 1025;
	
					}
					else if (charaY < 312 && gameState == 1 && setting == 3) {
						globalPos = charaY;
					}
	
					else if (charaY > 312 && gameState == 1 && setting == 4) {
						globalPos = 1075;
					}
					
					else if (charaY < 312 && gameState == 1 && setting == 4) {
						globalPos = 240;
					}
	
					else if(gameState == 2 && charaX > 625) {
						globalPos = (1250 - (640 - charaX));
					}
					else if (gameState == 2 && charaX < 625) {
						globalPos = charaX;
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
		else if (battling) {
			g.setColor(new Color(255, 255, 255));
			g.setFont(speakingFont);
			
			// menu background
			if(battle.menuState <= 3) {
				g.drawImage(menuImages[battle.menuState], 0, 0, null);
				g.drawImage(BD, 430, 85, null);
			}
			
			// stats
			g.drawString(battle.health + "", 345, 518);
			g.drawString("50", 425, 518);
			g.drawString(battle.heals + "", 645, 518);
			g.drawString("5", 710, 518);
			g.drawString("BOSS HEALTH: ", 385, 75);
			g.drawString(battle.bossHealth + "", 575, 75);
	
	
			// if u lose or win
			if (battle.menuState > 3 || battle.slashDraw) {
				if (battle.menuState == 4) {
					// We use if statements to ensure that the order of
					// execution is correct as the thread.sleep causes
					// issues with the order. Menus / Text need to be redrawn
					// as the text for HP freezes at "1", then the sleep runs.
					if(z == 0) {
						if(battle.x == 0) {
							g.drawImage(menuImages[3], 0, 0, null);
							g.drawImage(BD, 430, 85, null); 
							g.drawString(battle.health + "", 345, 518);
							g.drawString("50", 425, 518);
				    		g.drawString(battle.heals + "", 645, 518);
				    		g.drawString("5", 710, 518);
				    		g.drawString("BOSS HEALTH: ", 385, 75);
				    		g.drawString(battle.bossHealth + "", 575, 75);
				    		battle.x++;
				    		System.out.println("runed");
						} 
						else if(battle.x == 1) {
							super.paintComponent(g);
		    				g.drawImage(brokenHeart, battle.playerX, battle.playerY, null);
		    				battle.x++;
							System.out.println("runed2");
						}
						else if (battle.x == 2) {
							super.paintComponent(g);
		    				g.drawImage(brokenHeart, battle.playerX, battle.playerY, null);
		    				battle.x++;
		    				battle.dead = true;
		    				System.out.println("runed3");
						}
					}
					if(battle.dead) {
						over = true;
	    				try {audio.playMusic(gameState);} 
	    				catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {e1.printStackTrace();}
	    				try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
	    				battle.dead = false;
					}
					else if(battle.x == 3) {
	    				z++;
						if(z < 30) {g.drawImage(gameOver2, 0, 0, null);}
						if (z >= 30) {g.drawImage(gameOver, 0, 0, null); over = false;}
	    			}
	    		}
			
				// When dealing damage to the boss, it will create
				// a slash animation. To do so, you have to redraw
				// all the images and the slash images
				else if (battle.slashDraw) {
					if(battle.num <= 3) {
	    				super.paintComponent(g);
	    				g.drawImage(menuImages[2], 0, 0, null);
	    				g.drawImage(bar, battle.barX, 314, null);
						g.drawImage(BD, 430, 85, null); 
						g.drawString(battle.health + "", 345, 518);
						g.drawString("50", 425, 518);
			    		g.drawString(battle.heals + "", 645, 518);
			    		g.drawString("5", 710, 518);
			    		g.drawString("BOSS HEALTH: ", 385, 75);
			    		g.drawString(battle.bossHealth + "", 575, 75);
			    		g.drawString("MINUS " + battle.damage + " HP", 620, 150);
			    		if(battle.playHit) {
			    			try {audio.hitSound(battle.damage);} 
			    			catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
								e1.printStackTrace();}
			    			battle.playHit = false;
			    		}
	    				g.drawImage(slashImage[battle.num], slashCoords[battle.num].x, slashCoords[battle.num].y, null);
	    				System.out.println("SLASH DRAWN");
					}
					// Reset variable values to ensure
					// that the animation can be reused
					else {
						g.drawImage(bar, battle.barX, 314, null);
						battle.slashDraw = false;
						battle.slash = false;
						battle.num = 0;
						System.out.println("bared: " + battle.barX);
					}
					
				// If the boss is defeated
				if (battle.menuState == 5 && !battle.slashDraw) {
					animation.fade(deadBoss, allMaps[2][4], "slow");
					winner = true;
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
    			else if (battle.textState == 2) {
    				if (battle.health == 50)
	    				g.drawString("You are MAX HP!", 75, 360);

    				else if (battle.heals == 0) 
	    				g.drawString("You are out of heals!", 75, 360);
    					
    				else 
	    				g.drawString("You healed 10 HP! You have " + battle.heals + " left", 75, 360);
    				
    	
    			}
    			else if (battle.textState == 3) {
    				g.drawString("You fled the scene" , 75, 360);


    			}
    			g.drawImage(selectionImages[battle.selectionState], optionPos[battle.selectionState].x, optionPos[battle.selectionState].y, null);
    		}
    		
    		else if (battle.menuState == 2) {
    			// Reset bar values
    			if(battle.count == 0) {
    				battle.count = 1;
    				battle.barX = 60;
    				battle.damage = 0;
    				battle.stopBar = false;
    			}
        		if(battle.stopBar) {
        			try {
        				System.out.println("damaged");
        				Thread.sleep(1000);
        			} 
        			catch (InterruptedException e1) {
        				e1.printStackTrace();
        			}
        			
        			if (Math.random() >= 0.50)
        				 battle.variation = 1;
        			else battle.variation = 2;
        			System.out.println("variation = " + battle.variation);
        			
        			if (battle.attack == 1) {
        				firstAttack();
        			}
        			else if (battle.attack == 2) {
        				secondAttack();
        			}
        			battle.menuState = 3;
        			battle.stopBar = false;
    			}
        		else {
	        		g.drawImage(bar, battle.barX, 314, null);  		
	        		if(battle.barX >= 920 || battle.damage >= 1) {
	        			battle.stopBar = true;
	        		}
	        		if(!battle.stopBar) {battle.barX += 20;}
        		}
    		}
    		else if (battle.menuState == 3) {
    			g.drawImage(player, battle.playerX, battle.playerY, null);    			

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
				
				if (grabbedHeal){
					g.drawString(healScript.get(i), 300, 485);
					g.drawString("You now have " + battle.heals + ((battle.heals > 1)? " heals.": " heal."), 300, 485 + 40);

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

	@Override
	public void keyPressed(KeyEvent e) {
		// Determines which direction the user has pressed
		// and causes the character to move in that direction
		// if it is within the boundaries

		// When moving the map, the map is moving and the character is not
		// This means that her X or Y value will not change. Thus, we use
		// globalPos to track her theoretical position. If she is within certain
		// values, the map will move. Otherwise, if she goes beyond those values,
		// the map will stop moving and the character itself will.
		
		charaAnimation.x = e.getKeyCode();
		if(!battling) {
			if (!animation.fading && 1 <= gameState && gameState <= 3) {
				// change animation to run method as it depends on each click
				// for animation to change directions
				if (dialogue.speaking) {
					dialogue.keyPressed(e);
				}
				
				else if ((e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') && !visitedInteractables[gameState][setting]) {
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
							visitedInteractables[gameState][setting] = true;
							if (!battling) 
								dialogue.speaking = true;
							
						}

					}
				}

				else {
					if(e.getKeyCode() == 38)
					{
						up = true;
					}
					if(e.getKeyCode() == 37)
					{
						left = true;
					}
					if(e.getKeyCode() == 40)
					{
						down = true;
					}
					if(e.getKeyCode() == 39)
					{
						right = true;
					}
					System.out.println();
					System.out.println("globalPos: " + globalPos);
					System.out.println("mapX: " + mapX + " mapY: " + mapY);
	
					change = withinExit(charaX, charaY, allExits[gameState][setting]);
					System.out.println("chara x = " + charaX + " " + "charaY = " + charaY);
	
					if (change != 0) {
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
						audio.footS = true;
						try {
							audio.footstep(gameState);
						}
						catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							e1.printStackTrace();
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
		else if(battling){
			if(!over) {xe = e.getKeyCode();}
			if (battle.menuState == 1) {
	    		
	    		// start state
	    		if (battle.textState == 1) {
		    		
		    		// left 
					if (xe == 37) { 
						if (battle.selectionState - 1 > 0) {
							battle.selectionState --;
		
						}
					}
					
					// right
					else if (xe == 39) {
						if (battle.selectionState + 1 < 4) {
							battle.selectionState ++;
		
						}
						
					}
		    		// if the key is z, then select
					else if (xe == 90) {
						if (battle.selectionState == 1) {
							battle.textState = 1;
							battle.menuState = 2;
							battle.count = 0;
							System.out.println("menu2");
							
						}
						else if (battle.selectionState == 2 && battle.health < 50 && battle.heals > 0) {
							if (battle.health + 10 > 50) {
								battle.health += 50 - battle.health;
							}
							else battle.health += 10;
							battle.textState = 2;
							battle.heals--;
							
						}
						else if (battle.selectionState == 3) {
							battle.textState = 3;
							
						}
						
					}
					/*
					// if the key is x, then go back
					else if (x == 88) {
					}
					*/
	    		}
	    		
	    		// if you picked to heal or flee, you must click z another time to progress
	    		else {
	    			if (xe == 90) {
	    				if (battle.selectionState == 2) {
	    					battle.textState = 1;
	    					battle.menuState++;
	    				}
	    				else if (battle.selectionState == 3) {
	    					battling = false;
	    					
	    				}
	    				System.out.println("brodie");
	    			}
	    		}
				repaint();
	    	}
	    	
	    	
	    	// slider
	    	// Pressing 'z' will stop the slider, determine
	    	// the damage to apply onto the boss, and initalize
	    	// the slash animation
	    	else if (battle.menuState == 2 && !battle.slash) {
	    		if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
	    			if(battle.barX <= 275 || battle.barX >= 705) {
	    				battle.damage = 5;
	    			}
	    			else if(battle.barX <= 450 || battle.barX >= 530) {
	    				battle.damage = 10;
	    			}
	    			else {
	    				battle.damage = 15;
	    			}
	    			if(battle.damage > 0) {battle.slash = true;}
	    			battle.bossHealth -= battle.damage;
	    			if(battle.bossHealth <= 0) { battle.bossHealth = 0; battle.menuState = 5;}
	    			battle.playHit = true;
	    			System.out.println("DAMAGE: " + battle.damage);
	    		}
	    	}
	    	
	    	
	    	else if (battle.menuState == 3) {
				if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { // keycode 39 is the up arrow key
					battle.up = true;
				}
				
				if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
					battle.down = true;
				}
				
		
				if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
					battle.left = true;
				}
				
		
				if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
					battle.right = true;
				}
	    	}
	    	
	    	else if (battle.menuState == 4) {
	    		
	    		if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
	    			z = 0;
	    			battle.resetStats();
    				try {audio.playMusic(gameState);} 
    				catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {e1.printStackTrace();}
	    			System.out.println("quit");
	    		}

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
				gameState = 2;
				setting = 4;
				dialogue.speaking = true;
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


	}

	// Takes in character's current position and the array of boundaries for that
	// specific setting. Then, it checks if the character's current position is within
	// the X and Y values of any of the boundaries. Within those values, she will be
	// able to move freely. Otherwise, if her values go beyond the boundaries, the array
	// will return false and she will not be able to move.

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


	// 1 equals you go to the entrance of the next setting
	// 0 equals we haven't exited
	// 2 equals go to the exit of the previous setting

	//
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

	// in ruins3, when y > 500, move camera


	// When button is released, chara
	// should go back to default standing
	// position in whichever direction
	public void keyReleased(KeyEvent e) {
		if(battling) {
			if (battle.menuState == 3) {
				System.out.println("yo");
				if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) { 
					up = false;
				}
				
				if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
					down = false;
				}
				
		
				if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
					left = false;
				}
				
		
				if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
					right = false;
				}
				System.out.println(battle.playerX + " " + battle.playerY);
	    	}
		}
		else if(!animation.fading && 1 <= gameState && gameState <= 3 && !dialogue.speaking) {
			int x = e.getKeyCode();
			if(x >= 37 && x <= 40 && audio.footP && !battling) {
				audio.footS = true;
				System.out.println("yo");
				try {
					audio.footstep(gameState);
				} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) {
				System.out.println("up");
				up = false;
				curChara = 0;
			}

			if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
				down = false;
				curChara = 2;
				charaAnimation.legS = false;
			}


			if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
				left = false;
				curChara = 1;
				charaAnimation.legA = false;
			}


			if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
				right = false;
				curChara = 3;
			}

		}
		
		
	}


	@Override
	public void run() {

		while(true) {
			if(!battling) {
				try {
					Thread.sleep(1000 / fps);
					if (up && withinBounds(charaX, charaY - charaSpeed, allBounds[gameState][setting])) {
						charaAnimation.key = 1;
						if(gameState == 1) {
							if(setting > 2 && moveMap[gameState][setting].topLeft.y < globalPos && globalPos <= moveMap[gameState][setting].bottomRight.y) {
								mapY += charaSpeed;
								globalPos -= charaSpeed;
							}
							else {
								charaY -= charaSpeed;
								globalPos -= charaSpeed;
							}
						}
						else {
							charaY -= charaSpeed;
						} 
						charaAnimation.run();
	
					}
					if (down && withinBounds(charaX, charaY + charaSpeed, allBounds[gameState][setting])) {
						charaAnimation.key = 3;
						if(gameState == 1) {
							if(setting > 2 && moveMap[gameState][setting].topLeft.y <= globalPos && globalPos < moveMap[gameState][setting].bottomRight.y) {
								mapY -= charaSpeed;
								globalPos += charaSpeed;
							}
							else {
								charaY += charaSpeed;
								globalPos += charaSpeed;
							}
						}
						else {
							charaY += charaSpeed;
						}
						charaAnimation.run();
	
	
					}
					if (left && withinBounds(charaX - charaSpeed, charaY, allBounds[gameState][setting])) {
						charaAnimation.key = 2;
						// for moving the camera horizontally
						if(gameState == 2 && (setting == 1 || setting == 4)) {
							if(moveMap[gameState][setting].topLeft.x < globalPos && globalPos <= moveMap[gameState][setting].bottomRight.x) {
								mapX += charaSpeed;
							}
							else {
								charaX -= charaSpeed;
							}
							globalPos -= charaSpeed;
						}
						else {
							charaX -= charaSpeed;
						}
						charaAnimation.run();
	
					}
					if (right && withinBounds(charaX + charaSpeed, charaY, allBounds[gameState][setting])) {
						charaAnimation.key = 4;
						if(gameState == 2 && (setting == 1 || setting == 4)) {
							if(moveMap[gameState][setting].topLeft.x <= globalPos && globalPos < moveMap[gameState][setting].bottomRight.x) {
								mapX -= charaSpeed;
							}
							else {
								charaX += charaSpeed;
							}
							globalPos += charaSpeed;
						}
						else {
							charaX += charaSpeed;
						}
						charaAnimation.run();
					}
					if(stop && audio.footP) {
						stop = false;
						audio.clip2.stop();
						audio.footP = false;
					}
					
					if(up || down || left || right) {
						try {
							audio.footstep(gameState);
						} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint();
			}
			
			else if(battling){
				try {audio.playMusic(gameState);} 
				catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();}
				battle.run();
			}

		}

	}
	
	
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
			else grabbedHeal = true;
			return true;
		}

		return false;
		
	}
	// Attacks
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
	
	public void updateSecondAttack() {
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
