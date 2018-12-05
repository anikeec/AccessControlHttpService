/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.configuration;

import com.apu.TcpServerForAccessControlAPI.packet.AccessPacket;
import com.apu.TcpServerForAccessControlAPI.packet.EventType;
import com.apu.TcpServerForAccessControlAPI.packet.RawPacket;
import com.apu.accesscontrolhttpservice.encryptor.Encryptor;
import com.apu.accesscontrolhttpservice.encryptor.Gost28147.Gost28147Encryptor;
import com.apu.accesscontrolhttpservice.parser.Parser;
import com.apu.accesscontrolhttpservice.parser.Snt.SntParser;
import com.apu.accesscontrolhttpservice.serializer.Gson.GsonJsonSerializer;
import com.apu.accesscontrolhttpservice.serializer.JsonSerializer;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author apu
 */
public class CamelConfiguration implements Configuration {
    
    Encryptor encryptor;
    Parser parser;
    JsonSerializer serializer;
    byte[] encriptionKey;
    String httpHost;
    int httpPort;
    String httpPath;
    String tcpHost;
    int tctPort;
    
    private void initProcesses() {
        encryptor = new Gost28147Encryptor();
        parser = new SntParser();
        serializer = new GsonJsonSerializer();
        httpHost = "0.0.0.0";
        httpPort = 65532;
        httpPath = "add.php";
        tcpHost = "localhost";
        tctPort = 65530;
    }
    
    @Override
    public void configure() {
        
        initProcesses();
        
        CamelContext context = new DefaultCamelContext();        
        try {  
            final Processor httpRouteProcessor = new Processor() {
                public void process(Exchange exchange) throws Exception {
                    String pktdata = (String)exchange.getIn().getHeader("pktdata");
                    String name = (String)exchange.getIn().getHeader("name");
                    
                    //decrypt
//                    pktdata = "0000000000000000"; 
                    byte[] receivedBytes = 
                            DatatypeConverter.parseHexBinary(pktdata);

                    byte[] decodedBytes = encryptor.decode2bytes(receivedBytes);
                        
                    if(!encryptor.crc16Check(decodedBytes))//crc OK
                        decodedBytes = null;
                    
                    //parse
                    Object obj = null;
                    if(decodedBytes != null)
                        obj = parser.parse(decodedBytes);
                    
                    /*
                    //temp
                    int deviceNumber = 15;
                    int packetNumber = 25;
                    AccessPacket packet = new AccessPacket();
                    packet.setEventId(EventType.ENTER_QUERY.getIndex());
                    packet.setDeviceNumber(deviceNumber); 
                    packet.setCardNumber("11111111");
                    packet.setPacketNumber(packetNumber++);
                    packet.setTime(new Date());
                    
                    
                    //serialize
                    byte[] packetBytes;
                    byte[] packetBytesForSend; 
                    packetBytes = serializer.serializeBytes(packet);
                    packetBytesForSend = new byte[packetBytes.length + 2];
                    int i = 0;
                    for(i=0; i<packetBytes.length; i++) {
                        packetBytesForSend[i] = packetBytes[i];
                    }
                    packetBytesForSend[i++] = '\r';
                    packetBytesForSend[i++] = '\n';
                    String sendStr = new String(packetBytesForSend);
                    

                    ProducerTemplate template = 
                                exchange.getContext().createProducerTemplate();

                    Future<Object> future = template.asyncRequestBody(
                          "netty:tcp://" + tcpHost + ":" + tctPort + "?sync=true&textline=true", 
                          sendStr);
                    while(!future.isDone()) {
                        System.out.println("Doing something else while processing..." + future);
                        Thread.sleep(5);
                    }
                    String response = (String) future.get();
                    byte[] responseBytes = (byte[])future.get();
                    
                    //deserialize
                    RawPacket retPacket = 
                                serializer.deserializeBytes(responseBytes);
                    
                    //convert
                    byte[] convertedBytes = null;
                    if(retPacket != null)
                        convertedBytes = parser.convert(retPacket);
                    
                    //encrypt
                    byte[] encryptedBytes = null;
                    if(convertedBytes != null)
                        encryptedBytes = encryptor.encode2bytes(convertedBytes);

                    template.stop();
                    */
                    String response = (String)obj;
                    byte[] encryptedBytes = {};

                    exchange.getOut().setBody(response
                        + new String(encryptedBytes)
                        );                    
                }
            };

            RouteBuilder httpRouteBuilder = new RouteBuilder() {
                @Override
                public void configure() {
                    from("jetty://http://" + httpHost + ":" + httpPort + "/" + httpPath) 
                    .threads()
                    .log("Received a request")  
                    .process(httpRouteProcessor);
                  }
              }; 

            context.addRoutes(httpRouteBuilder);               
            context.start();        
            while(true) {}       
        } catch (Exception ex) {
            Logger.getLogger(CamelConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        } finally {        
            try {        
                context.stop();
            } catch (Exception ex) {
                Logger.getLogger(CamelConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
