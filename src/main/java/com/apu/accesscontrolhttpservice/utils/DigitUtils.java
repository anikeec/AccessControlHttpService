/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.utils;

import java.util.List;

/**
 *
 * @author apu
 */
public class DigitUtils {
    
    public static final int BYTE_MASK = 0xFF;
    
    /*------------------------------------------------------------------------*/
    public static int byte2UnsignedInt(byte input) {
        return ((int)input & 0xFF);
    }
    
    /*------------------------------------------------------------------------*/
    public static int byteArray2Integer(byte[] array, int start, int length) {
        if((start+length)>array.length)
            throw new IllegalArgumentException("Wrong parameters");
        int result = 0;
        for(int i=length; i>0; i--) {
            result |= byte2UnsignedInt(array[start+(length-i)]) << (8*(i-1));
        }
        return result;
    }
    
    /*------------------------------------------------------------------------*/
    public static String arrayToString(Object[] array) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : array) {
            sb.append("  ").append(obj).append(" \r\n");
        }
        return sb.toString();
    }
    
    /*------------------------------------------------------------------------*/
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        for(Object obj : list) {
            sb.append("  ").append(obj).append(" \r\n");
        }
        return sb.toString();
    }
    
}
