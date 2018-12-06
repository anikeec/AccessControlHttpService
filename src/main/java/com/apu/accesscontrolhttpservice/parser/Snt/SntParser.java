/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.parser.Snt;

import com.apu.accesscontrolhttpservice.entity.EventZoneState;
import com.apu.accesscontrolhttpservice.entity.FullStatePacket;
import com.apu.accesscontrolhttpservice.entity.ZoneState;
import com.apu.accesscontrolhttpservice.entity.ZoneLevelState;
import com.apu.accesscontrolhttpservice.entity.ZoneAlarmState;
import com.apu.accesscontrolhttpservice.parser.Parser;
import static com.apu.accesscontrolhttpservice.utils.DigitUtils.arrayToString;
import static com.apu.accesscontrolhttpservice.utils.DigitUtils.byte2UnsignedInt;
import static com.apu.accesscontrolhttpservice.utils.DigitUtils.byteArray2Integer;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author apu
 */
public class SntParser implements Parser {

    private final int DEVICE_TYPE_START_BYTE_INDEX = 0;
    private final int DEVICE_TYPE_LENGTH = 1;
    private final int SERIAL_NUMBER_START_BYTE_INDEX = 1;
    private final int SERIAL_NUMBER_LENGTH = 2;
    private final int LOG_EVENT_CODE_START_INDEX = 3;
    private final int LOG_EVENT_CODE_LENGTH = 1;
    private final int TIME_START_INDEX = 4;
    private final int TIME_LENGTH = 4;
    private final int USER_AND_FLAGS_START_INDEX = 8;
    private final int USER_AND_FLAGS_LENGTH = 2;
    private final int ZONE_AND_FLAGS_AND_COUNTER_START_INDEX = 10;
    private final int ZONE_AND_FLAGS_AND_COUNTER_LENGTH = 3;
    private final int CME_ERROR_NUMBER_START_INDEX = 13;
    private final int CME_ERROR_NUMBER_LENGTH = 2;
    private final int ZONE_LEVEL_STATE_BITS_START_INDEX = 15;
    private final int ZONE_LEVEL_STATE_BITS_LENGTH = 1;
    private final int TEMPERATURE_START_INDEX = 16;
    private final int TEMPERATURE_LENGTH = 1;
    private final int SYGNAL_LEVEL_START_INDEX = 17;
    private final int SYGNAL_LEVEL_LENGTH = 1;
    private final int VOLTAGE_LEVEL_START_INDEX = 18;
    private final int VOLTAGE_LEVEL_LENGTH = 1;
    private final int ZONE_ALARM_STATE_BITS_START_INDEX = 19;
    private final int ZONE_ALARM_STATE_BITS_LENGTH = 1;
    private final int PACKET_NUMBER_START_INDEX = 20;
    private final int PACKET_NUMBER_LENGTH = 2;
    private final int CRC16_START_INDEX = 22;
    private final int CRC16_LENGTH = 2;    
            
    private final int PACKET_NUMBER_FOR_ANSWER_BYTE_INDEX = 20;
    
    class PacketWLU {
        
    }
    
    @Override
    public Object parse(byte[] bytes) {
        if(bytes == null)
            throw new IllegalArgumentException("Input buffer hasn't be null");
        
        int serialNumber = parseSerialNumber(bytes);
        
        int logEvent = parseLogEvents(bytes);
        
        String dateTime = parseDateTime(bytes);
        
        //user number and flags
        
        EventZoneState eventZoneState = parseZoneFlagCounter(bytes);      
        
        int cmeErrorNumber = parseCmeErrorNumber(bytes);
        
        ZoneLevelState[] zoneLevelStateArray = 
            zoneLevelStateByte2Array(bytes[ZONE_LEVEL_STATE_BITS_START_INDEX]);
        
        int deviceTemperature = parseTemperature(bytes);
        
        int sygnalLevel = parseSygnalLevel(bytes);
        
        int deviceVoltage = parseVoltage(bytes);
        
        ZoneAlarmState[] zoneAlarmStateArray = 
            zoneAlarmStateByte2Array(bytes[ZONE_ALARM_STATE_BITS_START_INDEX]);

        int packetNumber = 
                byte2UnsignedInt(bytes[PACKET_NUMBER_FOR_ANSWER_BYTE_INDEX]);
        
        FullStatePacket fullStatePacket = null;
        
        String result = 
                "srcString : " + new String(Hex.encodeHex(bytes)) + "\r\n" +
                fullStatePacket.toString();
        
        return "+SRVDBANSW: " + packetNumber + " \r\n" + result;
    }

