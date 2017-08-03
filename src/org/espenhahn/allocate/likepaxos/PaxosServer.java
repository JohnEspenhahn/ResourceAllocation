package org.espenhahn.allocate.likepaxos;

import inputport.rpc.AnRPCProxyInvocationHandler;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public class PaxosServer extends LearnerImpl<Short> {
	private static final BigDecimal TEN_K = new BigDecimal(10000.0);

	private short myID;
	private Map<?,String> servers;
	private int countForMajority;
	
	private int nextProposalNumber;
	
	/**
	 * Every server needs to be assigned a globally unique, final id at creation time
	 * @param id Globally unique, final id
	 * @param servers All the associated proxies
	 */
	public void setup(short id, Map<PaxosServerDebuggable,String> servers) throws RemoteException {
		this.myID = id;
		this.servers = servers;
		this.countForMajority = servers.size()/2 + 1;
	}

	protected short getId() {
	    return myID;
    }
	
	@Override
	public int getCountForMajority() {
		return countForMajority;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<LearnerRemote<Short>> getLearners() {
		return (Set<LearnerRemote<Short>>) servers.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<AcceptorRemote<Short>> getAcceptors() {
		return (Set<AcceptorRemote<Short>>) servers.keySet();
	}
	
	@Override
	public String getAcceptorName(AcceptorRemote<Short> acceptor) {
		return ((AnRPCProxyInvocationHandler) Proxy.getInvocationHandler(acceptor)).getProxyDestination();
	}
	
	@Override
	public String getProposerName(ProposerRemote<Short> proposer) {
		return ((AnRPCProxyInvocationHandler) Proxy.getInvocationHandler(proposer)).getProxyDestination();
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
		sendPromiseRequest();
	}
	
	@Override
	public Proposal<Short> getProposal(double proposalNumber, Short value) {
		if (value == null) value = myID;  // If no value given, can be any (aka the value I want)
		return new ProposalImpl<>(proposalNumber, value);
	}
	
	
	
}
