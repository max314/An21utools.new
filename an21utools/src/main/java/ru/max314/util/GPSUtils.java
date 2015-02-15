package ru.max314.util;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by max on 25.01.2015.
 */
public class GPSUtils {
    static LogHelper Log = new LogHelper(GPSUtils.class);

    /**
     * сброс данных FGPS
     *
     * @param context
     */
    public static void clearAGPS(Context context, boolean showToast) {
        try {
            Log.i("clearAGPS - start");
            ((LocationManager) context.getSystemService(context.LOCATION_SERVICE)).sendExtraCommand("gps", "delete_aiding_data", null);
            if (showToast)
                Toast.makeText(context, "AGPS запрос на сброс данных", Toast.LENGTH_LONG).show();
            Log.i("clearAGPS - end");
            return;
        } catch (Exception exception) {
            Log.e("clear AGPS", exception);
        }

    }

    /**
     * Загрузка данных AGPS
     *
     * @param context
     */
    public static void loadAGPS(Context context, boolean showToast) {
        try {
            Log.i("loadAGPS - start");
            LocationManager locationmanager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            Bundle bundle = new Bundle();
            locationmanager.sendExtraCommand("gps", "force_xtra_injection", bundle);
            locationmanager.sendExtraCommand("gps", "force_time_injection", bundle);
            if (showToast)
                Toast.makeText(context, "AGPS запрос на обновление данных", Toast.LENGTH_LONG).show();
            Log.i("loadAGPS - end");
            return;
        } catch (Exception exception) {
            Log.e("clear AGPS", exception);
        }
    }
}
