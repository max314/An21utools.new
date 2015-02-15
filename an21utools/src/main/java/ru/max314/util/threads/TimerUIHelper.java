package ru.max314.util.threads;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import ru.max314.util.LogHelper;

/**
 * Created by max on 21.12.2014.
 */
public class TimerUIHelper {
    protected static LogHelper Log = new LogHelper(TimerUIHelper.class);


    private final Timer timer;
    private final Handler handler;

    public TimerUIHelper(long delayMillisec, final Runnable runnable){
        timer = new Timer(); // таймер
        handler = new Handler(); // захват вызывающего потока
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // а тута ничего не делаем ждем тика
                handler.post(runnable); // ушли в маин поток
            }
        },0 ,delayMillisec);
    }

    public void cancel(){
        timer.cancel();
    }
}
