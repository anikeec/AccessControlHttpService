/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.serializer.Gson;

import com.apu.TcpServerForAccessControlAPI.packet.AccessPacket;
import com.apu.TcpServerForAccessControlAPI.packet.InfoPacket;
import com.apu.TcpServerForAccessControlAPI.packet.MessageType;
import com.apu.TcpServerForAccessControlAPI.packet.RawPacket;
import com.apu.TcpServerForAccessControlAPI.packet.ServicePacket;
import com.apu.accesscontrolhttpservice.serializer.JsonSerializer;
import com.apu.accesscontrolhttpservice.serializer.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
/**
 *
 * @author apu
 */
public class GsonJsonSerializer implements JsonSerializer {
    
    @Override
    public RawPacket deserialize(String inputJson) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd.MM.yy HH:mm:ss");
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        JsonObject object = gson.fromJson(inputJson, JsonObject.class);
        String messageType = null;
        if(object.get("mt")  != null) {
            messageType = object.get("mt").getAsString();
        }
        RawPacket packet = null;
        if((messageType != null) && 
           (messageType.equals(MessageType.ACCESS.toString()))) {
            packet = gson.fromJson(inputJson, AccessPacket.class);
        } else 
        if((messageType != null) && 
           (messageType.equals(MessageType.SERVICE.toString()))) {
            packet = gson.fromJson(inputJson, ServicePacket.class);
        } else 
        if((messageType != null) && 
           (messageType.equals(MessageType.INFO.toString()))) {
            packet = gson.fromJson(inputJson, InfoPacket.class);
        }
        
        return packet;
    }
    
    @Override
    public RawPacket deserializeBytes(byte[] inputBytes) {
        return this.deserialize(new String(inputBytes));
    }
    
    @Override
    public String serialize(RawPacket inputPkt) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd.MM.yy HH:mm:ss");
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String resultJson = gson.toJson(inputPkt);
        return resultJson;
    }
    
    @Override
    public byte[] serializeBytes(RawPacket inputPkt) {
        return this.serialize(inputPkt).getBytes();
    }
    
}
