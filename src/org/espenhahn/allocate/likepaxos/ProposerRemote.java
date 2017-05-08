package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;

public interface ProposerRemote<E> {

	/**
	 * Called by a acceptor to accept the proposal with the given proposal number. If a previous proposal with a smaller
	 * proposal number was already accepted, this call also includes the previously accepted value
	 * @param acceptor The acceptor that is accepting
	 * @param proposalNumber The proposal number being accepted (must be greater than the proposal number of prevProposal)
	 * @param prevProposal (Nullable) The proposal previously accepted for a smaller proposal number
	 */
	void acceptProposal(AcceptorRemote<E> acceptor, double proposalNumber, Proposal<E> prevProposal) throws RemoteException;
	
	/**
	 * Called by acceptor to inform the proposer that its accept request was infact accepted (aka learned)
	 * @param proposal The proposal that was acccepted
	 */
//	void proposerLearn(Proposal<E> proposal);
	
}
