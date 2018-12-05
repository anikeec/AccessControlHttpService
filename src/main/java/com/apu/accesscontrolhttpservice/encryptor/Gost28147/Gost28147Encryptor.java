/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.encryptor.Gost28147;

import com.apu.accesscontrolhttpservice.utils.PropertyLoader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author apu
 */
public class Gost28147Encryptor {
    
    private final String PROPERTIES_FILE_NAME = "./security.properties";
    private final String SECUR_KEY_PROPERTY = "gost28147.key";
    
    private void eraseAll() {
        for(int i=0; i<buffer_N1.length; i++)
            buffer_N1[i] = 0;
        for(int i=0; i<buffer_N2.length; i++)
            buffer_N2[i] = 0;
        for(int i=0; i<data_buffer.length; i++)
            data_buffer[i] = 0;
        for(int i=0; i<decodebuff.length; i++)
            decodebuff[i] = 0;
        for(int i=0; i<allbuff.length; i++)
            allbuff[i] = 0;
        rezerv_byte = 0;
        key_number = 0;
    }
    
    int[] buffer_N1 = new int[4];
    int[] buffer_N2 = new int[4];
    int[] data_buffer = new int[4];
    byte[] decodebuff = new byte[24];
    byte[] allbuff = new byte[24];
    int rezerv_byte;
    byte key_number;
    
    int[][] s_block = {
            {1,15,13,0,5,7,10,4,9,2,3,14,6,11,8,12},
            {13,11,4,1,3,15,5,9,0,10,14,7,6,8,2,12},
            {4,11,10,0,7,2,1,13,3,6,8,5,9,12,15,14},
            {6,12,7,1,5,15,13,8,4,10,9,14,0,3,11,2},
            {7,13,10,1,0,8,9,15,14,4,6,12,11,2,5,3},
            {5,8,1,13,10,3,4,2,14,15,12,7,6,0,9,11},
            {14,11,4,12,6,13,15,10,2,3,8,1,0,7,5,9},
            {4,10,9,2,13,8,0,14,6,11,1,12,7,15,5,3}
    };

    int[][] k_block = new int[8][4];
    
    /*------------------------------------------------------------------------*/
    public Gost28147Encryptor() {
        loadSecurityKey();
    }
    
    /*------------------------------------------------------------------------*/
    private void loadSecurityKey() {
        String keyStr = 
                PropertyLoader.getPropertyFromFile(PROPERTIES_FILE_NAME, 
                                                    SECUR_KEY_PROPERTY);
        byte[] keyBytes = DatatypeConverter.parseHexBinary(keyStr);
        int index = 0;
        for(int row=0; row<8; row++) {        
            for(int col=0; col<4; col++) {               
                k_block[row][col] = ((int)keyBytes[index++]) & 0xFF;
            }
        }
    }
    
    /*------------------------------------------------------------------------*/
    private void func_F1() {
        data_buffer[0]=(((s_block[6][data_buffer[0]>>4])<<4) + s_block[7][data_buffer[0] & 15]);
        data_buffer[1]=(((s_block[4][data_buffer[1]>>4])<<4) + s_block[5][data_buffer[1] & 15]);
        data_buffer[2]=(((s_block[2][data_buffer[2]>>4])<<4) + s_block[3][data_buffer[2] & 15]);
        data_buffer[3]=(((s_block[0][data_buffer[3]>>4])<<4) + s_block[1][data_buffer[3] & 15]);

        rezerv_byte=data_buffer[3];
        data_buffer[3]=data_buffer[2];
        data_buffer[2]=data_buffer[1];
        data_buffer[1]=data_buffer[0];
        data_buffer[0]=rezerv_byte;

        int ddd0=((data_buffer[0]<<3) & 248);
        int ddd1=((data_buffer[1]<<3) & 248);
        int ddd2=((data_buffer[2]<<3) & 248);
        int ddd3=((data_buffer[3]<<3) & 248);

        int ddt0=(data_buffer[0]>>5);
        int ddt1=(data_buffer[1]>>5);
        int ddt2=(data_buffer[2]>>5);
        int ddt3=(data_buffer[3]>>5);

        data_buffer[0]=(ddd0 + ddt3);
        data_buffer[1]=(ddd1 + ddt0);
        data_buffer[2]=(ddd2 + ddt1);
        data_buffer[3]=(ddd3 + ddt2);
    }
    
