package org.espenhahn.allocate.likecompiler;

public interface Action {
	
	/**
	 * @return Resource being overridden by this action (can be null)
	 */
	Resource out();
	
	/**
	 * Evaluate this action
	 * @return True if should follow jump path
	 */
	boolean evaluate();
	
	/**
	 * @return Resources used in this action
	 */
	Resource[] in();
	
	Integer getBranchAddress();
	
	boolean dataflowFollowLinearNext();
	
	boolean dataflowFollowJumpNext();
	
}
