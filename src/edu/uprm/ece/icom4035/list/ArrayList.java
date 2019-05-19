package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;



public class ArrayList<E> implements List<E> {
	private static final int INITCAP = 5; 
	private static final int CAPTOAR = 3; 
	private static final int MAXEMPTYPOS = 3; 
	private E[] elements; 
	private int size;
	
	@SuppressWarnings("unchecked")
	public ArrayList() { 
		elements = (E[]) new Object[INITCAP]; 
		size = 0; 
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> it = new ArrayListIterator<>(this);
		return it;
	}
     

	@Override
	public void add(E obj) {
		if(size == elements.length) {changeCapacity(CAPTOAR);}
		elements[size] = obj;
		size++;
	}
	
	@Override
	public void add(int index, E obj) {
		if(index > size || index < 0) {throw new IndexOutOfBoundsException("add: Invalid index = " + index );}
		if(size == elements.length) {changeCapacity(CAPTOAR);}
		if(index < size) {moveDataOnePositionTR(index, size - 1);}
		elements[index] = obj;
		size++;
	}

	@Override
	public boolean remove(E obj) {
		Iterator<E> iter = this.iterator();
		while(iter.hasNext()) {
			if(iter.next().equals(obj)) return true;
		}
		return false;
	}

	@Override
	public boolean remove(int index) {
		if(index > size - 1 || index < 0) {throw new IndexOutOfBoundsException("remove: Invalid index = " + index );}
		if(index < size) {moveDataOnePositionTL(index + 1, size - 1);}
		elements[size - 1] = null;
		size--;
		if(elements.length - size > MAXEMPTYPOS) {changeCapacity(-CAPTOAR);}
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
		return removed;
	}

	@Override
	public E get(int index) {
		if(index > size - 1 || index < 0) {throw new IndexOutOfBoundsException("get: Invalid index = " + index );}
		return elements[index]; 
	}

	@Override
	public E set(int index, E obj) {
		if(index > size - 1 || index < 0) {throw new IndexOutOfBoundsException("set: Invalid index = " + index );}
		E old = elements[index];
		elements[index] = obj;
		return old;
	}

	@Override
	public E first() {
		if(isEmpty()) {return null;}
		return elements[0];
	}

	@Override
	public E last() {
		if(isEmpty()) {return null;}
		return elements[size];
	}

	@Override
	public int firstIndex(E obj) {
		int currIndx = 0;
		for(E curr: this) {
			if(curr.equals(obj)) return currIndx;
			currIndx++;
		}
		return -1;
	}

	@Override
	public int lastIndex(E obj) {
		int targetIndx = -1;
		int currIndx = 0;
		for(E curr: this) {
			if(curr.equals(obj)) targetIndx = currIndx;
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
		for(E curr: this) {
			if(curr.equals(obj)) return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		elements = (E[]) new Object[INITCAP];
		size = 0;
	}
	
	//Re-using changeCapacity method from a previous lab
	//@auth pirvos
	private void changeCapacity(int change) { 
		int newCapacity = elements.length + change; 
		@SuppressWarnings("unchecked")
		E[] newElement = (E[]) new Object[newCapacity]; 
		for (int i=0; i<size; i++) { 
			newElement[i] = elements[i]; 
			elements[i] = null; 
		} 
		elements = newElement; 
	}
	
	//Re-using moveOneDataPositionTR from a previous lab
	//@auth pirvos
	private void moveDataOnePositionTR(int low, int sup) { 
		// pre: 0 <= low <= sup < (element.length - 1)
		for (int pos = sup; pos >= low; pos--)
			elements[pos+1] = elements[pos]; 
	}

	//Re-using moveOneDataPositionTL from a previous lab
	//@auth pirvos
	private void moveDataOnePositionTL(int low, int sup) { 
		// pre: 0 < low <= sup <= (element.length - 1)
		for (int pos = low; pos <= sup; pos++)
			elements[pos-1] = elements[pos]; 
	}
	
	@SuppressWarnings("hiding")
	private class ArrayListIterator<E> implements Iterator<E>{
		private ArrayList<E> array;
		private int current = 0;
		public ArrayListIterator(ArrayList<E> arr){
			this.array = arr;
		}
		
		@Override
		public boolean hasNext() {
			if(current < size()) return true;
			return false;
		}

		@Override
		public E next() {
			if(!this.hasNext()) throw new NoSuchElementException("There are no more elements");
			return array.get(current++);
		}
		
	}
}
