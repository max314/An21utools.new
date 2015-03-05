package ru.max314.an21utools;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ru.max314.an21utools.gps.GPSActivityConst;
import ru.max314.an21utools.util.GPSUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class GpsAlertDialog extends Activity implements LocationListener {


    public GpsAlertDialog() {
        // Required empty public constructor
    }

    Button btGpsClear;
    TextView tvGpsSpeed;
    TextView tvGpsLocLat;
    TextView tvGpsLocLon;
    TextView tvGpsMessage;
    LocationManager locationManager;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Проблемы с Gps");
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_gps_alert_dialog);


        tvGpsSpeed = (TextView) findViewById(R.id.tvGpsSpeed);
        tvGpsLocLat = (TextView) findViewById(R.id.tvGpsLocLat);
        tvGpsLocLon = (TextView) findViewById(R.id.tvGpsLocLon);
        tvGpsMessage = (TextView) findViewById(R.id.tvGpsMessage);

        btGpsClear = (Button) findViewById(R.id.btGpsClear);
        btGpsClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSUtils.clearAGPS(getBaseContext(),true);
                finish();
            }
        });
        findViewById(R.id.btGpsCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String message = getIntent().getStringExtra(GPSActivityConst.GPS_ACTIVITY_ACTION_START_MESSAGE);
        if (message==null){
            message = "Сообщения нет. Вероятно это тестовый вызов.";
        }
        tvGpsMessage.setText(message);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    //region LocationListiner
    @Override
    public void onLocationChanged(Location location) {
        if (location!=null){
           String buff;
            buff = String.format("%2.3f",location.getSpeed()*3.6);
            tvGpsSpeed.setText(buff);
            buff = String.format("%2.7f",location.getLatitude());
            tvGpsLocLat.setText(buff);
            buff = String.format("%2.7f",location.getLongitude());
            tvGpsLocLon.setText(buff);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //endregion

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onCreate
     * @see #onStop
     * @see #onResume
     */
    @Override
    protected void onStart() {
        super.onStart();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //int timeout = App.getInstance().getModel().getGpsActivityShowTime() * 1000; // переходим к милисекундам
        int timeout = 60 * 1000; // переходим к милисекундам
        if (timeout!=0){
            countDownTimer = new CountDownTimer(timeout, 1000) {

                public void onTick(long millisUntilFinished) {
                    int outsec = Math.round(millisUntilFinished / 1000);
                    String str = String.format("Сброс AGPS\n секунд осталось: %d", outsec);
                    btGpsClear.setText(str);
                }
                public void onFinish() {
                    GPSUtils.clearAGPS(getBaseContext(),true);
                    finish();
                }
            }.start();
        }
        playDefaultNotificationSound();

    }

    private void playDefaultNotificationSound() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     * <p/>
     * <p>Note that this method may never be called, in low memory situations
     * where the system does not have enough memory to keep your activity's
     * process running after its {@link #onPause} method is called.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestart
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onDestroy
     */
    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
        countDownTimer.cancel();
    }
}
