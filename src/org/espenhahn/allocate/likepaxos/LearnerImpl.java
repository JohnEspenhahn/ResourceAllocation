package org.espenhahn.allocate.likepaxos;

public abstract class LearnerImpl<E> extends AcceptorImpl<E> implements LearnerLocal<E>, LearnerRemote<E> {

	private Proposal<E> currentProposal;
	private int countForCurrentProposal;

	@Override
	public synchronized void learnerLearnRequest(Proposal<E> proposal) {
		if (currentProposal == null || proposal.getProposalNumber() >= currentProposal.getProposalNumber()) {
			if (currentProposal != null && proposal.getValue().equals(currentProposal.getValue())) {
				countForCurrentProposal += 1;
			} else {
				countForCurrentProposal = 1;
			}

			currentProposal = proposal;
			System.out.println("[Learner] Accepting learn " + proposal.getProposalNumber() + " with value "
					+ proposal.getValue() + " (" + countForCurrentProposal + "/" + this.getCountForMajority() + ")");
			
			if (countForCurrentProposal >= this.getCountForMajority()) {
				onLearnerLearned(proposal.getValue());

				// Safe to reset because got majority
				currentProposal = null;
			}
		} else {
			System.out.println("[Learner] Ignoring learn " + proposal.getProposalNumber());
		}
	}

	@Override
	public void onLearnerLearned(E value) {
		System.out.println("[Learner] Learner learned " + value);
	}

}
