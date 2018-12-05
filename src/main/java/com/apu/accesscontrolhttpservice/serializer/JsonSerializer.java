/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.serializer;

import com.apu.TcpServerForAccessControlAPI.packet.RawPacket;

/**
 *
 * @author apu
 */
public interface JsonSerializer {

    RawPacket deserialize(String inputJson);

    RawPacket deserializeBytes(byte[] inputBytes);

    String serialize(RawPacket inputPkt);

    byte[] serializeBytes(RawPacket inputPkt);
    
}
