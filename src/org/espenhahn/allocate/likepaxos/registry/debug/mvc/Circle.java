package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.Graphics2D;

public class Circle implements Drawable {
	
	private int x;
	private int y;
	private int radius;
	private Text label;
	
	public Circle(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		
		if (this.label != null)
			this.label.setPos(x, y);
	}
	
	public void translate(int dx, int dy) {
		setPos(this.x + dx, this.y + dy);
	}
	
	public void rotateAbout(int centerX, int centerY, double degrees) {
		int tX = this.x - centerX;
		int tY = this.y - centerY;
		
		int rX = (int) (tX * Math.cos(degrees) - tY * Math.sin(degrees)) + centerX;
		int rY = (int) (tX * Math.sin(degrees) + tY * Math.cos(degrees)) + centerY;
		
		this.setPos(rX, rY);
	}
	
	public void setLabel(String lbl) {
		this.label = new Text(lbl, x, y);
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawOval(x-radius, y-radius, radius*2, radius*2);
		
		if (label != null) label.render(g);
	}
	
}