package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects;

import java.awt.Graphics2D;

public class Line extends Drawable {

	private int x, y;
	private int dx, dy;
	private DrawableOptions options;
	
	public Line(int x, int y, int dx, int dy) {
		this(x,y,dx,dy,null);
	}
	
	public Line(int x, int y, int dx, int dy, DrawableOptions options) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.options = options;
	}
	
	public void setRelativePos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void translateDelta(int dx, int dy) {
		this.dx += dx;
		this.dy += dy;
	}
	
	public int getRelativeX() {
		return x;
	}
	
	public int getRelativeY() {
		return y;
	}
	
	@Override
	public void render(Graphics2D g2) {
		// Load options
		DrawableOptions old_options = null;
		if (options != null) {
			old_options = new DrawableOptions(g2);
			options.apply(g2);
		}
		
		// Render
		g2.drawLine(getX(), getY(), getX()+dx, getY()+dy);
		
		// Reset
		if (old_options != null) {
			old_options.apply(g2);
		}
	}
	
}
