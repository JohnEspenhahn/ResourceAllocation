package org.espenhahn.allocate.likecompiler.actions;

import org.espenhahn.allocate.likecompiler.Action;
import org.espenhahn.allocate.likecompiler.Resource;

public class ActionBranchImpl implements Action {
	
	private static final Resource[] IN = new Resource[0];

	private int branch_addr;
	
	public ActionBranchImpl(int branch_addr) {		
		this.branch_addr = branch_addr;
	}
	
	@Override
	public boolean evaluate() {
		return true;
	}

	@Override
	public Resource[] in() {
		return IN;
	}

	@Override
	public Resource out() {
		return null;
	}
	
	@Override
	public Integer getBranchAddress() {
		return branch_addr;
	}
	
	@Override
	public boolean dataflowFollowLinearNext() {
		return false;
	}
	
	@Override
	public boolean dataflowFollowJumpNext() {
		return true;
	}

}
