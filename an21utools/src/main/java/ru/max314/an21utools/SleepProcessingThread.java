package ru.max314.an21utools;

import android.os.Handler;
import android.os.Looper;

import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SpeechUtils;
import ru.max314.an21utools.util.tw.TWSleeper;

/**
 * Created by max on 15.02.2015.
 */
public class SleepProcessingThread extends Thread {

    static LogHelper Log = new LogHelper(SleepProcessingThread.class);
    private Handler handler;


    public SleepProcessingThread() {
        super("SleepProcessingThread");
    }


    @Override
    public void run() {
        try {
            Log.d("run");
            SpeechUtils.speech("Запуск потока отслеживание сна", false);
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
    public void tryStop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                down();
                Looper.myLooper().quit();
            }
        });
    }

    private TWSleeper twSleeper;
    private void up(){
        if (twSleeper!=null)
            return;
        twSleeper = new TWSleeper();

    }
    private void down(){
        if (twSleeper!=null){
            twSleeper.end();
            twSleeper = null;
        }
    }


}
