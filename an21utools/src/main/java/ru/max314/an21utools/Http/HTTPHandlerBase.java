package ru.max314.an21utools.Http;

import fi.iki.elonen.NanoHTTPD;
import ru.max314.an21utools.util.LogHelper;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by max on 26.11.2015.
 */
public abstract class HTTPHandlerBase implements IHTTPHandler {
    protected static final LogHelper LOG = new LogHelper(HTTPHandlerBase.class);
    @Override
    public void onStart() {

    }

    @Override
    public NanoHTTPD.Response Process(NanoHTTPD.IHTTPSession session) {
        try {
            return onProcess(session);
        } catch (Exception e) {
            LOG.e("Error processing" +this.getClass().getName(),e);
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML, "<html><body> "+e.toString()+" </body></html>");
        }
    }


    protected abstract NanoHTTPD.Response onProcess(NanoHTTPD.IHTTPSession session);

    @Override
    public void onStop() {

    }
}
