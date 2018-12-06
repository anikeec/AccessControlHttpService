/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.entity;

import static com.apu.accesscontrolhttpservice.utils.DigitUtils.listToString;
import java.util.Date;
import java.util.List;

/**
 *
 * @author apu
 */
public class FullStatePacket {
    private int serialNumber;
    private int logEvent;
    private Date dateTime;        
    private EventZoneState eventZoneState;        
    private int cmeErrorNumber;
    private List<ZoneLevelState> zoneLevelStateArray;
    private int deviceTemperature;
    private int sygnalLevel;
    private int deviceVoltage;
    private List<ZoneAlarmState> zoneAlarmStateArray;
    private int packetNumber;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();        
        sb.append("serialNumber : ").append(serialNumber).append("\r\n");
        sb.append("logEvent : ").append(logEvent).append("\r\n");
        sb.append("dateTime : ").append(dateTime).append("\r\n");
        sb.append(eventZoneState.toString());
        sb.append("cmeErrorNumber : ").append(cmeErrorNumber).append("\r\n");
        sb.append("zoneLevelStates :").append("\r\n");
        sb.append(listToString(zoneLevelStateArray));
        sb.append("deviceTemperature : ").append(deviceTemperature).append("\r\n");
        sb.append("sygnalLevel : ").append(sygnalLevel).append("\r\n");
        sb.append("deviceVoltage : ").append(deviceVoltage).append("\r\n");
        sb.append("zoneAlarmStates : ").append("\r\n");
        sb.append(listToString(zoneAlarmStateArray));
        return sb.toString();
    }
    
    
}
