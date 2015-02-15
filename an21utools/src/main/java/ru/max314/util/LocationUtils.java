package ru.max314.util;

import android.location.Location;

/**
 * Created by max on 18.12.2014.
 */
public class LocationUtils {
    private static final double DELTA_LOCATION = 0.000001;

    /**
     * Loccation speed == 0 with delta <code>0.1</code>
     * @param location
     * @return
     */
    public static boolean isSpeedZerro(Location location){
        return (FloatUtils.compareFloats(location.getSpeed(), 0f, 0.1f)==0);
    }

    /**
     * compare location coordinate with delta <code>0.000001</code>
     * @param location1
     * @param location2
     * @return
     */
    public static boolean isEq(Location location1,Location location2){
        if (location1==null) return false;
        if (location2==null) return false;

        boolean flag = (FloatUtils.compareDouble(location1.getLatitude(),location2.getLatitude(),DELTA_LOCATION)==0) &&
                (FloatUtils.compareDouble(location1.getLongitude(),location2.getLongitude(),DELTA_LOCATION)==0);
        return flag;

    }



}
