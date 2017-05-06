package org.espenhahn.allocate.likepaxos;

public interface LearnListener<E> {

	void onLearned(E value);
	
}
