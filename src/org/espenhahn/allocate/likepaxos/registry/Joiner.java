package org.espenhahn.allocate.likepaxos.registry;

import java.rmi.Remote;

import org.espenhahn.allocate.likepaxos.PaxosServer;

public class Joiner implements Remote {

	private PaxosServer server;
	
	public void join(Registry r) {
		this.server = new PaxosServer();
		r.join(this);
	}
	
	public PaxosServer getServer() {
		return this.server;
	}
	
}
