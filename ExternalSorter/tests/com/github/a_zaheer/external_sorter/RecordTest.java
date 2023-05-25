package com.github.a_zaheer.external_sorter;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Copied from starter code. Only minor modifications made along with
 * documentation. Tests the Record Class.
 *
 * @author Ali Zaheer
 * @version Oct 26, 2022
 */
public class RecordTest
    extends TestCase
{

    private Record r;
    private Record r2;

    // ----------------------------------------------------------
    /**
     * Tests the functionality of the Record constructors and methods.
     */
    public void testBasics()
    {
        // constructors and getters....
        r = new Record(5, 6);
        assertEquals(5, r.getKey());
        assertEquals(6, r.getValue());
        byte[] bArr = { 0, 23, 2, 0, };
        r2 = new Record(bArr);
        assertEquals(23, r2.getKey());
        assertEquals(512, r2.getValue());
        assertEquals("Record: (23, 512)", r2.toString());
        byte[] bArr2 = { 0, 23, 4, 0, 1, 2, 0, 1 };
        Record[] recs = Record.toRecArray(bArr2);
        assertEquals(2, recs.length);
        assertEquals("Record: (23, 1024)", recs[0].toString());
        assertEquals("Record: (258, 1)", recs[1].toString());

        // comparisons...
        assertEquals(0, r.compareTo(r));
        r2 = new Record(7, 3);
        assertEquals(7, r2.getKey());
        assertTrue(r.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r) > 0);

        // swaps ...
        byte[] bArrSwap = { 0, 23, 4, 0, 1, 2, 0, 1 };
        recs = Record.toRecArray(bArrSwap);
        assertEquals("Record: (23, 1024)", recs[0].toString());
        assertEquals("Record: (258, 1)", recs[1].toString());
        recs[0].setTo(recs[1]);
        recs[1].setTo(r2);
        assertEquals("Record: (258, 1)", recs[0].toString());
        assertEquals("Record: (7, 3)", recs[1].toString());
        // swap should also change the original arrays that held the record
        // data...
        assertEquals(1, bArrSwap[0]);
        assertEquals(2, bArrSwap[1]);
        assertEquals(0, bArrSwap[2]);
        assertEquals(1, bArrSwap[3]);
        assertEquals(0, bArrSwap[4]);
        assertEquals(7, bArrSwap[5]);
        assertEquals(0, bArrSwap[6]);
        assertEquals(3, bArrSwap[7]);

    }

}
