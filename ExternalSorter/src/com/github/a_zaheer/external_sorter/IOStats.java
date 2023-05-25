package com.github.a_zaheer.external_sorter;
// -------------------------------------------------------------------------
/**
 * Used to log the performance of the buffer pool.
 *
 * @author Ali Zaheer
 * @version Oct 21, 2022
 */
public class IOStats
{
    // ~ Fields ................................................................
    private int hits; // data found in one of the buffers
    private int misses; // data not found in any of the buffers
    private int reads; // number of blocks read from the file
    private int writes; // number of blocks written to the file

    // ~ Constructors ..........................................................
    // ----------------------------------------------------------
    /**
     * Create a new IOStats object.
     *
     * @param h
     *            hits
     * @param m
     *            misses
     * @param r
     *            reads
     * @param w
     *            writes
     */
    public IOStats(int h, int m, int r, int w)
    {
        hits = h;
        misses = m;
        reads = r;
        writes = w;
    }


    // ----------------------------------------------------------
    /**
     * Create a new IOStats object with default values of 0.
     */
    public IOStats()
    {
        this(0, 0, 0, 0);
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * Getter
     *
     * @return number of hits
     */
    public int getHits()
    {
        return hits;
    }


    // ----------------------------------------------------------
    /**
     * Setter
     *
     * @param h
     *            new number of hits
     */
    public void setHits(int h)
    {
        hits = h;
    }


    // ----------------------------------------------------------
    /**
     * Getter
     *
     * @return number of misses
     */
    public int getMisses()
    {
        return misses;
    }


    // ----------------------------------------------------------
    /**
     * Setter
     *
     * @param m
     *            new number of misses
     */
    public void setMisses(int m)
    {
        misses = m;
    }


    // ----------------------------------------------------------
    /**
     * Getter
     *
     * @return number of reads
     */
    public int getReads()
    {
        return reads;
    }


    // ----------------------------------------------------------
    /**
     * Setter
     *
     * @param r
     *            new number of reads
     */
    public void setReads(int r)
    {
        reads = r;
    }


    // ----------------------------------------------------------
    /**
     * Getter
     *
     * @return number of writes
     */
    public int getWrites()
    {
        return writes;
    }


    // ----------------------------------------------------------
    /**
     * Setter
     *
     * @param w
     *            new number of writes
     */
    public void setWrites(int w)
    {
        writes = w;
    }


    // ----------------------------------------------------------
    /**
     * Resets the values back to default of 0.
     */
    public void reset()
    {
        hits = 0;
        misses = 0;
        reads = 0;
        writes = 0;
    }
}

