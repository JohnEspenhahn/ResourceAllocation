package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations;

import java.util.function.Consumer;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.Drawable;

public abstract class TranslateAnimation<T extends Drawable> implements Animation {

	private T drawable;
	
	private double d;
	private int x1, y1;
	private int dx, dy;
	private Consumer<T> callback;

	public TranslateAnimation(T drawable, int end_x, int end_y, Consumer<T> callback) {
		this.drawable = drawable;
		
		this.d = 0;
		this.x1 = drawable.getX();
		this.y1 = drawable.getY();
		this.dx = (end_x - x1);
		this.dy = (end_y - y1);
		this.callback = callback;
	}
	
	protected T getDrawable() {
		return this.drawable;
	}
	
	protected abstract void apply(int x, int y);
	
	@Override
	public boolean animate() {
		this.d += 0.1;

		apply((int) (x1 + dx*d), (int) (y1 + dy*d));
		
		if (this.d >= 1.0) {
			if (callback != null) callback.accept(drawable);
			
			return true;
		} else {
			return false;
		}
	}

}