    /*------------------------------------------------------------------------*/
    private void addc_4byteN1() {
        int ddd0=(buffer_N1[0] + k_block[key_number][0]);
        data_buffer[0]=(ddd0 & 255);

        int ddd1=(buffer_N1[1] + k_block[key_number][1] + (ddd0>>8));
        data_buffer[1]=(ddd1 & 255);

        int ddd2=(buffer_N1[2] + k_block[key_number][2] + (ddd1>>8));
        data_buffer[2]=(ddd2 & 255);

        int ddd3=(buffer_N1[3] + k_block[key_number][3] + (ddd2>>8));
        data_buffer[3]=(ddd3 & 255);
    }    
    
    /*------------------------------------------------------------------------*/
    private void addc_4byteN2() {
        int ddd0=(buffer_N2[0] + k_block[key_number][0]);
        data_buffer[0]=(ddd0 & 255);

        int ddd1=(buffer_N2[1] + k_block[key_number][1] + (ddd0>>8));
        data_buffer[1]=(ddd1 & 255);

        int ddd2=(buffer_N2[2] + k_block[key_number][2] + (ddd1>>8));
        data_buffer[2]=(ddd2 & 255);

        int ddd3=(buffer_N2[3] + k_block[key_number][3] + (ddd2>>8));
        data_buffer[3]=(ddd3 & 255);
    }    
    
    /*------------------------------------------------------------------------*/
    private void xor_4byteN1() {
        buffer_N1[0]=(buffer_N1[0] ^ data_buffer[0]);
        buffer_N1[1]=(buffer_N1[1] ^ data_buffer[1]);
        buffer_N1[2]=(buffer_N1[2] ^ data_buffer[2]);
        buffer_N1[3]=(buffer_N1[3] ^ data_buffer[3]);
    }
    
    /*------------------------------------------------------------------------*/
    private void xor_4byteN2() {
        buffer_N2[0]=(buffer_N2[0] ^ data_buffer[0]);
        buffer_N2[1]=(buffer_N2[1] ^ data_buffer[1]);
        buffer_N2[2]=(buffer_N2[2] ^ data_buffer[2]);
        buffer_N2[3]=(buffer_N2[3] ^ data_buffer[3]);
    }
    
    /*------------------------------------------------------------------------*/
    private void func01() {
        addc_4byteN1();
        func_F1();
        xor_4byteN2();
    }
    
    /*------------------------------------------------------------------------*/
    private void func02() {
        addc_4byteN2();
        func_F1();
        xor_4byteN1();
    }
    
    /*------------------------------------------------------------------------*/
    private void decrypt01234567() {
        key_number=0;

        func01();
        key_number++;
        func02();
        key_number++;

        func01();
        key_number++;
        func02();
        key_number++;

        func01();
        key_number++;
        func02();
        key_number++;

        func01();
        key_number++;
        func02();
        key_number++;
    }
    
    /*------------------------------------------------------------------------*/
    private void decrypt76543210() {
        key_number=7;

        func01();
        key_number--;
        func02();
        key_number--;

        func01();
        key_number--;
        func02();
        key_number--;

        func01();
        key_number--;
        func02();
        key_number--;

        func01();
        key_number--;
        func02();
        key_number--;
    }
    
    /*------------------------------------------------------------------------*/
    private void gostdecrypt() {
        decrypt01234567();
        decrypt76543210();
        decrypt76543210();
        decrypt76543210();

        int ddd0=buffer_N1[0];
        int ddd1=buffer_N1[1];
        int ddd2=buffer_N1[2];
        int ddd3=buffer_N1[3];

        buffer_N1[0]=buffer_N2[0];
        buffer_N1[1]=buffer_N2[1];
        buffer_N1[2]=buffer_N2[2];
        buffer_N1[3]=buffer_N2[3];

        buffer_N2[0]=ddd0;
        buffer_N2[1]=ddd1;
        buffer_N2[2]=ddd2;
        buffer_N2[3]=ddd3;
    }
    
    /*------------------------------------------------------------------------*/
    private void gostcrypt() {
        
        decrypt01234567();
        decrypt01234567();
        decrypt01234567();
        decrypt76543210();
        
        int ddd0 = buffer_N1[0];
        int ddd1 = buffer_N1[1];
        int ddd2 = buffer_N1[2];
        int ddd3 = buffer_N1[3];
        
        buffer_N1[0]=buffer_N2[0];
        buffer_N1[1]=buffer_N2[1];
        buffer_N1[2]=buffer_N2[2];
        buffer_N1[3]=buffer_N2[3];

        buffer_N2[0]=ddd0;
        buffer_N2[1]=ddd1;
        buffer_N2[2]=ddd2;
        buffer_N2[3]=ddd3;
    }
    
