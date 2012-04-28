/**
 * 
 */
package n4l.games.breakoid;

import android.util.Log;
import android.view.SurfaceHolder;

/**
 * @author Vytis Valentinavičius <xytis2000@gmail.com>
 * 
 */
public class MainThread extends Thread {
	
	private static final String TAG = MainThread.class.getSimpleName();

	private SurfaceHolder surfaceHolder;
	private MainGameView gameView;
	// flag to hold game state
	private boolean running;

	public MainThread(SurfaceHolder surfaceHolder, MainGameView gameView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gameView = gameView;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		long tickCount = 0L;
		Log.d(TAG, "Starting game loop");
		while (running) {
			tickCount++;
			// update game state
			// render state to the screen
		}
		Log.d(TAG, "Game loop executed " + tickCount + " times");
	}

}
