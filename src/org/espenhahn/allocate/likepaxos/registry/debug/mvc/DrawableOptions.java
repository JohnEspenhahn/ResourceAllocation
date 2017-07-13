package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.Graphics2D;
import java.awt.Stroke;

public class DrawableOptions {
	
	public Stroke stroke;
	
	public DrawableOptions(Graphics2D g2) {
		this(g2.getStroke());
	}
	
	public DrawableOptions(Stroke stroke) {
		this.stroke = stroke;
	}

	public void apply(Graphics2D g2) {
		if (stroke != null) g2.setStroke(stroke);
	}
	
}
