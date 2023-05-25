package com.github.a_zaheer.external_sorter;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests FakeBufferPool
 *
 * @author Ali Zaheer
 * @version Oct 27, 2022
 */
public class FakeBufferPoolTest
    extends TestCase
{
    // ~ Fields ................................................................
    private Integer[]      vals;
    private Integer[]      vals2;
    private FakeBufferPool bp;

    // ~ Constructors ..........................................................
    /**
     * sets up test methods
     */
    public void setUp()
    {
        Integer[] temp = { 4, 7, 2, 2, 3, 9, 1 };
        vals = temp;

        Integer[] temp2 = { 1, 2, 3, 4, 5, 6, 7 };
        vals2 = temp2;

        bp = new FakeBufferPool(vals);
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * tests getArray() and setArray()
     */
    public void testArray()
    {
        assertEquals(vals, bp.getArray());

        bp.setArray(vals2);
        assertEquals(vals2, bp.getArray());
    }


    // ----------------------------------------------------------
    /**
     * tests getElem(), setElem(), and swapElems()
     */
    public void testElem()
    {
        assertTrue(4 == bp.getElem(0));
        bp.setElem(0, 8);
        assertTrue(8 == bp.getElem(0));

        bp.swapElems(0, 6);
        assertTrue(1 == bp.getElem(0));
        assertTrue(8 == bp.getElem(6));
    }


    // ----------------------------------------------------------
    /**
     * tests clear() and length();
     */
    public void testClear()
    {
        assertEquals(7, bp.length());

        bp.clear();
        for (int i = 0; i < 7; i++)
        {
            assertNull(bp.getElem(i));
        }
        assertEquals(7, bp.length());
    }
}

