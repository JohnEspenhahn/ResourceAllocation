package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations.Animation;
import org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations.GrowLineAnimation;

public class DebugRenderer {

	private Frame mainFrame;
	public Button backButton, nextButton;
	
	private AnimatedCanvas canvas;
	private Panel controlPanel;
	
	private List<Circle> ring;
	private List<Drawable> objects;

	public DebugRenderer() {
		this.ring = new ArrayList<Circle>();
		this.objects = new ArrayList<Drawable>();
		
		prepareGUI();
	}

//	public static void main(String[] args) {
//		DebugRenderer awt = new DebugRenderer();
//		awt.show();
//	}
	
	public void invoke(Runnable r) {
		SwingUtilities.invokeLater(() -> {
			r.run();
			canvas.repaint();
		});
	}
	
	public void addCircleToRing(Circle c) {
		ring.add(c);
		for (int i = 0, ii = ring.size(); i < ii; i++) {
			Circle c2 = ring.get(i);
			c2.setPos(200, 50);
			c2.rotateAbout(200, 125, (2*Math.PI) * (double)i/ii);
		}
		
		// Async repaint
		SwingUtilities.invokeLater(() -> canvas.repaint());
	}
	
	public Drawable addLineBetween(Circle c1, Circle c2) {
		return addLineBetween(c1, c2, null);
	}
	
	public Drawable addLineBetween(Circle c1, Circle c2, Consumer<Line> callback) {
		Line l = new Line(c1.getX(), c1.getY(), c1.getX(), c1.getY());
		objects.add(l);
		
		canvas.addAnimation(new GrowLineAnimation(l, c2.getX(), c2.getY(), callback));
		
		return l;
	}
	
	public Drawable addTextBetween(String lbl, Drawable d1, Drawable d2, Consumer<Text> callback) {
		Text t = new Text(lbl, d1.getX(), d1.getY());
		objects.add(t);
		
		canvas.addAnimation(new MoveDrawableAnimation(t, d2.getX(), d2.getY(), callback));
		
		return t;
	}
	
	public boolean removeShape(Drawable object) {
		return this.objects.remove(object);
	}

	private void prepareGUI() {
		mainFrame = new Frame("Paxois-like");
		mainFrame.setSize(650, 750);
		mainFrame.setLayout(new GridLayout(2, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		Panel actionPanel = new Panel();
		actionPanel.setSize(650, 100);
		actionPanel.setLayout(new FlowLayout());
		backButton = new Button("Back");
		actionPanel.add(backButton);
		
		nextButton = new Button("Next");
		actionPanel.add(nextButton);

		controlPanel = new Panel();
		controlPanel.setSize(650, 650);
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(controlPanel);
		mainFrame.add(actionPanel);
	}

	public void show() {
		canvas = new AnimatedCanvas();
		controlPanel.add(canvas);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	class AnimatedCanvas extends Canvas {
		private static final long serialVersionUID = 7392411307006281114L;
		private Deque<Animation> animations;

		public AnimatedCanvas() {
			this.animations = new ArrayDeque<Animation>();
			
			setBackground(Color.gray);
			setSize(650, 650);
		}
		
		public void addAnimation(Animation a) {
			synchronized (animations) {
				animations.addLast(a);
			}
		}

		public void paint(Graphics g) {
			synchronized (animations) {
				int size = animations.size();
				for (int i = 0; i < size; i++) {
					Animation a = animations.removeFirst();
					boolean done = a.animate();
					if (!done) animations.addLast(a); 
				}
			}
			
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.clearRect(0, 0, getWidth(), getHeight());
			
			for (Circle c: ring)
				c.render(g2);
			
			for (Drawable d: objects)
				d.render(g2);
		}
	}
}