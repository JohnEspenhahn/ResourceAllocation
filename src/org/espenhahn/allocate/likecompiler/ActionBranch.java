package org.espenhahn.allocate.likecompiler;

public class ActionBranch implements Action {
	
	private static final Resource[] IN = new Resource[0];

	private int branch_addr;
	
	public ActionBranch(int branch_addr) {		
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
