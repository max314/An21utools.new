package ru.max314.an21utools.model;


import java.util.List;
import java.util.Observable;

import ru.max314.an21utools.PowerAmpListinerThread;
import ru.max314.an21utools.SleepProcessingThread;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.tw.TWUtilDecorator;

/**
 * Created by max on 28.10.2014.
 */
public class AutoRunModel extends Observable {
    private static LogHelper Log = new LogHelper(AutoRunModel.class);
    /**
     * Назваания адаптеров для виджета музыки
     */
    private String[] musicProviders = {
            "Унверсальный (эмуляция гарнитуры)",
            "AIMP (напрямую)"
    };

    /// Стартовать при запуске
    private boolean starting;
    /*
        Задержка после старта сервиса
     */
    private int startDelay = 100;
    /*
       Задержка после старта приложения
     */
    private int applicationDelay = 100;

    /*
       Задержка отправки сообщения RESUME poweramp
     */
    private int powerampResumeDelay = 2000;

    private boolean startSleepThread = true;

    private boolean startPowerampThread = true;

    /*
       Список приложений
     */
    private List<AppInfo> appInfoList;

    /*
      Переключиться после последнего приложения на домашний экран
     */
    private boolean shitchToHomeScreen = false;

    /**
     * индекс провайдера для музыки
     */
    private int musicProvederIndex = 0;

    /*
      Показывать отладочные сообщения на виджетах
    */
    private boolean musicWidgetToast = false;


    /**
     * получение настроки
     * Переключиться после последнего приложения на домашний экран
     * @return
     */
    public boolean isShitchToHomeScreen() {
        return shitchToHomeScreen;
    }

    /**
     * установка настроки
     * Переключиться после последнего приложения на домашний экран
     * @param shitchToHomeScreen
     */
    public void setShitchToHomeScreen(boolean shitchToHomeScreen) {
        this.shitchToHomeScreen = shitchToHomeScreen;
    }

    /**
     * Запускать при старте
     * @return
     */
    public boolean isStarting() {
        return starting;
    }

    /**
     * Установить признак запуска при старте
     * @param starting
     */
    public void setStarting(boolean starting) {
        this.starting = starting;
        setChanged();
        this.notifyObservers();
    }

    /**
     * получить Список приложений автозапуска
     * @return
     */
    public List<AppInfo> getAppInfoList() {
        return appInfoList;
    }

    /***
     * установить Список приложений автозапуска
     * @param appInfoList
     */
    public void setAppInfoList(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    /**
     * Добавить приложение
     * @param name
     */
    public void addAppinfo(String name){
        for (int i = 0; i < appInfoList.size(); i++) {
            if (appInfoList.get(i).getName().compareTo(name)==0)
                return;
        }
        this.appInfoList.add(new AppInfo(name));
        setChanged();
        this.notifyObservers();
    }

    /**
     * Удалить приложение
     * @param index
     */
    public void removeAppinfo(int index){
        this.appInfoList.remove(index);
        setChanged();
        this.notifyObservers();
    }

    /**
     * Сдвинуть приложение вверх по спискау
     * @param index
     */
    public void shiftUpAppinfo(int index){
        if (index<=0)
            return;
        this.swapAppInfo(index-1,index);
        setChanged();
        this.notifyObservers();
    }
    /**
     * Сдвинуть приложение вниз по списку
     * @param index
     */
    public void shiftDownAppinfo(int index){
        if (index>=getAppInfoList().size()-1)
            return;
        this.swapAppInfo(index, index + 1);
        setChanged();
        this.notifyObservers();
    }

    /**
     * Поменять 2 элемента массива
     * @param index1
     * @param index2
     */
    private void swapAppInfo(int index1,int index2){
        AppInfo buff = this.getAppInfoList().get(index1);
        this.getAppInfoList().set(index1,this.getAppInfoList().get(index2));
        this.getAppInfoList().set(index2,buff);
    }

    /**
     * Задержка в секундах при запуске сервиса
     * @return
     */
    public int getStartDelay() {
        return startDelay;
    }

    /**
     * установить задержку в секундах при запуске сервиса
     * @param startDelay
     */
    public void setStartDelay(int startDelay) {
        if (startDelay<99)
            startDelay=100;
        this.startDelay = startDelay;
        setChanged();
        this.notifyObservers();

    }

    /***
     * Задержка в секундах между стартом приложений
     * @return
     */
    public int getApplicationDelay() {
        return applicationDelay;
    }

    /**
     * установить задержку в секундах между стартом приложений
     * @param applicationDelay
     */
    public void setApplicationDelay(int applicationDelay) {
        if (applicationDelay<99){
            applicationDelay=100;
        }
        this.applicationDelay = applicationDelay;
        setChanged();
        this.notifyObservers();
    }

    /**
     * получить список описаний провайдеров для музыки
     * @return
     */
    public String[] getMusicProviders() {
        return musicProviders;
    }


    /**
     * Получить индекс текущего провайдера
     * @return
     */
    public int getMusicProvederIndex() {
        return musicProvederIndex;
    }

    /**
     * Установить индекс текущего провайдера
     * @param musicProvederIndex
     */
    public void setMusicProvederIndex(int musicProvederIndex) {
        if (musicProvederIndex<0 && musicProvederIndex>1){
            return;
        }
        this.musicProvederIndex = musicProvederIndex;
        setChanged();
        this.notifyObservers();

    }

    /**
     * получить Показывать отладочные сообщения на виджетах
     * @return
     */
    public boolean isMusicWidgetToast() {
        return musicWidgetToast;
    }

    /**
     * установить Показывать отладочные сообщения на виджетах
     * @param musicWidgetToast
     */
    public void setMusicWidgetToast(boolean musicWidgetToast) {
        this.musicWidgetToast = musicWidgetToast;
        setChanged();
        this.notifyObservers();

    }

    public synchronized int getPowerampResumeDelay() {
        return powerampResumeDelay;
    }

    public synchronized void setPowerampResumeDelay(int powerampResumeDelay) {
        if (powerampResumeDelay<99){
            powerampResumeDelay = 100;
        }
        this.powerampResumeDelay = powerampResumeDelay;
        setChanged();
        this.notifyObservers();
    }

    /***
     * Стартовать слип треад
     * @return
     */
    public synchronized boolean isStartSleepThread() {
        return startSleepThread;
    }

    /**
     * Установить режим стартовать слип треад
     * @param startSleepThread
     */
    public synchronized void setStartSleepThread(boolean startSleepThread) {
        this.startSleepThread = startSleepThread;
    }

    public boolean isStartPowerampThread() {
        return startPowerampThread;
    }

    public void setStartPowerampThread(boolean startPowerampThread) {
        this.startPowerampThread = startPowerampThread;
    }
}
