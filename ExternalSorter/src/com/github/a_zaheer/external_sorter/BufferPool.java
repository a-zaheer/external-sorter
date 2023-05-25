package com.github.a_zaheer.external_sorter;
// -------------------------------------------------------------------------
/**
 * The buffer pool provides a loading area for operations between a caller
 * method/function and an file. It does this by managing buffers, each of which
 * can work on the file. The buffer pool allows the caller to view and work with
 * the file as one big array. The overall goal is to reduce the number of file
 * reads/writes while maintaining that array functionality.
 *
 * @author Ali Zaheer
 * @version Oct 18, 2022
 * @param <E>
 *            type of object in the file
 */
public interface BufferPool<E>
{
    // ----------------------------------------------------------
    /**
     * Retrieves the element located at the specified position.
     *
     * @param pos
     *            the position in the virtual array
     * @return the element
     */
    public E getElem(int pos);


    // ----------------------------------------------------------
    /**
     * Changes the element located at the specified position.
     *
     * @param pos
     *            the position in the virtual array
     * @param newElem
     *            the new element
     */
    public void setElem(int pos, E newElem);


    /**
     * Swap the two elements at the specified positions.
     *
     * @param a
     *            the first element's position
     * @param b
     *            the second element's position
     */
    public void swapElems(int a, int b);


    // ----------------------------------------------------------
    /**
     * Flushes all the buffers.
     */
    public void clear();
    // ----------------------------------------------------------


    // ----------------------------------------------------------
    /**
     * Retrieves the length of the virtual array.
     *
     * @return length
     */
    public int length();
}
