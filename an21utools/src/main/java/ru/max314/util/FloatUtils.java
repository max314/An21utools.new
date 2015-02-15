package ru.max314.util;

/**
 * Created by max on 18.12.2014.
 */
public class FloatUtils {

    /**
     * Compare float with delta for delta.
     */
    public static int compareFloats(float f1, float f2, float delta)
    {
        if (Math.abs(f1 - f2) < delta)
        {
            return 0;
        } else
        {
            if (f1 < f2)
            {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * Uses <code>0.001f</code> for delta.
     */
    public static int compareFloats(float f1, float f2)
    {
        return compareFloats(f1, f2, 0.001f);
    }

    /**
     * Compare float with delta for delta.
     */
    public static int compareDouble(double f1, double f2, double delta)
    {
        if (Math.abs(f1 - f2) < delta)
        {
            return 0;
        } else
        {
            if (f1 < f2)
            {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * Uses <code>0.001f</code> for delta.
     */
    public static int compareDouble(double f1, double f2)
    {
        return compareDouble(f1, f2, 0.001);
    }

}
