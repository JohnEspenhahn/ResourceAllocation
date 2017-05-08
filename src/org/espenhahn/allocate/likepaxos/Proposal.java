package org.espenhahn.allocate.likepaxos;

import java.io.Serializable;

public interface Proposal<E> extends Serializable {

	double getProposalNumber();
	
	E getValue();
	
}
