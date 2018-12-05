/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.encryptor.Gost28147;

import com.apu.accesscontrolhttpservice.encryptor.Encryptor;
import javax.xml.bind.DatatypeConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apu
 */
public class Gost28147EncryptorTest {
    
    String encoded = "F0B1C9224826CEC1E6C049EBC3242A9DA8EA0FA086FE6805";
    String decoded = "5000d3368320b55a00000000000000008000d500d20b9315";
    
    public Gost28147EncryptorTest() {
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
     * Test of encode2bytes method, of class Gost28147Encryptor.
     */
    @Test
    public void testEncode2bytes() {
        System.out.println("encode2bytes");
        Encryptor instance = new Gost28147Encryptor();
        byte[] inputBytes = DatatypeConverter.parseHexBinary(decoded);
        byte[] expResult = DatatypeConverter.parseHexBinary(encoded);        
        byte[] result = instance.encode2bytes(inputBytes);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of decode2bytes method, of class Gost28147Encryptor.
     */
    @Test
    public void testDecode2bytes() {
        System.out.println("decode2bytes");
        Encryptor instance = new Gost28147Encryptor();
        byte[] inputBytes = DatatypeConverter.parseHexBinary(encoded);
        byte[] expResult = DatatypeConverter.parseHexBinary(decoded);        
        byte[] result = instance.decode2bytes(inputBytes);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of crc16Check method, of class Gost28147Encryptor.
     */
    @Test
    public void testCrc16Check() {
        System.out.println("crc16Check");        
        Gost28147Encryptor instance = new Gost28147Encryptor();
        byte[] buffer = DatatypeConverter.parseHexBinary(decoded);
        boolean expResult = true;
        boolean result = instance.crc16Check(buffer);
        assertEquals(expResult, result);
        buffer = DatatypeConverter.parseHexBinary(encoded);
        expResult = false;
        result = instance.crc16Check(buffer);
        assertEquals(expResult, result);
    }
    
}
