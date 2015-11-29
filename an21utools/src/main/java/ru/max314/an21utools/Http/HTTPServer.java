package ru.max314.an21utools.Http;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;
import ru.max314.an21utools.util.LogHelper;

/**
 * Created by max on 06.11.2015.
 */
public class HTTPServer extends NanoHTTPD {
    HashMap<String, IHTTPHandler> mapProcessor = new HashMap<String, IHTTPHandler>();
    private static final LogHelper LOG = new LogHelper(HTTPServer.class);
    public HTTPServer(Context parentContext) {
        super(8080);
        mapProcessor.put("/", new HomeHTTPHandler());
        mapProcessor.put("/state",new StateHTTPHandler(this));
        mapProcessor.put("/data",new TorquerHTTPHandler(parentContext));
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        uri = uri.trim().replace(File.separatorChar, '/');
        if (uri.indexOf('?') >= 0) {
            uri = uri.substring(0, uri.indexOf('?'));
        }
        if ((uri == null) || uri=="/")
            uri = "/";
        uri = uri.toLowerCase();
        if (mapProcessor.containsKey(uri)){
            try {
                return mapProcessor.get(uri.toLowerCase()).Process(session);
            } catch (Exception e) {
                LOG.e("error processing "+ uri,e);
            }
        }
        return new DefaultHTTPHandler().Process(session);


//        Response r =  newFixedLengthResponse(
//                "<html><body>Redirected: <a href=\"" + uri + "\">" +
//                        uri + "</a></body></html>");
//        r.addHeader( "Location", uri );
//        return r;

//        Response stdout =  super.serve(session);
//        return stdout;
    }

    @Override
    public void start() throws IOException {
        super.start();
        for (IHTTPHandler handler : mapProcessor.values()) {
            handler.onStart();
        }
    }

    @Override
    public void stop() {
        super.stop();
        for (IHTTPHandler handler : mapProcessor.values()) {
            handler.onStop();
        }
    }
}
