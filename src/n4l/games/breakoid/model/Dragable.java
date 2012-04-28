package n4l.games.breakoid.model;

public interface Dragable {
	public void handleActionDown(int eventX, int eventY);

	public void handleActionMove(int eventX, int eventY);

	public void handleActionUp(int eventX, int eventY);
}
