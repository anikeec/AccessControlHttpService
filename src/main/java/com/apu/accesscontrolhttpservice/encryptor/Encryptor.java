/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.encryptor;

/**
 *
 * @author apu
 */
public interface Encryptor {

    String decode(String str, byte[] key);

    byte[] decode(byte[] str, byte[] key);

    byte[] decodeWithoutPadding(byte[] str, byte[] key);

    byte[] decodeZeroPadding(byte[] str, byte[] key);

    String encode(String str, byte[] key);

    byte[] encode(byte[] str, byte[] key);

    byte[] encodeWithoutPadding(byte[] str, byte[] key);

    byte[] encodeZeroPadding(byte[] str, byte[] key);
    
}
