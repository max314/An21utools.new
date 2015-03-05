package ru.max314.an21utools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import ru.max314.an21utools.util.DisplayToast;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SysUtils;
import ru.max314.an21utools.util.tw.TWSleeper;
import ru.max314.an21utools.util.tw.TWUtilDecorator;

/**
 * Created by max on 28.10.2014.
 */
public class AboutActivity extends Activity {
    static LogHelper Log = new LogHelper(AboutActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        final Button btn = (Button) findViewById(R.id.AboutTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Останавливаем сервис
                new DisplayToast(AboutActivity.this, "Останов сервиса...", false).run();
                Intent intent = new Intent(AboutActivity.this, ControlService.class);
                stopService(intent);

                btn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AboutActivity.this, ControlService.class);
                        startService(intent);
                        new DisplayToast(AboutActivity.this, "Запуск сервиса...", false).run();
                    }
                }, TimeUnit.MILLISECONDS.convert(2, TimeUnit.SECONDS));
            }
        });
        Button btnAboutTestWriteLog = (Button) findViewById(R.id.btAboutTestWriteLog);
        btnAboutTestWriteLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Тестовая строка ушла в лог");
                startActivity(new Intent(getBaseContext(),GpsAlertDialog.class));
            }
        });
        Button btAboutTestSendSleep = (Button) findViewById(R.id.btAboutTestSendSleep);
        btAboutTestSendSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.createAndSayBrodcastIntent(TWSleeper.BRD_TAG_SLEEP);
            }
        });
        Button btAboutTestSendWakeUp = (Button) findViewById(R.id.btAboutTestSendWakeUp);
        btAboutTestSendWakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.createAndSayBrodcastIntent(TWSleeper.BRD_TAG_WAKEUP);
            }
        });
        Button btAboutTestSendShutdown = (Button) findViewById(R.id.btAboutTestSendShutdown);
        btAboutTestSendShutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SysUtils.createAndSayBrodcastIntent(TWSleeper.BRD_TAG_SHUTDOWN);
            }
        });

        TextView tv = (TextView) findViewById(R.id.textViewDebug);
        StringBuilder stringBuilder = null;
        try {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("Версия приложения = %s \n", App.getInstance().getVersion()));
            stringBuilder.append(String.format("Помидоры можно прислать на max314.an21u@gmail.com \n"));
            if (TWUtilDecorator.isAvailable()) {
                // Медленно
                int id = TWUtilDecorator.getCarDeviceID();
                stringBuilder.append(String.format("Устройство:ID %d(%s) \n", id, TWUtilDecorator.getCarDeviceString(id)));
            } else {
                stringBuilder.append(String.format("Устройство:TWUtil не доступно \n"));
            }
        } catch (Exception e) {
            Log.e("Error", e);
        }
//        stringBuilder.append(String.format("Побочные эффекты ------------------------\n"));
//        stringBuilder.append(String.format("Root доступен = %s \n", RootTools.isRootAvailable()));
//        stringBuilder.append(String.format("Busybox доступен = %s \n", RootTools.isBusyboxAvailable()));
        tv.setText(stringBuilder.toString());
    }

    BroadcastReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.example.Broadcast");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSettings:
                Intent intent = new Intent(AboutActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}