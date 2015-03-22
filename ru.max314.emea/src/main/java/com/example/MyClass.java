package com.example;

import com.example.emea.FileUtilsNMEA;
import com.example.emea.NMEAParser;
import com.example.emea.Packet;
import com.example.emea.exception.NMEAParserException;

import java.io.File;
import java.io.FileNotFoundException;

public class MyClass {
    public String FileName = "C:\\work\\android\\NMEA\\logs\\all.log";

    public void Temp(){
        String str ="$GPRMC,151017.000,A,4712.0964,N,03854.4100,E,0.0,34.4,140315,,,A*58";
        NMEAParser parser = new NMEAParser();
        try {
            Packet pack = parser.parse(str);
            String buff = pack.toString();
        } catch (NMEAParserException e) {
            e.printStackTrace();
        }
    }

    public void testFromFile(){
        File file = new File(FileName);
        final NMEAParser nmeaParser = new NMEAParser();
        try {
            //System.out.println(FileUtilsNMEA.getFileContents(file));
            FileUtilsNMEA.processFileLines(file, new FileUtilsNMEA.LineProcessor() {
                @Override
                public void process(int lineNumber, String lineContents) {
                    System.out.println(lineNumber + ": " + lineContents);
                    if (lineContents.startsWith("$")){
                        try {
                            Packet pack = nmeaParser.parse(lineContents);
                            System.out.println(pack.toString());
                        } catch (NMEAParserException e) {
                            System.out.println("NotParsed "+lineContents );
                        }
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void testGSAFromFile(){
        File file = new File(FileName);
        final NMEAParser nmeaParser = new NMEAParser();
        try {
            //System.out.println(FileUtilsNMEA.getFileContents(file));
            FileUtilsNMEA.processFileLines(file, new FileUtilsNMEA.LineProcessor() {
                @Override
                public void process(int lineNumber, String lineContents) {

                    if (lineContents.startsWith("$")){
                        try {
                            Packet pack = nmeaParser.parse(lineContents);
                            if (pack.getId().equals("GPGSA"))
                                System.out.println(pack.toString());
                        } catch (NMEAParserException e) {
                            //System.out.println("NotParsed "+lineContents );
                        }
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void testGGAFromFile(){
        File file = new File(FileName);
        final NMEAParser nmeaParser = new NMEAParser();
        try {
            //System.out.println(FileUtilsNMEA.getFileContents(file));
            FileUtilsNMEA.processFileLines(file, new FileUtilsNMEA.LineProcessor() {
                @Override
                public void process(int lineNumber, String lineContents) {

                    if (lineContents.startsWith("$")){
                        try {
                            if (lineNumber==5000){
                                int j = 10;
                            }
                            Packet pack = nmeaParser.parse(lineContents);
                            if (pack.getId().equals("GPGGA"))
                                System.out.println("line"+lineNumber+ pack.toString());
                        } catch (NMEAParserException e) {
                            System.out.println("NotParsed "+lineContents );
                        }
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


