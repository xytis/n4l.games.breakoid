/**
 * 
 */
package n4l.games.breakoid;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author xytis
 * 
 */
public class MainGameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "DEBUG: " + MainGameView.class.getSimpleName();

	private MainThread m_thread;

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
		// When surface is created
		m_thread.setRunning(true);
		m_thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
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
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (event.getY() > getHeight() - 50) {
				m_thread.setRunning(false);
				((Activity) getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		 canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 10, 10, null);
	}

}
