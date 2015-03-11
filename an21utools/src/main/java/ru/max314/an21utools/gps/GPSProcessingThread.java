package ru.max314.an21utools.gps;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import ru.max314.an21utools.App;
import ru.max314.an21utools.SleepProcessingThread;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SpeechUtils;
import ru.max314.an21utools.util.tw.TWSleeper;

/**
 * Created by max on 04.03.2015.
 */
public class GPSProcessingThread extends Thread {
    static LogHelper Log = new LogHelper(GPSProcessingThread.class);
    private Handler handler;

    public GPSProcessingThread() {
        super("GPSProcessingThread");
    }

    @Override
    public void run() {
        try {
            Log.d("run");
            Looper.prepare();
            handler = new Handler();
            up();
            Looper.loop();
        } catch (Exception e) {
            Log.e("LoopingThread", e);
        }
    }

    /**
     * Остановить
     */
    public synchronized void tryStop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                down();
                Looper.myLooper().quit();
            }
        });
    }

    private GPSLogProcessing gpsProcessing;

    private synchronized void up() {
        if (gpsProcessing != null)
            return;
        gpsProcessing = new GPSLogProcessing((android.location.LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE));
    }

    private synchronized void down() {
        if (gpsProcessing != null) {
            gpsProcessing.stop();
            gpsProcessing = null;
        }
    }
}

