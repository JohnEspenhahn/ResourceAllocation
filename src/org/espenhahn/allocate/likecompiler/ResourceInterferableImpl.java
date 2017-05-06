package org.espenhahn.allocate.likecompiler;

import java.util.HashSet;
import java.util.Set;

import org.espenhahn.allocate.likecompiler.color.AssignedColor;
import org.espenhahn.allocate.likecompiler.color.AssignedColorFactoryService;

public class ResourceInterferableImpl implements ResourceInterferable {

	private int val;
	private String label;
	
	private Set<ResourceInterferable> interferance;
	private int removed_edges;
	
	private AssignedColor assignedColor;
	
	public ResourceInterferableImpl(String label) {
		this.label = label;
		
		this.interferance = new HashSet<ResourceInterferable>();
		this.removed_edges = 0;
		
		this.assignedColor = AssignedColorFactoryService.getFactory().newAssignedColor();
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
		return label + "[" + getColor() + "]";
	}
	
	@Override
	public void onBeingRemoved() {
		// Remove other edges
		for (ResourceInterferable r: interferance)
			r.decrementInterferance();
	}
	
	@Override
	public void decrementInterferance() {
		removed_edges += 1;
	}

	@Override
	public void addInterferance(ResourceInterferable r) {
		interferance.add(r);
	}

	@Override
	public int getInterferanceCount() {
		return interferance.size() - removed_edges;
	}
	
	@Override
	public int getColor() {
		return assignedColor.getColor();
	}

	@Override
	public void assignColor() {
		for (ResourceInterferable r: interferance) {
			assignedColor.setSeen(r.getColor());
		}
		
		if (!assignedColor.assigUnseenColor()) {
			throw new RuntimeException("Ran out of colors!");
		}
	}
	
	@Override
	public int compareTo(ResourceInterferable o) {
		return o.getInterferanceCount() - getInterferanceCount();
	}
	
}
