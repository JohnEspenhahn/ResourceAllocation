package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;

public abstract class AcceptorImpl<E> extends ProposerImpl<E> implements AcceptorLocal<E>, AcceptorRemote<E> {

	protected double largestProposalNumber;
	private Proposal<E> largestPreviousProposal;
	
	@Override
	public void promiseRequest(ProposerRemote<E> proposer, double proposalNumber) throws RemoteException {
		if (proposalNumber > largestProposalNumber) {
			System.out.printf("accept:%f:[Acceptor]\n", proposalNumber);
			
			largestProposalNumber = proposalNumber;
			proposer.acceptProposal(this, proposalNumber, largestPreviousProposal);
		} else {
			System.out.println("[Acceptor] Ignoring proposal " + proposalNumber + " (needs " + largestProposalNumber + ")");
			
//			proposer.rejectProposal(proposalNumber, largestPreviousProposal);
		}
	}

	@Override
	public void acceptRequest(ProposerRemote<E> proposer, Proposal<E> proposal) throws RemoteException {
		if (proposal.getProposalNumber() >= largestProposalNumber) {
			System.out.println("[Acceptor] Accepting accept request " + proposal.getProposalNumber() + " with value " + proposal.getValue());
			
			largestProposalNumber = proposal.getProposalNumber();
			largestPreviousProposal = proposal;
			
			for (LearnerRemote<E> l: this.getLearners()) {
				l.learnerLearnRequest(proposal);
			}
		} else {
			System.out.println("[Acceptor] Ignoring accept request " + proposal.getProposalNumber() + " with value " + proposal.getValue());
			
//			proposer.rejectAcceptRequest(largestProposalNumber, largestPreviousProposal);
		}
	}

}
