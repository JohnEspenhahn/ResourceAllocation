package org.espenhahn.allocate.likepaxos;

public interface Proposal<E> {

	int getProposalNumber();
	
	E getValue();
	
}
