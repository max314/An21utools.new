package ru.max314.an21utools.Http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;

import fi.iki.elonen.NanoHTTPD;
import ru.max314.an21utools.util.TorqueHelper;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by max on 26.11.2015.
 */
public class TorquerHTTPHandler extends HTTPHandlerBase {

    private Context context;
    private String dataJson = "";

    public TorquerHTTPHandler(Context context) {
        this.context = context;
    }

    @Override
    protected NanoHTTPD.Response onProcess(NanoHTTPD.IHTTPSession session) {

        NanoHTTPD.Response res =  newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", "["+dataJson+"]");
        res.addHeader("Access-Control-Allow-Origin", "*");
        return res;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.context.registerReceiver(mMessageReceiver,
                new IntentFilter(TorqueHelper.c_Action));
    }

    @Override
    public void onStop() {
        super.onStop();
        this.context.unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra(TorqueHelper.c_ActionParam);
            LOG.d("Got message: " + message);
            dataJson = message;
        }
    };
}
