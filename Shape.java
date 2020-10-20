import java.util.List;
import java.util.ArrayList;


public class Shape {

	public static class Point {

		@Override
		public String toString() {
			return String.format("Point: [x: %f, y: %f]", this.x, this.y);
		}
		public float x, y;


		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	private List<Point> corners = new ArrayList<Point>();

	public Shape() {}

	public void addPoint(float x, float y) {
		this.corners.add(new Point(x, y));
	}

	public boolean contains(float x, float y) {
		// ray casting
		
		int collisions = countCollisions(x, y, 0.123123f, 0.34566345f);

		return collisions % 2 != 0;
	}
	
	public List<Point> getPoints() {
		return this.corners;
	}

	public static Point doLinesIntersect(float px, float py, float dx, float dy, float sx, float sy, float vx, float vy, Point result) {
		final float dxDdy = dx / dy;
		final float z = (px - sx + (sy - py) * dxDdy) / (vx - vy * dxDdy);
		final float t = (sy - py + z * vy) / dy;
		
		result.x = z;
		result.y = t;
		return result;
	}

	public int countCollisions(float x, float y, float xDir, float yDir) {
		if(this.corners.size() == 0) return 0;
		Point previous = this.corners.get(0);

		int intersections = 0;
		Point scalars = new Point(0.0f, 0.0f);
		if(this.corners.size() > 1) {
			Point last = this.corners.get(this.corners.size() - 1);

			float dx = previous.x - last.x;
			float dy = previous.y - last.y;
			doLinesIntersect(x, y, xDir, yDir, last.x, last.y, dx, dy, scalars);

			if(scalars.x >= 0 && scalars.x <= 1.0f && scalars.y >= 0.0f) {
				intersections++;
			}
		}

		for(int i = 1; i < this.corners.size(); i++) {
			Point current = this.corners.get(i);
			
			float dx = current.x - previous.x;
			float dy = current.y - previous.y;
			doLinesIntersect(x, y, xDir, yDir, previous.x, previous.y, dx, dy, scalars);
			if(scalars.x >= 0 && scalars.x <= 1.0f && scalars.y >= 0.0f) {
				intersections++;
			}
			
			previous = current;
		}

		return intersections;	
	}
}
