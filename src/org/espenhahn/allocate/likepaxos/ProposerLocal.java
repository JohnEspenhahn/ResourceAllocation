package org.espenhahn.allocate.likepaxos;

import java.util.List;

public interface ProposerLocal<E> extends PaxosObject {
	
	List<AcceptorRemote<E>> getAcceptors();
	
	/**
	 * Get the next, globally unique, proposal number for this proposer
	 * @return The proposal number
	 */
	int getNextProposalNumber();
	
	/**
	 * After a proposal failure, force the next proposal number to be greater than the given one
	 * @param proposalNumber The proposal number that my next proposal number must be greater than
	 */
	void forceProposalNumberAbove(int proposalNumber);

	/**
	 * Send a proposal to a quorum of acceptors
	 */
	void sendPromiseRequest();
	
	/**
	 * If a previous proposal failed, try to repropose with new information given by acceptors
	 * @param proposalNumber The largest previous proposalNumber returned from acceptors
	 */
	void resendPromiseRequest(int proposalNumber);
	
	/**
	 * Called in proposer when enough acceptors accept
	 * @param proposalNumber The proposalNumber being accepted
	 * @param value (Nullable) The value from a previous proposal with the largest proposalNumber less than or equal to this proposalNumber
	 * @return The proposal to send in the accept request
	 */
	Proposal<E> getProposal(int proposalNumber, E value);
	
	/**
	 * Send accept request for give proposal to acceptors
	 * @param proposal The proposal that should be accepted (value/proposalNumber might be different than originally proposed value)
	 */
	void sendAcceptRequest(Proposal<E> proposal);
	
}
