package ru.max314.an21utools.gps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ru.max314.an21utools.App;
import ru.max314.an21utools.GpsAlertDialog;
import ru.max314.an21utools.util.GPSUtils;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.threads.TimerHelper;

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
public class GPSProcessing {
    private static LogHelper Log = new LogHelper(GPSProcessing.class);
    private LocationManager locationManager = null;
    private LocationListener defaultLocListiner = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setLastLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private LocationListener controlLocListiner = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };


    private GpsStatus.Listener defaultGpsListiner = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.d("GPS статус - GPS_EVENT_STARTED");
                    run();
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    switch (state) {
                        case START:
                            setState(GPSState.FIX);
                    }
                    ;
                    lastFixLocationTime = SystemClock.elapsedRealtime();
//                    Log.d("GPS статус - GPS_EVENT_FIRST_FIX");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    clean();
                    setState(GPSState.STOPED);
                    Log.d("GPS статус - GPS_EVENT_STOPPED");
                    break;
            }
        }
    };

    /**
     * интервал с которым будет запрашиваться контрольная фиксация
     */
    private final long controlFixInerval = TimeUnit.MILLISECONDS.convert(20, TimeUnit.SECONDS);

    private final Handler controlGPSRunner = new Handler();


    /**
     * Состояние класса
     */
    private GPSState state = GPSState.CREATED;
    /**
     * Последний раз когда нам сообщилои местоположение
     */
    private Long lastLocationTime;
    /**
     * Последний раз когда была фиксация местоположения
     */
    private Long lastFixLocationTime;
    /**
     * Последний раз когда запросил местоположение
     */
    private Long lastControlFixRequestTime;

    /**
     * Последний раз когда запросил местоположение
     */
    private Long lastRunTime;

    private ControlFixRunner controlFixRunner = new ControlFixRunner();

    private SelfCheckRunner selfCheckRunner = new SelfCheckRunner();

    /**
     * последнне местоположение
     */
    private Location lastLocation;

    public GPSProcessing(LocationManager locationManager) {
        this.locationManager = locationManager;
    }


    private void setLastLocation(Location location) {
        if (location == null)
            return;
        switch (state) {
            case FIX:
                setState(GPSState.WORK);
        }
        ;
        lastLocationTime = SystemClock.elapsedRealtime();
        lastLocation = location;
    }

    private void setState(GPSState a_state) {
        if (a_state == state)
            return;
        Log.d("State has changed: " + a_state);
        state = a_state;
    }


    /**
     * Запустить нас на выполнение
     */
    public void run() {
        Log.d("run() enter");
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled)
            return;
        switch (state) {
            case CREATED:
            case STOPED:
            case CLEAN:
                Log.d("run() setState");
                setState(GPSState.START);
                lastRunTime = SystemClock.elapsedRealtime();
                locationManager.removeUpdates(defaultLocListiner);
                locationManager.removeGpsStatusListener(defaultGpsListiner);
                controlGPSRunner.removeCallbacks(controlFixRunner);
                controlGPSRunner.removeCallbacks(selfCheckRunner);
                Log.d("run() remove All CallBack");

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, defaultLocListiner);
                locationManager.addGpsStatusListener(defaultGpsListiner);
                controlGPSRunner.postDelayed(controlFixRunner, TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
                controlGPSRunner.postDelayed(selfCheckRunner, TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
                Log.d("run() setup All CallBack");
                break;
            default:
        }
        Log.d("run() leave");
    }

    private void selfCheck() {
        switch (state) {
            case WORK_CONTROL:
                if (TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS) < Math.abs(lastLocationTime - SystemClock.elapsedRealtime())) {
                    // 10 секунд без координат
                    reportProblem("В течении более 10 секунд не было полученно местоположение");
                }
                if (controlFixInerval > Math.abs(lastControlFixRequestTime - lastFixLocationTime)) {
                    // жопа не проходит фиксация
                    reportProblem("В течении полуминуты не было фиксации GPS");
                }
                return;
            case STOPED:
                return;
            case CLEAN:
                return;
            case CREATED:
                return;
            case START:
            case WORK:
                // особая проверка в течении 5 минут после старта не перешли в нормальное состояние
                if (TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES) < Math.abs(lastRunTime - SystemClock.elapsedRealtime())) {
                    reportProblem("В течении 5 минут не перешли в рабочее состояние");
                }
                break;
        }

    }

    private void reportProblem(String s) {
        Log.d("reportProblem: " + s);
        if (App.getInstance().getModel().isGpsAutoClear()) {
            clean();
            GPSUtils.clearAGPS(App.getInstance(), true);
            run();
        } else {
            try {
                // старт активити
                //ошибко щоб долго не искать
                Intent intent = new Intent(App.getInstance(), GpsAlertDialog.class);
                intent.putExtra(GPSActivityConst.GPS_ACTIVITY_ACTION_START_MESSAGE, s);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getInstance().startActivity(intent);
            } catch (Exception e) {
                Log.e("reportProblem() error run activity", e);
            }
        }
    }

    private void clean() {
        Log.d("clean() enter");
        setState(GPSState.CLEAN);
        locationManager.removeGpsStatusListener(defaultGpsListiner);
        controlGPSRunner.removeCallbacks(controlFixRunner);
        controlGPSRunner.removeCallbacks(selfCheckRunner);
        Log.d("clean() leave");
    }

    public void stop() {
        clean();
        setState(GPSState.CREATED);
        locationManager.removeUpdates(defaultLocListiner);
    }

    private class ControlFixRunner implements Runnable {
        @Override
        public void run() {
            switch (state) {
                case WORK:
                case WORK_CONTROL:
                    // Запрашиваем обновления и отписываемся сразу нас интересует фикс который прийдеть в другого слушателя
                    setState(GPSState.WORK_CONTROL);
                    lastControlFixRequestTime = SystemClock.elapsedRealtime();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, controlLocListiner);
                    locationManager.removeUpdates(controlLocListiner);
                    break;
            }
            controlGPSRunner.postDelayed(this, controlFixInerval);
        }
    }

    private class SelfCheckRunner implements Runnable {
        @Override
        public void run() {
            selfCheck();
            controlGPSRunner.postDelayed(this, TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));
        }
    }
}
