package edu.uprm.ece.icom4035.list;

public class SinglyLinkedListFactory<E> implements ListFactory<E> {

	@Override
	public List<E> newInstance() {
		return new SinglyLinkedList<E>();
	}


}
