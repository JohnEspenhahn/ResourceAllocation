package org.espenhahn.allocate.likecompiler;

public class RegisterMain {

	public static void main(String[] args) {
		Resource t0 = new ResourceImpl("t0"),
				 t1 = new ResourceImpl("t1"),
				 t2 = new ResourceImpl("t2"),
				 t3 = new ResourceImpl("t3"),
				 x = new ResourceImpl("x"),
				 y = new ResourceImpl("y"),
				 z = new ResourceImpl("z");
		
		Action[] actions = new Action[] {
				new ActionBinaryImpl(ActionExpression.Action.GTR, t0, x, z), // 0
				new ActionConditionalBranch(ActionConditionalBranch.Action.NOT_IF, t0, 8), // 1
				new ActionBinaryImpl(ActionExpression.Action.SUB, t1, x, z), // 2
				new ActionLoadLitImpl(t2, 3), // 3
				new ActionBinaryImpl(ActionExpression.Action.MULT, x, t1, t2), // 4
				new ActionLoadLitImpl(t3, 5), // 5
				new ActionBinaryImpl(ActionExpression.Action.ADD, z, x, t3), // 6
				new ActionBranch(0), // 7
				new ActionLoadImpl(y, x) // 8
		};
		
		boolean repeat;
		ActionListNode root = ActionListNode.build(actions);
		do {
			repeat = false;
			ActionListNode next = root.getLinearNext();
			while (next != null) {
				repeat |= next.analyzeDataflow();
				next = next.getLinearNext();
			}
		} while (repeat);
		
		ActionListNode next = root.getLinearNext();
		next.print();
	}

}
