package com.github.a_zaheer.external_sorter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the main method in HeapSort and helper methods
 *
 * @author Ali Zaheer
 * @version Oct 29, 2022
 */
public class HeapSortTest
    extends TestCase
{
    private final File copy     = new File("data\\working\\workingCopy50.bin");
    private final File original = new File("data\\working\\sampleBlock50.bin");
    private String[]   args;
    private String     out;
    private Path       pathSrc;
    private Path       pathDest;

    /**
     * Test the main method
     *
     * @throws IOException
     *             if an unexpected read/write error occurs
     */
    @SuppressWarnings("null")
    public void testMain()
        throws IOException
    {
        // if the number of args != 3
        args = new String[2];
        HeapSort.main(args);
        out = systemOut().getHistory();
        assertFuzzyEquals(
            "Usage: HeapSort <data-file-name> <num-buffers> <stat-file-name>",
            out);
        systemOut().clearHistory();

        // if number of buffers is not a number
        args = new String[3];
        args[0] = "data\\working\\sampleBlock3.bin";
        args[1] = "not_number";
        args[2] = "data\\working\\stats.txt";
        HeapSort.main(args);
        out = systemOut().getHistory();
        assertFuzzyEquals("Invalid number format for number of buffers", out);
        systemOut().clearHistory();

        // if number of buffers is less than 1
        args[1] = "0";
        HeapSort.main(args);
        out = systemOut().getHistory();
        assertFuzzyEquals("Number of buffers needs to be positive", out);
        systemOut().clearHistory();

        // file is not found
        args[0] = "data\\working\\file_not_exist";
        args[1] = "2";
        args[2] = "data\\working\\stats.txt";
        HeapSort.main(args);
        out = systemOut().getHistory();
        assertFuzzyEquals("File does not exist: data\\working\\file_not_exist", out);
        systemOut().clearHistory();

        // copy the data file
        pathSrc = Paths.get(original.getPath());
        pathDest = Paths.get(copy.getPath());

        Files.copy(pathSrc, pathDest, StandardCopyOption.REPLACE_EXISTING);

        // test output to console
        args[0] = copy.getPath();
        args[1] = "20";
        HeapSort.main(args);
        out = systemOut().getHistory();
        String[] lines = out.split("\n");
        assertEquals(7, lines.length);
        String[] records;
        for (int i = 0; i < lines.length - 1; i++)
        {
            records = lines[i].trim().split("\\s+");
            assertEquals(16, records.length); // key + value = 2
        }
        records = lines[lines.length - 1].split("\\s+");
        assertEquals(4, records.length);
        systemOut().clearHistory();

        // test output to stats file
        File statsFile = new File(args[2]);
        try (Scanner statsScan = new Scanner(statsFile))
        {
            String statsLine = null;
            while (statsScan.hasNextLine())
                statsLine = statsScan.nextLine();

            // last line is the time to sort
            assertNotNull(statsLine);
            String[] timeLine = statsLine.trim().split("\\s+");
            assertEquals(4, timeLine.length);
            long runTime = Long.valueOf(timeLine[3]);
            assertTrue(0 < runTime);
            assertTrue(runTime < 60000); // it takes less than a minute to sort
        }

        copy.deleteOnExit(); // delete the working copy
    }

}

