package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations;

import java.util.function.Consumer;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Text;

public class TranslateTextAnimation extends TranslateAnimation<Text> {

	public TranslateTextAnimation(Text t, int end_x, int end_y, Consumer<Text> callback) {
		super(t, end_x, end_y, callback);
	}

	@Override
	protected void apply(int dx, int dy) {
		getDrawable().translate(dx, dy);
	}

}
