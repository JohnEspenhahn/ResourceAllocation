package org.espenhahn.allocate.likepaxos;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import org.espenhahn.allocate.likepaxos.registry.PaxosRegistry;
import org.espenhahn.allocate.likepaxos.registry.PaxosRegistryImpl;

import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;

public class PaxosServerDebuggableImpl extends PaxosServer implements PaxosServerDebuggable {

	// TODO make blocking, but doesn't work when [Client 1 Proxy -> Server Proxy -> Client 2 Local Impl]
	public void acceptorSetLargestProposalNumber(int proposalNumber) {
		System.out.println("[Learner] Forcing Largest Proposal Number " + proposalNumber);
		
		largestProposalNumber = proposalNumber;
	}

	public void setup(short id, List<PaxosServerDebuggable> servers) throws RemoteException {
		super.setup(id, servers);

		System.out.println("Setup as " + id);

		// Make minority fail on first request
		for (int i = 0; i < this.getCountForMajority(); i++) {
			PaxosServerDebuggable s = servers.get(i);
			s.acceptorSetLargestProposalNumber(0x20000);
		}

		this.sendPromiseRequest();
	}

	public static void main(String[] args) throws RemoteException, NotBoundException {
		String ip = "localhost";

		PaxosServerDebuggableImpl psd = new PaxosServerDebuggableImpl();

		GIPCRegistry gipc_registry = GIPCLocateRegistry.getRegistry(ip, PaxosRegistryImpl.GIPC_PORT, UUID.randomUUID().toString());
		PaxosRegistry reg = (PaxosRegistry) gipc_registry.lookup(PaxosRegistry.class, PaxosRegistryImpl.SERVER_NAME);
		reg.join(psd);

		// Registry rmi_registry = LocateRegistry.getRegistry(ip,
		// PaxosRegistryImpl.GIPC_PORT);
		// PaxosRegistry reg = (PaxosRegistry)
		// rmi_registry.lookup(PaxosRegistryImpl.SERVER_NAME);
		// Remote stub = UnicastRemoteObject.exportObject(psd, 0);
		// reg.join((PaxosServerDebuggable) stub);
	}

}
