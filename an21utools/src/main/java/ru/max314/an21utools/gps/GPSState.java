package ru.max314.an21utools.gps;

/**
 * Created by max on 04.03.2015.
 */
public enum GPSState {
    /**
     * Старт автомата
      */
    CREATED,
    /**
     * Старт автомата
      */
    START,
    /**
     * Прошел перваый фикс
     */
    FIX,
    /**
     * Штатная работа
     */
    WORK,
    /**
     * Штатная работа под контролем
     */
    WORK_CONTROL,

    /**
     * Нас отресетили но могут перезапустить
     */
    CLEAN,
    /**
     * GPS остановили
     */
    STOPED,

}
