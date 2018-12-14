/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.entity;

/**
 *
 * @author apu
 */
public class EventZoneState {
    
    private int zoneNumber;         //zone's number
    private ZoneState zoneState;    //zone's state (NORMAL | SHORTED | NORMAL1 | OPENED)
    private int zoneCounter;        //zone's alarm counter

    public EventZoneState(int zoneNumber, ZoneState zoneState, int zoneCounter) {
        this.zoneNumber = zoneNumber;
        this.zoneState = zoneState;
        this.zoneCounter = zoneCounter;
    }
    
    public int getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public ZoneState getZoneState() {
        return zoneState;
    }

    public void setZoneState(ZoneState zoneState) {
        this.zoneState = zoneState;
    }

    public int getZoneCounter() {
        return zoneCounter;
    }

    public void setZoneCounter(int zoneCounter) {
        this.zoneCounter = zoneCounter;
    } 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event zone states : ").append("\r\n");
        sb.append("  zoneNumber : ").append(zoneNumber).append("\r\n");
        sb.append("  zoneState : ").append(zoneState).append("\r\n");
        sb.append("  zoneCounter : ").append(zoneCounter).append("\r\n");        
        return sb.toString();
    }
    
    
    
}
