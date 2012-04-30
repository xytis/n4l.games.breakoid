/**
 * 
 */
package n4l.games.breakoid;

import java.util.ArrayList;
import java.util.Iterator;

import n4l.games.breakoid.model.*;
import android.content.Context;
import android.graphics.Bitmap;
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

	private ArrayList<Brick> bricks;
	private ArrayList<Ball> balls;
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

		paddle = new Paddle(BitmapFactory.decodeResource(getResources(),
				R.drawable.paddle), getWidth() / 2, getHeight() - 25, -10,
				getWidth() + 10);

		resetBall();

		layThoseBricks();

		// When surface is created
		m_thread.setRunning(true);
		m_thread.start();
	}
	
	private void resetBall() {
		balls = new ArrayList<Ball>();
		balls.add(new Ball(BitmapFactory.decodeResource(getResources(),
				R.drawable.ball), paddle.getX(), paddle.getY()
				- paddle.getBitmap().getHeight() / 2 - 17));
	}

	private void layThoseBricks() {
		bricks = new ArrayList<Brick>();
		// For now, just add static amount of bricks. No fancy stuff.
		Bitmap brick = BitmapFactory.decodeResource(getResources(),
				R.drawable.brick);
		int sizeX = (getWidth() - 10) / brick.getWidth();
		int sizeY = (getHeight() / 2 - 10) / brick.getHeight();

		// Center horizontally
		int offsetX = (brick.getWidth() / 2)
				+ (getWidth() - brick.getWidth() * sizeX) / 2;
		// 5 pixels above
		int offsetY = (brick.getHeight() / 2) + 5;

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				bricks.add(new Brick(brick, brick.getWidth() * i + offsetX,
						brick.getHeight() * j + offsetY));
			}
		}
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
		}
		if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			Log.d(TAG, "Handling ACTION_MOVE");
			paddle.handleActionMove((int) event.getX(), (int) event.getY());
		}
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "Handling ACTION_UP");
			paddle.handleActionUp((int) event.getX(), (int) event.getY());
			Iterator<Ball> i = balls.iterator();
			while (i.hasNext()) {
				(i.next()).launch(paddle.getX());
			}
		}
		return true;
	}

	protected void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		Iterator<Ball> i = balls.iterator();
		while (i.hasNext()) {
			(i.next()).draw(canvas);
		}
		Iterator<Brick> j = bricks.iterator();
		while (j.hasNext()) {
			(j.next()).draw(canvas);
		}
		paddle.draw(canvas);
	}

	public void update() {
		paddle.update();
		for (int i = 0; i < balls.size(); i++)
		{
			// Boundary params for later use.
			Ball ball = balls.get(i);
			ball.update(bricks, paddle, 0, getWidth(), 0, getHeight());
			if (ball.isOut())
			{
				balls.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < bricks.size(); i++)
		{
			if (bricks.get(i).isDestroyed())
			{
				bricks.remove(i);
				i--;
			}
		}		
		
		if (balls.isEmpty())
		{
			resetBall();
		}
		if (bricks.isEmpty())
		{
			layThoseBricks();
			resetBall();
		}
	}

}
