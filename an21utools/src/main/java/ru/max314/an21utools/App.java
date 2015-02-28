package ru.max314.an21utools;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ru.max314.an21utools.model.AppModel;
import ru.max314.an21utools.util.LogHelper;

/**
 * Приложение
 * Created by max on 30.10.2014.
 */
@ReportsCrashes(
        formKey = "",
        mode = ReportingInteractionMode.TOAST,
        customReportContent = {
                ReportField.USER_CRASH_DATE,
                ReportField.USER_COMMENT,
                ReportField.USER_EMAIL,
                ReportField.ANDROID_VERSION,
                ReportField.PHONE_MODEL,
                ReportField.BRAND,
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.STACK_TRACE,
//                ReportField.APPLICATION_LOG,
                ReportField.LOGCAT},
        mailTo = "max314.an21u@gmail.com",
        forceCloseDialogAfterToast = false, // optional, default false
        resToastText = R.string.crash_toast_text,
        logcatArguments = {"-t", "300", "-v", "long"}
)
public class App extends Application {
    private static LogHelper Log = new LogHelper(App.class);

    public App() {
        super();
        Log.d("App ctor *************************************************************************************************");

    }

    static App self;

    public static App getInstance() {
        return self;
    }

    AppModel model;
    public synchronized AppModel getModel(){
        if (model==null)
            model = new AppModel(this);
        return model;
    }

    public String getVersion(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return String.format("Name:%s Version:%s (%d)",info.packageName, info.versionName, info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getVersion() error",e);
        }
        return "Unknown";
    }

    @Override
    public void onCreate() {
        Log.d("App onCreate start-------------------------------------------------------------------------");
        Log.d(String.format("Версия приложения = %s ----------------------------------------------------------\n", this.getVersion()));
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.pref_autorun, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_sleep, false);

        self = this;
        ACRA.init(this);
        LocalReportSender sender = new LocalReportSender(this);
        ACRA.getErrorReporter().addReportSender(sender);

        Log.d("App onCreate start");
        this.startService(new Intent(this, ControlService.class));
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d("App finalize() *************************************************************************************************");
        super.finalize();
    }

    @Override
    public void onTerminate() {
        Log.d("App onTerminate() *************************************************************************************************");
        super.onTerminate();
    }

    private class LocalReportSender implements ReportSender {

        private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>();
        private FileWriter crashReport = null;

        public LocalReportSender(Context ctx) {
            // the destination
            File logFile = new File(Environment.getExternalStorageDirectory(), "ru.max314.cardashboard.error.log");

            try {
                crashReport = new FileWriter(logFile, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void send(CrashReportData report) throws ReportSenderException {
            final Map<String, String> finalReport = remap(report);

            try {
                BufferedWriter buf = new BufferedWriter(crashReport);

                Set<Map.Entry<String, String>> set = finalReport.entrySet();
                Iterator<Map.Entry<String, String>> i = set.iterator();

                while (i.hasNext()) {
                    Map.Entry<String, String> me = (Map.Entry<String, String>) i.next();
                    buf.append("[" + me.getKey() + "]=" + me.getValue());
                }

                buf.flush();
                buf.close();
            } catch (IOException e) {
                Log.e("IO ERROR", e);
            }
        }

        private boolean isNull(String aString) {
            return aString == null || ACRAConstants.NULL_VALUE.equals(aString);
        }

        private Map<String, String> remap(Map<ReportField, String> report) {

            ReportField[] fields = ACRA.getConfig().customReportContent();
            if (fields.length == 0) {
                fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
            }

            final Map<String, String> finalReport = new HashMap<String, String>(
                    report.size());
            for (ReportField field : fields) {
                if (mMapping == null || mMapping.get(field) == null) {
                    finalReport.put(field.toString(), report.get(field));
                } else {
                    finalReport.put(mMapping.get(field), report.get(field));
                }
            }
            return finalReport;
        }

    }
}
