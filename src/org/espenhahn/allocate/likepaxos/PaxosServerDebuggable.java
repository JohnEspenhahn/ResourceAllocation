package org.espenhahn.allocate.likepaxos;

import java.rmi.RemoteException;
import java.util.List;

public interface PaxosServerDebuggable extends LearnerRemote<Short>, AcceptorRemote<Short>, ProposerRemote<Short> {

	boolean acceptorSetLargestProposalNumber(int proposalNumber) throws RemoteException;
	
	void setup(short id, List<PaxosServerDebuggable> servers) throws RemoteException;
	
}
