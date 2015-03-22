// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.example.emea;

/*
 Satellites in View

  $GPGSV,2,1,08,01,40,083,46,02,17,308,41,12,07,344,39,14,22,228,45*75

Where:
      GSV          Satellites in view
      2            Number of sentences for full data
      1            sentence 1 of 2
      08           Number of satellites in view

      01           Satellite PRN number
      40           Elevation, degrees
      083          Azimuth, degrees
      46           SNR - higher is better
           for up to 4 satellites per sentence
      *75          the checksum data, always begins with *

 */

// Referenced classes of package googoo.android.common.nmea:
//            Packet, ParserHelper, ObjectToString

import com.example.emea.exception.NMEAParserException;

public class PacketGSV extends Packet {
    private int numberOfSentences;
    private Satellite[] satellites;
    private int satellitesInView;
    private int sentenceNumber;

    public static class Satellite {
        private int azimuth;
        private int elevation;
        private int snr;
        private int svId;

        public int getSvId() {
            return this.svId;
        }

        public int getElevation() {
            return this.elevation;
        }

        public int getAzimuth() {
            return this.azimuth;
        }

        public int getSnr() {
            return this.snr;
        }

        public String toString() {
            return ObjectToString.on(this).add("svId", Integer.valueOf(this.svId)).add("elevation", Integer.valueOf(this.elevation)).add("azimuth", Integer.valueOf(this.azimuth)).add("snr", Integer.valueOf(this.snr)).toString();
        }
    }

    public PacketGSV(String[] tokens) throws NMEAParserException {
        super(tokens);
    }

    public String getId() {
        return "GPGSV";
    }

    protected void parse(String[] s) throws NMEAParserException {
        try {
            this.numberOfSentences = ParserHelper.toInt(s[1]);
            this.sentenceNumber = ParserHelper.toInt(s[2]);
            this.satellitesInView = ParserHelper.toInt(s[3]);
            this.satellites = new Satellite[(this.sentenceNumber < this.numberOfSentences ? 4 : this.satellitesInView - ((this.sentenceNumber - 1) * 4))];
            for (int i = 0; i < this.satellites.length; i++) {
                Satellite sat = new Satellite();
                sat.svId = ParserHelper.toInt(s[(i * 4) + 4]);
                sat.elevation = ParserHelper.toInt(s[(i * 4) + 5], -1);
                sat.azimuth = ParserHelper.toInt(s[(i * 4) + 6], -1);
                sat.snr = ParserHelper.toInt(s[(i * 4) + 7], -1);
                this.satellites[i] = sat;
            }
        } catch (Throwable e) {
            throw new NMEAParserException(e);
        }
    }

    public int getNumberOfSentences() {
        return this.numberOfSentences;
    }

    public int getSentenceNumber() {
        return this.sentenceNumber;
    }

    public int getSatellitesInView() {
        return this.satellitesInView;
    }

    public Satellite[] getSatellites() {
        return this.satellites;
    }

    public String toString() {
        return ObjectToString.on(this).add("numberOfSentences", Integer.valueOf(this.numberOfSentences)).add("sentenceNumber", Integer.valueOf(this.sentenceNumber)).add("satellitesInView", Integer.valueOf(this.satellitesInView)).add("satellites", this.satellites).toString();
    }
}