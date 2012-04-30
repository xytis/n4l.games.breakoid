package n4l.games.breakoid.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Brick extends Model {

	protected int m_health = 1;

	protected boolean destroyed = false;

	public Brick(Bitmap bitmap, int x, int y) {
		super(bitmap, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isDestroyed()
	{
		return destroyed;
	}

	public boolean isHit(int x, int y, int radius) {
		if (destroyed) {
			return false;
		}
		// No optimization, yet.
		// if way below -- break.
		if ((y - radius) > (this.y + this.bitmap.getHeight() / 2)) {
			return false;
		}
		// if way above -- break
		if ((y + radius) < (this.y - this.bitmap.getHeight() / 2)) {
			return false;
		}
		// if way left -- break
		if ((x + radius) < (this.x - this.bitmap.getWidth() / 2)) {
			return false;
		}
		// if way right -- break
		if ((x - radius) > (this.x + this.bitmap.getWidth() / 2)) {
			return false;
		}
		// Else -- we have a collision
		return true;
	}

	public void hit(int damage) {
		m_health -= damage;
		if (m_health <= 0) {
			destroyed = true;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (!destroyed) {
			canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2),
					y - (bitmap.getHeight() / 2), null);
		}
	}
}
