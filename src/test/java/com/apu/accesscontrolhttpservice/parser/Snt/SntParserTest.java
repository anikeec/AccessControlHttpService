/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.parser.Snt;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author apu
 */
public class SntParserTest {
    
    public SntParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class SntParser.
     */
    @Test
    @Ignore
    public void testParse() {
        System.out.println("parse");
        byte[] bytes = null;
        SntParser instance = new SntParser();
        Object expResult = null;
        Object result = instance.parse(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convert method, of class SntParser.
     */
    @Test
    @Ignore
    public void testConvert() {
        System.out.println("convert");
        Object object = null;
        SntParser instance = new SntParser();
        byte[] expResult = null;
        byte[] result = instance.convert(object);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of byteArray2Integer method, of class SntParser.
     */
    @Test
    public void testByteArray2Integer() {
        System.out.println("byteArray2Integer");
        SntParser instance = new SntParser();
        byte[] array = {0x20, 0x00, 0x30, 0x34};
        int start = 0;
        int length = 3;
        int expResult = 0x200030;
        int result = instance.byteArray2Integer(array, start, length);
        assertEquals(expResult, result);
    }
    
}
