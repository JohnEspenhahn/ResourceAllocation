package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;
import java.util.Map;

public class PaxosServerDebuggableImpl extends PaxosServer implements PaxosServerDebuggable {

	// TODO make blocking, but doesn't work when [Client 1 Proxy -> Server Proxy -> Client 2 Local Impl]
	public void acceptorSetLargestProposalNumber(double proposalNumber) {
		System.out.printf("accept:%f:[Acceptor] Forcing Largest Proposal Number\n", proposalNumber);
		
		largestProposalNumber = proposalNumber;
	}

	@Override
	public void setup(short id, Map<PaxosServerDebuggable,String> servers) throws RemoteException {
		super.setup(id, servers);

		System.out.println("Setup as " + id);

//		this.sendPromiseRequest();
	}

}
