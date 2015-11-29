package ru.max314.an21utools;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ru.max314.an21utools.Http.HTTPServer;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SpeechUtils;
import ru.max314.an21utools.util.TorqueHelper;

/**
 * Created by max on 17.02.2015.
 */
public class TorgueListinerThread extends Thread {
    static LogHelper Log = new LogHelper(TorgueListinerThread.class);
    private Handler handler;


    public TorgueListinerThread() {

        super("TorgueListinerThread");
        torqueHelper = new TorqueHelper(App.getInstance());
        httpServer = new HTTPServer(App.getInstance());

        exec = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void run() {
        try {
            Log.d("run");
            SpeechUtils.speech("Запуск потока Torgue", false);
            Looper.prepare();
            handler = new Handler();
            up();

            exec.scheduleWithFixedDelay(new refresher(), 100, 500, TimeUnit.MILLISECONDS);
            Looper.loop();

        } catch (Exception e) {
            Log.e("LoopingThread", e);
        }
    }
   public class refresher implements Runnable{
           @Override
           public void run() {
               torqueHelper.refresh();
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

    TorqueHelper torqueHelper = null;
    HTTPServer httpServer ;
    ScheduledThreadPoolExecutor exec;

    private synchronized void up(){
        torqueHelper.start();
        try {
            httpServer.start();
        } catch (IOException e) {
            Log.e("error http ",e);
        }

    }
    private synchronized void down(){
        exec.shutdown();
        torqueHelper.stop();
        httpServer.stop();
    }


}
