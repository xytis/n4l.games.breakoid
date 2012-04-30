/**
 * 
 */
package n4l.games.breakoid.model;

import java.util.ArrayList;
import java.util.Iterator;

import n4l.games.breakoid.MainThread;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author xytis
 * 
 */
public class Ball extends Model {

	private static final String TAG = "DEBUG: "
			+ MainThread.class.getSimpleName();

	private Speed speed;
	private int motion_factor = 30;
	protected int damage = 1;
	protected boolean stuck = true;

	/**
	 * @param x
	 * @param y
	 * @param bitmap
	 */
	public Ball(Bitmap bitmap, int x, int y) {
		super(bitmap, x, y);
		// TODO Auto-generated constructor stub
		this.speed = new Speed(0, 0, motion_factor);
	}

	public void update(ArrayList<Brick> bricks, Paddle paddle, int minX, int maxX, int minY, int maxY) {
		// Move
		x += (speed.getXv() * speed.getxDirection());
		y += (speed.getYv() * speed.getyDirection());
		// Check for walls
		// check collision with right wall if heading right
		if (speed.getxDirection() == Speed.DIRECTION_RIGHT
				&& getX() + bitmap.getWidth()/2 >= maxX) {
			speed.setxDirection(Speed.DIRECTION_LEFT);
		}
		// check collision with left wall if heading left
		if (speed.getxDirection() == Speed.DIRECTION_LEFT
				&& getX() - bitmap.getWidth()/2 <= minX) {
			speed.setxDirection(Speed.DIRECTION_RIGHT);
		}
		// check for top
		if (speed.getyDirection() == Speed.DIRECTION_UP
				&& getY() - bitmap.getHeight()/2 <= minY) {
			speed.setyDirection(Speed.DIRECTION_DOWN);
		}
		// check for bottom
		if (speed.getyDirection() == Speed.DIRECTION_DOWN
				&& getY() + bitmap.getHeight()/2 >= maxY) {
			speed.setyDirection(Speed.DIRECTION_UP);
			//Die later on.
		}
		// Check for paddle
		if (speed.getyDirection() == Speed.DIRECTION_DOWN
				&& getY() + bitmap.getHeight()/2 >= (paddle.getY() - paddle.getBitmap().getHeight()/2)) {
			//Below paddle surface. Now do we hit the paddle?
			if ((getX() > paddle.getX() - paddle.getBitmap().getWidth()/2) &&
					(getX() < paddle.getX() + paddle.getBitmap().getWidth()/2))
			{
				//Lets speculate the angle
				speed.setyDirection(Speed.DIRECTION_UP);
				// near center should be 0, near sides should be large.
				double diff = speed.getXv()*speed.getxDirection() + ((double) (getX()- paddle.getX()))/paddle.getBitmap().getWidth()*motion_factor;
				// deduce where we are headed now
				if (diff < 0)
				{
					speed.setxDirection(Speed.DIRECTION_LEFT);
				}
				else
				{
					speed.setxDirection(Speed.DIRECTION_RIGHT);
				}
				speed.setXv((int) Math.abs(diff)); 
			}
			
		}

		Iterator<Brick> j = bricks.iterator();
		while (j.hasNext()) {
			Brick brick = j.next();
			if (brick.isHit(x, y, bitmap.getHeight() / 2))
			{
				Log.d(TAG, "Hit");
				brick.hit(damage);
				//Reflect, later here should go power check.
				//if ()
			}
		}
	}
	
	public void launch(int paddleX)
	{
		if (stuck)
		{
			stuck = false;
			speed.setyDirection(Speed.DIRECTION_UP);
			if (paddleX - x < 0)
			{
				speed.setxDirection(Speed.DIRECTION_RIGHT);
			}
			else
			{
				speed.setxDirection(Speed.DIRECTION_LEFT);
			}
			speed.checkSet((int) Math.abs(paddleX - x), motion_factor);
		}
	}
}
