package org.espenhahn.allocate.likecompiler.actions;

import org.espenhahn.allocate.likecompiler.Action;
import org.espenhahn.allocate.likecompiler.Resource;

public class ActionLoadImpl implements Action {

	private Resource out;
	private Resource[] in;
	
	public ActionLoadImpl(Resource out, Resource in) {
		this.out = out;
		this.in = new Resource[] { in };
	}
	
	@Override
	public Resource out() {
		return out;
	}

	@Override
	public boolean evaluate() {
		out.set(in[0].get());
		return false;
	}

	@Override
	public Resource[] in() {
		return in;
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

}
