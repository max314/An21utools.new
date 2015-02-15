package ru.max314.util;

import android.os.Environment;
import android.util.Log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by max on 31.10.2014.
 * Хелпер для логирования в качестве тега - имя класса
 */
public class LogHelper {
    private static final String MY_SDFOLDER = "ru.max314";
    private static final String MY_FILENAME = "ru.max314.an21utools.log";
    private static final String TAG = LogHelper.class.getName();
    static {
        final LogConfig logConfigurator = new LogConfig();

        // Создать каталог для логоф
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + MY_SDFOLDER);
        try {
            if (!file.exists())
                file.mkdir();
        }catch (Throwable e){
            Log.e(TAG,"Error init log4j:  make dir",e);
        }

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator +  MY_SDFOLDER+ File.separator +  MY_FILENAME);
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();
    }
    private String tag;
    private Logger log;


    public LogHelper(Class<?> classForTag) {
        tag = classForTag.getName();
        log =  Logger.getLogger(classForTag);
    }

    public LogHelper(String tag) {
        this.tag = tag;
        log =  Logger.getLogger(tag);
    }

    public void d(String str) {
        Log.d(tag, str);
        log.debug(str);
    }

    public void i(String str) {
        Log.i(tag, str);
        log.info(str);
    }

    public void e(String str) {
        Log.e(tag, str);
        log.error(str);
    }

    public void e(String str, Throwable e) {
        Log.e(tag, str, e);
        log.error(str,e);
    }
}
