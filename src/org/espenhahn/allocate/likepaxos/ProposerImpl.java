package org.espenhahn.allocate.likepaxos;

import java.util.LinkedList;
import java.util.List;

import org.espenhahn.allocate.likepaxos.registry.Registry;

public abstract class ProposerImpl<E> implements ProposerLocal<E>, ProposerRemote<E> {

	private boolean waiting = false;
	
	private int waitingForProposalNumber = -1;
	
	/** Acceptors that have accepted the last proposal */
	private List<AcceptorRemote<E>> accepted;
	private int rejectCount;
	
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
	public void sendPromiseRequest() {
		resendPromiseRequest(getNextProposalNumber());
	}

	@Override
	public synchronized void resendPromiseRequest(int proposalNumber) {
		if (proposalNumber <= waitingForProposalNumber) return; // Ignore abandoned or already sent
		
//		this.waiting = true;
		this.rejectCount = 0;
		this.accepted.clear();
//		this.maxPrevProposal = null;
		this.waitingForProposalNumber = proposalNumber;
		
		for (AcceptorRemote<E> a: getAcceptors()) {
			a.promiseRequest(this, proposalNumber);
		}
	}

	@Override
	public void acceptProposal(AcceptorRemote<E> acceptor, int proposalNumber, Proposal<E> prevProposal) {
		boolean didAccept = false;
		
		synchronized (this) {
			if (proposalNumber < waitingForProposalNumber) return; // Ignore if abandoned proposal
			
			accepted.add(acceptor);
			
			// Keep track of max previous proposal value
			if (prevProposal != null && (this.maxPrevProposal == null || prevProposal.getProposalNumber() > this.maxPrevProposal.getProposalNumber()))
				this.maxPrevProposal = prevProposal;
			
			// Notify
			if (accepted.size() > this.getCountForMajority()) {
				didAccept = true;
				E value = (maxPrevProposal != null ? maxPrevProposal.getValue() : null);
				
//				waiting = false;
				sendAcceptRequest(getProposal(proposalNumber, value));
			}
		}
		
		// Don't want this part to be synchronized, call to outside object
		if (didAccept && acceptListener != null) acceptListener.onAccept(proposalNumber);
	}

	@Override
	public synchronized void rejectProposal(int proposalNumber, Proposal<E> outstandingProposal) {
		if (proposalNumber < waitingForProposalNumber) return; // Ignore if abandoned proposal
		
		// Keep track of max previous proposal value
		if (outstandingProposal != null && (maxPrevProposal == null || outstandingProposal.getProposalNumber() > maxPrevProposal.getProposalNumber()))
			this.maxPrevProposal = outstandingProposal;
		
		this.rejectCount += 1;
		if (repropose && rejectCount >= this.getCountForMajority()) {
			// Re-propose with larger proposal number
			if (maxPrevProposal != null) forceProposalNumberAbove(maxPrevProposal.getProposalNumber());
			sendPromiseRequest();
		}
	}

	@Override
	public synchronized void sendAcceptRequest(Proposal<E> proposal) {
		if (proposal.getProposalNumber() < waitingForProposalNumber) return; // Ignore abandoned
		
		this.rejectCount = 0; // reuse for accept request rejection
		for (AcceptorRemote<E> a: accepted) {
			a.acceptRequest(this, proposal);
		}
	}
	
	@Override
	public synchronized void rejectAcceptRequest(int proposalNumber, Proposal<E> outstandingProposal) {
		rejectProposal(proposalNumber, outstandingProposal); // TODO
		
		// TODO proposer needs to know from learners at some point if its value was learened
	}
	
//	@Override
//	public void proposerLearn(Proposal<E> proposal) {
//		// don't want this part to be synchronized, call to outside object
//		if (learnListener != null) learnListener.onLearned(proposal.getValue());
//	}

}
