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
	protected boolean out = false;

	private int flame = 0;

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

	public Speed getSpeed() {
		return speed;
	}

	public boolean isOut() {
		return out;
	}

	public void setFlame(int flame) {
		this.flame = flame;
	}

	public boolean isFlaming() {
		return flame > 0;
	}

	public void update(ArrayList<Brick> bricks, Paddle paddle, int minX,
			int maxX, int minY, int maxY) {
		// If ball is stuck -- follow paddle
		if (stuck) {
			x += paddle.getSpeed().getXv() * paddle.getSpeed().getxDirection();
		}

		flame--;

		// Move
		x += (speed.getXv() * speed.getxDirection());
		y += (speed.getYv() * speed.getyDirection());
		// Check for walls
		// check collision with right wall if heading right
		if (speed.getxDirection() == Speed.DIRECTION_RIGHT
				&& getX() + bitmap.getWidth() / 2 >= maxX) {
			speed.setxDirection(Speed.DIRECTION_LEFT);
		}
		// check collision with left wall if heading left
		if (speed.getxDirection() == Speed.DIRECTION_LEFT
				&& getX() - bitmap.getWidth() / 2 <= minX) {
			speed.setxDirection(Speed.DIRECTION_RIGHT);
		}
		// check for top
		if (speed.getyDirection() == Speed.DIRECTION_UP
				&& getY() - bitmap.getHeight() / 2 <= minY) {
			speed.setyDirection(Speed.DIRECTION_DOWN);
		}
		// check for bottom
		if (speed.getyDirection() == Speed.DIRECTION_DOWN
				&& getY() + bitmap.getHeight() / 2 >= maxY) {
			// Die later on.
			out = true;
		}
		// Check for paddle
		if (speed.getyDirection() == Speed.DIRECTION_DOWN
				&& getY() + bitmap.getHeight() / 2 >= (paddle.getY() - paddle
						.getBitmap().getHeight() / 2)) {
			// Below paddle surface. Now do we hit the paddle?
			if ((getX() + getBitmap().getWidth() / 2 > paddle.getX()
					- paddle.getBitmap().getWidth() / 2)
					&& (getX() - getBitmap().getWidth() / 2 < paddle.getX()
							+ paddle.getBitmap().getWidth() / 2)) {
				// Lets speculate the angle
				speed.setyDirection(Speed.DIRECTION_UP);
				// near center should be 0, near sides should be large.
				double diff = speed.getXv() * speed.getxDirection()
						+ ((double) (getX() - paddle.getX()))
						/ paddle.getBitmap().getWidth() * 2 * motion_factor;
				// deduce where we are headed now
				if (diff < 0) {
					speed.setxDirection(Speed.DIRECTION_LEFT);
				} else {
					speed.setxDirection(Speed.DIRECTION_RIGHT);
				}
				speed.constantSet((int) Math.abs(diff), speed.getYv());
			}

		}

		Iterator<Brick> j = bricks.iterator();
		while (j.hasNext()) {
			Brick brick = j.next();
			if (brick.isHit(x, y, bitmap.getHeight() / 2)) {
				Log.d(TAG, "Hit");
				brick.hit(damage);
				// Reflect, later here should go power check.
				// 'cause isHit is aimed at speed, not precise information.
				// TODO Maybe swapDirection would work?
				if (flame <= 0) {
					if ((brick.getX() - brick.getBitmap().getWidth() / 2 < x)
							&& (x < brick.getX() + brick.getBitmap().getWidth()
									/ 2)) {
						speed.toggleYDirection();
					}
					if ((brick.getY() - brick.getBitmap().getHeight() / 2 < y)
							&& (y < brick.getY()
									+ brick.getBitmap().getHeight() / 2)) {
						speed.toggleXDirection();
					}
				}
			}
		}
	}

	public void launch(int paddleX) {
		if (stuck) {
			stuck = false;
			speed.setyDirection(Speed.DIRECTION_UP);
			if (paddleX - x < 0) {
				speed.setxDirection(Speed.DIRECTION_RIGHT);
			} else {
				speed.setxDirection(Speed.DIRECTION_LEFT);
			}
			speed.constantSet((int) Math.abs(paddleX - x), motion_factor);
		}
	}
}
