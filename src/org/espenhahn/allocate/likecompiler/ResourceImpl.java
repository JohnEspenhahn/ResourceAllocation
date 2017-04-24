package org.espenhahn.allocate.likecompiler;

public class ResourceImpl implements Resource {

	private int val;
	private String label;
	
	public ResourceImpl(String label) {
		this.label = label;
	}
	
	@Override
	public void set(int x) {
		val = x;
	}

	@Override
	public int get() {
		return val;
	}
	
	@Override
	public String toString() {
		return label;
	}

}
