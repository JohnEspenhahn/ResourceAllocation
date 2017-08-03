package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;
import java.util.Map;

public class PaxosServerDebuggableImpl extends PaxosServer implements PaxosServerDebuggable {

	// TODO make blocking, but doesn't work when [Client 1 Proxy -> Server Proxy -> Client 2 Local Impl]
	public void acceptorSetLargestProposalNumber(double proposalNumber) {
		System.out.printf("accept:%f:%d\n", proposalNumber, this.getId());
		
		this.largestProposalNumber = proposalNumber;
	}

	@Override
	public void setup(short id, Map<PaxosServerDebuggable,String> servers) throws RemoteException {
		System.out.printf("setup:%d\n", id);

		super.setup(id, servers);

//		this.sendPromiseRequest();
	}

}
