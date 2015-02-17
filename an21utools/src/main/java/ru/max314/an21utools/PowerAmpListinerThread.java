package ru.max314.an21utools;

import android.os.Handler;
import android.os.Looper;

import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SpeechUtils;
import ru.max314.an21utools.util.tw.TWSleeper;

/**
 * Created by max on 17.02.2015.
 */
public class PowerAmpListinerThread extends Thread {
    static LogHelper Log = new LogHelper(PowerAmpListinerThread.class);
    private Handler handler;


    public PowerAmpListinerThread() {
        super("PowerAmpListinerThread");
    }


    @Override
    public void run() {
        try {
            Log.d("run");
            SpeechUtils.speech("Запуск потока отслеживание PowerAmp", false);
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

    private PowerAmpProcessing powerAmpProcessing;

    private synchronized void up(){
        if (powerAmpProcessing!=null)
            return;
        powerAmpProcessing = new PowerAmpProcessing(App.getInstance());

    }
    private synchronized void down(){
        if (powerAmpProcessing!=null){
            powerAmpProcessing.down();
            powerAmpProcessing = null;
        }
    }


}
