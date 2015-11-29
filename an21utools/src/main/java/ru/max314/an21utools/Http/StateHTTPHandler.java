package ru.max314.an21utools.Http;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by max on 25.11.2015.
 */
public class StateHTTPHandler extends HTTPHandlerBase {

    HTTPServer parent;

    public StateHTTPHandler(HTTPServer parent) {
        this.parent = parent;
    }


    @Override
    protected NanoHTTPD.Response onProcess(NanoHTTPD.IHTTPSession session) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, "<html><body>"+ getNetwork()+"</body></html>");
    }

    private String getNetwork1() {
        StringBuilder sb = new StringBuilder();
        parent.getHostname();
        parent.getListeningPort();

        return sb.toString();

    }

    private String getNetwork()  {
        try {
            StringBuilder sb = new StringBuilder();
            // Iterate over all network interfaces.
            for (Enumeration<NetworkInterface> en =
                 NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                sb.append(String.format("{%s}\n<br>",intf.toString()));
                // Iterate over all IP addresses in each network interface.
                for (Enumeration<InetAddress> enumIPAddr =
                     intf.getInetAddresses(); enumIPAddr.hasMoreElements();)
                {
                    InetAddress iNetAddress = enumIPAddr.nextElement();
                    // Loop back address (127.0.0.1) doesn't count as an in-use
                    // IP address.

                    if (!iNetAddress.isLoopbackAddress())
                    {
                        String sLocalIP = iNetAddress.getHostAddress().toString();
                        String sInterfaceName = intf.getName();
                        sb.append(String.format("{%s}:{%s}\n<br>",sInterfaceName,sLocalIP));
                    }
                }
            }
            return sb.toString();
        } catch (SocketException e) {
            return e.toString();
        }
    }

}
