package org.espenhahn.allocate.likepaxos.registry;

import inputport.datacomm.simplex.buffer.nio.AScatterGatherSelectionManager;
import port.sessionserver.ASessionServerLauncher;

public class PaxosSessionServerLauncher extends ASessionServerLauncher {
	
	public static final String SERVER_ID = "9090";
	public static final String SERVER_NAME = "PAXOS_SESSION_SERVER";

	public PaxosSessionServerLauncher(String aSessionServerId, String aSessionServerName, String aLogicalServerName) {
		super(aSessionServerId, aSessionServerName, aLogicalServerName);
	}

	public PaxosSessionServerLauncher(String aSessionServerId, String aSessionServerName) {
		super(aSessionServerId, aSessionServerName);
	}

	public static void main(String args[]) {
		AScatterGatherSelectionManager.setMaxOutstandingWrites(1000);

		(new PaxosSessionServerLauncher(SERVER_ID, SERVER_NAME)).launch();
	}

}
