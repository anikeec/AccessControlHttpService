/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.parser;

/**
 *
 * @author apu
 */
public interface Parser {
    
    Object parse(byte[] bytes);
    byte[] convert(Object object);
    
}
