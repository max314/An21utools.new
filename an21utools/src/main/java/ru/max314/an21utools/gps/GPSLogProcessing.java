package ru.max314.an21utools.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ru.max314.an21utools.App;
import ru.max314.an21utools.GpsAlertDialog;
import ru.max314.an21utools.util.GPSUtils;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.tw.TWSleeper;

/**
 * Created by max on 04.03.2015.
 * Класс будет у нас следить за ЖПС
 * <p/>
 * алгоритм пока такой:
 * подписываемся на обновления местоположения
 * запоминаем каждое последнее местоположение
 * раз в 20 секунд требуем месположение на новый обработчик
 * при этом поджна пролететь фиксация GpsStatus.Listener(GPS_EVENT_FIRST_FIX:)
 * причем она даже прийдет на первый листенер тоесть тупой пинок менеджера
 */
public class GPSLogProcessing {
    private static LogHelper Log = new LogHelper(GPSLogProcessing.class);
    private LocationManager locationManager = null;
    private StringBuilder stringBuilder = new StringBuilder();

    private void appendLine(String str){
        stringBuilder.append(str+"\n");
    }


    private LocationListener defaultLocListiner = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            appendLine(location.toString());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            appendLine("onStatusChanged "+i);
        }

        @Override
        public void onProviderEnabled(String s) {
            appendLine("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String s) {
            appendLine("onProviderDisabled");
        }
    };


    private GpsStatus.Listener defaultGpsListiner = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.d("GPS статус - GPS_EVENT_STARTED");
                    appendLine("GPS статус - GPS_EVENT_STARTED");
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    appendLine("GPS статус - GPS_EVENT_FIRST_FIX");
                    Log.d("GPS статус - GPS_EVENT_FIRST_FIX");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    appendLine("GPS статус - GPS_EVENT_STOPPED");
                    Log.d("GPS статус - GPS_EVENT_STOPPED");
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    appendLine("GPS статус - GPS_EVENT_SATELLITE_STATUS");
                    break;
            }
        }
    };

    GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            stringBuilder.append(nmea);
        }
    };


    private FlushRunner flushRunner = new FlushRunner();

    private final Handler flushHandler = new Handler();
    BroadcastReceiver sleepReceiver;

    /**
     * последнне местоположение
     */
    private Location lastLocation;

    public GPSLogProcessing(LocationManager locationManager) {
        self = this;
        this.locationManager = locationManager;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, defaultLocListiner);
        locationManager.addGpsStatusListener(defaultGpsListiner);
        locationManager.addNmeaListener(nmeaListener);
        flushHandler.postDelayed(flushRunner, TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS));


        sleepReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    Log.d("sleepIntent action=" + action);
                    if (action.equals(TWSleeper.BRD_TAG_SLEEP)) {
                        Log.d("flush on sleep");
                        flush();
                    }
                } else {
                    Log.e("sleepIntent: intent is null");
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        // подписались на перезагрузку
        intentFilter.addAction(TWSleeper.BRD_TAG_SLEEP);
        intentFilter.addAction(TWSleeper.BRD_TAG_WAKEUP);
        App.getInstance().registerReceiver(sleepReceiver, intentFilter);


    }

    public void stop() {
        locationManager.removeUpdates(defaultLocListiner);
        locationManager.removeGpsStatusListener(defaultGpsListiner);
        locationManager.removeNmeaListener(nmeaListener);
        App.getInstance().unregisterReceiver(sleepReceiver);
    }

    public void flush() {
        final String buff = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String filename = df.format(new Date())+".llog";
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + MY_SDFOLDER);
                try {
                    if (!file.exists())
                        file.mkdir();
                }catch (Throwable e){
                    Log.e("Error ", e);
                }
                String fullFilename = Environment.getExternalStorageDirectory() + File.separator +  MY_SDFOLDER+ File.separator +  filename;
                try {
                    FileWriter out = new FileWriter(fullFilename);
                    out.write(buff);
                    out.close();
                } catch (IOException e) {
                    Log.e("Error write string as file", e);
                }

            }
        }).run();
    }
    private final String MY_SDFOLDER = "ru.max314";



    private class FlushRunner implements Runnable {

        @Override
        public void run() {
            flush();
        }

    }

    //region никогда так не делать в рабочем приложеннии
    private static GPSLogProcessing self;

    public static GPSLogProcessing getSelf(){return self;}
    //endregion

}
