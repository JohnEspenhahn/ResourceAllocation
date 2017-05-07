package org.espenhahn.allocate.likepaxos;

public abstract class AcceptorImpl<E> extends ProposerImpl<E> implements AcceptorLocal<E>, AcceptorRemote<E> {

	private int largestProposalNumber;
	private Proposal<E> largestPreviousProposal;
	
	public AcceptorImpl(boolean repropose) {
		super(repropose);
	}

	@Override
	public void promiseRequest(ProposerRemote<E> proposer, int proposalNumber) {
		if (proposalNumber > largestProposalNumber) {
			largestProposalNumber = proposalNumber;
			proposer.acceptProposal(this, proposalNumber, largestPreviousProposal);
		} else {
			proposer.rejectProposal(proposalNumber, largestPreviousProposal);
		}
	}

	@Override
	public void acceptRequest(ProposerRemote<E> proposer, Proposal<E> proposal) {
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
