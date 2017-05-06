package org.espenhahn.allocate.likepaxos;

public interface AcceptListener<E> {

	void onAccept(int proposalNumber, E value);
	
}
