package com.example;

import junit.framework.TestCase;

public class MyClassTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testFirst() throws Exception {
        MyClass myClass = new MyClass();
        myClass.Temp();

    }
    public void testFile() throws Exception {
        MyClass myClass = new MyClass();
        myClass.testFromFile();

    }
    public void testGSAFile() throws Exception {
        MyClass myClass = new MyClass();
        myClass.testGSAFromFile();

    }
    public void testGGAFile() throws Exception {
        MyClass myClass = new MyClass();
        myClass.testGGAFromFile();

    }


}