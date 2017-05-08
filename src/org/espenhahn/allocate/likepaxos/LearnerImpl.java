package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;

public abstract class LearnerImpl<E> extends AcceptorImpl<E> implements LearnerLocal<E>, LearnerRemote<E> {

	private int currentProposalNumber;
	private int countForCurrentProposal;
	
	@Override
	public synchronized void learnerLearnRequest(Proposal<E> proposal) {
		if (proposal.getProposalNumber() > currentProposalNumber) {
			currentProposalNumber = proposal.getProposalNumber();
			countForCurrentProposal = 1;
		} else if (proposal.getProposalNumber() == currentProposalNumber) {
			countForCurrentProposal += 1;
			
			if (countForCurrentProposal >= this.getCountForMajority()) {
				onLearnerLearned(proposal.getValue());
				
				// Safe to reset because got majority
				countForCurrentProposal = 0;
			}
		}
	}
	
	@Override
	public void onLearnerLearned(E value) {
		System.out.println("Learner learned " + value);
	}

}
