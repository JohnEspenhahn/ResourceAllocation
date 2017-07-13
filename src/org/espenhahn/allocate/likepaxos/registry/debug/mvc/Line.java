package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.Graphics2D;

public class Line implements Drawable {

	private int x1, y1;
	private int x2, y2;
	private DrawableOptions options;
	
	public Line(int x1, int y1, int x2, int y2) {
		this(x1,y1,x2,y2,null);
	}
	
	public Line(int x1, int y1, int x2, int y2, DrawableOptions options) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.options = options;
	}
	
	public void setEnd(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
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
		g2.drawLine(x1, y1, x2, y2);
		
		// Reset
		if (old_options != null) {
			old_options.apply(g2);
		}
	}
	
}
