package org.espenhahn.allocate.likepaxos;

import java.util.LinkedList;
import java.util.List;

import org.espenhahn.allocate.likepaxos.registry.Registry;

public abstract class ProposerImpl<E> implements ProposerLocal<E>, ProposerRemote<E> {

	private boolean waiting = false;
	
	private int waitingForProposalNumber = -1;
	
	/** Acceptors that have accepted the last proposal */
	private List<AcceptorRemote<E>> accepted;
	
	/** The previous proposal with the largest proposalNumber */
	private Proposal<E> maxPrevProposal;
	
	// Config
	private final boolean repropose;
	private AcceptListener<E> acceptListener;
	private LearnListener<E> learnListener;
	
	public ProposerImpl(boolean repropose) {
		this.repropose = repropose;
		
		this.accepted = new LinkedList<AcceptorRemote<E>>();
	}
	
	public void setListeners(AcceptListener<E> acceptListener, LearnListener<E> learnListener) {
		this.acceptListener = acceptListener;
		this.learnListener = learnListener;
	}

	@Override
	public int getCountForMajority() {
		return Registry.NUM_MAJORITY;
	}

	@Override
	public void acceptProposal(AcceptorRemote<E> acceptor, int proposalNumber, Proposal<E> prevProposal) {
		if (!waiting || proposalNumber < waitingForProposalNumber) return; // Ignore if abandoned proposal
		
		accepted.add(acceptor);
		
		// Keep track of max previous proposal value
		if (prevProposal != null && (this.maxPrevProposal == null || prevProposal.getProposalNumber() > this.maxPrevProposal.getProposalNumber()))
			this.maxPrevProposal = prevProposal;
		
		// Notify
		if (accepted.size() > this.getCountForMajority()) {
			E value = (maxPrevProposal != null ? maxPrevProposal.getValue() : null);
			
			waiting = false;
			acceptListener.onAccept(proposalNumber, value);
		}
	}

	@Override
	public void rejectProposal(int proposalNumber, Proposal<E> outstandingProposal) {
		if (!waiting || proposalNumber < waitingForProposalNumber) return; // Ignore if abandoned proposal
		
		// Keep track of max previous proposal value
		if (outstandingProposal != null && (this.maxPrevProposal == null || outstandingProposal.getProposalNumber() > this.maxPrevProposal.getProposalNumber()))
			this.maxPrevProposal = outstandingProposal;
		
		// TODO rejectProposal
		if (repropose) {
			// TODO resend promise, but with updated proposal number from outstandingProposal
		}
	}

	@Override
	public void proposerLearn(Proposal<E> proposal) {
		this.learnListener.onLearned(proposal.getValue());
	}

	@Override
	public int getNextProposalNumber() {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public void sendPromiseRequest() {
		resendPromiseRequest(getNextProposalNumber());
	}

	@Override
	public void resendPromiseRequest(int proposalNumber) {
		if (proposalNumber <= waitingForProposalNumber) return; // Ignore abandoned or already sent
		
		this.waiting = true;
		this.accepted.clear();
		this.maxPrevProposal = null;
		this.waitingForProposalNumber = proposalNumber;
		
		for (AcceptorRemote<E> a: this.getAcceptors()) {
			a.promiseRequest(this, proposalNumber);
		}
	}

	@Override
	public void sendAcceptRequest(Proposal<E> proposal) {
		if (proposal.getProposalNumber() < waitingForProposalNumber) return; // Ignore abandoned
		
		for (AcceptorRemote<E> a: this.getAcceptors()) {
			a.acceptRequest(this, proposal);
		}
	}

}
