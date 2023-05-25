package com.github.a_zaheer.external_sorter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests FileBufferPool
 *
 * @author Ali Zaheer
 * @version Oct 28, 2022
 */
public class FileBufferPoolTest
    extends TestCase
{
    // ~ Fields ................................................................
    private final File       copy     = new File("data\\working\\BPCopy3.bin");
    private final File       original = new File("data\\working\\sampleBlock3.bin");
    private Path             pathSrc;
    private Path             pathDest;

    private RandomAccessFile raf;
    private IOStats          stats;
    private final int        numBuffs = 2;
    private FileBufferPool   bp;
    private Record           rec11;
    private Record           rec12;
    private Record           rec2;
    private Record           rec3;
    private Record           rec0;
    private Exception        exception;

    // ~ Constructors ..........................................................
    /**
     * sets up test methods
     *
     * @throws IOException
     *             if an IO error occurs
     */
    public void setUp()
        throws IOException
    {
        pathSrc = Paths.get(original.getPath());
        pathDest = Paths.get(copy.getPath());

        Files.copy(pathSrc, pathDest, StandardCopyOption.REPLACE_EXISTING);

        raf = new RandomAccessFile(copy, "rw");
        stats = new IOStats(0, 0, 0, 0);
        bp = new FileBufferPool(raf, numBuffs, stats);
        rec0 = new Record(0, 0);
        exception = null;

        copy.deleteOnExit();
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * Tests getElem(), setElem, and the LRU buffer replacement
     *
     * @throws IOException
     *             if error happens while trying to close the file
     */
    public void testLRU()
        throws IOException
    {
        // read from disk, bp empty
        rec11 = bp.getElem(0);
        bp.setElem(10, rec0); // toggle dirty bit
        assertEquals(1, stats.getHits());
        assertEquals(1, stats.getMisses());
        assertEquals(1, stats.getReads());
        assertEquals(0, stats.getWrites());
        assertEquals(3734, rec11.getKey());
        assertEquals(4807, rec11.getValue());

        // already in buffer, bp not full
        rec12 = bp.getElem(1);
        assertEquals(2, stats.getHits());
        assertEquals(1, stats.getMisses());
        assertEquals(1, stats.getReads());
        assertEquals(0, stats.getWrites());
        assertEquals(24210, rec12.getKey());
        assertEquals(16923, rec12.getValue());

        // read from disk, bp not full
        rec2 = bp.getElem(1024);
        bp.setElem(1034, rec0); // toggle dirty bit
        assertEquals(3, stats.getHits());
        assertEquals(2, stats.getMisses());
        assertEquals(2, stats.getReads());
        assertEquals(0, stats.getWrites());
        assertEquals(23189, rec2.getKey());
        assertEquals(1559, rec2.getValue());

        // read from disk, bp full
        rec3 = bp.getElem(2048);
        bp.setElem(2058, rec0); // toggle dirty bit
        assertEquals(4, stats.getHits());
        assertEquals(3, stats.getMisses());
        assertEquals(3, stats.getReads());
        assertEquals(1, stats.getWrites());
        assertEquals(7077, rec3.getKey());
        assertEquals(17306, rec3.getValue());

        // check LRU, see if buffer 1 was replaced
        rec11 = bp.getElem(2);
        assertEquals(4, stats.getHits());
        assertEquals(4, stats.getMisses());
        assertEquals(4, stats.getReads());
        assertEquals(2, stats.getWrites());
        assertEquals(19128, rec11.getKey());
        assertEquals(10424, rec11.getValue());

        // check LRU again, see if buffer 3 was replaced
        rec2 = bp.getElem(1025);
        assertEquals(4, stats.getHits());
        assertEquals(5, stats.getMisses());
        assertEquals(5, stats.getReads());
        assertEquals(3, stats.getWrites());
        assertEquals(3665, rec2.getKey());
        assertEquals(29401, rec2.getValue());

        // nothing changed in buffers, not need to write
        rec3 = bp.getElem(2049);
        assertEquals(4, stats.getHits());
        assertEquals(6, stats.getMisses());
        assertEquals(6, stats.getReads());
        assertEquals(3, stats.getWrites());

        // force IOException to check conversion
        raf.close();
        try
        {
            rec3 = bp.getElem(0);
        }
        catch (RuntimeException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(
            "Unexpected error happened with the file",
            exception.getMessage());

        assertEquals(4, stats.getHits());
        assertEquals(7, stats.getMisses());
        assertEquals(6, stats.getReads());
        assertEquals(3, stats.getWrites());
    }


    // ----------------------------------------------------------
    /**
     * Tests clear() and swapElems()
     *
     * @throws IOException
     *             if the file cannot be closed
     */
    public void testClear()
        throws IOException
    {
        // clearing an empty bp
        bp.clear();
        assertEquals(0, stats.getHits());
        assertEquals(0, stats.getMisses());
        assertEquals(0, stats.getReads());
        assertEquals(0, stats.getWrites());

        // checking swap
        bp.swapElems(0, 1);
        rec11 = bp.getElem(0);
        rec12 = bp.getElem(1);
        assertEquals(3734, rec12.getKey());
        assertEquals(4807, rec12.getValue());
        assertEquals(24210, rec11.getKey());
        assertEquals(16923, rec11.getValue());
        // checking clear with dirty bit on
        bp.clear();
        assertEquals(5, stats.getHits());
        assertEquals(1, stats.getMisses());
        assertEquals(1, stats.getReads());
        assertEquals(1, stats.getWrites());

        // checking clear with dirty bit off
        rec2 = bp.getElem(1024);
        rec3 = bp.getElem(2048);
        bp.clear();
        assertEquals(5, stats.getHits());
        assertEquals(3, stats.getMisses());
        assertEquals(3, stats.getReads());
        assertEquals(1, stats.getWrites());

        // force IOException to check conversion
        rec2 = bp.getElem(1024);
        raf.close();
        try
        {
            bp.clear();
        }
        catch (RuntimeException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(
            "Unexpected error happened with the file",
            exception.getMessage());
    }


    // ----------------------------------------------------------
    /**
     * tests length()
     *
     * @throws IOException
     */
    public void testLength()
        throws IOException
    {
        assertEquals(3072, bp.length());

        raf.close();
        try
        {
            assertEquals(3072, bp.length());
        }
        catch (RuntimeException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(
            "Unexpected error happened with the file",
            exception.getMessage());
    }
}

