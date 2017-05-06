package org.espenhahn.allocate.likecompiler.color;

public class AssignedColorImpl implements AssignedColor {

	private boolean[] seenColors;
	private int color;
	
	public AssignedColorImpl(int numColors) {
		seenColors = new boolean[numColors];
		color = -1;
	}
	
	@Override
	public int getColor() {
		return color;
	}
	
	@Override
	public void setSeen(int color) {
		if (color < 0) return;
		
		seenColors[color] = true;
	}

	@Override
	public boolean assigUnseenColor() {
		for (int i = 0; i < seenColors.length; i++) {
			if (!seenColors[i]) {
				color = i;
				return true;
			}
		}
		
		return false;
	}

}
