
// INTRO COMMENTS
// This file is used to integrate the logic of the boss fight
// The following is used to set the proper values to each variable
// used in the battle. In return, it is used in the paint component
// in undertale.java. It also includes the run, key pressed,
// and key released, which is unique to the battles.

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;



// implement runnable for constant update in main file
public class battle extends JPanel implements Runnable, KeyListener {
	// Player / Boss stats
    public static int playerX = 500;
    public static int playerY = 400;
    public static int playerSpeed = 5;
    public static int health = 50;
    public static int bossHealth = 100;

    // Movement
    public static boolean up = false;
    public static boolean down = false;
    public static boolean right = false;
    public static boolean left = false;

    // Slash
    // num is to display each frame of the slash animation
    public static int num = 0;
    // Slash is used to add a thread.sleep to ensure it is timed properly
    // slashDraw is the variable where it actually draws
    public static boolean slash = false;
    public static boolean slashDraw = false;
    
    // Game states
    public static int menuState = 1;
    public static int selectionState = 1;
    public static int textState = 1;
    
    // Fonts
    public static Font font;
    public static Font damageFont;

    // When user runs out of hp
    // x is the order of execution when player dies
    public static int x = 0;
    public static boolean dead = false;

    // Bar
    public static int barX = 60;
    public static int count = 0;
    public static boolean stopBar = false;

    // Attacks
    public static int damage;
    public static boolean playHit = false;

    public static int attack = 1;
    public static int variation;

    public static int boneHeight = 100;
    public static int boneWidth = 15;
    
    // bonesReleased tells the program if a specific bone has been released or not
    // boneOrder is the order of the bones that should be released
    public static boolean[] bonesReleased;
    public static LinkedList<Integer> boneOrder = new LinkedList<Integer>();
    public static int startBone;
    public static int endBone;

    public static Thread thread;


    public undertale game;
    public battle(undertale e){
        game = e;
    }
    audio audio = new audio(this);

    public battle() {
        setPreferredSize(new Dimension(990, 615));
        setBackground(new Color(0, 0, 0));
        thread = new Thread(this);
        thread.start();
        
        // Font
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(30f);
            damageFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/DTM-Sans.ttf")).deriveFont(50f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // use the font
        this.setFont(font);
    }

    public void paintComponent(Graphics g) {}


