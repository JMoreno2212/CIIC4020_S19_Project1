package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList<E> implements List<E> {
	private Node<E> header;
	private int size;
	public SinglyLinkedList() {
		header = new Node<E>();
		size = 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new SinglyLinkedListIterator<>(this);
	}


	@Override
	public void add(E obj) {
		Node<E> curr = header;
		while(curr.getNext() != null) curr = curr.getNext();
		curr.setNext(new Node<E>(obj));
		size++;
	}

	@Override
	public void add(int index, E obj) {
		int counter = 0;
		Node<E> curr = header;
		Node<E> nuevo = new Node<E>(obj);
		if(index == 0) {
			nuevo.setNext(header.getNext());
			header.setNext(nuevo);

		}
		else {
			while(counter < index - 1) {
				curr = curr.getNext();
				counter++;
			}
			nuevo.setNext(curr.getNext());
			curr.setNext(nuevo);
		}
		size++;
	}

	@Override
	public boolean remove(E obj) {
		if(size == 0 || !(this.contains(obj))) return false;
		Node<E> curr = header.getNext();
		Node<E> prev = header;
		while(curr.getNext() != null) {
			if(curr.getElement().equals(obj)) {
				prev.setNext(curr.getNext());
				size--;
				return true;
			}
			prev = curr;
			curr = curr.getNext();
		}
		return false;
	}

	@Override
	public boolean remove(int index) {
		if(size == 0 || index > size || index < 0) {throw new IndexOutOfBoundsException("remove: Invalid index = " + index );}
		int count = 0;
		Node<E> curr = header.getNext();
		Node<E> prev = header;
		while(count < index) {
			prev = curr;
			curr = curr.getNext();
			count++;
		}
		prev.setNext(curr.getNext());
		size--;
		return true;
	}

	@Override
	public int removeAll(E obj) {
		Iterator<E> iter = this.iterator();
		int removed = 0;
		while(iter.hasNext()) {
			if(iter.next().equals(obj)) {
				this.remove(obj);
				removed++;
			}
		}
		size = size - removed;
		return removed;
	}

	@Override
	public E get(int index) {
		if(index < 0 || index > size || size == 0) {throw new IndexOutOfBoundsException("get: Invalid index = " + index );}
		int count = 0;
		Node<E> curr = header.getNext();
		while(count < index) {
			curr = curr.getNext();
			count++;
		}
		return curr.getElement();
	}

	@Override
	public E set(int index, E obj) {
		if(index < 0 || index > size || size == 0) {throw new IndexOutOfBoundsException("set: Invalid index = " + index );}
		int count = 0;
		Node<E> curr = header.getNext();
		Node<E> prev = header;
		while(curr.getNext() != null && count < index) {
			prev = curr;
			curr = curr.getNext();
			count++;
		}
		E etr = curr.getElement();
		prev.setNext(new Node<E>(obj, curr.getNext()));		
		return etr;
	}

	@Override
	public E first() {
		if(isEmpty()) {return null;}
		return header.getNext().getElement();
	}

	@Override
	public E last() {
		if(isEmpty()) return null;
		Node<E> curr = header.getNext();
		while(curr.getNext() != null) curr = curr.getNext();
		return curr.getElement();
	}

	@Override
	public int firstIndex(E obj) {
		int count = 0;
		Node<E> curr = header.getNext();
		while(curr.getNext() != null ) {
			if(curr.getElement().equals(obj)) return count;
			curr = curr.getNext();
			count++;
		}
		if(curr.getElement().equals(obj)) return count; // Checking last Node
		return -1;
	}

	@Override
	public int lastIndex(E obj) {
		int currIndx = 0;
		int targetIndx = -1;
		Node<E> curr = header.getNext();
		while(curr.getNext() != null) {
			if(curr.getElement().equals(obj)) targetIndx = currIndx;
			curr = curr.getNext();
			currIndx++;
		}
		return targetIndx;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(E obj) {
		Node<E> curr = header.getNext();
		while(curr.getNext() != null) {
			if(curr.getElement().equals(obj)) return true;
			curr = curr.getNext();
		}
		return false;
	}

	@Override
	public void clear() {
		Node<E> prev = header;
		Node<E> curr = header.getNext();
		while(curr.getNext() != null) {
			prev.setElement(null);
			prev.setNext(null);
			prev = curr;
			curr = curr.getNext();
			size = 0;
		}

	}

	private static class Node<E> {
		private E element;
		private Node<E> next;
		public Node() {
			element = null;
			next = null;
		}
		public Node(E element) {
			this.element = element;
			next = null;
		}
		public Node(E element, Node<E> next) {
			this.element = element;
			this.next = next;
		}

		public E getElement() {return element;}
		public void setElement(E newElement) {this.element = newElement;}

		public Node<E> getNext() {return next;}
		public void setNext(Node<E> newNext) {this.next = newNext;}
	}

	@SuppressWarnings("hiding")
	private class SinglyLinkedListIterator<E> implements Iterator<E> {

		private SinglyLinkedList<E> list;
		private int current = 0;

		public SinglyLinkedListIterator(SinglyLinkedList<E> list) {
			this.list = list;
		}

		@Override
		public boolean hasNext() {
			if(current < size()) {return true;}
			return false;
		}

		@Override
		public E next() {
			if(!this.hasNext()) throw new NoSuchElementException("There are no more elements");
			return list.get(current++);
		}

	}

}
