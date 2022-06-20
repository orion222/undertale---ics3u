
// INTRO COMMENTS
// The purpose of this file is to integrate audio into the game.
// Each map and unique screen will have a specific audio that will
// play once the player enters that area or initiate the screen.
// most settings will have a script that will play once the player
// The file includes multiple methods to easily transition to other
// songs and play sound effects


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class audio {
    // importing audio files
    File boss = new File("assets/audio/mus_boss.wav");
    File end = new File("assets/audio/mus_end.wav");
    File musicBox = new File("assets/audio/mus_musicBox.wav");
    File ruinSound = new File("assets/audio/mus_ruins.wav");
    File snowdenSound = new File("assets/audio/mus_snowden.wav");
    File snowWalk = new File("assets/audio/mus_snowwalk.wav");
    File loser = new File("assets/audio/lose.wav");
    File hit = new File("assets/audio/hit.wav");
    File dmgTaken = new File("assets/audio/damageTaken.wav");

    // x allows the program to understand
    // if a map has been changed. Change
    // is to switch the song and playing
    // tells us if a song is currently playing.
    public static int x = -1;
    public static boolean change = false;
    public static boolean playing = false;

    // footP = Play footstep sound
    // footS = Stop footstep sound
    public static boolean footP = false;
    public static boolean footS = false;

    // audio
    // Map songs
    public static Clip clip;
    public static AudioInputStream audioStream;

    // Separated variables for SFX as music will
    // be playing at the same time
    // Variables dedicated to footsteps
    public static Clip clip2;
    public static AudioInputStream audioStream2;

    // Variables dedicated to hit sound and damage taken sound
    public static Clip clip3;
    public static AudioInputStream audioStream3;

    public undertale game;
    public battle fight;

    public audio(undertale e) {
        game = e;
    }

    public audio(battle e) {
        fight = e;
    }

    // The general background music for each setting and battle
    // This method takes in the current gameState of the main file
    // a displays a specific song according to the map
    public void playMusic(int gameState) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        // music when in battle
        // "loser" is when Chara has been defeated and "boss" is the general bg music
        if(game.battling) {
            clip.stop();
            playing = true;
            if(battle.dead) {
                audioStream = AudioSystem.getAudioInputStream(loser);
            }
            else {
                audioStream = AudioSystem.getAudioInputStream(boss);
            }
        }
        // whenever you are not battling, the variable "x" allows the computer
        // to understand whenever a map has been changed. thus, x will be set
        // to that game state value and set "change" to true.
        if(!game.battling) {
            if(x != game.gameState) {
                System.out.println("changess");
                x = game.gameState;
                change = true;
                if(game.gameState > 0) {
                    clip.stop();
                }
            }
            // if change is true, this is where the next song will be decided
            // based on the next / previous map
            if(change) {
                playing = true;
                change = false;
                if(game.gameState == 0) {
                    audioStream = AudioSystem.getAudioInputStream(musicBox);
                }
                else if(game.gameState == 1) {
                    audioStream = AudioSystem.getAudioInputStream(ruinSound);
                }
                else if(game.gameState == 2) {
                    audioStream = AudioSystem.getAudioInputStream(snowdenSound);
                }
                else if(game.gameState == 3) {
                    audioStream = AudioSystem.getAudioInputStream(end);
                }
            }
        }
        // where the songs will be played
        if(playing) {
            playing = false;
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            if(!battle.dead) {clip.loop(Clip.LOOP_CONTINUOUSLY);}
        }
    }

    // footstep sound effect in snowden
    // The method takes in the gameState, though it only matters
    // on snowden
    public void footstep(int gameState) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioStream2 = AudioSystem.getAudioInputStream(snowWalk);
        if(gameState == 2) {
            // this if statement is to stop the sound effect from playing
            // the condition to the right is for the sound effect to stop playing
            // when Chara is on the bridge on snowden's fourth setting
            if(footS || (game.setting == 4 && game.globalPos >= 375 && game.globalPos <= 1520)) {
                System.out.println("STOP FOOT");
                clip2.stop();
                footS = false;
                footP = false;
            }
            // this if statement allows the clip to be played when it is not playing
            // at the moment
            else if(!footP) {
                clip2 = AudioSystem.getClip();
                clip2.open(audioStream2);
                clip2.start();
                clip2.loop(Clip.LOOP_CONTINUOUSLY);
                footP = true;
            }
        }
    }

    // hit sound when doing damage to the boss in battle
    // This method takes in damage during the battle
    // and only plays when you deal damage to the boss
    public void hitSound(int damage) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioStream3 = AudioSystem.getAudioInputStream(hit);
        if(damage > 0) {
            clip3 = AudioSystem.getClip();
            clip3.open(audioStream3);
            clip3.start();
        }
    }

    // damage taken sound when taking damage during battle
    public void dmgTakenSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioStream3 = AudioSystem.getAudioInputStream(dmgTaken);
        clip3 = AudioSystem.getClip();
        clip3.open(audioStream3);
        clip3.start();
    }
}