package com.github.a_zaheer.external_sorter;
// -------------------------------------------------------------------------
/**
 * Max-heap implementation by Patrick Sullivan, based on OpenDSA Heap code.
 * Customized for ExternalSorting with a BufferPool as the heap's underlying
 * data structure. The buffer pool provides all the functionality of an array,
 * and it is an array from the MaxHeap's perspective.
 *
 * @param <E>
 *            type of buffer pool's elements
 * @author ALi Zaheer
 * @version Oct 25, 2022
 */
class MaxHeap<E extends Comparable<E>>
{
    private BufferPool<E> heap; // Pointer to the buffer pool, acts like an
                                // array
    private int           n; // Number of things currently in heap

    // ----------------------------------------------------------
    /**
     * Create a new MaxHeap object from a pre-loaded buffer pool.
     *
     * @param bp
     *            source buffer pool
     * @param heapSize
     *            number of elements
     */
    MaxHeap(BufferPool<E> bp, int heapSize)
    {
        heap = bp;
        n = heapSize;
        buildHeap();
    }


    // ----------------------------------------------------------
    /**
     * Return position for left child of pos
     *
     * @param pos
     *            current position
     * @return child's position
     */
    public static int leftChild(int pos)
    {
        return 2 * pos + 1;
    }


    // ----------------------------------------------------------
    /**
     * Return position for right child of pos
     *
     * @param pos
     *            current position
     * @return child's position
     */
    public static int rightChild(int pos)
    {
        return 2 * pos + 2;
    }


    // ----------------------------------------------------------
    /**
     * Return position for the parent of pos
     *
     * @param pos
     *            current position
     * @return parent's position
     */
    public static int parent(int pos)
    {
        return (pos - 1) / 2;
    }


    // ----------------------------------------------------------
    /**
     * Return current size of the heap
     *
     * @return the size
     */
    public int heapSize()
    {
        return n;
    }


    // ----------------------------------------------------------
    /**
     * Return true if pos is a leaf position, false otherwise
     *
     * @param pos
     *            specified position
     * @return boolean
     */
    public boolean isLeaf(int pos)
    {
        return (n / 2 <= pos) && (pos < n);
    }


    // ----------------------------------------------------------
    /**
     * Remove and return maximum value.
     *
     * @return max value
     */
    public E removeMax()
    {
        if (n <= 0)
            throw new IllegalStateException("Heap is empty");
        n--;
        if (n > 0)
        {
            swap(0, n); // Swap maximum with last value
            siftDown(0); // Put new heap root val in correct place
        }
        return heap.getElem(n);
    }


    // ----------------------------------------------------------
    /**
     * Organize contents of array to satisfy the heap structure
     */
    private void buildHeap()
    {
        for (int i = parent(n - 1); i >= 0; i--)
        {
            siftDown(i);
        }
    }


    // ----------------------------------------------------------
    /**
     * Moves an element down to its correct place
     */
    private void siftDown(int pos)
    {
        int curr = pos;
        while (!isLeaf(curr))
        {
            int child = leftChild(curr);
            if ((child + 1 < n) && isGreaterThan(child + 1, child))
            {
                child = child + 1; // child is now index with the smaller value
            }
            if (!isGreaterThan(child, curr))
            {
                return; // stop early
            }
            swap(curr, child);
            curr = child; // keep sifting down
        }
    }


    /**
     * swaps the elements at the two positions
     */
    private void swap(int pos1, int pos2)
    {
        heap.swapElems(pos1, pos2);
    }


    /**
     * does fundamental comparison used for checking heap validity
     */
    private boolean isGreaterThan(int pos1, int pos2)
    {
        return heap.getElem(pos1).compareTo(heap.getElem(pos2)) > 0;
    }

}

