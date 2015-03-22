// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;

import com.example.emea.exception.NMEAParserException;

import java.util.Date;



/*

 RMC - NMEA has its own version of essential gps pvt (position, velocity, time) data. It is called RMC, The Recommended Minimum, which will look similar to:
				$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A

				   Where:
				     RMC          Recommended Minimum sentence C
				     123519       Fix taken at 12:35:19 UTC
				     A            Status A=active or V=Void.
				     4807.038,N   Latitude 48 deg 07.038' N
				     01131.000,E  Longitude 11 deg 31.000' E
				     022.4        Speed over the ground in knots
				     084.4        Track angle in degrees True
				     230394       Date - 23rd of March 1994
				     003.1,W      Magnetic Variation
				     *6A          The checksum data, always begins with *
				*/

/**
 *
 */
public class PacketRMC extends Packet
{

    public PacketRMC(String as[])
        throws NMEAParserException
    {
        super(as);
    }

    public Date getDate()
    {
        return date;
    }

    public Date getDateTime()
    {
        return dateTime;
    }

    public double getGroundSpeed()
    {
        return groundSpeed;
    }
    public double getGroundSpeedMetric()
    {
        return groundSpeed * 0.514444;
    }

    public String getId()
    {
        return "GPRMC";
    }

    public double getMagneticVariation()
    {
        return magneticVariation;
    }

    public String getMode()
    {
        return mode;
    }

    public Geopoint getPosition()
    {
        return position;
    }

    public String getStatus()
    {
        return status;
    }

    public Date getTime()
    {
        return time;
    }

    public double getTrackAngle()
    {
        return trackAngle;
    }

    protected void parse(String as[])
        throws NMEAParserException
    {
        try
        {
            date = ParserHelper.toDate(as[9]);
            time = ParserHelper.toTime(as[1]);
            dateTime = new Date(date.getTime() + time.getTime());
            status = as[2];
            position = ParserHelper.toGeopoint(as[3], as[4], as[5], as[6]);
            groundSpeed = ParserHelper.toDouble(as[7]);
            trackAngle = ParserHelper.toDouble(as[8]);
            magneticVariation = ParserHelper.toDouble(as[10]) * (double)ParserHelper.toSign(as[11]);
            if(as.length > 13)
                mode = as[12];
            return;
        }
        catch(Exception exception)
        {
            throw new NMEAParserException(exception);
        }
    }

    public String toString()
    {
        return ObjectToString.on(this).add("dateTime", dateTime).add("status", status).add("position", position).add("groundSpeed", Double.valueOf(groundSpeed)).add("trackAngle", Double.valueOf(trackAngle)).add("magneticVariation", Double.valueOf(magneticVariation)).add("mode", mode).toString();
    }

    private Date date;
    private Date dateTime;
    private double groundSpeed;
    private double magneticVariation;
    private String mode;
    private Geopoint position;
    private String status;
    private Date time;
    private double trackAngle;
}
