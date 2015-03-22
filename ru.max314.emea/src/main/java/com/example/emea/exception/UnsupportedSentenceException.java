// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea.exception;


// Referenced classes of package googoo.android.common.nmea.exception:
//            NMEAParserException

public class UnsupportedSentenceException extends NMEAParserException
{

    public UnsupportedSentenceException()
    {
    }

    public UnsupportedSentenceException(String s)
    {
        super(s);
    }

    public UnsupportedSentenceException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public UnsupportedSentenceException(Throwable throwable)
    {
        super(throwable);
    }

    private static final long serialVersionUID = 1L;
}
