package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;

public abstract class LearnerImpl<E> extends AcceptorImpl<E> implements LearnerLocal<E>, LearnerRemote<E> {

	private double currentProposalNumber;
	private int countForCurrentProposal;
	
	@Override
	public synchronized void learnerLearnRequest(Proposal<E> proposal) {
		if (proposal.getProposalNumber() > currentProposalNumber) {
			System.out.println("[Learner] Accepting new learn " + proposal.getProposalNumber() + " (1/" + this.getCountForMajority() + ")");
			
			currentProposalNumber = proposal.getProposalNumber();
			countForCurrentProposal = 1;
		} else if (proposal.getProposalNumber() == currentProposalNumber) {
			countForCurrentProposal += 1;
			
			System.out.println("[Learner] Accepting learn " + proposal.getProposalNumber() + " (" + countForCurrentProposal + "/" + this.getCountForMajority() + ")");
			if (countForCurrentProposal >= this.getCountForMajority()) {
				onLearnerLearned(proposal.getValue());
				
				// Safe to reset because got majority
				countForCurrentProposal = 0;
			}
		} else {
			System.err.println("[Learner] Ignoring learn " + proposal.getProposalNumber());
		}
	}
	
	@Override
	public void onLearnerLearned(E value) {
		System.out.println("[Learner] Learner learned " + value);
	}

}
