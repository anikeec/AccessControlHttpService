/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.parser.Snt;

import com.apu.accesscontrolhttpservice.parser.Parser;

/**
 *
 * @author apu
 */
public class SntParser implements Parser {

    private final int SERIAL_NUMBER_START_BYTE_INDEX = 0;
    private final int SERIAL_NUMBER_LENGTH = 6;
    private final int LOG_EVENT_CODE_START_INDEX = 6;
    private final int LOG_EVENT_CODE_LENGTH = 2;
    private final int TIME_START_INDEX = 8;
    private final int TIME_LENGTH = 8;
    private final int USER_AND_FLAGS_START_INDEX = 16;
    private final int USER_AND_FLAGS_LENGTH = 4;
    private final int ZONE_AND_FLAGS_AND_COUNTER_START_INDEX = 20;
    private final int ZONE_AND_FLAGS_AND_COUNTER_LENGTH = 6;
    private final int ERROR_NUMBER_START_INDEX = 26;
    private final int ERROR_NUMBER_LENGTH = 4;
    private final int STATE_8_ZONE_START_INDEX = 30;
    private final int STATE_8_ZONE_LENGTH = 2;
    private final int TEMPERATURE_START_INDEX = 32;
    private final int TEMPERATURE_LENGTH = 2;
    private final int SYGNAL_LEVEL_START_INDEX = 34;
    private final int SYGNAL_LEVEL_LENGTH = 2;
    private final int VOLTAGE_LEVEL_START_INDEX = 36;
    private final int VOLTAGE_LEVEL_LENGTH = 2;
    private final int STATE2_8_ZONE_START_INDEX = 38;
    private final int STATE2_8_ZONE_LENGTH = 2;
    private final int PACKET_NUMBER_START_INDEX = 40;
    private final int PACKET_NUMBER_LENGTH = 4;
    private final int CRC16_START_INDEX = 44;
    private final int CRC16_LENGTH = 4;    
            
    private final int PACKET_NUMBER_FOR_ANSWER_BYTE_INDEX = 20;
    
    
    
    @Override
    public Object parse(byte[] bytes) {
        if(bytes == null)
            throw new IllegalArgumentException("Input buffer hasn't be null");
        
        int serialNumber = byteArray2Integer(bytes, 
                                    SERIAL_NUMBER_START_BYTE_INDEX, 
                                    SERIAL_NUMBER_LENGTH/2);
        
        int packetNumber = (int)bytes[PACKET_NUMBER_FOR_ANSWER_BYTE_INDEX]&0xFF;
        
        return "+SRVDBANSW: " + packetNumber + " \r\n";
    }

    @Override
    public byte[] convert(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*------------------------------------------------------------------------*/
    public int byteArray2Integer(byte[] array, int start, int length) {
        if((start+length)>array.length)
            throw new IllegalArgumentException("Wrong parameters");
        int result = 0;
        for(int i=length; i>0; i--) {
            result |= ((int)array[start+(length-i)] & 0xFF) << (8*(i-1));
        }
        return result;
    }
    
}
