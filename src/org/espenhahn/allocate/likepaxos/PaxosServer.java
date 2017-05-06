package org.espenhahn.allocate.likepaxos;

import java.rmi.Remote;
import java.util.List;

public class PaxosServer extends LearnerImpl<String> implements Remote, AcceptListener<String> {

	private final int id = (int) (Math.random()*Integer.MAX_VALUE);
	private Object servers;
	
	public PaxosServer() {
		super(true);
		
		setListeners(this, this);
	}
	
	public void setServers(List<PaxosServer> servers) {
		this.servers = servers;
		
		// Propose self as leader
		this.resendPromiseRequest(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LearnerRemote<String>> getLearners() {
		return (List<LearnerRemote<String>>) servers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AcceptorRemote<String>> getAcceptors() {
		return (List<AcceptorRemote<String>>) servers;
	}

	@Override
	public void onAccept(int proposalNumber, String value) {
		this.sendAcceptRequest(new ProposalImpl<String>(proposalNumber, value));
	}
	
}
