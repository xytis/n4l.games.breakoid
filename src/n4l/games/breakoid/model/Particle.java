package n4l.games.breakoid.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Particle {

	private int life = 20;
	private int vx = 0;
	private int vy = 0;

	private int x = 0;
	private int y = 0;

	private int radius = 4;

	private int color;
	private Paint paint;

	public Particle(int x, int y) {
		this.x = (int) (x + Math.random() * 5);
		this.y = (int) (y + Math.random() * 5);
		this.vx = 2 - (int) (Math.random() * 4);
		this.vy = 2 - (int) (Math.random() * 4);
		this.color = Color.argb(255, 255, 0, 50);
		this.paint = new Paint(this.color);
	}

	public boolean isDead() {
		return life < 0;
	}

	public void update() {
		life--;
		x += vx;
		y += vy;
	}

	public void draw(Canvas canvas) {
		paint.setColor(this.color);
		canvas.drawRect(x, y, x + radius, y + radius, paint);
	}

}
