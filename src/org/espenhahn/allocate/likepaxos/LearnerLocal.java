package org.espenhahn.allocate.likepaxos;

public interface LearnerLocal<E> {
	
	void onLearnerLearned(E value);

}
