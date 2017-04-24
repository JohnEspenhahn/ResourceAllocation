package org.espenhahn.allocate.likecompiler.actions;

import org.espenhahn.allocate.likecompiler.Action;
import org.espenhahn.allocate.likecompiler.Resource;

public class ActionConditionalBranchImpl implements Action {

	
	private Action op;
	private Resource[] rs;
	
	private int branch_addr;
	
	public ActionConditionalBranchImpl(Action op, Resource r, int branch_addr) {
		this.op = op;
		this.rs = new Resource[] { r };
		
		this.branch_addr = branch_addr;
	}
	
	@Override
	public boolean evaluate() {
		switch (op) {
		case IF:
			return rs[0].get() != 0;
		case NOT_IF:
			return rs[0].get() == 0;
		}
		
		throw new RuntimeException();
	}

	@Override
	public Resource[] in() {
		return rs;
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
		return true;
	}
	
	@Override
	public boolean dataflowFollowJumpNext() {
		return true;
	}
	
	public enum Action {
		IF, NOT_IF
	}

}
