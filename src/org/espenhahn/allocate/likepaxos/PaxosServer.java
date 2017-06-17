package org.espenhahn.allocate.likepaxos;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

public class PaxosServer extends LearnerImpl<Short> {
	private static final BigDecimal TEN_K = new BigDecimal(10000.0);

	private short myID;
	private List<?> servers;
	private int countForMajority;
	
	private int nextProposalNumber;
	
	/**
	 * Every server needs to be assigned a globally unique, final id at creation time
	 * @param id
	 * @param servers
	 */
	public void setup(short id, List<PaxosServerDebuggable> servers) throws RemoteException {
		this.myID = id;
		this.servers = servers;
		this.countForMajority = servers.size()/2 + 1;
	}
	
	@Override
	public int getCountForMajority() {
		return countForMajority;
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
		// Get precise double
		double pn = new BigDecimal(nextProposalNumber).multiply(TEN_K).add(BigDecimal.ONE).divide(TEN_K, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
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