    /*------------------------------------------------------------------------*/
    public String cryptProcess(boolean cryptDecrypt, String inputStr) {
        if(inputStr.length() != 48)
            throw new IllegalArgumentException("Input message length incorrect.");
        eraseAll();
        
        allbuff = DatatypeConverter.parseHexBinary(inputStr);
        
        buffer_N1[0]=((int)allbuff[16])&0xFF;
        buffer_N1[1]=((int)allbuff[17])&0xFF;
        buffer_N1[2]=((int)allbuff[18])&0xFF;
        buffer_N1[3]=((int)allbuff[19])&0xFF;
        buffer_N2[0]=((int)allbuff[20])&0xFF;
        buffer_N2[1]=((int)allbuff[21])&0xFF;
        buffer_N2[2]=((int)allbuff[22])&0xFF;
        buffer_N2[3]=((int)allbuff[23])&0xFF;
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        if(cryptDecrypt)
            gostcrypt();
        else 
            gostdecrypt();
        decodebuff[16]=(byte)buffer_N1[0];
        decodebuff[17]=(byte)buffer_N1[1];
        decodebuff[18]=(byte)buffer_N1[2];
        decodebuff[19]=(byte)buffer_N1[3];
        decodebuff[20]=(byte)buffer_N2[0];
        decodebuff[21]=(byte)buffer_N2[1];
        decodebuff[22]=(byte)buffer_N2[2];
        decodebuff[23]=(byte)buffer_N2[3];

        buffer_N1[0]=((int)allbuff[8])&0xFF;
        buffer_N1[1]=((int)allbuff[9])&0xFF;
        buffer_N1[2]=((int)allbuff[10])&0xFF;
        buffer_N1[3]=((int)allbuff[11])&0xFF;
        buffer_N2[0]=((int)allbuff[12])&0xFF;
        buffer_N2[1]=((int)allbuff[13])&0xFF;
        buffer_N2[2]=((int)allbuff[14])&0xFF;
        buffer_N2[3]=((int)allbuff[15])&0xFF;
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        if(cryptDecrypt)
            gostcrypt();
        else 
            gostdecrypt();
        decodebuff[8]=(byte)(buffer_N1[0]^(((int)allbuff[16])&0xFF));
        decodebuff[9]=(byte)(buffer_N1[1]^(((int)allbuff[17])&0xFF));
        decodebuff[10]=(byte)(buffer_N1[2]^(((int)allbuff[18])&0xFF));
        decodebuff[11]=(byte)(buffer_N1[3]^(((int)allbuff[19])&0xFF));
        decodebuff[12]=(byte)(buffer_N2[0]^(((int)allbuff[20])&0xFF));
        decodebuff[13]=(byte)(buffer_N2[1]^(((int)allbuff[21])&0xFF));
        decodebuff[14]=(byte)(buffer_N2[2]^(((int)allbuff[22])&0xFF));
        decodebuff[15]=(byte)(buffer_N2[3]^(((int)allbuff[23])&0xFF));

        buffer_N1[0]=((int)allbuff[0]&0xFF);
        buffer_N1[1]=((int)allbuff[1]&0xFF);
        buffer_N1[2]=((int)allbuff[2]&0xFF);
        buffer_N1[3]=((int)allbuff[3]&0xFF);
        buffer_N2[0]=((int)allbuff[4]&0xFF);
        buffer_N2[1]=((int)allbuff[5]&0xFF);
        buffer_N2[2]=((int)allbuff[6]&0xFF);
        buffer_N2[3]=((int)allbuff[7]&0xFF);
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        if(cryptDecrypt)
            gostcrypt();
        else 
            gostdecrypt();
        decodebuff[0]=(byte)(buffer_N1[0]^allbuff[16]);
        decodebuff[1]=(byte)(buffer_N1[1]^allbuff[17]);
        decodebuff[2]=(byte)(buffer_N1[2]^allbuff[18]);
        decodebuff[3]=(byte)(buffer_N1[3]^allbuff[19]);
        decodebuff[4]=(byte)(buffer_N2[0]^allbuff[20]);
        decodebuff[5]=(byte)(buffer_N2[1]^allbuff[21]);
        decodebuff[6]=(byte)(buffer_N2[2]^allbuff[22]);
        decodebuff[7]=(byte)(buffer_N2[3]^allbuff[23]);
        
        return new String(Hex.encodeHex(decodebuff));
    }
    
