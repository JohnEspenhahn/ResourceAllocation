package org.espenhahn.allocate.likepaxos.registry;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.espenhahn.allocate.likepaxos.PaxosServerDebuggable;

import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;

public class PaxosRegistryImpl implements PaxosRegistry {
	private static final long serialVersionUID = 5955611028919581213L;
	
	public static final int GIPC_PORT = 9090;
	public static final String SERVER_NAME = "registry";
	
	public static final int NUM_PROPOSERS = 3;
	public static final int NUM_MAJORITY = (int) Math.ceil(NUM_PROPOSERS/2.0);
	
	private List<PaxosServerDebuggable> servers = new ArrayList<PaxosServerDebuggable>();
	
	public static void main(String[] args) throws RemoteException {
		PaxosRegistryImpl server = new PaxosRegistryImpl();
		
//		SerializerSelector.setSerializerFactory(new MySerializerFactory());
		GIPCRegistry reg = GIPCLocateRegistry.createRegistry(GIPC_PORT);
		reg.rebind(SERVER_NAME, server);
		
//		Registry reg = LocateRegistry.createRegistry(GIPC_PORT);
//		reg.rebind(SERVER_NAME, server);
		
		System.out.println("Registries started");
		
		// Control server settings from terminal
		Scanner s = new Scanner(System.in);
		while (s.hasNext()) {
			String line = s.nextLine();
			if (line == "") break;
		}
		s.close();
	}
	
	public PaxosRegistryImpl() throws RemoteException {
		super();
	}
	
	public void join(PaxosServerDebuggable j) {
		if (servers.size() < NUM_PROPOSERS) {
			System.out.println("Server joined");
			servers.add(j);
			if (servers.size() == NUM_PROPOSERS) {
				System.out.println("Sending setup");
				short id = 1;
				for (PaxosServerDebuggable s: servers) {
					try {
						s.setup(id++, servers);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
