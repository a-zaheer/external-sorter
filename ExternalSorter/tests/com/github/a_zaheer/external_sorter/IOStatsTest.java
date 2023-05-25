package com.github.a_zaheer.external_sorter;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Ali Zaheer
 * @version Oct 25, 2022
 */
public class IOStatsTest
    extends TestCase
{
    // ~ Fields ................................................................
    private IOStats stats;
    // ~ Constructors ..........................................................

    /**
     * sets up the test methods
     */
    public void setUp()
    {
        stats = new IOStats(1, 1, 1, 1);
    }


    // ~Public Methods ........................................................
    /**
     * tests the getters and setters for hits, misses, reads, and writes
     */
    public void testStats()
    {
        assertEquals(1, stats.getHits());
        stats.setHits(0);
        assertEquals(0, stats.getHits());

        assertEquals(1, stats.getMisses());
        stats.setMisses(0);
        assertEquals(0, stats.getMisses());

        assertEquals(1, stats.getReads());
        stats.setReads(0);
        assertEquals(0, stats.getReads());

        assertEquals(1, stats.getWrites());
        stats.setWrites(0);
        assertEquals(0, stats.getWrites());
    }


    // ----------------------------------------------------------
    /**
     * tests reset()
     */
    public void testReset()
    {
        stats.reset();
        assertEquals(0, stats.getHits());
        assertEquals(0, stats.getMisses());
        assertEquals(0, stats.getReads());
        assertEquals(0, stats.getWrites());
    }
}

