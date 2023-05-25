package com.github.a_zaheer.external_sorter;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests MaxHeap
 *
 * @author Ali Zaheer
 * @version Oct 25, 2022
 */
public class MaxHeapTest
    extends TestCase
{
    private Integer[]        vals;
    private Integer[]        vals2;
    private FakeBufferPool   bp;
    private FakeBufferPool   bp2;
    private MaxHeap<Integer> heap;

    /**
     * sets up the test methods
     */
    public void setUp()
    {

        Integer[] temp = { 4, 7, 2, 2, 3, 9, 1 };
        vals = temp;

        bp = new FakeBufferPool(vals);

        heap = new MaxHeap<Integer>(bp, 7);
        // This constructor calls build-heap automatically

        Integer[] temp2 = { 1, 2, 3, 4, 5, 6, 7 };
        vals2 = temp2;
    }


    // ----------------------------------------------------------
    /**
     * tests that the heap was built correctly and tests heapSize()
     */
    public void testHeap()
    {
        assertEquals(7, heap.heapSize());

        assertEquals(9, (int)vals[0]);
        assertEquals(7, (int)vals[1]);
        assertEquals(4, (int)vals[2]);
        assertEquals(2, (int)vals[3]);
        assertEquals(3, (int)vals[4]);
        assertEquals(2, (int)vals[5]);
        assertEquals(1, (int)vals[6]);

        bp2 = new FakeBufferPool(vals2);
        heap = new MaxHeap<Integer>(bp2, 0);
        // build heap does nothing
        assertEquals(1, (int)vals2[0]);
        assertEquals(2, (int)vals2[1]);
        assertEquals(3, (int)vals2[2]);
        assertEquals(4, (int)vals2[3]);
        assertEquals(5, (int)vals2[4]);
        assertEquals(6, (int)vals2[5]);
        assertEquals(7, (int)vals2[6]);

    }


    // ----------------------------------------------------------
    /**
     * tests isLeaf()
     */
    public void testIsLeaf()
    {
        assertTrue(heap.isLeaf(3));
        assertFalse(heap.isLeaf(2));
        assertFalse(heap.isLeaf(7));
    }


    // ----------------------------------------------------------
    /**
     * tests the static methods rightChild(), leftChild(), parent()
     */
    public void testPositions()
    {
        assertEquals(4, MaxHeap.rightChild(1));
        assertEquals(2, MaxHeap.rightChild(0));

        assertEquals(3, MaxHeap.leftChild(1));
        assertEquals(1, MaxHeap.leftChild(0));

        assertEquals(1, MaxHeap.parent(3));
        assertEquals(1, MaxHeap.parent(4));
        assertEquals(2, MaxHeap.parent(5));

        assertEquals(0, MaxHeap.parent(0));
    }


    // ----------------------------------------------------------
    /**
     * tests removeMax()
     */
    public void testRemoveMax()
    {
        assertEquals(9, (int)heap.removeMax());
        assertEquals(7, (int)vals[0]);
        assertEquals(9, (int)vals[6]); // still in array, but out of heap
        assertEquals(6, heap.heapSize());

        assertEquals(7, (int)heap.removeMax());
        assertEquals(4, (int)heap.removeMax());
        assertEquals(3, (int)heap.removeMax());
        assertEquals(2, (int)heap.removeMax());
        assertEquals(2, (int)heap.removeMax());
        assertEquals(1, (int)heap.removeMax());

        assertEquals(0, heap.heapSize());

        Exception exception = null;
        try
        {
            heap.removeMax();
        }
        catch (IllegalStateException e)
        {
            exception = e;
            assertNotNull(exception);
            assertEquals(IllegalStateException.class, exception.getClass());
            assertEquals("Heap is empty", exception.getMessage());
        }

    }
}

