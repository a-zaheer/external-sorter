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
 * Tests Buffer
 *
 * @author Ali Zaheer
 * @version Oct 28, 2022
 */
public class BufferTest
    extends TestCase
{
    // ~ Fields ................................................................
    private final File       copy     = new File("data\\working\\BCopy3.bin");
    private final File       original = new File("data\\working\\sampleBlock3.bin");
    private Path             pathSrc;
    private Path             pathDest;

    private RandomAccessFile raf;
    private Buffer           buffer;
    private Record           expected;
    private Exception        exception;

    // ~ Constructors ..........................................................
    /**
     * sets up the test methods
     *
     * @throws IOException
     */
    public void setUp()
        throws IOException
    {
        pathSrc = Paths.get(original.getPath());
        pathDest = Paths.get(copy.getPath());

        Files.copy(pathSrc, pathDest, StandardCopyOption.REPLACE_EXISTING);

        raf = new RandomAccessFile(copy, "rw");
        buffer = new Buffer();
        exception = null;
        expected = new Record(24210, 16923); // 2nd record, located in first
                                             // block at byte position 4

        copy.deleteOnExit();
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * Tests the methods when a buffer is empty
     *
     * @throws IOException
     *             if error occurs when reading or writing
     */
    public void testEmptyBuffer()
        throws IOException
    {
        assertTrue(buffer.isEmpty());
        try
        {
            buffer.getRecord(0);
        }
        catch (IllegalStateException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Reading from an empty buffer", exception.getMessage());
        exception = null;

        try
        {
            buffer.setRecord(0, null); // record can be whatever
        }
        catch (IllegalStateException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Writing to an empty buffer", exception.getMessage());
        exception = null;

        try
        {
            buffer.flush(raf);
        }
        catch (IllegalStateException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(
            "Error writing empty buffer to file",
            exception.getMessage());

        assertFalse(buffer.hasBytes(raf.getFilePointer(), 1));
        raf.close();
    }


    // ----------------------------------------------------------
    /**
     * tests fill(), getRecord() and hasBytes()
     *
     * @throws IOException
     *             if an error occurs while reading/writing
     */
    public void testFill()
        throws IOException
    {
        long fileStart = raf.getFilePointer();
        buffer.fill(raf);
        assertFalse(buffer.isEmpty());
        assertEquals(fileStart, buffer.getOffset());

        try
        {
            buffer.getRecord(-1);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Position must be within range", exception.getMessage());
        exception = null;

        try
        {
            buffer.getRecord(Buffer.BUFFER_CAPACITY);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Position must be within range", exception.getMessage());

        Record actual = buffer.getRecord(1);
        assertEquals(0, expected.compareTo(actual));

        // test hasByte()
        assertFalse(buffer.hasBytes(fileStart - 1, 1));
        assertFalse(buffer.hasBytes(fileStart, Buffer.BUFFER_CAPACITY + 1));
        assertFalse(buffer.hasBytes(fileStart, 0));
        assertTrue(buffer.hasBytes(fileStart, Buffer.BUFFER_CAPACITY));

        raf.close();
    }


    // ----------------------------------------------------------
    /**
     * tests flush() methods and setRecord()
     *
     * @throws IOException
     *             if an error occurs while reading/writing
     */
    public void testFlush()
        throws IOException
    {
        try
        {
            buffer.setRecord(-1, null);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Position must be within range", exception.getMessage());
        exception = null;

        try
        {
            buffer.setRecord(Buffer.BUFFER_CAPACITY, null);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Position must be within range", exception.getMessage());

        // simple flush
        long fileStart = raf.getFilePointer();
        buffer.fill(raf);
        buffer.flush();
        assertTrue(buffer.isEmpty());

        // flush to file with no changes
        raf.seek(fileStart);
        buffer.fill(raf);
        raf.seek(fileStart); // tests if anything is written
        assertFalse(buffer.flush(raf));
        assertTrue(buffer.isEmpty());
        assertEquals(fileStart, raf.getFilePointer()); // nothing written

        // flush to file with overwrite
        Record newRec = new Record(0, 0);
        buffer.fill(raf); // already be at file start
        raf.seek(fileStart);
        buffer.setRecord(1, newRec);
        assertTrue(buffer.flush(raf));
        assertTrue(buffer.isEmpty());
        assertEquals(fileStart + Buffer.BUFFER_CAPACITY, raf.getFilePointer());

        // check that the file was overwritten correctly
        raf.seek(fileStart);
        buffer.fill(raf);
        Record actual = buffer.getRecord(1);
        assertEquals(0, newRec.compareTo(actual));

        raf.close();
    }
}

