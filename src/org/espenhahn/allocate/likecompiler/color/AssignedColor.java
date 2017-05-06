package org.espenhahn.allocate.likecompiler.color;

public interface AssignedColor {
	
	int getColor();

	void setSeen(int color);
	
	boolean assigUnseenColor();
	
}
