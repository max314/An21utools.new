// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;


// Referenced classes of package googoo.android.common.nmea:
//            PacketGGA, PacketRMC, PacketGSA, PacketGSV, 
//            Packet

import com.example.emea.exception.InvalidFormatException;
import com.example.emea.exception.NMEAParserException;
import com.example.emea.exception.UnsupportedSentenceException;

public class NMEAParser
{

    public NMEAParser()
    {
    }


    public static String[] fastSplit(String s, int delimiter) {
        if (s == null) {
            return null;
        }
        int stringLength = s.length();
        int count = 0;
        int start = 0;
        while (true) {
            int end = s.indexOf(delimiter, start);
            if (end == -1) {
                break;
            }
            count++;
            start = end + 1;
        }
        String[] result = new String[(count + 1)];
        count = 0;
        start = 0;
        while (true) {
            int end = s.indexOf(delimiter, start);
            if (end == -1) {
                result[count] = s.substring(start, stringLength);
                return result;
            }
            result[count] = s.substring(start, end);
            count++;
            start = end + 1;
        }
    }

    private String[] tokenize(String s)
    {
        if(s == null)
            return null;
        int i = s.indexOf('$');
        int j = s.indexOf('*');
        if(j < 0)
            j = s.length();
        return fastSplit(s.substring(i + 1, j), 44);
    }

    public Packet parse(String s)
        throws NMEAParserException
    {
        if(s == null || s.length() == 0)
            throw new InvalidFormatException("Null or empty nmea sentense.");
        String as[] = tokenize(s);
        String s1 = as[0];
        if("GPGGA".equals(s1))
            return new PacketGGA(as);
        if("GPRMC".equals(s1))
            return new PacketRMC(as);
        if("GPGSA".equals(s1))
            return new PacketGSA(as);
        if("GPGSV".equals(s1))
            return new PacketGSV(as);
        else
            throw new UnsupportedSentenceException((new StringBuilder("Unsupported Sentence type - ")).append(s1).toString());
    }
}
