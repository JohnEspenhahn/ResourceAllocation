package org.espenhahn.allocate.likecompiler.actions;

import org.espenhahn.allocate.likecompiler.Resource;

/**
 * An expression that doesn't specify the out destination
 * @author John
 *
 */
public class ActionSubExpressionImpl {
	private Action op;
	private Resource[] ins;
	
	public ActionSubExpressionImpl(Action op, Resource p1, Resource p2) {
		this.op = op;
		this.ins = new Resource[] { p1, p2 };
	}
	
	public int evaluate() {
		switch (op) {
		case ADD:
			return ins[0].get() + ins[1].get();
		case SUB:
			return ins[0].get() - ins[1].get();
		case MULT:
			return ins[0].get() * ins[1].get();
		case DIV:
			return ins[0].get() / ins[1].get();
		case GTR:
			return (ins[0].get() > ins[1].get() ? 1 : 0);
		case LSS:
			return (ins[0].get() < ins[1].get() ? 1 : 0);
		default:
			throw new RuntimeException();
		}
		
	}

	public Resource[] in() {
		return ins;
	}
	
	@Override
	public int hashCode() {
		int hash = 17 + op.hashCode();
		switch (op) {
		case ADD: case MULT: // Commutative
			hash = hash*31 + (ins[0].hashCode() ^ ins[1].hashCode());
			break;
		default:
			hash = hash*31 + ins[0].hashCode();
			hash = hash*31 + ins[1].hashCode();
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ActionSubExpressionImpl) {
			ActionSubExpressionImpl e = (ActionSubExpressionImpl) o;
			return e.op == op && e.ins[0].equals(ins[0]) && e.ins[1].equals(ins[1]);
		} else {
			return false;
		}
	}
	
	public enum Action {
		ADD, SUB, MULT, DIV, GTR, LSS
	}
}
