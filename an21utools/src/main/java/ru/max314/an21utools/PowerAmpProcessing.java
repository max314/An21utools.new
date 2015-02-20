package ru.max314.an21utools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.maxmpz.poweramp.player.PowerampAPI;

import ru.max314.an21utools.model.ModelFactory;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.tw.TWSleeper;

/**
 * Класс для слеженеия управления powerAmnp
 */
public class PowerAmpProcessing {
    private static LogHelper Log = new LogHelper(PowerAmpProcessing.class);
    /*
        флаг того что мы получали обновления от повер ампа
     */
    private boolean resiveStatusIntent = false;
    /*
      флаг того что ровер амп сейчас чтото воспроизводит
     */
    private boolean powerampPlaying = false;

    /**
     * флаг того что нужно при выходе из сна начать играть
     */
    private boolean needPlayOnWakeUp = false;

    /**
     * ресивер для поверампа
     */
    private BroadcastReceiver powerampReceiver;

    /**
     * ресивер для слипа
     */
    private BroadcastReceiver sleepReceiver;

    /**
     * Захватить контекст
     */
    private Context context;


    public PowerAmpProcessing(Context context) {
        Log.d("PowerAmpProcessing() ctor enter");
        this.context = context;


        powerampReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    resiveStatusIntent = true;
                    int status = intent.getIntExtra(PowerampAPI.STATUS, -1);
                    boolean paused = intent.getBooleanExtra(PowerampAPI.PAUSED, false);
                    boolean failed = intent.getBooleanExtra(PowerampAPI.FAILED, false);
                    powerampPlaying = ((status == PowerampAPI.Status.TRACK_PLAYING) && (!paused));
                    Log.d("statusIntent status=" + status + " paused=" + paused + " failed=" + failed + " poweramp playing=" + powerampPlaying);
                } else {
                    Log.e("statusIntent: intent is null");
                }
            }
        };

        sleepReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    Log.d("sleepIntent action=" + action);
                    if (action.equals(TWSleeper.BRD_TAG_SLEEP)) {
                        doSleep(powerampPlaying);
                    } else if (action.equals(TWSleeper.BRD_TAG_WAKEUP)) {
                        doWakeUp();
                    }
                } else {
                    Log.e("sleepIntent: intent is null");
                }
            }
        };

        // подписались на поверамп
        context.registerReceiver(powerampReceiver, new IntentFilter(PowerampAPI.ACTION_STATUS_CHANGED));
        IntentFilter intentFilter = new IntentFilter();
        // подписались на перезагрузку
        intentFilter.addAction(TWSleeper.BRD_TAG_SLEEP);
        intentFilter.addAction(TWSleeper.BRD_TAG_WAKEUP);
        context.registerReceiver(sleepReceiver, intentFilter);
        Log.d("PowerAmpProcessing() ctor leave");
    }


    private void doSleep(boolean a_powerampPlaying) {
        // Определяемся что нам делать при следующем просыпании
        needPlayOnWakeUp = resiveStatusIntent// мы получили от поверампа нотификацию хотябы один раз
                && a_powerampPlaying; // и он сечас играет
        Log.d("doSleep(): needPlayOnWakeUp = " + needPlayOnWakeUp);

        if (needPlayOnWakeUp) {
            context.startService(new Intent(PowerampAPI.ACTION_API_COMMAND).putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.PAUSE)); // ставим на паузу
            Log.d("doSleep(): poweramp request to pause ");
        }
    }

    private void doWakeUp() {
        Log.d("doWakeUp(): needPlayOnWakeUp = " + needPlayOnWakeUp);
        if (needPlayOnWakeUp) {
            Log.d("doWakeUp(): poweramp delayead request to resume");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("doWakeUp(): poweramp request to resume ");
                    context.startService(new Intent(PowerampAPI.ACTION_API_COMMAND).putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.RESUME)); // ставим на плай
                }
            }, ModelFactory.getAutoRunModel().getPowerampResumeDelay());


        }

    }

    /**
     * Отписаться от всего и готовитьтся умереть под сборщиком мусора
     */
    public void down() {
        Log.d("PowerAmpProcessing() down()");
        context.unregisterReceiver(powerampReceiver);
        context.unregisterReceiver(sleepReceiver);
    }
}
