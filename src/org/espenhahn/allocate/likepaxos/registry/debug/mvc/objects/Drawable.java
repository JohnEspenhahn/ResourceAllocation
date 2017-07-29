package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects;

import java.awt.Graphics2D;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class Drawable {
	
	private SortedMap<Integer, Text> labels;
	private Drawable owner;
	
	public Drawable() {
		this(null);
	}
	
	public Drawable(Drawable owner) {
		this.labels = new TreeMap<>();
		this.owner = owner;
	}
	
	protected void setOwner(Drawable d) {
		this.owner = d;
	}
	
	public void addLabel(int id, Text label) {
		labels.put(id, label);
		label.setOwner(this);
	}
	
	public void renderLabels(Graphics2D g) {
		for (Entry<Integer, Text> e: labels.entrySet()) {
			e.getValue().render(g);
		}
	}
	
	public abstract void render(Graphics2D g2);
	
	public int getX() {
		return getRelativeX() + (owner != null ? owner.getX() : 0);
	}
	
	public abstract int getRelativeX();
	
	public int getY() {
		return getRelativeY() + (owner != null ? owner.getY() : 0);
	}
	
	public abstract int getRelativeY();
	
	public abstract void setRelativePos(int x, int y);
	
	public void translate(int dx, int dy) {
		setRelativePos(getRelativeX() + dx, getRelativeY() + dy);
	}
}
