package org.espenhahn.allocate.likepaxos.registry;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.espenhahn.allocate.likepaxos.PaxosServerDebuggable;
import org.espenhahn.allocate.likepaxos.PaxosServerDebuggableImpl;

import examples.mvc.local.duplex.ACounter;
import examples.mvc.local.duplex.Counter;
import inputport.rpc.DirectedRPCProxyGenerator;
import inputport.rpc.duplex.AnAbstractDuplexRPCClientPortLauncher;
import port.PortAccessKind;
import port.PortKind;
import port.SessionChoice;
import sessionport.rpc.duplex.DuplexRPCSessionPort;
import util.trace.Tracer;

public class PaxosNodeLauncher extends AnAbstractDuplexRPCClientPortLauncher {

	private short clientId;
	private PaxosServerDebuggable localPaxosInstance;
	private List<PaxosServerDebuggable> allPaxosNodes;

	public PaxosNodeLauncher(String clientName, short clientId, String serverHost, String serverId, String serverName) {
		super(clientName, serverHost, serverId, serverName);
		DirectedRPCProxyGenerator.setDoShortCircuitLocalCallsToRemotes(false);

		this.clientId = clientId;
		this.allPaxosNodes = new ArrayList<PaxosServerDebuggable>();
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

	protected PaxosServerDebuggable connectDirectTo(String name) {
		PaxosServerDebuggable proxy = (PaxosServerDebuggable) DirectedRPCProxyGenerator
				.generateRPCProxy((DuplexRPCSessionPort) mainPort, name, PaxosServerDebuggable.class, null);
		
		this.register(name, proxy);
		allPaxosNodes.add(proxy);

		System.out.println("Connected to " + name);

		return proxy;
	}

	protected void start() {
		try {
			localPaxosInstance.setup(clientId, allPaxosNodes);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length == 0)
			throw new RuntimeException("Requires node name!");

		Tracer.showWarnings(false);
		Tracer.showInfo(false);

		String name = args[0];
		short port = (short) (9000 + (name.hashCode() % 56000));
		if (args.length > 1)
			port = Short.parseShort(args[1]);

		String address = "localhost";
		if (args.length > 2)
			address = args[2];

		PaxosNodeLauncher launcher = (new PaxosNodeLauncher(name, port, address, PaxosSessionServerLauncher.SERVER_ID,
				PaxosSessionServerLauncher.SERVER_NAME));

		launcher.launch();

		Scanner s = new Scanner(System.in);

		while (s.hasNextLine()) {
			String str = s.nextLine().trim();
			if (str.equalsIgnoreCase("start")) {
				launcher.start();
				break;
			} else {
				launcher.connectDirectTo(str);
			}
		}

		s.close();

	}

}
