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
	private float minX = Float.MAX_VALUE;
	private float maxX = Float.MIN_VALUE;
	private float minY = Float.MAX_VALUE;
	private float maxY = Float.MIN_VALUE;


	public void addPoint(float x, float y) {
		this.corners.add(new Point(x, y));
		if(x < this.minX)
			this.minX = x;
		if(x > this.maxX)
			this.maxX = x;
		if(y < this.minY)
			this.minY = y;
		if(y > this.maxY)
			this.maxY = y;
	}

	public boolean contains(float x, float y) {
		if(x < this.minX || x > this.maxX || y < this.minY || y > this.maxY)
			return false;
		int collisions = countCollisions(x, y, 0.123123f, 0.34566345f);

		return collisions % 2 != 0;
	}
	
	public List<Point> getPoints() {
		return this.corners;
	}

	/**
	 * First line is defined as<br>
	 * p + t * d where p and d are both a vector<br>
	 * The second line is defined as<br>
	 * s + z * v where s and d are both a vector<br>
	 *
	 * This functions finds the scalars "t" and "z" so that the equation of the corresponding line function returns the point of intersection.<br>
	 * Undefined behaviour if the vectors d and v are linear dependent!<br>
	 *
	 * @param px x - start coordinate of first line
	 * @param py y - start coordinate of first line
	 * @param dx x direction of first line
	 * @param dy y direction of first line
	 * @param sx x - start coordinate of second line
	 * @param sy y - start coordinate of second line
	 * @param vx x direction of second line
	 * @param vy y direction of second line
	 * @param result the tuple of scalars
	 * @return the result variable wich contains the scalars (result.x stores the scalar t and result.y stores the scalar z) 
	 */
	private static Point getLineIntersectionScalars(float px, float py, float dx, float dy, float sx, float sy, float vx, float vy, Point result) {
		final float dxDdy = dx / dy;
		final float z = (px - sx + (sy - py) * dxDdy) / (vx - vy * dxDdy);
		final float t = (sy - py + z * vy) / dy;
		
		result.x = t;
		result.y = z;
		return result;
	}

	public boolean intersects(Shape other) {
		if(this.corners.size() == 0 || other.corners.size() == 0)
			return false;
		// check aabb
		if(this.maxX < other.minX || other.maxX < this.minX || this.maxY < other.minY || other.maxY < this.minY)
			return false;

		return intersectsReverse(other) || other.intersectsReverse(this);
	}

	public boolean intersectsReverse(Shape other) {
		// check if a single point is inside this shape
		Point oFirst = other.corners.get(0);
		if(this.contains(oFirst.x, oFirst.y))
			return true;
		// check intersections
		Point res = new Point(0.0f, 0.0f);

		Point prev = this.corners.get(this.corners.size() - 1);
		for(int i = 0; i < this.corners.size(); i++) {
			Point current = this.corners.get(i);
			float mdx = current.x - prev.x;
			float mdy = current.y - prev.y;

			Point oprev = other.corners.get(other.corners.size() - 1);
			for(int j = 0; j < other.corners.size(); j++) {
				Point ocurrent = other.corners.get(j);
				float odx = ocurrent.x - oprev.x;
				float ody = ocurrent.y - oprev.y;


				getLineIntersectionScalars(prev.x, prev.y, mdx, mdy, oprev.x, oprev.y, odx, ody, res);
				if(res.x >= 0.0 && res.x <= 1.0f && res.y >= 0.0f && res.y <= 1.0f)
					return true;

				oprev = ocurrent;
			}

			prev = current;
		}

		return false;
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
			getLineIntersectionScalars(x, y, xDir, yDir, last.x, last.y, dx, dy, scalars);

			if(scalars.y >= 0 && scalars.y <= 1.0f && scalars.x >= 0.0f) {
				intersections++;
			}
		}

		for(int i = 1; i < this.corners.size(); i++) {
			Point current = this.corners.get(i);
			
			float dx = current.x - previous.x;
			float dy = current.y - previous.y;
			getLineIntersectionScalars(x, y, xDir, yDir, previous.x, previous.y, dx, dy, scalars);
			if(scalars.y >= 0 && scalars.y <= 1.0f && scalars.x >= 0.0f) {
				intersections++;
			}
			
			previous = current;
		}

		return intersections;	
	}
}
