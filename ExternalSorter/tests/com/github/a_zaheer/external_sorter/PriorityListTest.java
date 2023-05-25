package com.github.a_zaheer.external_sorter;
import java.util.NoSuchElementException;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests PriorityList
 *
 * @author Ali Zaheer
 * @version Oct 26, 2022
 */
public class PriorityListTest
    extends TestCase
{
    // ~ Fields ................................................................
    private PriorityList<Integer> list;
    private Exception             exception;

    // ~ Constructors ..........................................................
    /**
     * sets up test methods
     */
    public void setUp()
    {
        list = new PriorityList<Integer>();
        exception = null;
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * tests the constructor and methods when the list is empty
     */
    public void testEmpty()
    {
        assertTrue(list.isAtEnd()); // curr is set to tail
        assertEquals(0, list.currPos()); // head points to tail

        try
        {
            list.getValue();
        }
        catch (NoSuchElementException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        exception = null;

        assertEquals(0, list.length());
        assertTrue(list.isEmpty());

        try
        { // trying to remove from an empty list
            list.remove();
        }
        catch (NoSuchElementException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }


    // ----------------------------------------------------------
    /**
     * tests insert() and append()
     */
    public void testAdd()
    {
        list.append(1);
        assertEquals(0, list.currPos()); // current points to 1
        assertFalse(list.isAtEnd());

        assertTrue(1 == list.getValue());
        assertEquals(1, list.length());
        assertFalse(list.isEmpty());

        list.append(2);
        list.append(3);

        assertEquals(0, list.currPos()); // current position doesn't change
        assertTrue(1 == list.getValue());
        // values get added to end of the list

        assertFalse(list.isAtEnd());
        list.insert(0);
        assertEquals(4, list.length());
        assertEquals(0, list.currPos()); // current position doesn't change
        assertTrue(0 == list.getValue());
        // value inserted into current position

        list.moveToEnd();
        assertTrue(list.isAtEnd());
        list.insert(4);
        assertFalse(list.isAtEnd()); // new tail created
        list.next();
        assertTrue(list.isAtEnd());
    }


    // ----------------------------------------------------------
    /**
     * tests next(), prev(), moveToStart(), moveToEnd(), moveToPos();
     */
    public void testMovers()
    {
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);

        // at the end of the list
        assertEquals(0, list.currPos());
        assertFalse(list.isAtEnd());
        list.moveToEnd();
        assertEquals(4, list.currPos());
        assertTrue(list.isAtEnd());
        list.next(); // do nothing
        assertEquals(4, list.currPos());

        // at the start of the list
        list.moveToStart();
        assertEquals(0, list.currPos());
        list.prev(); // do nothing
        assertEquals(0, list.currPos());

        // middle of the list
        list.moveToPos(3);
        assertEquals(3, list.currPos());
        assertFalse(list.isAtEnd());
        list.next();
        assertTrue(list.isAtEnd());
        list.prev();
        assertEquals(3, list.currPos());

        // outside the list
        assertFalse(list.moveToPos(-1));
        assertFalse(list.moveToPos(5));
    }


    // ----------------------------------------------------------
    /**
     * tests remove()
     */
    public void testRemove()
    {
        list.append(1);
        list.append(2);
        list.append(3);

        assertTrue(1 == list.remove());
        assertEquals(2, list.length());
        assertEquals(0, list.currPos());
        assertFalse(list.isAtEnd());

        list.remove();
        list.remove();
        assertEquals(0, list.currPos());
        assertEquals(0, list.length());
        assertTrue(list.isAtEnd());
    }


    // ----------------------------------------------------------
    /**
     * tests prioritize()
     */
    public void testPrioritize()
    {
        list.append(1);
        list.append(2);
        list.append(3);

        // do nothing if at the start
        list.prioritize();
        assertEquals(0, list.currPos());
        assertTrue(1 == list.getValue());

        // do nothing if at the end
        list.moveToEnd();
        list.prioritize();
        list.moveToStart();
        assertTrue(1 == list.getValue());

        // move element to the start
        list.moveToPos(2);

        list.prioritize();
        assertEquals(0, list.currPos());
        assertTrue(3 == list.getValue());
        assertEquals(3, list.length());

        list.moveToPos(2);
        assertTrue(2 == list.getValue());

    }
}

