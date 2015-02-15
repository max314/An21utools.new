package ru.max314.util.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ru.max314.util.LogHelper;

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

