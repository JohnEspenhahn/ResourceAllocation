package org.espenhahn.allocate.util;

import java.util.Collection;
import java.util.Iterator;

/** Min-heap implementation */
public class MinHeap<E extends Comparable<? super E>> implements Collection<E> {
	private E[] heap; // Pointer to the heap array
	private int size; // Maximum size of the heap
	private int n; // Number of things in heap
	
	public MinHeap(E[] h) {
		this(h, h.length, h.length);
	}

	public MinHeap(E[] h, int num, int max) {
		heap = h;
		n = num;
		size = max;
		buildheap();
	}
	
	@Override
	public boolean isEmpty() {
		return n == 0;
	}

	/** Return current size of the heap */
	@Override
	public int size() {
		return n;
	}

	/** Is pos a leaf position? */
	public boolean isLeaf(int pos) {
		return (pos >= n / 2) && (pos < n);
	}

	/** Return position for left child of pos */
	public int leftchild(int pos) {
		assert pos < n / 2 : "Position has no left child";
		return 2 * pos + 1;
	}

	/** Return position for right child of pos */
	public int rightchild(int pos) {
		assert pos < (n - 1) / 2 : "Position has no right child";
		return 2 * pos + 2;
	}

	/** Return position for parent */
	public int parent(int pos) {
		assert pos > 0 : "Position has no parent";
		return (pos - 1) / 2;
	}

	/** Heapify contents of Heap. O(n) */
	public void buildheap() {
		for (int i = n / 2 - 1; i >= 0; i--)
			siftdown(i);
	}

	/** Insert into heap */
	@Override
	public boolean add(E val) {
		if (n < size) return false;
		int curr = n++;
		heap[curr] = val; // Start at end of heap
		// Now sift up until curr's parent's key > curr's key
		while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) < 0)) {
			swap(curr, parent(curr));
			curr = parent(curr);
		}
		return true;
	}

	/** Put element in its correct place */
	private void siftdown(int pos) {
		assert (pos >= 0) && (pos < n) : "Illegal heap position";
		while (!isLeaf(pos)) {
			int j = leftchild(pos);
			if ((j < (n - 1)) && (heap[j].compareTo(heap[j + 1]) > 0))
				j++; // j is now index of child with greater value
			if (heap[pos].compareTo(heap[j]) <= 0)
				return;
			swap(pos, j);
			pos = j; // Move down
		}
	}

	public E removemin() { // Remove minimum value
		assert n > 0 : "Removing from empty heap";
		swap(0, --n); // Swap minimum with last value
		if (n != 0) // Not on last element
			siftdown(0); // Put new heap root val in correct place
		return heap[n];
	}

	/** Remove element at specified position */
	public E remove(int pos) {
		assert (pos >= 0) && (pos < n) : "Illegal heap position";
		if (pos == (n - 1))
			n--; // Last element, no work to be done
		else {
			swap(pos, --n); // Swap with last value
			// If we just swapped in a small value, push it up
			while ((pos > 0) && (heap[pos].compareTo(heap[parent(pos)]) < 0)) {
				swap(pos, parent(pos));
				pos = parent(pos);
			}
			if (n != 0)
				siftdown(pos); // If it is big, push down
		}
		return heap[n];
	}

	private void swap(int i1, int i2) {
		E temp = heap[i1];
		heap[i1] = heap[i2];
		heap[i2] = temp;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		this.n = 0;
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size(); i++)
			if (heap[i].equals(o))
				return true;
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o: c)
			if (!contains(o)) 
				return false;
		
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean remove(Object o) {
		for (int i = 0; i < size(); i++) {
			if (heap[i].equals(o)) {
				remove(i);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[size()];
		System.arraycopy(heap, 0, arr, 0, size());
		return arr;
	}

	@Override
	public <T> T[] toArray(T[] arr) {
		System.arraycopy(heap, 0, arr, 0, Math.min(arr.length, size()));
		return arr;
	}
}