package org.espenhahn.allocate.likepaxos;

public interface AcceptListener<E> {

	/**
	 * Called in proposer when enough acceptors accept
	 * @param proposalNumber The proposalNumber being accepted
	 */
	void onAccept(double proposalNumber);
	
}
