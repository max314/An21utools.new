package ru.max314.an21utools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.max314.util.LogHelper;

/**
 * Created by max on 31.10.2014.
 * Ресивер сообщения о загрузке
 */
public class BROnBoot extends BroadcastReceiver {
    private static LogHelper Log = new LogHelper(BROnBoot.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive " + intent.getAction());
        context.startService(new Intent(context, BackgroudService.class));
    }
}

