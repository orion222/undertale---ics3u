
public class animation{
	public static float alpha = 1.0f;
	
	// -1 = no fade, 1 = fade in, 2 = fade out
	public static boolean fading = false;
	public static boolean faded = false;
	public static int fade = -1;
	public static int fadeSpeed = 1;
	public undertale game;
	
	public animation(undertale e){
		
		 game = e;
	}
	
	public void fadeOut() {
		alpha -= 0.01 * fadeSpeed;
		if (alpha < 0) {
			alpha = 0;
			game.timer.stop();
			fade = 1;
		}
	}
	
	public void fadeIn() {	
		alpha += 0.01 * fadeSpeed;
		if (alpha > 1) {
			alpha = 1;
			game.timer.stop();
			fade = -1;
			fading = false;
		}
	}
}
