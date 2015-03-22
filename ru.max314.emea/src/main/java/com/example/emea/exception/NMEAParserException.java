// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea.exception;


public class NMEAParserException extends Exception
{

    public NMEAParserException()
    {
    }

    public NMEAParserException(String s)
    {
        super(s);
    }

    public NMEAParserException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public NMEAParserException(Throwable throwable)
    {
        super(throwable);
    }

    private static final long serialVersionUID = 1L;
}
