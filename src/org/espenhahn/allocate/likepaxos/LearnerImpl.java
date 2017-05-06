package org.espenhahn.allocate.likepaxos;

public abstract class LearnerImpl<E> extends AcceptorImpl<E> implements LearnerRemote<E> {

	public LearnerImpl(boolean repropose) {
		super(repropose);
	}

}
