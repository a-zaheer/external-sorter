package com.github.a_zaheer.external_sorter;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the Heap Sort algorithm in HeapSorter independently of any command line
 * arguments or file IO.
 *
 * @author Ali Zaheer
 * @version Oct 27, 2022
 */
public class HeapSorterTest
    extends TestCase
{
    // ~ Fields ................................................................
    private HeapSorter<Integer> sorter;
    private FakeBufferPool      bp;
    private Integer[]           vals;

    // ~ Constructors ..........................................................
    /**
     * sets up the test methods
     */
    public void setUp()
    {
        Integer[] temp = { 4, 7, 2, 2, 3, 9, 1 };
        vals = temp;

        bp = new FakeBufferPool(vals);
        sorter = new HeapSorter<Integer>(bp, 7);
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * tests sort()
     */
    public void testSort()
    {
        sorter.sort();

        assertEquals(7, bp.length());
        assertTrue(1 == bp.getElem(0));
        assertTrue(1 == vals[0]);

        assertTrue(2 == bp.getElem(1));
        assertTrue(2 == bp.getElem(2));
        assertTrue(3 == bp.getElem(3));
        assertTrue(4 == bp.getElem(4));
        assertTrue(7 == bp.getElem(5));
        assertTrue(9 == bp.getElem(6));
    }
}

