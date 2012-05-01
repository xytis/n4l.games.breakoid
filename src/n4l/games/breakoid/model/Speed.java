/**
 * 
 */
package n4l.games.breakoid.model;

/**
 * @author xytis
 * 
 */
public class Speed {

	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_LEFT = -1;
	public static final int DIRECTION_UP = -1;
	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_NONE = 0;

	private double xv = 1; // velocity value on the X axis
	private double yv = 1; // velocity value on the Y axis

	private double limit = -1; // max velocity for object

	private int xDirection = DIRECTION_NONE;
	private int yDirection = DIRECTION_NONE;

	public void checkSet(double xv, double yv) {
		double speed = xv * xv + yv * yv;
		if (limit < speed) {
			double factor = Math.sqrt(speed) / Math.sqrt(limit);
			this.xv = xv / factor;
			this.yv = yv / factor;
		} else {
			this.xv = xv;
			this.yv = yv;
		}
	}

	public void constantSet(double xv, double yv) {
		double speed = xv * xv + yv * yv;
		double factor = Math.sqrt(speed) / Math.sqrt(limit);
		this.xv = xv / factor;
		this.yv = yv / factor;
	}

	public Speed() {
		if (limit > 0) {
			this.xv = Math.sqrt(limit * limit / 2);
			this.yv = Math.sqrt(limit * limit / 2);
		} else {
			this.xv = 1;
			this.yv = 1;
		}
	}

	public Speed(double xv, double yv, double limit) {
		if (limit > 0) {
			this.limit = limit * limit;
			checkSet(xv, yv);
		} else {
			this.xv = xv;
			this.yv = yv;
		}
	}

	public double getXv() {
		return xv;
	}

	public void setXv(double xv) {
		if (limit > 0) {
			checkSet(xv, this.yv);
		} else {
			this.xv = xv;
		}
	}

	public double getYv() {
		return yv;
	}

	public void setYv(double yv) {
		if (limit > 0) {
			checkSet(this.xv, yv);
		} else {
			this.yv = yv;
		}
	}

	public int getxDirection() {
		return xDirection;
	}

	public void setxDirection(int xDirection) {
		this.xDirection = xDirection;
	}

	public int getyDirection() {
		return yDirection;
	}

	public void setyDirection(int yDirection) {
		this.yDirection = yDirection;
	}

	// changes the direction on the X axis
	public void toggleXDirection() {
		xDirection = xDirection * -1;
	}

	// changes the direction on the Y axis
	public void toggleYDirection() {
		yDirection = yDirection * -1;
	}

	public void stop() {
		xDirection = Speed.DIRECTION_NONE;
		yDirection = Speed.DIRECTION_NONE;
		this.xv = 0;
		this.yv = 0;
	}

	public void adjustX(int xs, int xf) {
		if (xs < xf) {
			setxDirection(Speed.DIRECTION_RIGHT);
			checkSet(xf - xs, 0);
		}
		if (xs > xf) {
			setxDirection(Speed.DIRECTION_LEFT);
			checkSet(xs - xf, 0);
		}
		if (xs == xf) {
			stop();
		}
	}
}
