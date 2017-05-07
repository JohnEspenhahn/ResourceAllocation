package org.espenhahn.allocate.likepaxos.registry;

import java.util.ArrayList;
import java.util.List;

import org.espenhahn.allocate.likepaxos.PaxosServer;

public class Registry {

	public static final int NUM_PROPOSERS = 3;
	public static final int NUM_MAJORITY = (int) Math.ceil(NUM_PROPOSERS/2.0);
	
	private List<PaxosServer> servers = new ArrayList<PaxosServer>();
	
	public void join(Joiner j) {
		if (servers.size() < NUM_PROPOSERS) {
			servers.add(j.getServer());
			if (servers.size() == NUM_PROPOSERS) {
				short id = 1;
				for (PaxosServer s: servers) {
					s.setup(id++, servers);
				}
			}
		}
	}
	
}
