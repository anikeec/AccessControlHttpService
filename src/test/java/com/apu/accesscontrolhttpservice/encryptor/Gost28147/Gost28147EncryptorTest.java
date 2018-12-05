/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.encryptor.Gost28147;

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
     * Test of cryptProcess method, of class Gost28147Encryptor.
     */
    @Test
    public void testCryptProcess() {
        System.out.println("cryptProcess");
        boolean cryptDecrypt = false;
        String inputStr = "";
        Gost28147Encryptor instance = new Gost28147Encryptor();
        String expResult = "";
        String result = instance.cryptProcess(cryptDecrypt, inputStr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decryptProcess method, of class Gost28147Encryptor.
     */
    @Test
    public void testDecryptProcess() {
        System.out.println("decryptProcess");
        String inputStr = encoded;
        Gost28147Encryptor instance = new Gost28147Encryptor();
        String expResult = decoded;
        String result = instance.cryptProcess(true, inputStr);
        assertEquals(expResult, result);
    }
    
}
