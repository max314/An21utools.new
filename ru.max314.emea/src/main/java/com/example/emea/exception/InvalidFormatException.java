// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea.exception;


// Referenced classes of package googoo.android.common.nmea.exception:
//            NMEAParserException

public class InvalidFormatException extends NMEAParserException
{

    public InvalidFormatException()
    {
    }

    public InvalidFormatException(String s)
    {
        super(s);
    }

    public InvalidFormatException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public InvalidFormatException(Throwable throwable)
    {
        super(throwable);
    }

    private static final long serialVersionUID = 1L;
}
