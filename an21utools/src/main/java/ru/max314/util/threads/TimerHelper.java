package ru.max314.util.threads;


import java.util.Timer;
import java.util.TimerTask;

import ru.max314.util.LogHelper;

/**
 * Created by max on 16.12.2014.
 */
public class TimerHelper {
    protected static LogHelper Log = new LogHelper(TimerHelper.class);
    Runnable task;
    long period;
    long delay;
    Timer timer = null;
    String name;

    public TimerHelper(String name,long delay, long period, Runnable task) {
        this.name = name;
        this.task = task;
        this.period = period;
        this.delay = delay;
    }

    public void start(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer(name);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.d(name + ": execute");
                    task.run();
                } catch (Throwable e) {
                    Log.e(name + ": error execute",e);
                }
            }
        }, delay, period);
    }

    public void stop(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }

}
