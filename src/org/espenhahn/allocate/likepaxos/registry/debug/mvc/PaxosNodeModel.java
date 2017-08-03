package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Circle;

public class PaxosNodeModel {
	private static final int LBL_ID = 0,
							 LBL_PROPOSAL_NUMBER = 1,
							 LBL_ACCEPTED_PROPOSAL_NUMBER = 2;
	
	private String name;
    private short id;
	
	private Circle circle;

    protected PaxosNodeModel(String name, short id) {
		this.name = name;
		this.id = id;
	}
	
	protected void setProposalNumber(Double pn) {
		this.circle.addLabel(LBL_PROPOSAL_NUMBER, (pn != null ? "" + pn : ""));
	}

    protected void setAcceptedProposalNumber(Double pn) {
		this.circle.addLabel(LBL_ACCEPTED_PROPOSAL_NUMBER, (pn != null ? "" + pn : ""));
	}

    protected Circle getCircle() {
		return this.circle;
	}

    protected void setCircle(Circle circle) {
		this.circle = circle;

		if (circle != null)
			this.circle.addLabel(LBL_ID, "" + id);
	}
	
}
