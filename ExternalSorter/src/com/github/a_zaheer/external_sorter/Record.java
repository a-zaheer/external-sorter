package com.github.a_zaheer.external_sorter;
import java.nio.ByteBuffer;

// -------------------------------------------------------------------------
/**
 * Represents the objects stored in the file. Consists of a key and a value,
 * stored by the underlying byte data.
 *
 * @author Ali Zaheer
 * @version Oct 25, 2022
 */
public class Record
    implements Comparable<Record>
{

    /**
     * size of record in bytes
     */
    public static final int SIZE_IN_BYTES    = 4;
    /**
     * key's location in byte array
     */
    public static final int BYTE_INDEX_KEY   = 0;
    /**
     * values location in byte array
     */
    public static final int BYTE_INDEX_VALUE = 2;
    /**
     * maximum key
     */
    public static final int KEY_MAXIMUM      = 30000;

    // This tiny ByteBuffer holds both the key and value as bytes
    private ByteBuffer      bb;

    // ----------------------------------------------------------
    /**
     * Makes a record and its backing ByteBuffer.
     *
     * @param key
     *            new record's key
     * @param val
     *            new record's value
     */
    public Record(short key, short val)
    {
        bb = ByteBuffer.allocate(SIZE_IN_BYTES);
        bb.putShort(BYTE_INDEX_KEY, key);
        bb.putShort(BYTE_INDEX_VALUE, val);
    }


    // ----------------------------------------------------------
    /**
     * Create a new Record object from integers by casting to shorts.
     *
     * @param key
     *            the key
     * @param val
     *            the value
     */
    public Record(int key, int val)
    {
        this((short)key, (short)val);
    }


    // ----------------------------------------------------------
    /**
     * Create a new Record object from a existing byte array. Does NOT copy but
     * refers
     *
     * @param bytes
     *            the byte array
     */
    public Record(byte[] bytes)
    {
        bb = ByteBuffer.wrap(bytes);
    }


    // Constructs using a given byte buffer. Does NOT copy but refers
    private Record(ByteBuffer bb)
    {
        this.bb = bb;
    }


    // ----------------------------------------------------------
    /**
     * Get the record's key
     *
     * @return the key
     */
    public short getKey()
    {
        return bb.getShort(BYTE_INDEX_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Get the record's value
     *
     * @return the value
     */
    public short getValue()
    {
        return bb.getShort(BYTE_INDEX_VALUE);
    }


    // ----------------------------------------------------------
    /**
     * Makes a whole array of records that are backed by the given byte array.
     * Changing the array will change records and vice versa!
     *
     * @param binaryData
     *            source byte array
     * @return the array of records
     */
    public static Record[] toRecArray(byte[] binaryData)
    {
        int numRecs = binaryData.length / SIZE_IN_BYTES;
        Record[] recs = new Record[numRecs];
        for (int i = 0; i < recs.length; i++)
        {
            int byteOffset = i * SIZE_IN_BYTES;
            ByteBuffer bb =
                ByteBuffer.wrap(binaryData, byteOffset, SIZE_IN_BYTES);
            recs[i] = new Record(bb.slice());
        }
        return recs;
    }


    // ----------------------------------------------------------
    /**
     * Copies the contents of another record. This is a DEEP copy.
     *
     * @param other
     *            the other record
     */
    public void setTo(Record other)
    {
        bb.putShort(BYTE_INDEX_KEY, other.getKey());
        bb.putShort(BYTE_INDEX_VALUE, other.getValue());
    }


    /**
     * Compares this record's keys to another record's keys.
     *
     * @param o
     *            other record
     * @return Negative if less than, positive if greater than, and zero if
     *         equal
     */
    @Override
    public int compareTo(Record o)
    {
        return Short.compare(this.getKey(), o.getKey());
    }


    /**
     * Prints record as "Record: ("KEY, VALUE")"
     *
     * @return the string representation
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Record: (");
        sb.append(this.getKey());
        sb.append(", ");
        sb.append(this.getValue());
        sb.append(")");
        return sb.toString();
    }

}