    /*------------------------------------------------------------------------*/
    public String decryptProcess(String inputStr) {
        if(inputStr.length() != 48)
            throw new IllegalArgumentException("Input message length incorrect.");
        eraseAll();
        
        allbuff = DatatypeConverter.parseHexBinary(inputStr);
        
        buffer_N1[0]=allbuff[16];
        buffer_N1[1]=allbuff[17];
        buffer_N1[2]=allbuff[18];
        buffer_N1[3]=allbuff[19];
        buffer_N2[0]=allbuff[20];
        buffer_N2[1]=allbuff[21];
        buffer_N2[2]=allbuff[22];
        buffer_N2[3]=allbuff[23];
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
            gostdecrypt();
        decodebuff[16]=(byte)buffer_N1[0];
        decodebuff[17]=(byte)buffer_N1[1];
        decodebuff[18]=(byte)buffer_N1[2];
        decodebuff[19]=(byte)buffer_N1[3];
        decodebuff[20]=(byte)buffer_N2[0];
        decodebuff[21]=(byte)buffer_N2[1];
        decodebuff[22]=(byte)buffer_N2[2];
        decodebuff[23]=(byte)buffer_N2[3];

        buffer_N1[0]=allbuff[8];
        buffer_N1[1]=allbuff[9];
        buffer_N1[2]=allbuff[10];
        buffer_N1[3]=allbuff[11];
        buffer_N2[0]=allbuff[12];
        buffer_N2[1]=allbuff[13];
        buffer_N2[2]=allbuff[14];
        buffer_N2[3]=allbuff[15];
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
            gostdecrypt();
        decodebuff[8]=(byte)(buffer_N1[0]^allbuff[16]);
        decodebuff[9]=(byte)(buffer_N1[1]^allbuff[17]);
        decodebuff[10]=(byte)(buffer_N1[2]^allbuff[18]);
        decodebuff[11]=(byte)(buffer_N1[3]^allbuff[19]);
        decodebuff[12]=(byte)(buffer_N2[0]^allbuff[20]);
        decodebuff[13]=(byte)(buffer_N2[1]^allbuff[21]);
        decodebuff[14]=(byte)(buffer_N2[2]^allbuff[22]);
        decodebuff[15]=(byte)(buffer_N2[3]^allbuff[23]);

        buffer_N1[0]=allbuff[0];
        buffer_N1[1]=allbuff[1];
        buffer_N1[2]=allbuff[2];
        buffer_N1[3]=allbuff[3];
        buffer_N2[0]=allbuff[4];
        buffer_N2[1]=allbuff[5];
        buffer_N2[2]=allbuff[6];
        buffer_N2[3]=allbuff[7];
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
            gostdecrypt();
        decodebuff[0]=(byte)(buffer_N1[0]^allbuff[16]);
        decodebuff[1]=(byte)(buffer_N1[1]^allbuff[17]);
        decodebuff[2]=(byte)(buffer_N1[2]^allbuff[18]);
        decodebuff[3]=(byte)(buffer_N1[3]^allbuff[19]);
        decodebuff[4]=(byte)(buffer_N2[0]^allbuff[20]);
        decodebuff[5]=(byte)(buffer_N2[1]^allbuff[21]);
        decodebuff[6]=(byte)(buffer_N2[2]^allbuff[22]);
        decodebuff[7]=(byte)(buffer_N2[3]^allbuff[23]);
        
        return new String(Hex.encodeHex(decodebuff));
    }
    
    public static void main(String[] args) {
        Gost28147Encryptor encryptor = new Gost28147Encryptor();
        String result;
        
        String source = "F0B1C9224826CEC1E6C049EBC3242A9DA8EA0FA086FE6805";
        result = 
           encryptor.cryptProcess(true, source);
        System.out.println("Encrypt");
        System.out.println("src: " + source);
        System.out.println("res: " + result);
        
        source = result;
        result = 
           encryptor.cryptProcess(false, source);
        System.out.println("Decrypt");
        System.out.println("src: " + source);
        System.out.println("res: " + result);
 
    }
    
}
