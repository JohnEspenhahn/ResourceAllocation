package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects;

import java.awt.Graphics2D;

public class Circle extends Drawable {
	
	private int x;
	private int y;
	private int radius;
	
	public Circle(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	@Override
	public int getRelativeX() {
		return x;
	}
	
	@Override
	public int getRelativeY() {
		return y;
	}
	
	@Override
	public void setRelativePos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(int dx, int dy) {
		setRelativePos(this.getX() + dx, this.getY() + dy);
	}
	
	public void rotateAbout(int centerX, int centerY, double degrees) {
		int tX = this.getX() - centerX;
		int tY = this.getY() - centerY;
		
		int rX = (int) (tX * Math.cos(degrees) - tY * Math.sin(degrees)) + centerX;
		int rY = (int) (tX * Math.sin(degrees) + tY * Math.cos(degrees)) + centerY;
		
		this.setRelativePos(rX, rY);
	}
	
	public void addLabel(int id, String lbl) {
		addLabel(id, new Text(lbl, 0, id * 20));
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawOval(getX()-radius, getY()-radius, radius*2, radius*2);
		this.renderLabels(g);
	}
	
}