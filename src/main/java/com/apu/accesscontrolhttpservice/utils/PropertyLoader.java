/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import jdk.nashorn.internal.objects.NativeDebug;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author apu
 */
public class PropertyLoader {
    
    private static final Logger LOGGER = LogManager.getLogger(PropertyLoader.class.getName());
    
    public String getPropertyFromFile(String filename, String property) {
        if((filename == null) || (property == null))
            throw new IllegalArgumentException("Input parameters haven't be null");
        
        String result = null;
        InputStream is = null;
        Properties prop = new Properties();        
        try {
            is = this.getClass().getResourceAsStream("/"+filename);
            prop.load(is);
            result = prop.getProperty(property);            
        } catch (IOException ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
        } finally {            
            try {
                if(is != null)
                    is.close();
            } catch (IOException ex) {
                LOGGER.error(ExceptionUtils.getStackTrace(ex));
            }
        }
        if(result != null)
            return result;
        else
            return null;
    } 
    
}
