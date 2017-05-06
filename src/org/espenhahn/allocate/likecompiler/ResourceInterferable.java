package org.espenhahn.allocate.likecompiler;

public interface ResourceInterferable extends Resource, Comparable<ResourceInterferable> {
	
	void onBeingRemoved();
	
	void decrementInterferance();
	
	void addInterferance(ResourceInterferable r);
	
	int getInterferanceCount();
	
	/**
	 * Get the color of this resource
	 * @return Integer.MAX_VALUE == undefined
	 */
	int getColor();
	
	void assignColor();
	
}
