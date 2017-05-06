package org.espenhahn.allocate.likecompiler.color;

public class AssignedColorFactoryImpl implements AssignedColorFactory {

	private static final int MAX_COLORS = 10;
	
	@Override
	public AssignedColor newAssignedColor() {
		return new AssignedColorImpl(MAX_COLORS);
	}

}
