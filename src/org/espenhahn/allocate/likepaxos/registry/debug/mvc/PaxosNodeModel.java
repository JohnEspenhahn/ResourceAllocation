package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

public class PaxosNodeModel {
	public String name;
	public short id;
	
	private Circle circle;

	public PaxosNodeModel(String name, short id) {
		this.name = name;
		this.id = id;
	}
	
	public void setLargestProposalNumber(double pn) {
		this.circle.setLabel("" + pn);
	}
	
	public Circle getCircle() {
		return this.circle;
	}
	
	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	
}
