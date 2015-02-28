package ru.max314.an21utools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;

import ru.max314.an21utools.model.AppModel;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SysUtils;
import ru.max314.an21utools.util.tw.TWUtilDecorator;

public class ControlService extends Service {
    private static LogHelper Log = new LogHelper(ControlService.class);
    public static final String CS_ACTION_STARTBOOT = "ru.max314.cs.startboot";
//    public static final String CS_ACTION_STOP = "ru.max314.cs.stop";
//    public static final String CS_ACTION_REFRESH = "ru.max314.cs.refresh";

    private static final int notif_id = 13;

    private SleepProcessingThread sleepProcessingThread;
    private PowerAmpListinerThread powerAmpListinerThread;
    private AppModel model;

    public ControlService() {
        Log.d("ControlService ctor");
        model = App.getInstance().getModel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ControlService onStartCommand action(" + intent.getAction() + ")");
        if (CS_ACTION_STARTBOOT.equals(intent.getAction())) {
            startFormBoot();
        } else {
            refresh();
        }
        return Service.START_STICKY;
    }

    private void stopme() {
        // stop threads
        stopSleep();
        stopPowerAmpThread();
        stopForeground(true);
    }

    private void startFormBoot() {
        startAutoRun();
        refresh();
    }

    private void startAutoRun() {
        Log.d("ControlService startAutoRun()");
        int notifyID = 14;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // ничего не делаем пока
        if (!model.isStarting()) {
            Log.d("отключен автоматический запуск");
            return;
        }
        if (model.getAppList().size() == 0) {
            Log.d("количество запускаемых приложений =0");
            return;
        }

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Автозапуск приложений")
                .setContentText("Ожидание запуска")
                .setSmallIcon(R.drawable.ic_terminal);
        Notification notification = null;

        try {
            // Первоначальный останов задержка

            notification = builder.build();
            notificationManager.notify(notifyID, notification);
            Thread.sleep(model.getStartDelay());
            // пошли по приложениям

            for (int i = 0; i < model.getAppList().size(); i++) {
                String pakageName = model.getAppList().get(i);
                String info = String.format("Запускаем -> %s", pakageName);
                Log.d(info);
                builder.setContentText(info);
                builder.setProgress(model.getAppList().size(), i + 1, false);
                notification = builder.build();
                notificationManager.notify(notifyID, notification);
                try {
                    SysUtils.runAndroidPackage(App.getInstance(), pakageName);
                    Thread.sleep(model.getApplicationDelay());
                } catch (Exception e) {
                    Log.e("ошибка запуска", e);
                }
            }
            builder.setContentText("Завершение");
            builder.setProgress(0, 0, false);
            notification = builder.build();
            notificationManager.notify(notifyID, notification);
            Thread.sleep(100);

            if (model.isShitchToHomeScreen()) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notificationManager.cancel(notifyID);
    }

    private void refresh() {
        Log.d("ControlService refresh()");
        startForeground(notif_id, createNotification("Служба прослушивание sleep"));
        if (model.isStartSleepThread()) {
            startSleep();
        } else {
            stopSleep();
        }
        if (model.isStartPowerampThread()) {
            startPowerAmpThread();
        } else {
            stopPowerAmpThread();
        }
    }

    /**
     * Запустить слипер
     */
    private synchronized void startSleep() {
        if (!TWUtilDecorator.isAvailable()) {
            Log.d("TWUtil unavaiable sleepn not started");
            return;
        }
        if (!model.isStartSleepThread()) {
            Log.d("ControlService startSleep() - model.isStartSleepThread() = false exit");
            return;
        }
        if (sleepProcessingThread != null)
            return;
        sleepProcessingThread = new SleepProcessingThread();
        sleepProcessingThread.start();
        Log.d("ControlService startSleep() - started");
    }

    /**
     * остановить слипер
     */
    private synchronized void stopSleep() {
        if (sleepProcessingThread == null)
            return;
        sleepProcessingThread.tryStop();
        sleepProcessingThread = null;
        Log.d("ControlService stopSleep() - stoped");
    }

    private synchronized void startPowerAmpThread() {
        if (!model.isStartSleepThread()) {
            Log.d("ControlService startPowerAmpThread() - model.isStartSleepThread() = false exit");
            return;
        }

        if (powerAmpListinerThread != null)
            return;
        powerAmpListinerThread = new PowerAmpListinerThread();
        powerAmpListinerThread.start();
        Log.d("ControlService startPowerAmpThread() - started");
    }

    private synchronized void stopPowerAmpThread() {
        if (powerAmpListinerThread == null) {
            return;
        }
        powerAmpListinerThread.tryStop();
        powerAmpListinerThread = null;
        Log.d("ControlService stopPowerAmpThread() - stoped");
    }


    private Notification createNotification(String a_info) {
        Intent intent = new Intent(this, AboutActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AboutActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(a_info)
                .setSmallIcon(R.drawable.image_loading)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(false);

        Notification notification = builder.build();
        return notification;
    }

    @Override
    public void onDestroy() {
        Log.d("ControlService onDestroy()");
        stopme();
        super.onDestroy();
    }
}
