package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public class PaxosServer extends LearnerImpl<Short> {

	private short myID;
	private List<?> servers;
	
	private int nextProposalNumber;
	
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
	public double getNextProposalNumber() {
		double pn = nextProposalNumber + (myID / 1000.0);
		nextProposalNumber += 1;

		if (pn <= 0) throw new RuntimeException("Ran out of proposal numbers!");
		
		return pn;
	}
	
	@Override
	public void resendPromiseRequest(double proposalNumber) {
		// Force proposal number above
		nextProposalNumber = (int) Math.max(nextProposalNumber, Math.floor(proposalNumber));
		
		// send the promise request
		sendPromiseRequest();
	}
	
	@Override
	public Proposal<Short> getProposal(double proposalNumber, Short value) {
		if (value == null) value = myID;  // If no value given, can be any (aka the value I want)
		return new ProposalImpl<Short>(proposalNumber, value);
	}
	
	
	
}
