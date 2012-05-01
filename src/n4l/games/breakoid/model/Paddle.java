/**
 * 
 */
package n4l.games.breakoid.model;

import android.graphics.Bitmap;

/**
 * @author xytis
 * 
 */
public class Paddle extends MotionModel {

	private Speed speed;
	private int motion_factor = 20;

	private int minX;
	private int maxX;

	private int target;

	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public Paddle(Bitmap bitmap, int x, int y, int minX, int maxX) {
		super(bitmap, x, y);
		this.speed = new Speed(0, 0, motion_factor);
		this.minX = minX + getBitmap().getWidth() / 2;
		this.maxX = maxX - getBitmap().getWidth() / 2;
		// TODO Auto-generated constructor stub
	}

	public Speed getSpeed() {
		return speed;
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		if (eventX > minX) {
			if (eventX < maxX) {
				target = eventX;
			} else {
				target = maxX;
			}
		} else {
			target = minX;
		}
	}

	@Override
	public void handleActionMove(int eventX, int eventY) {
		if (eventX > minX) {
			if (eventX < maxX) {
				target = eventX;
			} else {
				target = maxX;
			}
		} else {
			target = minX;
		}
	}

	@Override
	public void handleActionUp(int eventX, int eventY) {
		if (eventX > minX) {
			if (eventX < maxX) {
				target = eventX;
			} else {
				target = maxX;
			}
		} else {
			target = minX;
		}
	}

	public void checkPosition() {

		// check collision with right wall if heading right
		if (speed.getxDirection() == Speed.DIRECTION_RIGHT
				&& getX() + (speed.getXv() * speed.getxDirection()) >= maxX) {
			target = maxX;
		}
		// check collision with left wall if heading left
		if (speed.getxDirection() == Speed.DIRECTION_LEFT
				&& getX() + (speed.getXv() * speed.getxDirection()) <= minX) {
			target = minX;
		}
		// Find delta to target, move there.
		speed.adjustX(getX(), target);

	}

	public void update() {
		checkPosition();
		x += (speed.getXv() * speed.getxDirection());
	}
}
