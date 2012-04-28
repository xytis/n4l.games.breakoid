/**
 * 
 */
package n4l.games.breakoid.model;

import n4l.games.breakoid.MainGameView;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author xytis
 * 
 */
public class MotionModel extends Model implements Dragable {

	private static final String TAG = "DEBUG: "
			+ MainGameView.class.getSimpleName();

	// Jei bus keli objektai, neveiks gerai.
	protected boolean isFocused;

	/**
	 * @param x
	 * @param y
	 * @param bitmap
	 */
	public MotionModel(Bitmap bitmap, int x, int y) {
		super(bitmap, x, y);
		// TODO Auto-generated constructor stub
	}

	public void setFocused(boolean focused) {
		this.isFocused = focused;
	}

	public boolean getFocused() {
		return this.isFocused;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see n4l.games.breakoid.model.Dragable#handleActionDown(int, int)
	 */
	@Override
	public void handleActionDown(int eventX, int eventY) {
		// If event on model:
		if (eventX >= (x - bitmap.getWidth() / 2)
				&& (eventX <= (x + bitmap.getWidth() / 2))) {
			if (eventY >= (y - bitmap.getHeight() / 2)
					&& (eventY <= (y + bitmap.getHeight() / 2))) {
				setFocused(true);
			} else {
				setFocused(false);
			}
		} else {
			setFocused(false);
		}
		Log.d(TAG, "Focused now: " + this.isFocused);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see n4l.games.breakoid.model.Dragable#handleActionMove(int, int)
	 */
	@Override
	public void handleActionMove(int eventX, int eventY) {
		if (this.isFocused) {
			this.setX(eventX);
			this.setY(eventY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see n4l.games.breakoid.model.Dragable#handleActionUp(int, int)
	 */
	@Override
	public void handleActionUp(int eventX, int eventY) {
		this.setFocused(false);
	}

}
