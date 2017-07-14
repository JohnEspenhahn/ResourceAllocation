package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class ProposerImpl<E> implements ProposerLocal<E>, ProposerRemote<E> {
	
	// Timer
	private static final int TIMEOUT_TIME = 2;
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final Runnable reproposeEvent = () -> repropose();
	
	/** Timeout for waiting for promise responses from acceptors */
	private ScheduledFuture<?> promiseTimer;
	
	/** The minimum proposal number we are waiting to accept */
	private double waitingForProposalNumber = -1;
	
	/** Acceptors that have accepted the last proposal */
	private List<AcceptorRemote<E>> accepted;
	private int rejectCount;
	
	/** The previous proposal with the largest proposalNumber */
	private Proposal<E> maxPrevProposal;
	
	// Config
	private AcceptListener<E> acceptListener;
	private LearnListener<E> learnListener;
	
	public ProposerImpl() {
		this.accepted = new LinkedList<AcceptorRemote<E>>();
	}
	
	public void setListeners(AcceptListener<E> acceptListener, LearnListener<E> learnListener) {
		this.acceptListener = acceptListener;
		this.learnListener = learnListener;
	}

	@Override
	public synchronized void sendPromiseRequest() {
		cancelPromiseTimer();
		
		this.rejectCount = 0;
		this.accepted.clear();
//		this.maxPrevProposal = null;
//		this.waitingForPromise = true;
		this.waitingForProposalNumber = getNextProposalNumber();
		
		this.promiseTimer = executor.schedule(reproposeEvent, TIMEOUT_TIME, TimeUnit.SECONDS);
		
		for (AcceptorRemote<E> a: getAcceptors()) {
			try {
				System.out.printf("propose:%f:%f:[Proposer] Sending proposal\n", waitingForProposalNumber, getAcceptorName(a));
				a.promiseRequest(this, waitingForProposalNumber);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void cancelPromiseTimer() {
		if (promiseTimer != null)
			promiseTimer.cancel(false);
	}
	
	private void repropose() {		
		if (maxPrevProposal != null) 
			resendPromiseRequest(maxPrevProposal.getProposalNumber());
		else 
			sendPromiseRequest();
	}

	@Override
	public void acceptProposal(AcceptorRemote<E> acceptor, double proposalNumber, Proposal<E> prevProposal) {
		if (proposalNumber < waitingForProposalNumber) return; // Ignore if abandoned proposal		
		accepted.add(acceptor);
		
		System.out.println("[Proposer] Proposal accepted " + proposalNumber + " (" + accepted.size() + "/" + getCountForMajority() + ")");
		
		// Keep track of max previous proposal value
		if (prevProposal != null && (maxPrevProposal == null || prevProposal.getProposalNumber() > maxPrevProposal.getProposalNumber()))
			this.maxPrevProposal = prevProposal;
		
		// Notify
		if (accepted.size() >= getCountForMajority()) {
			E value = (maxPrevProposal != null ? maxPrevProposal.getValue() : null);
			
			System.out.println("[Proposer] Proposal accepted quorum for " + proposalNumber + " with previous value " + value);
			
			waitingForProposalNumber += 1; // stop waiting for this proposal, only listen for any subsequent ones
			sendAcceptRequest(getProposal(proposalNumber, value));
			
			// Notify listener
			if (acceptListener != null) acceptListener.onAccept(proposalNumber);
		}
	}

//	@Override
//	public synchronized void rejectProposal(double proposalNumber, Proposal<E> outstandingProposal) {
//		if (proposalNumber < waitingForProposalNumber) return; // Ignore if abandoned proposal
//		System.out.println("[Proposer] Proposal rejected " + proposalNumber);
//		
//		// Keep track of max previous proposal value
//		if (outstandingProposal != null && (maxPrevProposal == null || outstandingProposal.getProposalNumber() > maxPrevProposal.getProposalNumber()))
//			this.maxPrevProposal = outstandingProposal;
//		
//		this.rejectCount += 1;
//		if (shouldRepropose && rejectCount >= getCountForMajority()) {			
//			repropose();
//		}
//	}

	@Override
	public synchronized void sendAcceptRequest(Proposal<E> proposal) {
		cancelPromiseTimer();
		
		System.out.println("[Proposer] Sending accept request " + proposal.getProposalNumber() + " with value " + proposal.getValue());
		
		this.rejectCount = 0; // reuse for accept request rejection
		for (AcceptorRemote<E> a: accepted) {
			try {
				a.acceptRequest(this, proposal);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
//	@Override
//	public synchronized void rejectAcceptRequest(double betterProposalNumber, Proposal<E> outstandingProposal) {		
//		// TODO proposer needs to know from learners at some point if its value was learened
//	}

}
