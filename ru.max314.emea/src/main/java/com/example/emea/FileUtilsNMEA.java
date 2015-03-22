package com.example.emea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by max on 22.03.2015.
 */
public class FileUtilsNMEA {

    public static String getFileContents(File file)
            throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            return scanner.useDelimiter("\\Z").next();
        }
    }

    public static void processFileLines(File file, LineProcessor lineProcessor)
            throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\n");
            int lineNumber = 0;
            while (scanner.hasNext()) {
                lineProcessor.process(++lineNumber, scanner.next());
            }
        }
    }

    public interface LineProcessor {
        void process(int lineNumber, String lineContents);
    }
}
