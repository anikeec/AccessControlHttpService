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
    
    String decode(byte[] bytes);
    
    byte[] decode2bytes(String str);

    byte[] decode2bytes(byte[] bytes);

    String encode(String str);
    
    String encode(byte[] bytes);
    
    byte[] encode2bytes(String str);

    byte[] encode2bytes(byte[] bytes);
    
}
