package ru.max314.an21utools.Http;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by max on 25.11.2015.
 */
public class DefaultHTTPHandler extends HTTPHandlerBase  {
    @Override
    protected NanoHTTPD.Response onProcess(NanoHTTPD.IHTTPSession session) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
    }
}
