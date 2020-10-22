import java.awt.*;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.event.*;

public class Main {

	private static JFrame window;
	private static Shape shape = new Shape();
	private static Shape collisionShape = new Shape();
	public static float mouseX = 0.0f, mouseY = 0.0f;

	private static List<Shape.Point> testPoints = new LinkedList<>();

	public static void main(final String[] args) {
		window = new JFrame();
		window.setDefaultCloseOperation(3);
		window.setSize(1020, 720);
		MouseAdapter adapter;
		window.addMouseListener(adapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Main.onKeyPressed(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				Main.mouseX = (float) e.getX() - 7;
				Main.mouseY = (float) e.getY() - 27;
				Main.window.repaint();
			}
		});
		window.addMouseMotionListener(adapter);

		window.add(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

				g2d.setColor(new Color(51, 51, 51));
				g2d.fill(this.getBounds());
				g2d.setColor(Color.RED);

				drawShape(g2d, Main.shape);

				if(collisionShape.intersects(shape))
					g2d.setColor(Color.ORANGE);
				else
					g2d.setColor(Color.GREEN);

				drawShape(g2d, collisionShape);

				if(shape.contains(Main.mouseX, Main.mouseY)) {
					g2d.setColor(Color.YELLOW);
				}else{
					g2d.setColor(Color.BLUE);
				}
				final int r = 5;
				g2d.fillOval((int) Main.mouseX - r, (int) Main.mouseY - r, r << 1, r << 1);	

				Iterator<Shape.Point> it = testPoints.iterator();
				while(it.hasNext()) {
					Shape.Point p = it.next();
					if(shape.contains(p.x, p.y)) {
						g2d.setColor(Color.YELLOW);
					}else{
						g2d.setColor(Color.BLUE);
					}
					final int pr = 2;
					g2d.fillOval((int) p.x - pr, (int) p.y - pr, pr << 1, pr << 1);	
				}

			}

			protected void drawShape(Graphics2D g2d, Shape shape) {
				Shape.Point prev = shape.getLastPoint();

				for(Shape.Point point : shape.getPoints()) {
					g2d.fillOval((int) point.x - 3, (int) point.y - 3, 6, 6);
					g2d.drawLine((int) point.x, (int) point.y, (int) prev.x, (int) prev.y);
					prev = point;
				}

			}
		});

		collisionShape.addPoint(100.0f, 300.0f);
		collisionShape.addPoint(150.0f, 330.0f);
		collisionShape.addPoint(160.0f, 230.0f);
		collisionShape.addPoint(360.0f, 230.0f);
		collisionShape.addPoint(560.0f, 400.0f);

		for(int i = 0; i < 10000; i++)
			testPoints.add(new Shape.Point((float) Math.random() * 1020, (float) Math.random() * 720));

		window.setVisible(true);
		
	}

	public static void onKeyPressed(MouseEvent e) {
		int x = e.getX() - 7;
		int y = e.getY() - 27;
		if(e.getButton() == MouseEvent.BUTTON1) {
			shape.addPoint(x, y);
		}else{
			testPoints.add(new Shape.Point(x, y));
		}
		window.repaint();

	}
}
