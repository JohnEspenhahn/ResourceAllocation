package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;

public abstract class AcceptorImpl<E> extends ProposerImpl<E> implements AcceptorLocal<E>, AcceptorRemote<E> {

	protected int largestProposalNumber;
	private Proposal<E> largestPreviousProposal;
	
	@Override
	public void promiseRequest(ProposerRemote<E> proposer, int proposalNumber) throws RemoteException {
		if (proposalNumber > largestProposalNumber) {
			largestProposalNumber = proposalNumber;
			proposer.acceptProposal(this, proposalNumber, largestPreviousProposal);
		} else {
			proposer.rejectProposal(proposalNumber, largestPreviousProposal);
		}
	}

	@Override
	public void acceptRequest(ProposerRemote<E> proposer, Proposal<E> proposal) throws RemoteException {
		if (proposal.getProposalNumber() < largestProposalNumber) {
			proposer.rejectAcceptRequest(largestProposalNumber, largestPreviousProposal);
			return;
		}
		
		largestProposalNumber = proposal.getProposalNumber();
		largestPreviousProposal = proposal;
		
		for (LearnerRemote<E> l: this.getLearners()) {
			l.learnerLearnRequest(proposal);
		}
	}

}
