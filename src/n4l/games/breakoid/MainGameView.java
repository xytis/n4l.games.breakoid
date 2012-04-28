/**
 * 
 */
package n4l.games.breakoid;

import n4l.games.breakoid.model.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author xytis
 * 
 */
public class MainGameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "DEBUG: "
			+ MainGameView.class.getSimpleName();

	private MainThread m_thread;

	private Ball ball;
	private Paddle paddle;

	public MainGameView(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		// Create the game thread
		m_thread = new MainThread(getHolder(), this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		ball = new Ball(BitmapFactory.decodeResource(getResources(),
				R.drawable.ball), getWidth() / 2, 50);
		paddle = new Paddle(BitmapFactory.decodeResource(getResources(),
				R.drawable.paddle), getWidth() / 2, getHeight() - 50, -10,
				getWidth() + 10);

		// When surface is created
		m_thread.setRunning(true);
		m_thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		m_thread.setRunning(false);

		boolean retry = true;
		while (retry) {
			try {
				m_thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			Log.d(TAG, "Handling ACTION_DOWN");
			paddle.handleActionDown((int) event.getX(), (int) event.getY());
			// if (event.getY() > getHeight() - 50) {
			// m_thread.setRunning(false);
			// ((Activity) getContext()).finish();
			// } else {
			//
			// Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			// }
		}
		if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			Log.d(TAG, "Handling ACTION_MOVE");
			paddle.handleActionMove((int) event.getX(), (int) event.getY());
		}
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "Handling ACTION_UP");
			paddle.handleActionUp((int) event.getX(), (int) event.getY());
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		ball.draw(canvas);
		paddle.draw(canvas);
	}

	public void update() {
		paddle.update();
	}

}
