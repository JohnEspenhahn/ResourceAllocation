package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;
import java.util.List;

public class PaxosServerDebuggableImpl extends PaxosServer implements PaxosServerDebuggable {

	// TODO make blocking, but doesn't work when [Client 1 Proxy -> Server Proxy -> Client 2 Local Impl]
	public void acceptorSetLargestProposalNumber(double proposalNumber) {
		System.out.printf("accept:%f:[Acceptor] Forcing Largest Proposal Number\n", proposalNumber);
		
		largestProposalNumber = proposalNumber;
	}

	public void setup(short id, List<PaxosServerDebuggable> servers) throws RemoteException {
		super.setup(id, servers);

		System.out.println("Setup as " + id);

//		this.sendPromiseRequest();
	}

}
