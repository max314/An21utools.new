// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;


import com.example.emea.exception.InvalidFormatException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ParserHelper {
    private static final BigDecimal BD100;
    private static final BigDecimal BD60;
    private static final SimpleDateFormat DF_DATE;
    private static final SimpleDateFormat DF_DATETIME;
    private static final SimpleDateFormat DF_TIME;

    static {
        BD60 = new BigDecimal(60);
        BD100 = new BigDecimal(100);
        DF_TIME = new SimpleDateFormat("HHmmss");
        DF_DATE = new SimpleDateFormat("ddMMyy");
        DF_DATETIME = new SimpleDateFormat("ddMMyyHHmmss");
        DF_TIME.setTimeZone(TimeZone.getTimeZone("Zulu"));
        DF_DATE.setTimeZone(TimeZone.getTimeZone("Zulu"));
        DF_DATETIME.setTimeZone(TimeZone.getTimeZone("Zulu"));
    }

    public static double toDouble(String s) {
        return toDouble(s, 0.0d);
    }

    public static double toDouble(String s, double nval) {
        String ns = nullString(s);
        return ns != null ? Double.parseDouble(ns) : nval;
    }

    public static int toInt(String s) {
        return toInt(s, 0);
    }

    public static int toInt(String s, int nval) {
        String ns = nullString(s);
        return ns != null ? Integer.parseInt(ns) : nval;
    }

    public static Date toDate(String s) throws InvalidFormatException {
        String ns = nullString(s);
        if (ns == null) {
            return null;
        }
        try {
            return DF_DATE.parse(ns);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InvalidFormatException(new StringBuilder("Invalid date - ").append(ns).toString(), e);
        }
    }

    public static Date toTime(String s) throws InvalidFormatException {
        String ns = nullString(s);
        if (ns == null) {
            return null;
        }
        try {
            String hh = ns.substring(0, 2);
            String mm = ns.substring(2,4);
            String ss = ns.substring(4);
            return new Date((long) (((double) ((3600000 * Integer.parseInt(hh)) + (60000 * Integer.parseInt(mm)))) + (1000.0d * Double.parseDouble(ss))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidFormatException(new StringBuilder("Invalid time - ").append(ns).toString(), e);
        }
    }

    public static int toSign(String nsew) {
        return ("W".equalsIgnoreCase(nsew) || "S".equalsIgnoreCase(nsew)) ? -1 : 1;
    }

    public static Date toDateTime(String d, String t) throws InvalidFormatException {
        if (isEmpty(d) || isEmpty(t)) {
            return null;
        }
        try {
            return DF_DATETIME.parse(new StringBuilder(String.valueOf(d)).append(t).toString());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InvalidFormatException(new StringBuilder("Invalid datetime - ").append(d).append(",").append(t).toString(), e);
        }
    }

    public static Geopoint toGeopoint(String lats, String ns, String lons, String ew) throws InvalidFormatException {
        return (isEmpty(lats) || isEmpty(lons)) ? null : new Geopoint(coordToE6(lats, ns), coordToE6(lons, ew));
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    private static int coordToE6(String coord, String dir) throws InvalidFormatException {
        try {
            BigDecimal[] tmp = new BigDecimal(coord).divideAndRemainder(BD100);
            int degE6 = tmp[0].add(tmp[1].divide(BD60, 6, RoundingMode.DOWN)).scaleByPowerOfTen(6).intValue();
            return ("W".equalsIgnoreCase(dir) || "S".equalsIgnoreCase(dir)) ? -degE6 : degE6;
        } catch (Exception e) {
            throw new InvalidFormatException(new StringBuilder("Invalid coordinate - ").append(coord).append(",").append(dir).toString(), e);
        }
    }

    private static String nullString(String s) {
        if (s != null) {
            s = s.trim();
        }
        return (s == null || s.length() <= 0) ? null : s;
    }

    public static double parseNmeaSpeed(String speed,String metric){
        double meterSpeed = 0.0f;
        if (speed != null && metric != null && !speed.equals("") && !metric.equals("")){
            double temp1 = Double.parseDouble(speed)/3.6d;
            if (metric.equals("K")){
                meterSpeed = temp1;
            } else if (metric.equals("N")){
                meterSpeed = temp1*1.852d;
            }
        }
        return meterSpeed;
    }

}