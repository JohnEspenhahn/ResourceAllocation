package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;

public interface LearnerRemote<E> {

	/**
	 * Called from acceptor. Only learn if received majority for same value
	 * @param proposal The proposal to learn
	 */
	void learnerLearnRequest(Proposal<E> proposal) throws RemoteException;
	
}
