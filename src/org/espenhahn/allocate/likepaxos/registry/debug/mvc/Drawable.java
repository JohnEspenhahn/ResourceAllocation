package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.Graphics2D;

public interface Drawable {
	
	public void render(Graphics2D g2);
	
	public int getX();
	
	public int getY();
	
	public void setPos(int x, int y);
}
