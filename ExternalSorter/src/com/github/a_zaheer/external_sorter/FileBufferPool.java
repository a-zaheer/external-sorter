package com.github.a_zaheer.external_sorter;
import java.io.IOException;
import java.io.RandomAccessFile;

// -------------------------------------------------------------------------
/**
 * Implements a buffer pool for a RandomAccessFile (RAF). The buffer pool holds
 * a list of buffers, the number specified by the caller, that work with blocks
 * of data from the RAF. The buffers are maintained using an Least Recently Used
 * strategy. The IO statistics are logged in the IOStats object provided by the
 * caller. Assuming the RAF is formatted to consist of records, the buffer pool
 * allows the caller to access the file as one big array of records.
 *
 * @author Ali Zaheer
 * @version Oct 18, 2022
 */
public class FileBufferPool
    implements BufferPool<Record>
{
    // ~ Fields ................................................................
    private RandomAccessFile     raf;
    private PriorityList<Buffer> buffers;
    private final int            maxBuffers;
    private int                  activeBuffers;
    private IOStats              stats;

    // ----------------------------------------------------------
    /**
     * Create a new FileBufferPool object.
     *
     * @param file
     *            source RAF
     * @param maxBuff
     *            maximum number of buffers
     * @param stats
     *            IO statistics log
     */
    // ~ Constructors ..........................................................

    public FileBufferPool(RandomAccessFile file, int maxBuff, IOStats stats)
    {
        raf = file;
        buffers = new PriorityList<Buffer>();
        maxBuffers = maxBuff;
        activeBuffers = 0;

        for (int i = 0; i < maxBuffers; i++)
        {
            buffers.insert(new Buffer());
        }

        this.stats = stats;
    }


    // ~Public Methods ........................................................
    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public Record getElem(int pos)
    {

        Buffer buff = getBuffer(pos);
        int posInBuff = posInBuff(pos, buff.getOffset());

        return buff.getRecord(posInBuff);
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void setElem(int pos, Record newRec)
    {

        Buffer buff = getBuffer(pos);
        int posInBuff = posInBuff(pos, buff.getOffset());

        buff.setRecord(posInBuff, newRec);
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void swapElems(int a, int b)
    {
        Record recA = getElem(a);
        Record recB = getElem(b);
        Record temp = new Record(recB.getKey(), recB.getValue());

        setElem(b, recA);
        setElem(a, temp);
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        buffers.moveToStart();

        Buffer temp;
        for (int i = 0; i < activeBuffers; i++)
        {
            temp = buffers.getValue();
            try
            {
                raf.seek(temp.getOffset());
                if (temp.flush(raf))
                    stats.setWrites(stats.getWrites() + 1);
            }
            catch (IOException e)
            {
                throw handleIOE(e);
            }

            buffers.next();
        }
        activeBuffers = 0;
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public int length()
    {
        try
        {
            return (int)(raf.length() / Record.SIZE_IN_BYTES);
        }
        catch (IOException e)
        {
            throw handleIOE(e);
        }
    }


    /**
     * Given an index for a record, finds and returns the buffer that would
     * contain that record. If that record cannot be found amongst the buffers
     * currently in use, the method follows the LRU strategy to fill a new
     * buffer with the block containing the record.
     */
    private Buffer getBuffer(int recPos)
    {
        long recOffset = recPos * Record.SIZE_IN_BYTES;
        Buffer found;

        buffers.moveToStart();
        for (int i = 0; i < activeBuffers; i++)
        {
            if (buffers.getValue().hasBytes(recOffset, Record.SIZE_IN_BYTES))
            {
                found = buffers.getValue();
                stats.setHits(stats.getHits() + 1);
                buffers.prioritize();
                return found;
            }
            buffers.next();
        }
        stats.setMisses(stats.getMisses() + 1);

        long bufferStart =
            (recOffset / Buffer.BUFFER_CAPACITY) * Buffer.BUFFER_CAPACITY;
        found = insertBlock(bufferStart);

        return found;
    }


    /*
     * Fills a buffer with the block of bytes starting at requested position. If
     * all the buffers are being used, flush the least recently used, fill it
     * with the new block, and move that buffer to the top of the use order
     */
    private Buffer insertBlock(long blockStart)
    {
        try
        {
            Buffer buff;
            if (activeBuffers < maxBuffers)
            {
                buffers.moveToPos(activeBuffers); // add to active buffers
                buff = buffers.getValue();

                raf.seek(blockStart);
                buff.fill(raf);

                buffers.prioritize();
                activeBuffers++;
            }

            else
            {
                buffers.moveToPos(maxBuffers - 1); // LRU buffer
                buff = buffers.getValue();

                raf.seek(buff.getOffset());
                if (buff.flush(raf))
                    stats.setWrites(stats.getWrites() + 1);

                raf.seek(blockStart);
                buff.fill(raf);

                buffers.prioritize();
            }

            stats.setReads(stats.getReads() + 1);

            return buff;
        }
        catch (IOException e)
        {
            throw handleIOE(e);
        }
    }


    /**
     * Helper method that converts the index of the record to the record's
     * position relative to the buffer.
     */
    private int posInBuff(int recPos, long bufferOffset)
    {
        long recOffset = recPos * Record.SIZE_IN_BYTES;
        return (int)((recOffset - bufferOffset) / (Record.SIZE_IN_BYTES));
    }


    /**
     * Converts IOException(checked) to RuntimeException(unchecked).
     * IOExceptions are not expected to happen due to the project's starting
     * conditions, so there is no good, simple way to handle them.
     */
    private RuntimeException handleIOE(IOException e)
    {
        return new RuntimeException(
            "Unexpected error happened with the file",
            e);
    }

}

