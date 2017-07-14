package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations;

import java.util.function.Consumer;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.Line;

public class GrowLineAnimation implements Animation {
	
	private Line l;
	private double d;
	private int x1, y1;
	private int dx, dy;
	private Consumer<Line> callback;

	public GrowLineAnimation(Line l, int end_x, int end_y, Consumer<Line> callback) {
		this.l = l;
		this.x1 = l.getX();
		this.y1 = l.getY();
		this.dx = (end_x - x1);
		this.dy = (end_y - y1);
		this.callback = callback;
	}
	
	@Override
	public boolean animate() {
		this.d += 0.1;

		l.setEnd((int) (x1 + dx*d), (int) (y1 + dy*d));
		
		if (this.d >= 1.0) {
			if (callback != null) callback.accept(l);
			
			return true;
		} else {
			return false;
		}
	}

}
