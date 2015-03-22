// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;

import java.text.DecimalFormat;

public class Geopoint
{

    public Geopoint(double d, double d1)
    {
        latitudeE6 = (int)Math.round(d * 1000000D);
        longitudeE6 = (int)Math.round(d1 * 1000000D);
        latDeg = (int)d;
        lonDeg = (int)d1;
        latMin = Math.abs(60D * (d - (double)latDeg));
        lonMin = Math.abs(60D * (d1 - (double)lonDeg));
    }

    public Geopoint(int i, int j)
    {
        latitudeE6 = i;
        longitudeE6 = j;
        double d = (double)i / 1000000D;
        double d1 = (double)j / 1000000D;
        latDeg = (int)d;
        lonDeg = (int)d1;
        latMin = Math.abs(60D * (d - (double)latDeg));
        lonMin = Math.abs(60D * (d1 - (double)lonDeg));
    }

    public double getLatitude()
    {
        return (double)latitudeE6 / 1000000D;
    }

    public int getLatitudeE6()
    {
        return latitudeE6;
    }

    public double getLongitude()
    {
        return (double)longitudeE6 / 1000000D;
    }

    public int getLongitudeE6()
    {
        return longitudeE6;
    }

    public void setLatitudeE6(int i)
    {
        latitudeE6 = i;
    }

    public void setLongitudeE6(int i)
    {
        longitudeE6 = i;
    }

    public String toDDMM()
    {
        return (new StringBuilder(String.valueOf(latDeg))).append("\260").append(DF_MIN.format(latMin)).append('\'').append(' ').append(lonDeg).append('\260').append(DF_MIN.format(lonMin)).append('\'').toString();
    }

    public String toDDMMSS()
    {
        double d = 60D * (latMin - (double)(int)latMin);
        double d1 = 60D * (lonMin - (double)(int)lonMin);
        return (new StringBuilder(String.valueOf(latDeg))).append("\260").append((int)latMin).append('\'').append(DF_SEC.format(d)).append("\" ").append(lonDeg).append('\260').append((int)lonMin).append('\'').append(DF_SEC.format(d1)).append('"').toString();
    }

    public String toString()
    {
        return (new StringBuilder("[")).append(getLatitude()).append(",").append(getLongitude()).append("]").toString();
    }

    private static DecimalFormat DF_MIN = new DecimalFormat("00.0000");
    private static DecimalFormat DF_SEC = new DecimalFormat("00.00");
    private int latDeg;
    private double latMin;
    private int latitudeE6;
    private int lonDeg;
    private double lonMin;
    private int longitudeE6;

}
