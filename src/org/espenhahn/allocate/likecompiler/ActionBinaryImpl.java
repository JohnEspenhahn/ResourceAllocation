package org.espenhahn.allocate.likecompiler;

public class ActionBinaryImpl implements Action {

	private Resource out;
	private ActionExpression sub;
	
	public ActionBinaryImpl(ActionExpression.Action op, Resource out, Resource p1, Resource p2) {
		this.out = out;
		this.sub = new ActionExpression(op, p1, p2);
	}
	
	@Override
	public boolean evaluate() {
		out.set(sub.evaluate());		
		return false;
	}

	@Override
	public Resource[] in() {
		return sub.in();
	}

	@Override
	public Resource out() {
		return out;
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
	public int hashCode() {
		int hash = 17;
		hash = hash*31 + out.hashCode();
		hash = hash*31 + sub.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ActionBinaryImpl) {
			ActionBinaryImpl bin = (ActionBinaryImpl) o;
			return bin.out.equals(out) && bin.sub.equals(sub);
		} else {
			return false;
		}
	}

}
