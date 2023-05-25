package com.github.a_zaheer.external_sorter;
// -------------------------------------------------------------------------
/**
 * HeapSorter allows Heap Sort to work on its own independent of the main method
 * and file IO.
 *
 * @author Ali Zaheer
 * @version Oct 23, 2022
 * @param <E>
 *            type of object in the heap
 */
public class HeapSorter<E extends Comparable<E>>
{
    // ~ Fields ................................................................
    private MaxHeap<E> h;
    private int        size;

    // ~ Constructors ..........................................................
    // ----------------------------------------------------------
    /**
     * Creates a HeapSorter and associated max heap. Uses the buffer pool as the
     * source "array".
     *
     * @param bp
     *            source "array"
     * @param s
     *            size of the buffer pool
     */
    public HeapSorter(BufferPool<E> bp, int s)
    {
        size = s;

        h = new MaxHeap<E>(bp, size);
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * Sorts the buffer pool "array" in place using heap sort.
     */
    public void sort()
    {
        // The heap constructor invokes the buildHeap method
        for (int i = 0; i < size; i++)
        {  // Now sort
            h.removeMax(); // Remove Max places max at end of heap
        }
    }
}
