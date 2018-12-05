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

    String decode(String str);

    byte[] decode(byte[] str);

    String encode(String str);

    byte[] encode(byte[] str);
    
}
