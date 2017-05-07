package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;
import java.util.List;

public class PaxosServer extends LearnerImpl<Short> implements Remote, AcceptListener<Short>, LearnListener<Short> {

	private short myID;
	private List<?> servers;
	
	private short nextProposalNumber;
	
	public PaxosServer() {
		super(false);
		
		setListeners(this, this);
	}
	
	/**
	 * Every server needs to be assigned a globally unique, final id
	 * @param id
	 * @param servers
	 */
	public void setup(short id, List<PaxosServer> servers) {
		this.myID = id;
		this.servers = servers;
		
		// Propose self as leader
		this.sendPromiseRequest();
	}
	
	@Override
	public int getNextProposalNumber() {
		return myID | ((nextProposalNumber++) << 16);
	}
	
	@Override
	public void forceProposalNumberAbove(int proposalNumber) {
		int prevProposalNumber = (proposalNumber >> 16);
		nextProposalNumber = (short) (prevProposalNumber+1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LearnerRemote<Short>> getLearners() {
		return (List<LearnerRemote<Short>>) servers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AcceptorRemote<Short>> getAcceptors() {
		return (List<AcceptorRemote<Short>>) servers;
	}
	
	@Override
	public Proposal<Short> getProposal(int proposalNumber, Short value) {
		if (value == null) value = myID;  // If no value given, can be any (aka the value I want)
		return new ProposalImpl<Short>(proposalNumber, value);
	}
	
	@Override
	public void onAccept(int proposalNumber) {
		System.out.println("onAccept with proposal number " + proposalNumber);
	}

	@Override
	public void onLearned(Short value) { 
		System.out.println("onLearned value " + value);
	}
	
	
}
