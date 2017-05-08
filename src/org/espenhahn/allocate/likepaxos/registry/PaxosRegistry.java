package org.espenhahn.allocate.likepaxos.registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.espenhahn.allocate.likepaxos.PaxosServerDebuggable;

public interface PaxosRegistry {
	void join(PaxosServerDebuggable j) throws RemoteException;
}
