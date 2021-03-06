package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects;

import java.awt.Graphics2D;

public class Text extends Drawable {
	
	private String lbl;
	private int x, y;
	
	private DrawableOptions options;

	public Text(String lbl, int x, int y) {
		this(lbl, x, y, null);
	}
	
	public Text(String lbl, int x, int y, DrawableOptions options) {
		this.x = x;
		this.y = y;
		this.lbl = lbl;
		this.options = options;
	}
	
	public void setRelativePos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getRelativeX() {
		return x;
	}
	
	public int getRelativeY() {
		return y;
	}

	@Override
	public void render(Graphics2D g) {
		// Load options
		DrawableOptions old_options = null;
		if (options != null) {
			old_options = new DrawableOptions(g);
			options.apply(g);
		}
		
		// Render
		int width = g.getFontMetrics().stringWidth(lbl);
		g.drawString(lbl, getX()-width/2, getY());
		
		// Reset
		if (old_options != null) {
			old_options.apply(g);
		}
	}

}
