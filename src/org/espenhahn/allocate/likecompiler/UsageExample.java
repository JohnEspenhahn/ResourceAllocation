package org.espenhahn.allocate.likecompiler;

import org.espenhahn.allocate.likecompiler.actions.ActionBinaryImpl;
import org.espenhahn.allocate.likecompiler.actions.ActionBranchImpl;
import org.espenhahn.allocate.likecompiler.actions.ActionConditionalBranchImpl;
import org.espenhahn.allocate.likecompiler.actions.ActionLoadImpl;
import org.espenhahn.allocate.likecompiler.actions.ActionLoadLitImpl;
import org.espenhahn.allocate.likecompiler.actions.ActionSubExpressionImpl;

public class UsageExample {

	public static void main(String[] args) {
		ResourceInterferable
				 t0 = new ResourceInterferableImpl("t0"),
				 t1 = new ResourceInterferableImpl("t1"),
				 t2 = new ResourceInterferableImpl("t2"),
				 t3 = new ResourceInterferableImpl("t3"),
				 x = new ResourceInterferableImpl("x"),
				 y = new ResourceInterferableImpl("y"),
				 z = new ResourceInterferableImpl("z");
		
		ResourceInterferable[] resources = new ResourceInterferable[] {
				t0, t1, t2, t3, x, y, z
		};
		
		Action[] actions = new Action[] {
				new ActionBinaryImpl(ActionSubExpressionImpl.Action.GTR, t0, x, z), // 0
				new ActionConditionalBranchImpl(ActionConditionalBranchImpl.Action.NOT_IF, t0, 8), // 1
				new ActionBinaryImpl(ActionSubExpressionImpl.Action.SUB, t1, x, z), // 2
				new ActionLoadLitImpl(t2, 3), // 3
				new ActionBinaryImpl(ActionSubExpressionImpl.Action.MULT, x, t1, t2), // 4
				new ActionLoadLitImpl(t3, 5), // 5
				new ActionBinaryImpl(ActionSubExpressionImpl.Action.ADD, z, x, t3), // 6
				new ActionBranchImpl(0), // 7
				new ActionLoadImpl(y, x) // 8
		};
		
		ActionListNode next = ActionListNode.buildAndAnalyze(actions, resources);
		next.print();
		
		
	}

}
