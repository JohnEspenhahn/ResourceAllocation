package org.espenhahn.allocate.likecompiler.color;

public class AssignedColorFactoryImpl implements AssignedColorFactory {

	private final int max_colors;
	
	public AssignedColorFactoryImpl(int max_colors) {
		this.max_colors = max_colors;
	}
	
	@Override
	public AssignedColor newAssignedColor() {
		return new AssignedColorImpl(max_colors);
	}

}
