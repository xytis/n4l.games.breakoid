/**
 * 
 */
package n4l.games.breakoid;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * @author Vytis Valentinavičius <xytis2000@gmail.com>
 * 
 */
public class MainThread extends Thread {

	private static final String TAG = "DEBUG: "
			+ MainThread.class.getSimpleName();

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
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			tickCount++;
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state
					this.gameView.update();
					// draws the canvas on the panel
					this.gameView.onDraw(canvas);
				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		Log.d(TAG, "Game loop executed " + tickCount + " times");
	}

}
