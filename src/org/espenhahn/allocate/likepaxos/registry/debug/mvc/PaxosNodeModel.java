package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Circle;

public class PaxosNodeModel {
	private static final int LBL_PROPOSAL_NUMBER = 0,
							 LBL_ACCEPTED_PROPOSAL_NUMBER = 1;
	
	public String name;
	public short id;
	
	private Circle circle;

	public PaxosNodeModel(String name, short id) {
		this.name = name;
		this.id = id;
	}
	
	public void setProposalNumber(Double pn) {
		this.circle.addLabel(LBL_PROPOSAL_NUMBER, (pn != null ? "" + pn : ""));
	}
	
	public void setAcceptedProposalNumber(Double pn) {
		this.circle.addLabel(LBL_ACCEPTED_PROPOSAL_NUMBER, (pn != null ? "" + pn : ""));
	}
	
	public Circle getCircle() {
		return this.circle;
	}
	
	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	
}
