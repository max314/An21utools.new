package com.example.emea;

import com.example.emea.exception.NMEAParserException;

/**
 * Created by max on 21.03.2015.
 */
public abstract class Packet {
    public Packet(String as[])
            throws NMEAParserException
    {
        init(as);
    }

    public abstract String getId();

    protected void init(String as[])
            throws NMEAParserException
    {
        if(as == null || as.length < 1)
            throw new NMEAParserException("Null or empty NMEA sentense.");
        if(!getId().equals(as[0]))
        {
            throw new NMEAParserException("Invalid sentense type");
        } else
        {
            parse(as);
            return;
        }
    }

    protected abstract void parse(String as[])
            throws NMEAParserException;


}
