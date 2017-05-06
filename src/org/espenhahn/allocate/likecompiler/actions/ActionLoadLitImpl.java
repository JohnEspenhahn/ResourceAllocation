package org.espenhahn.allocate.likecompiler.actions;

import org.espenhahn.allocate.likecompiler.Action;
import org.espenhahn.allocate.likecompiler.Resource;

public class ActionLoadLitImpl implements Action {
	
	private static final Resource[] IN = new Resource[0];
	
	private Resource out;
	private int lit;
	
	public ActionLoadLitImpl(Resource out, int lit) {
		this.out = out;
		this.lit = lit;
	}

	@Override
	public Resource out() {
		return out;
	}

	@Override
	public boolean evaluate() {
		out.set(lit);
		return false;
	}

	@Override
	public Resource[] in() {
		return IN;
	}

	@Override
	public Integer getBranchAddress() {
		return null;
	}
	
	@Override
	public boolean dataflowFollowLinearNext() {
		return true;
	}
	
	@Override
	public boolean dataflowFollowJumpNext() {
		return false;
	}
	
	@Override
	public String toString() {
		return out() + "=" + lit;
	}

}
