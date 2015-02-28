package ru.max314.an21utools.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.max314.an21utools.R;

/**
 * Created by max on 26.02.2015.
 */
public class AppModel extends TinyDB {
    Context context;
    public AppModel(Context appContext) {
        super(appContext);
        context = appContext;
    }

    private String RID(int id) {
        return context.getResources().getString(id);
    }

    /**
     * Запускать при старте
     * @return
     */
    public boolean isStarting() {
        return this.getBoolean(RID(R.string.pk_starting));
    }

    /**
     * получение настроки
     * Переключиться после последнего приложения на домашний экран
     * @return
     */
    public boolean isShitchToHomeScreen() {
        return this.getBoolean(RID(R.string.pk_shitchToHomeScreen));
    }

    /**
     * Задержка в секундах при запуске сервиса
     * @return
     */
    public int getStartDelay() {
        return this.getInt(RID(R.string.pk_startDelay));
    }

    /***
     * Задержка в секундах между стартом приложений
     * @return
     */
    public int getApplicationDelay() {
        return this.getInt(RID(R.string.pk_applicationDelay));
    }

    /**
     * Задержка при выходе из сна перед посылкой RESUME poweramp-у
     * @return
     */
    public int getPowerampResumeDelay() {
        return this.getInt(RID(R.string.pk_powerampResumeDelay));
    }

    /***
     * Стартовать слип треад
     * @return
     */
    public boolean isStartSleepThread() {
        return this.getBoolean(RID(R.string.pk_startSleepThread));
    }

    /**
     * Запускать поток обслуживания poweramp
     * @return
     */
    public boolean isStartPowerampThread() {
        return this.getBoolean(RID(R.string.pk_startPowerampThread));
    }

    public ArrayList<String> getAppList(){
        ArrayList<String> list = this.getList(RID(R.string.pk_applictionList));
        if (list==null)
            list = new ArrayList<String>();
        return list;
    }
    public void setAppList(ArrayList<String> list){
        putList(RID(R.string.pk_applictionList), list);
    }
}
