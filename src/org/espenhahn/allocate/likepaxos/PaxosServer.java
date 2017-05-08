package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public class PaxosServer extends LearnerImpl<Short> {

	private short myID;
	private List<?> servers;
	
	private short nextProposalNumber;
	
	public PaxosServer() {
		super();
		
		this.shouldRepropose = false;
	}
	
	/**
	 * Every server needs to be assigned a globally unique, final id at creation time
	 * @param id
	 * @param servers
	 */
	public void setup(short id, List<PaxosServerDebuggable> servers) throws RemoteException {
		this.myID = id;
		this.servers = servers;
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
	public int getNextProposalNumber() {
		int pn = myID | ((++nextProposalNumber) << 16);
		
		// This is a cheap way to create proposal numbers, but won't work well in long-run
		if (pn <= 0) throw new RuntimeException("Ran out of proposal numbers!");
		
		return pn;
	}
	
	@Override
	public void resendPromiseRequest(int proposalNumber) {
		// Force proposal number above
		nextProposalNumber = (short) Math.max(nextProposalNumber, proposalNumber >> 16);
		
		// send the promise request
		sendPromiseRequest();
	}
	
	@Override
	public Proposal<Short> getProposal(int proposalNumber, Short value) {
		if (value == null) value = myID;  // If no value given, can be any (aka the value I want)
		return new ProposalImpl<Short>(proposalNumber, value);
	}
	
	
	
}
