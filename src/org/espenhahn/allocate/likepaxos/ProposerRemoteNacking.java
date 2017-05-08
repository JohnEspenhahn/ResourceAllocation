package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;

public interface ProposerRemoteNacking<E> extends ProposerRemote<E> {

	/**
	 * Called by a acceptor to reject the proposal with the given proposal number. Also returns the outstanding proposal
	 * which a largest proposal number that caused this proposal to be rejected
	 * @param proposalNumber The proposal number that is being rejected (must be less than the proposal number of outstandingProposal)
	 * @param outstandingProposal (Nullable) The proposal previously accepted for a larger proposal number
	 */
	void rejectProposal(double proposalNumber, Proposal<E> outstandingProposal) throws RemoteException;
	
	/**
	 * Called by acceptor if the acceptor has gotten a better proposal since it accepted the given proposal
	 * @param betterProposalNumber The better proposal number
	 * @param outstandingProposal (Nullable) The proposal previously accepted for a larger proposal number
	 */
	void rejectAcceptRequest(double betterProposalNumber, Proposal<E> outstandingProposal) throws RemoteException;
	
}
