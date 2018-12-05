/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice;

import com.apu.accesscontrolhttpservice.configuration.CamelConfiguration;
import com.apu.accesscontrolhttpservice.configuration.Configuration;

/**
 *
 * @author apu
 */
public class Main {
    
    public static void main(String[] args) {
        Configuration config = new CamelConfiguration();
        config.configure();
    }
    
}
