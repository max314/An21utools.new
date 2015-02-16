package ru.max314.an21utools.util.threads;

import android.os.Handler;
import android.os.Looper;

import ru.max314.an21utools.util.LogHelper;

/**
 * Безконечный выполнятель
 * Created by max on 21.12.2014.
 */
public class LoopingThread extends Thread {
    protected static LogHelper Log = new LogHelper(TimerHelper.class);
    private Handler handler;

    public LoopingThread() {
        super();
        start();
    }

    @Override
    public void run() {
        try {
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
        } catch (Exception e) {
            Log.e("Error ", e);
        }
    }

    public Handler getHandler() {
        return handler;
    }
}

