package com.github.a_zaheer.external_sorter;
// -------------------------------------------------------------------------
/**
 * Simulates a buffer pool without the file read/write. This class is used to
 * test the heap sort without having to worry about IO.
 *
 * @author Ali Zaheer
 * @version Oct 21 2022
 */
class FakeBufferPool
    implements BufferPool<Integer>
{
    // ~ Fields ................................................................
    private Integer[] elemArray;

    // ~ Constructors ..........................................................
    // ----------------------------------------------------------
    /**
     * Create a new FakeBufferPool object to represent an integer array.
     *
     * @param newArray
     *            the underlying integer array.
     */
    public FakeBufferPool(Integer[] newArray)
    {
        elemArray = newArray;
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * Retrieves the buffer pool's underlying array.
     *
     * @return integer array
     */
    public Integer[] getArray()
    {
        return elemArray;
    }


    // ----------------------------------------------------------
    /**
     * Changes the buffer pool's underlying array.
     *
     * @param newArray
     *            the new integer array
     */
    public void setArray(Integer[] newArray)
    {
        elemArray = newArray;
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getElem(int pos)
    {
        return elemArray[pos];
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void setElem(int pos, Integer newElem)
    {
        elemArray[pos] = newElem;
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void swapElems(int a, int b)
    {
        Integer temp = elemArray[a];
        elemArray[a] = elemArray[b];
        elemArray[b] = temp;
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        for (int i = 0; i < elemArray.length; i++)
            elemArray[i] = null;

    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public int length()
    {
        return elemArray.length;
    }
}
