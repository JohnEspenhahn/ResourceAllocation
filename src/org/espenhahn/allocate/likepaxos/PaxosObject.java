package org.espenhahn.allocate.likepaxos;

public interface PaxosObject {

	/**
	 * @return The number of accepts needed for a majority
	 */
	int getCountForMajority();
	
}
