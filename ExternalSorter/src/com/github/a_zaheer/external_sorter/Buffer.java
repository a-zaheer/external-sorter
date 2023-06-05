package com.github.a_zaheer.external_sorter;
import java.io.IOException;
import java.io.RandomAccessFile;

// -------------------------------------------------------------------------
/**
 * Implements a buffer, which reads and writes from a file. The buffer holds
 * blocks of data at a time, reducing disk reads and writes.
 *
 * @author Ali Zaheer
 * @version Oct 18, 2022
 */
public class Buffer
{
    // ~ Fields ................................................................
    private long             blockOffset;
    private final int        capacity;
    private boolean          isDirty;
    private byte[]           byteData;
    private Record[]         records;

    /**
     * Default capacity of buffer in bytes.
     */
    public static final int  BUFFER_CAPACITY = 4096;

    private static final int EMPTY_OFFSET    = -1;

    // ----------------------------------------------------------
    /**
     * Create a new Buffer object.
     */
    public Buffer()
    {
        capacity = BUFFER_CAPACITY;
        isDirty = false;
        blockOffset = EMPTY_OFFSET;
        byteData = new byte[capacity];
        records = null;
    }

    // ~Public Methods ........................................................


    // ----------------------------------------------------------
    /**
     * Read a block of data from the file.
     *
     * @param raf
     *            the file
     * @return the filled buffer
     * @throws IOException
     *             if an error happens when reading file
     */
    public Buffer fill(RandomAccessFile raf)
        throws IOException
    {
        blockOffset = raf.getFilePointer();

        raf.read(byteData);

        records = Record.toRecArray(byteData);

        return this;
    }


    // ----------------------------------------------------------
    /**
     * Writes the buffer's data back to its original location in the file. It
     * overwrites the file if any changes were made to the buffer's data. Buffer
     * is empty after flush.
     *
     * @param raf
     *            the file
     * @return if the file was overwritten
     * @throws IOException
     *             if an error occurs while writing to the file
     */
    public boolean flush(RandomAccessFile raf)
        throws IOException
    {
        boolean overwritten = false;
        if (blockOffset == EMPTY_OFFSET)
            throw new IllegalStateException(
                "Error writing empty buffer to file");

        if (isDirty)
        {
            raf.seek(blockOffset);
            raf.write(byteData);
            overwritten = true;
        }
        this.flush();
        return overwritten;
    }


    // ----------------------------------------------------------
    /**
     * Clears the buffer without writing to anything.
     */
    public void flush()
    {
        isDirty = false;
        blockOffset = EMPTY_OFFSET;
        byteData = new byte[capacity];
        records = null;
    }


    // ----------------------------------------------------------
    /**
     * Get the record located at the "pos" position relative to the buffer.
     *
     * @param pos
     *            record's index
     * @return the record
     */
    public Record getRecord(int pos)
    {
        if (pos < 0 || pos >= capacity)
            throw new IllegalArgumentException("Position must be within range");

        if (blockOffset == EMPTY_OFFSET)
            throw new IllegalStateException("Reading from an empty buffer");

        return records[pos];
    }


    // ----------------------------------------------------------
    /**
     * Changes the record located at the "pos" position relative to the buffer.
     *
     * @param pos
     *            old record's index
     * @param newRec
     *            the new record
     */
    public void setRecord(int pos, Record newRec)
    {
        if (pos < 0 || pos >= capacity)
            throw new IllegalArgumentException("Position must be within range");

        if (blockOffset == EMPTY_OFFSET)
            throw new IllegalStateException("Writing to an empty buffer");

        isDirty = true;
        records[pos].setTo(newRec);
    }


    // ----------------------------------------------------------
    /**
     * Gets the buffer's block position relative to the starting offset of the
     * file.
     *
     * @return byte offset
     */
    public long getOffset()
    {
        return blockOffset;
    }


    // ----------------------------------------------------------
    /**
     * Checks if the buffer has the bytes starting at the "start" offset. All
     * the specified bytes have to be located in the buffer.
     *
     * @param start
     *            first byte's offset relative to the file
     * @param byteCount
     *            number of sequential bytes looking for
     * @return true or false
     */
    public boolean hasBytes(long start, int byteCount)
    {
        if (blockOffset == EMPTY_OFFSET || byteCount < 1)
            return false;
        return ((blockOffset <= start)
            && (start + byteCount <= blockOffset + capacity));
    }


    // ----------------------------------------------------------
    /**
     * Checks if the buffer is empty.
     *
     * @return true or false
     */
    public boolean isEmpty()
    {
        return (this.getOffset() == EMPTY_OFFSET);
    }
}
