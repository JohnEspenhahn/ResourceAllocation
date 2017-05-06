package org.espenhahn.allocate.likecompiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.espenhahn.allocate.util.MinHeap;

public class ActionListNode {

	private String lbl;
	
	private Action action;
	private ActionListNode next;
	private ActionListNode jmp_action;
	private Set<Resource> dataflow_set;
	
	private ActionListNode(String lbl, Action a) {
		this.lbl = lbl;
		
		this.action = a;
		this.dataflow_set = new HashSet<Resource>();
	}
	
	public static ActionListNode build(Action[] actions) {
		ActionListNode[] nodes = new ActionListNode[actions.length];
		
		// Add last node
		int end = actions.length-1;
		nodes[end] = new ActionListNode("" + (end+1), actions[end]);
		
		// Add mid nodes with links to linear next
		for (int i = actions.length-2; i >= 0; i--) {
			nodes[i] = new ActionListNode("" + (i+1), actions[i]);
			nodes[i].setNext(nodes[i+1]);
		}
		
		// Add jumps
		for (ActionListNode n: nodes) {
			Integer jmp = n.getAction().getBranchAddress();
			if (jmp != null)
				n.setJumpAction(nodes[jmp]);
		}
		
		ActionListNode root = new ActionListNode("root", null);
		root.setNext(nodes[0]);
		
		return root;
	}
	
	/**
	 * Convert the given actions to an ActionList, then analyzeDataflow for the entire list
	 * @param actions
	 * @param resource_in All the (temporary) resources used in the actions
	 * @return The first useful node after dataflow has been analyzed (will be root OR child of root)
	 */
	@SuppressWarnings("unchecked")
	public static ActionListNode buildAndAnalyze(Action[] actions, ResourceInterferable[] resource_in) {
		// Iterate until fixed point is reached
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
		
		// Create edges (each resource interferes with other resources in same dataflow set)
		ActionListNode next = root.getLinearNext();
		while (next != null) {
			ResourceInterferable[] set = next.dataflow_set.toArray(new ResourceInterferable[next.dataflow_set.size()]);
			for (int i = 0; i < set.length-1; i++) {
				ResourceInterferable r1 = set[i];
				for (int j = i+1; j < set.length; j++) {
					ResourceInterferable r2 = set[j];
					
					r1.addInterferance(r2);
					r2.addInterferance(r1);
				}
			}
			
			next = next.getLinearNext();
		}
		
		List<ResourceInterferable> order = new ArrayList<ResourceInterferable>(resource_in.length);
		MinHeap<ResourceInterferable> heap = new MinHeap<ResourceInterferable>(resource_in);
		while (!heap.isEmpty()) { // O(n^2)
			// Removing will change all weights, so need to rebuild
			ResourceInterferable r = heap.removemin();
			order.add(r); // O(lg n)
			r.onBeingRemoved();
			
			heap.buildheap(); // O(n)
		}
		
		// Try to assign colors
		for (ResourceInterferable r: order) {
			r.assignColor();
		}
		
		return root.getLinearNext();
	}
	
	public void print() {
		// System.out.print(dataflow_set);
		System.out.println(action.toString() + " {" + dataflow_set + "}");
		
		ActionListNode next = getLinearNext();
		if (next != null) next.print();
	}
	
	public ActionListNode evaluate() {
		if (isInvalid()) throw new RuntimeException();
		
		boolean branch = action.evaluate();
		if (branch) return getJumpNext();
		else return getLinearNext();
	}
	
	protected ActionListNode getLinearNext() {
		ActionListNode next_valid = next;
		while (next_valid != null && next_valid.isInvalid())
			next_valid = next_valid.getLinearNext();
		
		return next_valid;
	}
	
	protected ActionListNode getJumpNext() {		
		ActionListNode jmp_valid = jmp_action;
		while (jmp_valid != null && jmp_valid.isInvalid())
			jmp_valid = jmp_valid.getLinearNext();
		
		return jmp_valid;
	}
	
	private void setInvalid() {
		this.action = null;
	}
	
	private boolean isInvalid() {
		return action == null;
	}
	
	public void setNext(ActionListNode n) {
		this.next = n;
	}
	
	public void setJumpAction(ActionListNode n) {
		this.jmp_action = n;
	}
	
	public void insertBefore(Action a) {	
		ActionListNode n = new ActionListNode(this.lbl + "*", this.action); // Move this value ahead one
		n.setNext(this.next);
		
		this.action = a; // Insert new value at this location (to make jumps still valid)
		this.setNext(n);
	}
	
	public ActionListNode remove() {
		this.setInvalid();
		return this.getLinearNext();
	}
	
	protected Action getAction() {
		return action;
	}
	
	protected Set<Resource> getDataflowSet() {
		return this.dataflow_set;
	}
	
	protected void setDataflowSet(Set<Resource> set) {
		this.dataflow_set = set;
	}
	
	/**
	 * Run an iteration of dataflow analysis on this line
	 * @return True if needs to repeat (didn't reach fixed point)
	 */
	public boolean analyzeDataflow() {
		Set<Resource> nextSet = new HashSet<Resource>();
		
		// ((L_(i+1) U L_j) - { out }) U { in }
		Action action = getAction();
		
		if (action.dataflowFollowLinearNext()) {
			ActionListNode next = getLinearNext();
			if (next != null) nextSet.addAll(next.getDataflowSet());
		}
		
		if (action.dataflowFollowJumpNext()) {
			ActionListNode jmp = getJumpNext();
			if (jmp != null) nextSet.addAll(jmp.getDataflowSet());	
		}
		
		Resource out = action.out();
		if (out != null) nextSet.remove(out);
		
		for (Resource in: action.in())
			nextSet.add(in);
		
		boolean fixedPoint = getDataflowSet().equals(nextSet);
		this.setDataflowSet(nextSet);
		
		return !fixedPoint;
	}
	
}
