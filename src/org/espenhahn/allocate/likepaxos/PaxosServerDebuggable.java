package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;
import java.util.Map;

public interface PaxosServerDebuggable extends LearnerRemote<Short>, AcceptorRemote<Short>, ProposerRemote<Short> {

	void acceptorSetLargestProposalNumber(double proposalNumber) throws RemoteException;
	
	void setup(short id, Map<PaxosServerDebuggable,String> servers) throws RemoteException;
	
}
