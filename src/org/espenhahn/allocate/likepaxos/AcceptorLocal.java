package org.espenhahn.allocate.likepaxos;

import java.util.Set;

public interface AcceptorLocal<E> {
	
	Set<LearnerRemote<E>> getLearners();

}
