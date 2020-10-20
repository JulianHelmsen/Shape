import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;

public class Main {

	private static JFrame window;
	private static Shape shape = new Shape();
	public static float mouseX = 0.0f, mouseY = 0.0f;

	private static List<Shape.Point> testPoints = new ArrayList<>();

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


				int firstX = -1;
				int firstY = -1;
				int prevX = -1;
				int prevY = -1;
				for(Shape.Point point : shape.getPoints()) {

					int x = (int) point.x;
					int y = (int) point.y;
					g2d.fillOval(x - 3, y - 3, 6, 6);
					if(firstX == -1) {
						firstX = x;
						firstY = y;
					}else{
						g2d.drawLine(x, y, prevX, prevY);
					}
					prevX = x;
					prevY = y;
				}
				g2d.drawLine(firstX, firstY, prevX, prevY);
				int collisions = shape.countCollisions(Main.mouseX, Main.mouseY, 0.23453f, 0.123546f);

				if(shape.contains(Main.mouseX, Main.mouseY)) {
					g2d.setColor(Color.YELLOW);
				}else{
					g2d.setColor(Color.BLUE);
				}
				final int r = 5;
				g2d.fillOval((int) Main.mouseX - r, (int) Main.mouseY - r, r << 1, r << 1);	

				for(Shape.Point p : testPoints) {
					if(shape.contains(p.x, p.y)) {
						g2d.setColor(Color.YELLOW);
					}else{
						g2d.setColor(Color.BLUE);
					}
					final int pr = 2;
					g2d.fillOval((int) p.x - pr, (int) p.y - pr, pr << 1, pr << 1);	
				}

			}
		});

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
