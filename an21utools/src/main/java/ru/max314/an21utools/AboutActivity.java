package ru.max314.an21utools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.max314.an21utools.util.LogHelper;
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
        Button btn = (Button) findViewById(R.id.AboutTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BackgroudService.class);
                startService(intent);
            }
        });
        TextView tv = (TextView) findViewById(R.id.textViewDebug);
        StringBuilder stringBuilder = null;
        try {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("Версия приложения = %s \n", App.getInstance().getString(R.string.app_version)));
            stringBuilder.append(String.format("Помидоры можно прислать на max314.an21u@gmail.com \n\n"));
            if (TWUtilDecorator.isAvailable()){
                // Медленно
                int id = TWUtilDecorator.getCarDeviceID();
                stringBuilder.append(String.format("Устройство:ID %d(%s) \n", id ,TWUtilDecorator.getCarDeviceString(id)));
            }else {
                stringBuilder.append(String.format("Устройство:TWUtil не доступно \n" ));
            }
        } catch (Exception e) {
            Log.e("Error",e);
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
}