package n4l.games.breakoid.model;

import android.graphics.Bitmap;

public class PowerUp extends Model {
	
	public static final int BALLS = 1;
	public static final int FIRE_BALL = 2;
	
	public static int gravity = 10;
	
	private int type;
	
	private boolean out = false;
	private boolean cought = false;

	public PowerUp(Bitmap bitmap, int type, int x, int y) {
		super(bitmap, x, y);
		this.type = type;

	}
	
	public boolean isOut()
	{
		return out;
	}
	
	public boolean isCought()
	{
		return cought;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void update(Paddle paddle, int maxY) {
		y += gravity;
		if (getY() > maxY)
		{
			out = true;
			return;
		}
		if (getY() + getBitmap().getHeight()/2 > paddle.getY())
		{
			if ((getX() + getBitmap().getWidth()/2 > paddle.getX() - paddle.getBitmap().getWidth()/2) &&
					(getX() - getBitmap().getWidth()/2 < paddle.getX() + paddle.getBitmap().getWidth()/2))
			{
				out = true;
				cought = true;
				return;
			}
		}
	}

}
