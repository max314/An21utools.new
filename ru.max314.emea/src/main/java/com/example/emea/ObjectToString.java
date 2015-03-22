// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;

import java.util.Arrays;

public class ObjectToString
{

    public ObjectToString(Object obj)
    {
        className = "";
        values = new StringBuffer();
        count = 0;
        String s;
        if(obj != null)
            s = obj.getClass().getName();
        else
            s = "";
        className = s;
        object = obj;
    }

    public static ObjectToString on(Object obj)
    {
        return new ObjectToString(obj);
    }

    public ObjectToString add(String s, Object obj)
    {
        if(count != 0)
            values.append(", ");
        if(s != null)
            values.append((new StringBuilder(String.valueOf(s))).append("=").append(obj).toString());
        else
            values.append((new StringBuilder()).append(obj).toString());
        count = 1 + count;
        return this;
    }

    public ObjectToString add(String s, double ad[])
    {
        return add(s, Arrays.toString(ad));
    }

    public ObjectToString add(String s, int ai[])
    {
        return add(s, Arrays.toString(ai));
    }

    public ObjectToString add(String s, Object aobj[])
    {
        return add(s, Arrays.toString(aobj));
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(className))).append("{").append(values).append("}").toString();
    }

    private String className;
    private int count;
    private Object object;
    private StringBuffer values;
}
