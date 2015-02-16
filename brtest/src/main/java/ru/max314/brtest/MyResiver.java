package ru.max314.brtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Created by max on 16.02.2015.
 */
public class MyResiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        App.getInstance().buffer.append(String.format("%s: add brodcast %s \n", new Date(),action));
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
