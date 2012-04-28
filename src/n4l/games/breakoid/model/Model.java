/**
 * 
 */
package n4l.games.breakoid.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author xytis
 * 
 *         Uses center coordinates.
 */
public class Model implements Drawable {

	protected Bitmap bitmap; // the actual bitmap
	protected int x; // the X coordinate
	protected int y; // the Y coordinate

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Model(Bitmap bitmap, int x, int y) {
		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see n4l.games.breakoid.model.Drawable#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2),
				y - (bitmap.getHeight() / 2), null);
	}

}
