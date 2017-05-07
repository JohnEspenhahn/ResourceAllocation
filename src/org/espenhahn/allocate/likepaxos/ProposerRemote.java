package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;

public interface ProposerRemote<E> extends Remote {

	/**
	 * Called by a acceptor to accept the proposal with the given proposal number. If a previous proposal with a smaller
	 * proposal number was already accepted, this call also includes the previously accepted value
	 * @param acceptor The acceptor that is accepting
	 * @param proposalNumber The proposal number being accepted (must be greater than the proposal number of prevProposal)
	 * @param prevProposal (Nullable) The proposal previously accepted for a smaller proposal number
	 */
	void acceptProposal(AcceptorRemote<E> acceptor, int proposalNumber, Proposal<E> prevProposal);
	
	/**
	 * Called by a acceptor to reject the proposal with the given proposal number. Also returns the outstanding proposal
	 * which a largest proposal number that caused this proposal to be rejected
	 * @param proposalNumber The proposal number that is being rejected (must be less than the proposal number of outstandingProposal)
	 * @param outstandingProposal (Nullable) The proposal previously accepted for a larger proposal number
	 */
	void rejectProposal(int proposalNumber, Proposal<E> outstandingProposal);
	
	/**
	 * Called by acceptor if the acceptor has gotten a better proposal since it accepted the given proposal
	 * @param proposalNumber The better proposal number
	 * @param outstandingProposal (Nullable) The proposal previously accepted for a larger proposal number
	 */
	void rejectAcceptRequest(int proposalNumber, Proposal<E> outstandingProposal);
	
	/**
	 * Called by acceptor to inform the proposer that its accept request was infact accepted (aka learned)
	 * @param proposal The proposal that was acccepted
	 */
//	void proposerLearn(Proposal<E> proposal);
	
}
