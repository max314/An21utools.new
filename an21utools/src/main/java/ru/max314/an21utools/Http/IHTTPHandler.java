package ru.max314.an21utools.Http;

/**
 * Created by max on 25.11.2015.
 */

import fi.iki.elonen.NanoHTTPD;
/*
Интефейс для обработчикоф
 */
public interface IHTTPHandler {
    /**
     * Вызваеться при старте сервера
     */
    void onStart();

    /**
     * Собственно обработчик
     * @param session
     * @return
     */
    NanoHTTPD.Response Process(NanoHTTPD.IHTTPSession session);

    /**-
     * При стопе сервера
     */
    void onStop();
}
