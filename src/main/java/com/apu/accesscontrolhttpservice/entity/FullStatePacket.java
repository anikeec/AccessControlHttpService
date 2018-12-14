/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.entity;

import static com.apu.accesscontrolhttpservice.utils.DigitUtils.listToString;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author apu
 */
public class FullStatePacket {
    private int serialNumber;               //device's serial number
    private int logEvent;                   //
    private Date date;
    private Time time;
    @Transient
    private Date dateTime;                  //date&time of message (separate date & time for better productivity)
    private EventZoneState eventZoneState;  //if message was triggered by some event
                                            //this object contains event's details
    private int cmeErrorNumber;             //if we have some event we can see its number
    private List<ZoneLevelState> zoneLevelStateArray; 
                                            //zones levels current state(Shorted | Open)
    private int deviceTemperature;          //device's temperature
    private int sygnalLevel;                //device's GSM sygnal level
    private int deviceVoltage;              //device's supply voltage
    private List<ZoneAlarmState> zoneAlarmStateArray;
                                            //here we can see if some zones have alarm                                            
    private int packetNumber;               //packet number

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
