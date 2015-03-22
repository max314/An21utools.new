// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;


import com.example.emea.exception.NMEAParserException;

import java.util.Date;

				/*
GGA - essential fix data which provide 3D location and accuracy data.
				$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47


					Where:
					     GGA          Global Positioning System Fix Data
					     123519       Fix taken at 12:35:19 UTC
					     4807.038,N   Latitude 48 deg 07.038' N
					     01131.000,E  Longitude 11 deg 31.000' E
					     1            Fix quality: 0 = invalid
					                               1 = GPS fix (SPS)
					                               2 = DGPS fix
					                               3 = PPS fix
											       4 = Real Time Kinematic
											       5 = Float RTK
					                               6 = estimated (dead reckoning) (2.3 feature)
											       7 = Manual input mode
											       8 = Simulation mode
					     08           Number of satellites being tracked
					     0.9          Horizontal dilution of position
					     545.4,M      Altitude, Meters, above mean sea level
					     46.9,M       Height of geoid (mean sea level) above WGS84
					                      ellipsoid
					     (empty field) time in seconds since last DGPS update
					     (empty field) DGPS station ID number
					     *47          the checksum data, always begins with *
					     *
					     				/* fix quality:
				  	0= invalid
					1 = GPS fix (SPS)
					2 = DGPS fix
					3 = PPS fix
					4 = Real Time Kinematic
					5 = Float RTK
					6 = estimated (dead reckoning) (2.3 feature)
					7 = Manual input mode
					8 = Simulation mode


*/

/**
 *
 */
public class PacketGGA extends Packet
{

    public PacketGGA(String as[])
        throws NMEAParserException
    {
        super(as);
    }

    public double getAltitude()
    {
        return altitude;
    }

    public double getDilution()
    {
        return dilution;
    }

    public int getFixQuality()
    {
        return fixQuality;
    }

    public double getGeoidCorrectedAltitude()
    {
        return altitude + geoidHeight;
    }

    public double getGeoidHeight()
    {
        return geoidHeight;
    }

    public String getId()
    {
        return "GPGGA";
    }

    public int getNumberOfSatellites()
    {
        return numberOfSatellites;
    }

    public Geopoint getPosition()
    {
        return position;
    }

    public int getStationId()
    {
        return stationId;
    }

    public Date getTime()
    {
        return time;
    }

    public double getTimeSinceUpdate()
    {
        return timeSinceUpdate;
    }

    protected void parse(String as[])
        throws NMEAParserException
    {
        try
        {
            time = ParserHelper.toTime(as[1]);
            position = ParserHelper.toGeopoint(as[2], as[3], as[4], as[5]);
            fixQuality = ParserHelper.toInt(as[6]);
            numberOfSatellites = ParserHelper.toInt(as[7]);
            dilution = ParserHelper.toDouble(as[8]);
            altitude = ParserHelper.toDouble(as[9]);
            geoidHeight = ParserHelper.toDouble(as[11]);
            timeSinceUpdate = ParserHelper.toDouble(as[13]);
            stationId = ParserHelper.toInt(as[14]);
            return;
        }
        catch(Exception exception)
        {
            throw new NMEAParserException(exception);
        }
    }

    public void setAltitude(double d)
    {
        altitude = d;
    }

    public void setDilution(double d)
    {
        dilution = d;
    }

    public void setFixQuality(int i)
    {
        fixQuality = i;
    }

    public void setGeoidHeight(double d)
    {
        geoidHeight = d;
    }

    public void setNumberOfSatellites(int i)
    {
        numberOfSatellites = i;
    }

    public void setPosition(Geopoint geopoint)
    {
        position = geopoint;
    }

    public void setStationId(int i)
    {
        stationId = i;
    }

    public void setTime(Date date)
    {
        time = date;
    }

    public void setTimeSinceUpdate(double d)
    {
        timeSinceUpdate = d;
    }

    public String toString()
    {
        return ObjectToString.on(this).add("time", time).add("position", position).add("fixQuality", Integer.valueOf(fixQuality)).add("numberOfSatellites", Integer.valueOf(numberOfSatellites)).add("dilution", Double.valueOf(dilution)).add("altitude", Double.valueOf(altitude)).add("geoidHeight", Double.valueOf(geoidHeight)).add("timeSinceUpdate", Double.valueOf(timeSinceUpdate)).add("stationId", Integer.valueOf(stationId)).toString();
    }

    private double altitude;
    private double dilution;
    private int fixQuality;
    private double geoidHeight;
    private int numberOfSatellites;
    private Geopoint position;
    private int stationId;
    private Date time;
    private double timeSinceUpdate;
}
