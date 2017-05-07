package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;

public interface LearnerRemote<E> extends Remote, PaxosObject {

	/**
	 * Called from acceptor. Only learn if received majority for same value
	 * @param proposal The proposal to learn
	 */
	void learnerLearnRequest(Proposal<E> proposal);
	
}
