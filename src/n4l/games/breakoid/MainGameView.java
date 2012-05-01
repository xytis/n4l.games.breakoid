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
	private ArrayList<PowerUp> falling;
	private ArrayList<Particle> particles;
	private Paddle paddle;
	private Hud hud;

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
		
		hud = new Hud(0,getHeight()-30, getWidth(), getHeight());

		paddle = new Paddle(BitmapFactory.decodeResource(getResources(),
				R.drawable.paddle), getWidth() / 2, getHeight() - 55, -10,
				getWidth() + 10);

		resetBall();

		layThoseBricks();
		
		falling = new ArrayList<PowerUp>();
		particles = new ArrayList<Particle>();

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
	
	static int rndInt(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}
	
	private PowerUp generatePowerUp(Brick brick)
	{
		int type = rndInt(1,2);
		return new PowerUp(createBitmap(type), type, brick.getX(), brick.getY());
	}
	
	private Bitmap createBitmap(int type)
	{
		switch (type) {
		case PowerUp.BALLS:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.b);
		case PowerUp.FIRE_BALL:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.f);
		}
		return null;
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
		Iterator<PowerUp> k = falling.iterator();
		while (k.hasNext()) {
			(k.next()).draw(canvas);
		}
		
		paddle.draw(canvas);
		
		Iterator<Particle> l = particles.iterator();
		while (l.hasNext()) {
			(l.next()).draw(canvas);
		}
		
		hud.draw(canvas);
	}

	public void update() {
		paddle.update();
		for (int i = 0; i < balls.size(); i++)
		{
			// Boundary params for later use.
			Ball ball = balls.get(i);
			ball.update(bricks, paddle, 0, getWidth(), 0, getHeight());
			if (ball.isFlaming())
			{
				particles.add(new Particle(ball.getX(), ball.getY()));
			}
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
				hud.incScore(1);
				if (bricks.get(i).hasPowerUp())
				{
					falling.add(generatePowerUp(bricks.get(i)));
				}
				bricks.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < falling.size(); i++)
		{
			PowerUp item = falling.get(i);
			item.update(paddle, getHeight());
			if (item.isOut())
			{
				if (item.isCought())
				{
					switch (item.getType()) {
					case PowerUp.BALLS:
						if (!balls.isEmpty())
						{
							Ball ball = new Ball(BitmapFactory.decodeResource(getResources(),R.drawable.ball),
									balls.get(0).getX(),
									balls.get(0).getY());
							//Do some angular stuff =]
							ball.launch((int) (balls.get(0).getX() + balls.get(0).getSpeed().getXv()*balls.get(0).getSpeed().getyDirection()));
							balls.add(ball);
						}
						break;
					case PowerUp.FIRE_BALL:
						for (int j = 0; j < balls.size(); j++)
						{
							balls.get(j).setFlame(200);
						}
						break;
					default:
						break;
					}
				}
				falling.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < particles.size(); i++)
		{
			particles.get(i).update();
			if (particles.get(i).isDead())
			{
				particles.remove(i);
				i--;
			}
		}
		
		if (balls.isEmpty())
		{
			resetBall();
			hud.reset();
		}
		if (bricks.isEmpty())
		{
			layThoseBricks();
			resetBall();
			hud.incScore(100);
		}
	}

}
