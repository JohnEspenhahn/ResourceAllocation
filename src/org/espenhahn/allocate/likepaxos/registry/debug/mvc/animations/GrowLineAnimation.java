package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations;

import java.util.function.Consumer;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.Line;

public class GrowLineAnimation extends TranslateAnimation<Line> {

	public GrowLineAnimation(Line l, int end_x, int end_y, Consumer<Line> callback) {
		super(l, end_x, end_y, callback);
	}
	
	@Override
	protected void apply(int x, int y) {
		getDrawable().setEnd(x, y);
	}

}
