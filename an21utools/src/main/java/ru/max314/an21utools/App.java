package ru.max314.an21utools;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

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

import ru.max314.an21utools.model.ModelFactory;
import ru.max314.an21utools.util.LogHelper;

/**
 * Приложение
 * Created by max on 30.10.2014.
 */
@ReportsCrashes(
        formKey="",
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
                ReportField.LOGCAT },
        mailTo = "max314.an21u@gmail.com",
        forceCloseDialogAfterToast = false, // optional, default false
        resToastText = R.string.crash_toast_text,
        logcatArguments = { "-t", "300", "-v", "long" }
)
public class App extends Application {
    private static LogHelper Log = new LogHelper(App.class);

    static App self;

    public static App getInstance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        ACRA.init(this);
        LocalReportSender sender = new LocalReportSender(this);
        ACRA.getErrorReporter().addReportSender(sender);
        ModelFactory.getAutoRunModel(); // Запуститься если сервис не запустил
        Log.d("App onCreate start");
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
