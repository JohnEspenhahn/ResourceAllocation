package org.espenhahn.allocate.likepaxos;

import java.io.Serializable;

public interface Proposal<E> extends Serializable {

	int getProposalNumber();
	
	E getValue();
	
}
