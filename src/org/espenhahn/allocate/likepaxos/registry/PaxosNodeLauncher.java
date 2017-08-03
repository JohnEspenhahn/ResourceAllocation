package org.espenhahn.allocate.likepaxos.registry;

import inputport.rpc.DirectedRPCProxyGenerator;
import inputport.rpc.duplex.AnAbstractDuplexRPCClientPortLauncher;
import org.espenhahn.allocate.likepaxos.PaxosServerDebuggable;
import org.espenhahn.allocate.likepaxos.PaxosServerDebuggableImpl;
import port.PortAccessKind;
import port.PortKind;
import port.SessionChoice;
import sessionport.rpc.duplex.DuplexRPCSessionPort;
import util.trace.Tracer;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PaxosNodeLauncher extends AnAbstractDuplexRPCClientPortLauncher {

	private short clientId;
    private PaxosServerDebuggableImpl localPaxosInstance;
	
	/** Map<Server,Port> */
    private Map<PaxosServerDebuggable,String> allPaxosNodes;

	private PaxosNodeLauncher(String clientName, short clientId, String serverHost, String serverId, String serverName) {
		super(clientName, serverHost, serverId, serverName);
		DirectedRPCProxyGenerator.setDoShortCircuitLocalCallsToRemotes(false);

		this.clientId = clientId;
		this.allPaxosNodes = new HashMap<>();
		
		System.out.printf("register:%d\n", clientId);
	}

	@Override
	protected String getClientId() {
		return "" + clientId;
	}

	@Override
	protected void registerRemoteObjects() {
		localPaxosInstance = new PaxosServerDebuggableImpl();		
		register(localPaxosInstance);
	}

	@Override
	public PortKind getPortKind() {
		return PortKind.SESSION_PORT;
	}

	@Override
	public PortAccessKind getPortAccessKind() {
		return PortAccessKind.DUPLEX;
	}

	@Override
	protected SessionChoice getSessionChoice() {
		return SessionChoice.P2P;
	}

	private PaxosServerDebuggable connectDirectTo(String name) {
		PaxosServerDebuggable proxy = (PaxosServerDebuggable) DirectedRPCProxyGenerator
				.generateRPCProxy((DuplexRPCSessionPort) mainPort, name, PaxosServerDebuggable.class, null);
		
		this.register(name, proxy);
		allPaxosNodes.put(proxy, name);

		System.out.printf("connect:%s\n", name);

		return proxy;
	}

	private void start() {
		try {
			localPaxosInstance.setup(clientId, allPaxosNodes);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws RemoteException {
		if (args.length == 0)
			throw new RuntimeException("Requires node port!");

		Tracer.showWarnings(false);
		Tracer.showInfo(false);

		short port;
		try {
			port = Short.parseShort(args[0]);
		} catch (NumberFormatException | NullPointerException e) {
			throw new RuntimeException("Invalid port number: " + args[0]);
		}

		String address = "localhost";
		if (args.length > 1)
			address = args[1];

		PaxosNodeLauncher launcher = new PaxosNodeLauncher(
				"" + port, port, address, PaxosSessionServerLauncher.SERVER_ID,
				PaxosSessionServerLauncher.SERVER_NAME
		);
		launcher.launch();

		Scanner s = new Scanner(System.in);
		while (s.hasNext()) {
			String str = s.nextLine().trim();
			if (str.equalsIgnoreCase("start")) {
				launcher.start();
			} else if (str.equalsIgnoreCase("acceptorSetLargestProposalNumber")) {
				launcher.localPaxosInstance.acceptorSetLargestProposalNumber(s.nextDouble());
			} else if (str.equalsIgnoreCase("sendPromiseRequest")) {
				launcher.localPaxosInstance.sendPromiseRequest();
			} else {
				launcher.connectDirectTo(str);
			}
		}

		s.close();

	}

}
