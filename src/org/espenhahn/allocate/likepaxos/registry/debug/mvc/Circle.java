package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.Color;
import java.awt.Graphics2D;

public class Circle {
	
	private int x;
	private int y;
	private int radius;
	private String label;
	
	public Circle(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void rotateAbout(int centerX, int centerY, double degrees) {
		int tX = this.x - centerX;
		int tY = this.y - centerY;
		
		int rX = (int) (tX * Math.cos(degrees) - tY * Math.sin(degrees)) + centerX;
		int rY = (int) (tX * Math.sin(degrees) + tY * Math.cos(degrees)) + centerY;
		
		this.setPos(rX, rY);
	}
	
	public void setLabel(String lbl) {
		this.label = lbl;
	}
	
	public void render(Graphics2D g) {
		g.drawOval(x-radius, y-radius, radius*2, radius*2);
		
		if (label != null && label.length() > 0) {
			Color old_color = g.getColor();
			g.setColor(Color.GREEN);
			
			int width = g.getFontMetrics().stringWidth(label);
			g.drawString(label, x-width/2, y);
			
			g.setColor(old_color);
		}
	}
	
}