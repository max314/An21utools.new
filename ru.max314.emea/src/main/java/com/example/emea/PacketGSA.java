// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;
import com.example.emea.exception.NMEAParserException;


/*
 GSA - GPS DOP and active satellites. This sentence provides details on the nature of the fix. It includes the numbers of the satellites being used in the current solution and the DOP. DOP (dilution of precision) is an indication of the effect of satellite geometry on the accuracy of the fix. It is a unitless number where smaller is better. For 3D fixes using 4 satellites a 1.0 would be considered to be a perfect number, however for overdetermined solutions it is possible to see numbers below 1.0.

				 $GPGSA,A,3,04,05,,09,12,,,24,,,,,2.5,1.3,2.1*39

					Where:
					     GSA      Satellite status
					     A        Auto selection of 2D or 3D fix (M = manual)
					     3        3D fix - values include: 1 = no fix
					                                       2 = 2D fix
					                                       3 = 3D fix
					     04,05... PRNs of satellites used for fix (space for 12)
					     2.5      PDOP (Position dilution of precision)
					     1.3      Horizontal dilution of precision (HDOP)
					     2.1      Vertical dilution of precision (VDOP)
					     *39      the checksum data, always begins with *
				 */


/**
 *
 */
public class PacketGSA extends Packet
{

    public PacketGSA(String as[])
        throws NMEAParserException
    {
        super(as);
    }

    public int getFixType()
    {
        return fixType;
    }

    public double getHDOP()
    {
        return HDOP;
    }

    public String getId()
    {
        return "GPGSA";
    }

    public int getNumSatelliteUsed()
    {
        return numSatelliteUsed;
    }

    public double getPDOP()
    {
        return PDOP;
    }

    public int[] getSatelliteUsed()
    {
        return satelliteUsed;
    }

    public double getVDOP()
    {
        return VDOP;
    }

    public boolean isAutoMode()
    {
        return autoMode;
    }

    protected void parse(String[] s) throws NMEAParserException {
        try {
            int len = s.length;
            this.autoMode = "A".equals(s[1]);
            this.fixType = ParserHelper.toInt(s[2]);
            this.satelliteUsed = new int[12];
            int i = 0;
            while (i < 12 && i + 3 < len - 3) {
                this.satelliteUsed[i] = ParserHelper.toInt(s[i + 3]);
                if (this.satelliteUsed[i] > 0) {
                    this.numSatelliteUsed++;
                }
                i++;
            }
            this.PDOP = ParserHelper.toDouble(s[len - 3]);
            this.HDOP = ParserHelper.toDouble(s[len - 2]);
            this.VDOP = ParserHelper.toDouble(s[len - 1]);
        } catch (Throwable e) {
            throw new NMEAParserException(e);
        }
    }


    public String toString()
    {
        return ObjectToString.on(this).add("autoMode", Boolean.valueOf(autoMode)).add("fixType", Integer.valueOf(fixType)).add("satelliteUsed", satelliteUsed).add("PDOP", Double.valueOf(PDOP)).add("HDOP", Double.valueOf(HDOP)).add("VDOP", Double.valueOf(VDOP)).toString();
    }

    private double HDOP;
    private double PDOP;
    private double VDOP;
    private boolean autoMode;
    private int fixType;
    private int numSatelliteUsed;
    private int satelliteUsed[];
}
