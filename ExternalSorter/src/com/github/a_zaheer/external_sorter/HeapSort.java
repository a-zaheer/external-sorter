package com.github.a_zaheer.external_sorter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

// Virginia Tech Honor Code Pledge:
//
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of those who
// do.
// -- azaheer

/**
 * Main class used to run the HeapSort program. Reads command line inputs for
 * the source file name and other parameters.
 *
 * @author Ali Zaheer
 * @version Oct 27, 2022
 */
public class HeapSort
{

    private static final int RECORDS_PER_LINE  = 8;
    private static final int RECORDS_PER_BLOCK =
        Buffer.BUFFER_CAPACITY / Record.SIZE_IN_BYTES;

    /**
     * This is the entry point of the application
     *
     * @param args
     *            Command line arguments
     * @throws IOException
     *             if an unexpected read/write error happens
     */
    public static void main(String[] args)
        throws IOException
    {

        if (args.length != 3)
        {
            System.out.println(
                "Usage: HeapSort <data-file-name> <num-buffers>"
                    + " <stat-file-name>");
            return;
        }

        File byteFile = new File(args[0].trim());
        if (!byteFile.exists())
        {
            System.out.println("File does not exist: " + byteFile);
            return;
        }

        int maxBuff;
        try
        {
            maxBuff = Integer.valueOf(args[1].trim());
            if (maxBuff < 1)
            {
                System.out.println("Number of buffers needs to be positive");
                return;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid number format for number of buffers");
            return;
        }

        File statFile = new File(args[2].trim());

        try (
            RandomAccessFile byteRaf = new RandomAccessFile(byteFile, "rw");
            FileWriter statTxt = new FileWriter(statFile, true))
        {
            IOStats stats = new IOStats();
            FileBufferPool buffPool =
                new FileBufferPool(byteRaf, maxBuff, stats);
            int recordCount = (int)(byteRaf.length() / Record.SIZE_IN_BYTES);

            HeapSorter<Record> sorter =
                new HeapSorter<Record>(buffPool, recordCount);

            long timeStart = System.currentTimeMillis();
            sorter.sort();
            long timeEnd = System.currentTimeMillis();

            printToConsole(buffPool);
            buffPool.clear();

            writeStats(statTxt, byteFile.getPath(), stats, timeEnd - timeStart);
        }

    }


    private static void printToConsole(BufferPool<Record> bp)
    {
        Record temp;
        for (int i = 0, j = 1; i < bp.length(); i += RECORDS_PER_BLOCK, j++)
        {
            temp = bp.getElem(i);
            // maximum key has 5 digits so format that way
            System.out.printf("%5d %5d ", temp.getKey(), temp.getValue());
            if (j % RECORDS_PER_LINE == 0)
                System.out.println();
        }
        System.out.println();
    }


    private static
        void
        writeStats(FileWriter writer, String fileName, IOStats stats, long time)
            throws IOException
    {
        StringBuilder builder = new StringBuilder();
        builder.append("------  STATS ------\n");

        builder.append("File name: ");
        builder.append(fileName);
        builder.append("\n");

        builder.append("Cache Hits: ");
        builder.append(stats.getHits());
        builder.append("\n");

        builder.append("Cache Misses: ");
        builder.append(stats.getMisses());
        builder.append("\n");

        builder.append("Disk reads: ");
        builder.append(stats.getReads());
        builder.append("\n");

        builder.append("Disk Writes: ");
        builder.append(stats.getWrites());
        builder.append("\n");

        builder.append("Time to sort: ");
        builder.append(time);
        builder.append("\n");

        writer.append(builder.toString());

    }

}

