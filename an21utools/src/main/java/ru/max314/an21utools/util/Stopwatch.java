package ru.max314.an21utools.util;

import java.text.DecimalFormat;

/**
 * Created by max on 25.11.2015.
 */
public class Stopwatch {
    private final long start;


    /**
     * Initializes a new stopwatch.
     */
    public Stopwatch() {
        start = System.nanoTime();
    }


    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in mili seconds) since the stopwatch was created
     */
    public double elapsedTime() {
        long now = System.nanoTime();
        // Nano 10-6
        return (now - start) / 1000.0;
    }
    public String elapsedTimeToString() {
        double epl = elapsedTime();
        DecimalFormat df = new DecimalFormat("###,000.000 msec");
        return df.format(epl);
    }
}
