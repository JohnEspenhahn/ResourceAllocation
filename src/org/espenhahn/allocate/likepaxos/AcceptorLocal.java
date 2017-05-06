package org.espenhahn.allocate.likepaxos;

import java.util.List;

public interface AcceptorLocal<E> {
	
	List<LearnerRemote<E>> getLearners();

}
