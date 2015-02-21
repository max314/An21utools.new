package ru.max314.an21utools;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;

import ru.max314.an21utools.model.AutoRunModel;
import ru.max314.an21utools.model.ModelFactory;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SysUtils;

/**
 * Created by max on 31.10.2014.
 * Воновый сервис предполагаеться что это он будеть запускать приложения
 */
/*
public class BackgroudService extends IntentService  {

    public BackgroudService() {
        super("BackgroudService");
    }

    private static LogHelper Log = new LogHelper(BackgroudService.class);
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d("Create");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("OnBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int notifyID = 1;
        AutoRunModel model = ModelFactory.getAutoRunModel();
        // ничего не делаем пока
        if (!model.isStarting()){
            Log.d("отключен автоматический запуск");
            return;
        }
        if (model.getAppInfoList().size()==0){
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
            startForeground(notifyID, notification );
            notificationManager.notify(notifyID,notification);
            Thread.sleep(model.getStartDelay());
            // пошли по приложениям

            for (int i=0;i<model.getAppInfoList().size();i++){
                String pakageName =model.getAppInfoList().get(i).getName();
                String info = String.format("Запускаем -> %s", pakageName);
                Log.d(info);
                builder.setContentText(info);
                builder.setProgress(model.getAppInfoList().size(),i+1,false);
                notification = builder.build();
                notificationManager.notify(notifyID,notification);
                startForeground(notifyID, notification );
                try {
                    SysUtils.runAndroidPackage(App.getInstance(),pakageName);
                    Thread.sleep(model.getApplicationDelay());
                } catch (Exception e) {
                    Log.e("ошибка запуска",e);
                }
            }
            builder.setContentText("Завершение");
            builder.setProgress(0,0,false);
            notification = builder.build();
            notificationManager.notify(notifyID,notification);
            Thread.sleep(100);

            if (model.isShitchToHomeScreen()){
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopForeground(true);
        notificationManager.cancel(notifyID);
    }
}
*/