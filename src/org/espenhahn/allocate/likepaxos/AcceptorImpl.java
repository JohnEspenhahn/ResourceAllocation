package org.espenhahn.allocate.likepaxos;

public abstract class AcceptorImpl<E> extends ProposerImpl<E> implements AcceptorLocal<E>, AcceptorRemote<E> {

	public AcceptorImpl(boolean repropose) {
		super(repropose);
	}

	@Override
	public void promiseRequest(ProposerRemote<E> proposer, int proposalNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptRequest(ProposerRemote<E> proposer, Proposal<E> proposal) {
		// TODO Auto-generated method stub
		
	}

}
