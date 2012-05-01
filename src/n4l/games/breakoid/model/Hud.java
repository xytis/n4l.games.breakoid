package n4l.games.breakoid.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Hud {
	private long score = 0;
	
	private int x;
	private int y;
	
	
	public Hud(int x1, int y1, int x2, int y2)
	{
		x = x1 + (x2 - x1)/2;
		y = y1 + (y2 - y1)/2;
	}
	
	
	public void incScore(int num)
	{
		score += num;
	}
	
	public void reset()
	{
		score = 0;
	}
	
	public void draw(Canvas canvas)
	{
		String points = "Score: " + score;
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		canvas.drawText(points, x, y, paint);
	}

}
