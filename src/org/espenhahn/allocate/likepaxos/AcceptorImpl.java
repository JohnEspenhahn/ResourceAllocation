package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;

public abstract class AcceptorImpl<E> extends ProposerImpl<E> implements AcceptorLocal<E>, AcceptorRemote<E> {

	/** For debugging purposes only */
	protected double largestRejectedProposalNumber;
	
	protected double largestProposalNumber;
	private Proposal<E> largestPreviousProposal;
	
	@Override
	public void promiseRequest(ProposerRemote<E> proposer, double proposalNumber) throws RemoteException {
		if (proposalNumber > largestProposalNumber) {
			System.out.printf("accept:%f:%s\n", proposalNumber, getProposerName(proposer));
			
			largestProposalNumber = proposalNumber;
			proposer.acceptProposal(this, proposalNumber, largestPreviousProposal);
		} else {
			if (proposalNumber > largestRejectedProposalNumber) {
				// Debugger only renders one incoming proposal number, so only output reject debug message for
				// the last largest rejected proposal
				largestRejectedProposalNumber = proposalNumber;
				System.out.printf("reject:%f:%s\n", proposalNumber, "needs " + largestProposalNumber + "");
			}
			
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