    @Override
    public byte[] convert(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*------------------------------------------------------------------------*/
    public int parseSerialNumber(byte[] array) {
        return byteArray2Integer(array, 
                                    SERIAL_NUMBER_START_BYTE_INDEX, 
                                    SERIAL_NUMBER_LENGTH);
    }
    
    /*------------------------------------------------------------------------*/
    public int parseLogEvents(byte[] array) {
        return byteArray2Integer(array, 
                                    LOG_EVENT_CODE_START_INDEX, 
                                    LOG_EVENT_CODE_LENGTH);
    }
    
    /*------------------------------------------------------------------------*/
    public String parseDateTime(byte[] array) {
        //TIME_START_INDEX, TIME_LENGTH
        //8320b55a
        return null;
    }
    
    /*------------------------------------------------------------------------*/
    EventZoneState parseZoneFlagCounter(byte[] array) {       
        byte zoneNumber = array[ZONE_AND_FLAGS_AND_COUNTER_START_INDEX];
        int zoneStateIndex = 
            byte2UnsignedInt(array[ZONE_AND_FLAGS_AND_COUNTER_START_INDEX + 1]);
        ZoneState zoneState = ZoneState.values()[zoneStateIndex];
        int zoneCounter = 
            byte2UnsignedInt(array[ZONE_AND_FLAGS_AND_COUNTER_START_INDEX + 2]);
        
        return new EventZoneState(zoneNumber, zoneState, zoneCounter);
    }

    
    private static final int BYTE_TO_BITS = 8; 
    
    /*------------------------------------------------------------------------*/
    public int parseCmeErrorNumber(byte[] array) {
        return byteArray2Integer(array, 
                                    CME_ERROR_NUMBER_START_INDEX, 
                                    CME_ERROR_NUMBER_LENGTH);
    }
    
    /*------------------------------------------------------------------------*/
    public ZoneLevelState[] zoneLevelStateByte2Array(byte input) {
        ZoneLevelState[] resultArray = new ZoneLevelState[BYTE_TO_BITS];
        for(int i=0; i<BYTE_TO_BITS; i++) {
            if(((input >> i)&0x01) == ZoneLevelState.SHORTED.ordinal())
                resultArray[i] = ZoneLevelState.SHORTED;
            else
                resultArray[i] = ZoneLevelState.OPENED;
        }
        return resultArray;
    }
    
    /*------------------------------------------------------------------------*/
    int parseTemperature(byte[] array) {
        int result = byteArray2Integer(array, 
                                        TEMPERATURE_START_INDEX, 
                                        TEMPERATURE_LENGTH);
        if(result == 0x80)
            return 0;
        else
            return result;
    }
    
    /*------------------------------------------------------------------------*/
    int parseSygnalLevel(byte[] array) {
        int result = byteArray2Integer(array, 
                                        SYGNAL_LEVEL_START_INDEX, 
                                        SYGNAL_LEVEL_LENGTH);
        return result;
    }
    
    /*------------------------------------------------------------------------*/
    int parseVoltage(byte[] array) {
        int result = byteArray2Integer(array, 
                                        VOLTAGE_LEVEL_START_INDEX, 
                                        VOLTAGE_LEVEL_LENGTH);
        return result/18;
    }
    
    /*------------------------------------------------------------------------*/
    public ZoneAlarmState[] zoneAlarmStateByte2Array(byte input) {
        ZoneAlarmState[] resultArray = new ZoneAlarmState[BYTE_TO_BITS];
        for(int i=0; i<BYTE_TO_BITS; i++) {
            if(((input >> i)&0x01) == ZoneAlarmState.ALARMED.ordinal())
                resultArray[i] = ZoneAlarmState.ALARMED;
            else
                resultArray[i] = ZoneAlarmState.NORMAL;
        }
        return resultArray;
    }
    
    
    
}
