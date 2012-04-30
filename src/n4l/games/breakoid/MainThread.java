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
	// Flag to hold game state
	private boolean running;
	// Desired fps
	private final static int MAX_FPS = 25;
	// Maximum number of frames allowed to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// The frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

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
		Canvas canvas;
		Log.d(TAG, "Starting game loop");

		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;

		sleepTime = 0;

		while (running) {
			canvas = null;
			// To draw directly, surface must be locked.
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {

					beginTime = System.currentTimeMillis();
					framesSkipped = 0;

					// Do one cycle
					this.gameView.update();
					this.gameView.render(canvas);
					// Calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					// Check if cycle did fit in one frame period.
					// If not, the time will be negative.
					// Then frame dropping will be done.
					sleepTime = (int) (FRAME_PERIOD - timeDiff);

					// Waste time if possible
					if (sleepTime > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
						}
					}

					// Catch up with time.
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						this.gameView.update();
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				// If something went wrong, unlock the surface
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

}
