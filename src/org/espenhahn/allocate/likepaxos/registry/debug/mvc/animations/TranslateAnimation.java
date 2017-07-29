package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations;

import java.util.function.Consumer;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Drawable;

public abstract class TranslateAnimation<T extends Drawable> implements Animation {

	private T drawable;
	
	private int steps;
	private int x1, y1;
	private int dx, dy;
	private Consumer<T> callback;

	public TranslateAnimation(T drawable, int end_x, int end_y, Consumer<T> callback) {
		this.drawable = drawable;
		
		this.steps = 0;
		this.x1 = drawable.getX();
		this.y1 = drawable.getY();
		this.dx = (int) ((end_x - x1) * 0.1);
		this.dy = (int) ((end_y - y1) * 0.1);
		this.callback = callback;
	}
	
	protected T getDrawable() {
		return this.drawable;
	}
	
	protected abstract void apply(int x, int y);
	
	@Override
	public boolean animate() {
		this.steps += 1;
		apply(dx, dy);
		if (this.steps >= 10) {
			if (callback != null) callback.accept(drawable);
			
			return true;
		} else {
			return false;
		}
	}

}
