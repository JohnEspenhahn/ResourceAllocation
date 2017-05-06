package org.espenhahn.allocate.likepaxos;

public class ProposalImpl<E> implements Proposal<E> {
	
	private final int proposalNumber;
	private final E value;
	
	public ProposalImpl(int proposalNumber, E value) {
		this.proposalNumber = proposalNumber;
		this.value = value;
	}

	@Override
	public int getProposalNumber() {
		return proposalNumber;
	}

	@Override
	public E getValue() {
		return value;
	}

}