    public void run() {
        while (true) {
            if (game.battling) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                // The actual fight
                if (menuState == 3) { //  if you are fighting then we need this
                    System.out.println("up = " + up);
                    System.out.println("down = " + down);
                    System.out.println("left = " + left);
                    System.out.println("right = " + right);

                    // movement
                    if (up && withinBounds(playerX, playerY - playerSpeed, game.gameBounds)) {
                        playerY -= playerSpeed;
                    }
                    if (down && withinBounds(playerX, playerY + playerSpeed, game.gameBounds)) {
                        playerY += playerSpeed;

                    }
                    if (left && withinBounds(playerX - playerSpeed, playerY, game.gameBounds)) {
                        playerX -= playerSpeed;
                    }
                    if (right && withinBounds(playerX + playerSpeed, playerY, game.gameBounds)) {
                        playerX += playerSpeed;
                    }
                    
                    // Attack one
                    // Checking if there is any contact with each bone
                    if (attack == 1) {
                        for (int i = 1; i < 11; i++) {
                            undertale.corner cur = game.bonePositions[i];
                            if (collision(new undertale.corner(playerX, playerY), new undertale.corner(playerX + 25, playerY + 25), new undertale.corner(cur.x, cur.y), new undertale.corner(cur.x + boneWidth, cur.y + boneHeight))) {
                                health -= 1;
                                try {
                                    audio.dmgTakenSound();
                                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                                    e.printStackTrace();}

                                // player has died
                                if (health == 0) {
                                    menuState = 4;

                                }
                            }
                        }
                        // update bone positions
                        game.updateFirstAttack();
                        
                        // Variations of attacks (comes from the right or left)
                        if (variation == 1) {
                            if (game.bonePositions[10].x < 150) {
                                menuState = 1;
                                up = false;
                                down = false;
                                left = false;
                                right = false;
                                playerX = 500;
                                playerY = 400;
                                attack++;
                            }
                        } 
                        else if (variation == 2) {
                            if (game.bonePositions[10].x > 850) {
                                menuState = 1;
                                up = false;
                                down = false;
                                left = false;
                                right = false;
                                playerX = 500;
                                playerY = 400;
                                attack++;
                            }
                        }

                        
                    } 
                    // Attack two
                    // Checking for collision
                    else if (attack == 2) {
                        for (int i = 1; i < 11; i++) {
                            undertale.corner cur = game.bonePositions2[i];

                            // the width for a horizontal bone is 70
                            // the height for a horizontal one is the width of bone 1
                            if (collision(new undertale.corner(playerX, playerY), new undertale.corner(playerX + 25, playerY + 25), new undertale.corner(cur.x, cur.y), new undertale.corner(cur.x + 70, cur.y + boneWidth))) {
                                health -= 1;
                                try {
                                    audio.dmgTakenSound();
                                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                                    e.printStackTrace();}

                                // player has died
                                if (health == 0) {
                                    menuState = 4;

                                }
                            }
                        }
                        // update bone positions
                        game.updateSecondAttack();

                        // Variations of attacks (comes from right or left)
                        if (variation == 1) {
                            if (game.bonePositions2[endBone].x < 100) {
                                menuState = 1;
                                up = false;
                                down = false;
                                left = false;
                                right = false;
                                playerX = 500;
                                playerY = 400;
                                attack--;
                            }
                        } else if (variation == 2) {
                            if (game.bonePositions2[endBone].x > 900) {
                                menuState = 1;
                                up = false;
                                down = false;
                                left = false;
                                right = false;
                                playerX = 500;
                                playerY = 400;
                                attack--;
                            }
                        }

                    }


                }
                // when slash animation is initiated
                if (slash) {
                    game.repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    num++;
                    slashDraw = true;

                }
            game.repaint();
            }
            else {
                try {audio.playMusic(game.gameState);}
                catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
                    e1.printStackTrace();}
                game.run();
            }
        }
    }

    // Takes in character's current position and the array of boundaries for that
	// specific setting. Then, it checks if the character's current position is within
	// the X and Y values of any of the boundaries. Within those values, she will be
	// able to move freely. Otherwise, if her values go beyond the boundaries, the array
	// will return false and she will not be able to move.
    public static boolean withinBounds(int x, int y, ArrayList<undertale.dimension> gameBounds2) {
        for (undertale.dimension cur: gameBounds2) { // for every boundary in the current setting

            undertale.corner topL = cur.topLeft;
            undertale.corner bottomR = cur.bottomRight;
            if (topL.x <= x && x <= bottomR.x && topL.y <= y && y <= bottomR.y) {
                return true;
            }

        }
        return false;
    }

    // Same concept as withinBounds method
    // For each bone, this method checks if you are within their X and Y values.
    // If you are, it will return true. Otherwise, false is returned.
    public boolean collision(undertale.corner playerTopLeft, undertale.corner playerBottomRight, undertale.corner objectTopLeft, undertale.corner objectBottomRight) {
        //System.out.println(topL1.x  + " < " + topL2.x + " < " + bottomR1.x + "  OR  " + topL1.x + " < " + bottomR2.x + " < " + bottomR1.x);
        //System.out.println(topL1.y  + " < " + topL2.y + " < " + bottomR1.y + "  OR  " + topL1.y + " < " + bottomR2.y + " < " + bottomR1.y);

        if (playerTopLeft.y < objectBottomRight.y && playerBottomRight.y > objectTopLeft.y) {
            if (playerTopLeft.x < objectBottomRight.x && playerBottomRight.x > objectTopLeft.x) {
                return true;
            }

        }
        return false;
    }

    public static void main(String[] args) {}

    // Reset values
    public void resetStats() {
        health = 50;
        bossHealth = 100;
        playerX = 500;
        playerY = 400;
        menuState = 1;
        selectionState = 1;
        textState = 1;
        attack = 1;
        damage = 0;
        x = 0;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    
    public void keyPressed(KeyEvent e)
    {
        // key code
    	int xe = game.xe;
        if (menuState == 1) {

            // start state
            if (textState == 1) {

                // left
                if (xe == 37) {
                    if (selectionState - 1 > 0) {
                        selectionState --;

                    }
                }

                // right
                else if (xe == 39) {
                    if (selectionState + 1 < 4) {
                        selectionState ++;

                    }

                }
                // if the key is z, then select
                else if (xe == 90) {
                	// fight
                    if (selectionState == 1) {
                        textState = 1;
                        menuState = 2;
                        count = 0;
                        System.out.println("menu2");

                    }
                    // heal
                    else if (selectionState == 2 && health < 50 && game.heals > 0) {
                        if (health + 10 > 50) {
                            health += 50 - health;
                        }
                        else health += 10;
                        textState = 2;
                        game.heals--;


                    }
                    // quit
                    else if (selectionState == 3) {
                        textState = 3;

                    }

                }
            }

            // if you picked to heal or flee, you must click z another time to progress
            else {
                if (xe == 90) {
                	// heal (confirm)
                    if (selectionState == 2) {
                        textState = 1;
                        menuState = 3;
                        
                        // deciding variation and attack
                        if (Math.random() >= 0.50)
                            variation = 1;
                        else variation = 2;
                        System.out.println("variation = " + battle.variation);

                        if (attack == 1) {
                            game.firstAttack();
                        } 
                        else if (attack == 2) {
                            game.secondAttack();
                        }
                    }
                    // quit(confirm)
                    else if (selectionState == 3) {
                        audio.x = 1;
                        game.battling = false;
                        resetStats();   
                    }
                }
            }
            game.repaint();
        }


        // slider
        // Pressing 'z' will stop the slider, determine
        // the damage to apply onto the boss, and initalize
        // the slash animation
        else if (menuState == 2 && !slash) {
            if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
                if(barX <= 275 || barX >= 705) {
                    damage = 5;
                }
                else if(barX <= 450 || barX >= 530) {
                    damage = 10;
                }
                else {
                    damage = 20;
                }
                // start slash animation
                if(damage > 0) {slash = true;}
                bossHealth -= damage;
                // if boss is defeated
                if(bossHealth <= 0) { bossHealth = 0; menuState = 5;}
                // hit sound
                playHit = true;
                System.out.println("DAMAGE: " + damage);
            }
        }

        // the battle
        else if (menuState == 3) {
        	// movement
            if (e.getKeyCode() == 38) { // keycode 39 is the up arrow key
                up = true;
            }

            if (e.getKeyCode() == 40) {
                down = true;
            }

            if (e.getKeyCode() == 37) {
                left = true;
            }


            if (e.getKeyCode() == 39) {
                right = true;
            }
        }

        // if you lost
        else if (menuState == 4) {
        	// press z again to continue and battle again
            if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
                game.z = 0;
                resetStats();
                try {audio.playMusic(game.gameState);}
                catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {e1.printStackTrace();}
                System.out.println("quit");
            }

        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}
