package ru.max314.an21utools.Http;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by max on 25.11.2015.
 */
public class HomeHTTPHandler extends HTTPHandlerBase {
    @Override
    protected NanoHTTPD.Response onProcess(NanoHTTPD.IHTTPSession session) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, "<html><body> TestServer <a href=/state>State </body></html>");
    }
}
