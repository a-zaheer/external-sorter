package com.github.a_zaheer.external_sorter;
import java.util.NoSuchElementException;

// -------------------------------------------------------------------------
/**
 * Priority List provides list functionality in which the order of the elements
 * indicates their priority. Emulates a queue, with the added functionality that
 * any element can be accessed and reordered.
 *
 * @author Ali Zaheer
 * @version Oct 18, 2022
 * @param <E>
 */
// Linked list implementation
class PriorityList<E>
{
    private Link head;      // Pointer to list header
    private Link tail;      // Pointer to last element
    private Link curr;      // Access to current element
    private int  listSize;      // Size of list

    // ----------------------------------------------------------
    /**
     * Create a new, empty PriorityList object.
     */
    PriorityList()
    {
        clear();
    }


    // ----------------------------------------------------------
    /**
     * Remove all elements
     */
    public void clear()
    {
        tail = new Link(null); // Create trailer
        head = new Link(tail);        // Create header
        curr = tail;
        listSize = 0;
    }


    // ----------------------------------------------------------
    /**
     * Insert "it" at current position
     *
     * @param it
     *            element to insert
     */
    public void insert(E it)
    {
        curr.setNext(new Link(curr.element(), curr.next()));
        curr.setElement(it);
        if (curr == tail)
        {
            tail = curr.next();  // New tail
        }
        listSize++;
    }


    // ----------------------------------------------------------
    /**
     * Append "it" to list
     *
     * @param it
     *            element to append
     */
    public void append(E it)
    {
        tail.setNext(new Link(null));
        tail.setElement(it);
        tail = tail.next();
        listSize++;
    }


    // ----------------------------------------------------------
    /**
     * Move the current element to the front of the list. Does nothing if the
     * current position points to the tail
     */
    public void prioritize()
    {
        if (curr == head.next())
            return; // the link is already at the top

        if (curr != tail)
        {
            E temp = this.remove();
            this.moveToStart();
            this.insert(temp);
        }
    }


    // ----------------------------------------------------------
    /**
     * Remove and return current element
     *
     * @return the removed element
     * @throws NoSuchElementException
     *             if the position is at the end
     */

    public E remove()
        throws NoSuchElementException
    {
        if (curr == tail)
        {
            // Nothing to remove
            throw new NoSuchElementException(
                "remove() in LList has current of " + curr + " and size of "
                    + listSize + " that is not a a valid element");
        }
        E it = curr.element();                  // Remember value
        curr.setElement(curr.next().element()); // Pull forward the next element
        if (curr.next() == tail)
        {
            tail = curr;   // Removed last, move tail
        }
        curr.setNext(curr.next().next());       // Point around unneeded link
        listSize--;                             // Decrement element count
        return it;                              // Return value
    }


    // ----------------------------------------------------------
    /**
     * Set the current position at the list's start
     */
    public void moveToStart()
    {
        curr = head.next();
    }


    // ----------------------------------------------------------
    /**
     * Set the current position at the list's end
     */
    public void moveToEnd()
    {
        curr = tail;
    }


    // ----------------------------------------------------------
    /**
     * Move current position one step left; no change if now at front
     */
    public void prev()
    {
        if (head.next() == curr)
        {
            return; // No previous element
        }
        Link temp = head;
        // March down list until we find the previous element
        while (temp.next() != curr)
        {
            temp = temp.next();
        }
        curr = temp;
    }


    // ----------------------------------------------------------
    /**
     * Move current position one step right; no change if now at end
     */
    public void next()
    {
        if (curr != tail)
        {
            curr = curr.next();
        }
    }


    // ----------------------------------------------------------
    /**
     * Return list length
     *
     * @return length
     */
    public int length()
    {
        return listSize;
    }


    // ----------------------------------------------------------
    /**
     * Return the position of the current element
     *
     * @return position
     */
    public int currPos()
    {
        Link temp = head.next();
        int i;
        for (i = 0; curr != temp; i++)
        {
            temp = temp.next();
        }
        return i;
    }


    // ----------------------------------------------------------
    /**
     * Move down list to "pos" position
     *
     * @param pos
     *            the desired position
     * @return true if the position can be reached
     */
    public boolean moveToPos(int pos)
    {
        if ((pos < 0) || (pos > listSize))
        {
            return false;
        }
        curr = head.next();
        for (int i = 0; i < pos; i++)
        {
            curr = curr.next();
        }
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Checks if current position is at the end of the list
     *
     * @return true/false
     */
    public boolean isAtEnd()
    {
        return curr == tail;
    }


    // ----------------------------------------------------------
    /**
     * Return current element value. Note that null gets returned if current
     * value points to the the tail
     *
     * @return current element
     * @throws NoSuchElementException
     *             if at the tail
     */

    public E getValue()
        throws NoSuchElementException
    {
        if (curr == tail) // No current element
        {
            throw new NoSuchElementException(
                "getValue() in LList has current of " + curr + " and size of "
                    + listSize + " that is not a a valid element");
        }
        return curr.element();
    }


    // ----------------------------------------------------------
    /**
     * Checks if the list is empty.
     *
     * @return true/false
     */
    public boolean isEmpty()
    {
        return listSize == 0;
    }

    private class Link
    {         // Singly linked list node class
        private E    e;          // Value for this node
        private Link n;    // Point to next node in list

        // Constructors
        private Link(E it, Link inn)
        {
            e = it;
            n = inn;
        }


        private Link(Link inn)
        {
            e = null;
            n = inn;
        }


        private E element()
        {
            return e;
        }                        // Return the value


        private E setElement(E it)
        {
            e = it;
            return e;
        }            // Set element value


        private Link next()
        {
            return n;
        }                     // Return next link


        private Link setNext(Link inn)
        {
            n = inn;
            return n;
        } // Set next link
    }

}

