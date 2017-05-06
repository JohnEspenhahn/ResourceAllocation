package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;

public interface AcceptorRemote<E> extends Remote {
	
	// Can safely ignore any requests. For efficiency should return a NAK on propose failure (but not required)
	
	/**
	 * Called from Proposer. If proposalNumber is greater than an proposalNumber is has seen before, respond with accept. Else reject
	 * @param proposer The proposer that is proposing
	 * @param proposalNumber The proposalNumber
	 */
	void promiseRequest(ProposerRemote<E> proposer, int proposalNumber);
	
	/**
	 * Called from Proposer. If proposalNumber is less than the largest proposalNumber accepted in propose, ignore it. Otherwise
	 * send learn to proposer and all learners
	 * @param proposer The proposer that is proposing
	 * @param proposal The proposal to try to be accepted
	 */
	void acceptRequest(ProposerRemote<E> proposer, Proposal<E> proposal);

}
