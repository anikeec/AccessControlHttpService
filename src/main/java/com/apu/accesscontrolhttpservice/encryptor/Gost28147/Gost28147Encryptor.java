/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.encryptor.Gost28147;

import com.apu.accesscontrolhttpservice.encryptor.Encryptor;
import com.apu.accesscontrolhttpservice.utils.PropertyLoader;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author apu
 */
public class Gost28147Encryptor implements Encryptor {
    
    private final String PROPERTIES_FILE_NAME = "security.properties";
    private final String SECUR_KEY_PROPERTY = "gost28147.key";
    
    private void eraseAll() {
        for(int i=0; i<data_buffer.length; i++)
            data_buffer[i] = 0;
        for(int i=0; i<decodebuff.length; i++)
            decodebuff[i] = 0;
    }
    
    int[] data_buffer = new int[4];
    byte[] decodebuff = new byte[24];
    
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

        int rezerv_byte=data_buffer[3];
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
    private void addc_4byteN1(Byte key_number, int[] buffer_N1) {
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
    private void addc_4byteN2(Byte key_number, int[] buffer_N2) {
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
    private void xor_4byteN1(int[] buffer_N1) {
        buffer_N1[0]=(buffer_N1[0] ^ data_buffer[0]);
        buffer_N1[1]=(buffer_N1[1] ^ data_buffer[1]);
        buffer_N1[2]=(buffer_N1[2] ^ data_buffer[2]);
        buffer_N1[3]=(buffer_N1[3] ^ data_buffer[3]);
    }
    
    /*------------------------------------------------------------------------*/
    private void xor_4byteN2(int[] buffer_N2) {
        buffer_N2[0]=(buffer_N2[0] ^ data_buffer[0]);
        buffer_N2[1]=(buffer_N2[1] ^ data_buffer[1]);
        buffer_N2[2]=(buffer_N2[2] ^ data_buffer[2]);
        buffer_N2[3]=(buffer_N2[3] ^ data_buffer[3]);
    }
    
    /*------------------------------------------------------------------------*/
    private void func01(Byte key_number, int[] buffer_N1, int[] buffer_N2) {
        addc_4byteN1(key_number, buffer_N1);
        func_F1();
        xor_4byteN2(buffer_N2);
    }
    
    /*------------------------------------------------------------------------*/
    private void func02(Byte key_number, int[] buffer_N1, int[] buffer_N2) {
        addc_4byteN2(key_number, buffer_N2);
        func_F1();
        xor_4byteN1(buffer_N1);
    }
    
    /*------------------------------------------------------------------------*/
    private void decrypt01234567(Byte key_number, int[] buffer_N1, int[] buffer_N2) {
        key_number=0;

        func01(key_number, buffer_N1, buffer_N2);
        key_number++;
        func02(key_number, buffer_N1, buffer_N2);
        key_number++;

        func01(key_number, buffer_N1, buffer_N2);
        key_number++;
        func02(key_number, buffer_N1, buffer_N2);
        key_number++;

        func01(key_number, buffer_N1, buffer_N2);
        key_number++;
        func02(key_number, buffer_N1, buffer_N2);
        key_number++;

        func01(key_number, buffer_N1, buffer_N2);
        key_number++;
        func02(key_number, buffer_N1, buffer_N2);
        key_number++;
    }
    
    /*------------------------------------------------------------------------*/
    private void decrypt76543210(Byte key_number, int[] buffer_N1, int[] buffer_N2) {
        key_number=7;

        func01(key_number, buffer_N1, buffer_N2);
        key_number--;
        func02(key_number, buffer_N1, buffer_N2);
        key_number--;

        func01(key_number, buffer_N1, buffer_N2);
        key_number--;
        func02(key_number, buffer_N1, buffer_N2);
        key_number--;

        func01(key_number, buffer_N1, buffer_N2);
        key_number--;
        func02(key_number, buffer_N1, buffer_N2);
        key_number--;

        func01(key_number, buffer_N1, buffer_N2);
        key_number--;
        func02(key_number, buffer_N1, buffer_N2);
        key_number--;
    }
    
    /*------------------------------------------------------------------------*/
    private void gostEncode(int[] buffer_N1, int[] buffer_N2) {
        Byte key_number = new Byte((byte)0);
        
        decrypt01234567(key_number, buffer_N1, buffer_N2);
        decrypt76543210(key_number, buffer_N1, buffer_N2);
        decrypt76543210(key_number, buffer_N1, buffer_N2);
        decrypt76543210(key_number, buffer_N1, buffer_N2);

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
    private void gostDecode(int[] buffer_N1, int[] buffer_N2) {
        Byte key_number = new Byte((byte)0);
        
        decrypt01234567(key_number, buffer_N1, buffer_N2);
        decrypt01234567(key_number, buffer_N1, buffer_N2);
        decrypt01234567(key_number, buffer_N1, buffer_N2);
        decrypt76543210(key_number, buffer_N1, buffer_N2);
        
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
    public byte[] decode24bytesProcess(byte[] inputBytes) {
        if(inputBytes.length != 24)
            throw new IllegalArgumentException("Input message length incorrect.");
        eraseAll();
        
        int[] buffer_N1 = new int[4];
        int[] buffer_N2 = new int[4];
        
        buffer_N1[0]=((int)inputBytes[16])&0xFF;
        buffer_N1[1]=((int)inputBytes[17])&0xFF;
        buffer_N1[2]=((int)inputBytes[18])&0xFF;
        buffer_N1[3]=((int)inputBytes[19])&0xFF;
        buffer_N2[0]=((int)inputBytes[20])&0xFF;
        buffer_N2[1]=((int)inputBytes[21])&0xFF;
        buffer_N2[2]=((int)inputBytes[22])&0xFF;
        buffer_N2[3]=((int)inputBytes[23])&0xFF;
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        gostDecode(buffer_N1, buffer_N2);
        decodebuff[16]=(byte)buffer_N1[0];
        decodebuff[17]=(byte)buffer_N1[1];
        decodebuff[18]=(byte)buffer_N1[2];
        decodebuff[19]=(byte)buffer_N1[3];
        decodebuff[20]=(byte)buffer_N2[0];
        decodebuff[21]=(byte)buffer_N2[1];
        decodebuff[22]=(byte)buffer_N2[2];
        decodebuff[23]=(byte)buffer_N2[3];

        buffer_N1[0]=((int)inputBytes[8])&0xFF;
        buffer_N1[1]=((int)inputBytes[9])&0xFF;
        buffer_N1[2]=((int)inputBytes[10])&0xFF;
        buffer_N1[3]=((int)inputBytes[11])&0xFF;
        buffer_N2[0]=((int)inputBytes[12])&0xFF;
        buffer_N2[1]=((int)inputBytes[13])&0xFF;
        buffer_N2[2]=((int)inputBytes[14])&0xFF;
        buffer_N2[3]=((int)inputBytes[15])&0xFF;
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        gostDecode(buffer_N1, buffer_N2);
        decodebuff[8]=(byte)(buffer_N1[0]^(((int)inputBytes[16])&0xFF));
        decodebuff[9]=(byte)(buffer_N1[1]^(((int)inputBytes[17])&0xFF));
        decodebuff[10]=(byte)(buffer_N1[2]^(((int)inputBytes[18])&0xFF));
        decodebuff[11]=(byte)(buffer_N1[3]^(((int)inputBytes[19])&0xFF));
        decodebuff[12]=(byte)(buffer_N2[0]^(((int)inputBytes[20])&0xFF));
        decodebuff[13]=(byte)(buffer_N2[1]^(((int)inputBytes[21])&0xFF));
        decodebuff[14]=(byte)(buffer_N2[2]^(((int)inputBytes[22])&0xFF));
        decodebuff[15]=(byte)(buffer_N2[3]^(((int)inputBytes[23])&0xFF));

        buffer_N1[0]=((int)inputBytes[0]&0xFF);
        buffer_N1[1]=((int)inputBytes[1]&0xFF);
        buffer_N1[2]=((int)inputBytes[2]&0xFF);
        buffer_N1[3]=((int)inputBytes[3]&0xFF);
        buffer_N2[0]=((int)inputBytes[4]&0xFF);
        buffer_N2[1]=((int)inputBytes[5]&0xFF);
        buffer_N2[2]=((int)inputBytes[6]&0xFF);
        buffer_N2[3]=((int)inputBytes[7]&0xFF);
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        gostDecode(buffer_N1, buffer_N2);
        decodebuff[0]=(byte)(buffer_N1[0]^inputBytes[16]);
        decodebuff[1]=(byte)(buffer_N1[1]^inputBytes[17]);
        decodebuff[2]=(byte)(buffer_N1[2]^inputBytes[18]);
        decodebuff[3]=(byte)(buffer_N1[3]^inputBytes[19]);
        decodebuff[4]=(byte)(buffer_N2[0]^inputBytes[20]);
        decodebuff[5]=(byte)(buffer_N2[1]^inputBytes[21]);
        decodebuff[6]=(byte)(buffer_N2[2]^inputBytes[22]);
        decodebuff[7]=(byte)(buffer_N2[3]^inputBytes[23]);
        
        return decodebuff;
    }
    
    /*------------------------------------------------------------------------*/
    public byte[] encode24bytesProcess(byte[] inputBytes) {
        if(inputBytes.length != 24)
            throw new IllegalArgumentException("Input message length incorrect.");
        eraseAll();
        
        int[] buffer_N1 = new int[4];
        int[] buffer_N2 = new int[4];
        
        buffer_N1[0]=((int)inputBytes[16]&0xFF);
        buffer_N1[1]=((int)inputBytes[17]&0xFF);
        buffer_N1[2]=((int)inputBytes[18]&0xFF);
        buffer_N1[3]=((int)inputBytes[19]&0xFF);
        buffer_N2[0]=((int)inputBytes[20]&0xFF);
        buffer_N2[1]=((int)inputBytes[21]&0xFF);
        buffer_N2[2]=((int)inputBytes[22]&0xFF);
        buffer_N2[3]=((int)inputBytes[23]&0xFF);
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        gostEncode(buffer_N1, buffer_N2);
        decodebuff[16]=(byte)buffer_N1[0];
        decodebuff[17]=(byte)buffer_N1[1];
        decodebuff[18]=(byte)buffer_N1[2];
        decodebuff[19]=(byte)buffer_N1[3];
        decodebuff[20]=(byte)buffer_N2[0];
        decodebuff[21]=(byte)buffer_N2[1];
        decodebuff[22]=(byte)buffer_N2[2];
        decodebuff[23]=(byte)buffer_N2[3];

        buffer_N1[0]=((int)inputBytes[8]^decodebuff[16]);
        buffer_N1[1]=((int)inputBytes[9]^decodebuff[17]);
        buffer_N1[2]=((int)inputBytes[10]^decodebuff[18]);
        buffer_N1[3]=((int)inputBytes[11]^decodebuff[19]);
        buffer_N2[0]=((int)inputBytes[12]^decodebuff[20]);
        buffer_N2[1]=((int)inputBytes[13]^decodebuff[21]);
        buffer_N2[2]=((int)inputBytes[14]^decodebuff[22]);
        buffer_N2[3]=((int)inputBytes[15]^decodebuff[23]);
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        gostEncode(buffer_N1, buffer_N2);
        decodebuff[8]=(byte)(buffer_N1[0]);
        decodebuff[9]=(byte)(buffer_N1[1]);
        decodebuff[10]=(byte)(buffer_N1[2]);
        decodebuff[11]=(byte)(buffer_N1[3]);
        decodebuff[12]=(byte)(buffer_N2[0]);
        decodebuff[13]=(byte)(buffer_N2[1]);
        decodebuff[14]=(byte)(buffer_N2[2]);
        decodebuff[15]=(byte)(buffer_N2[3]);

        buffer_N1[0]=((int)inputBytes[0]^decodebuff[16]);
        buffer_N1[1]=((int)inputBytes[1]^decodebuff[17]);
        buffer_N1[2]=((int)inputBytes[2]^decodebuff[18]);
        buffer_N1[3]=((int)inputBytes[3]^decodebuff[19]);
        buffer_N2[0]=((int)inputBytes[4]^decodebuff[20]);
        buffer_N2[1]=((int)inputBytes[5]^decodebuff[21]);
        buffer_N2[2]=((int)inputBytes[6]^decodebuff[22]);
        buffer_N2[3]=((int)inputBytes[7]^decodebuff[23]);
        for(int i=0; i<4; i++) {
            buffer_N1[i] &= 0xFF;
            buffer_N2[i] &= 0xFF;
        }
        gostEncode(buffer_N1, buffer_N2);
        decodebuff[0]=(byte)(buffer_N1[0]);
        decodebuff[1]=(byte)(buffer_N1[1]);
        decodebuff[2]=(byte)(buffer_N1[2]);
        decodebuff[3]=(byte)(buffer_N1[3]);
        decodebuff[4]=(byte)(buffer_N2[0]);
        decodebuff[5]=(byte)(buffer_N2[1]);
        decodebuff[6]=(byte)(buffer_N2[2]);
        decodebuff[7]=(byte)(buffer_N2[3]);
        
        return decodebuff;
    }
    
    public static void main(String[] args) {
        Gost28147Encryptor encryptor = new Gost28147Encryptor();
        String result;
        
        String source = "F0B1C9224826CEC1E6C049EBC3242A9DA8EA0FA086FE6805";
        result = 
           encryptor.decode(source);
        System.out.println("Encrypt");
        System.out.println("src: " + source);
        System.out.println("res: " + result);
        
        source = result;
        result = 
           encryptor.encode(source);
        System.out.println("Decrypt");
        System.out.println("src: " + source);
        System.out.println("res: " + result); 
    }

    @Override
    public String decode(String str) {
        return this.decode(DatatypeConverter.parseHexBinary(str));
    }
    
    @Override
    public String decode(byte[] bytes) {
        return new String(Hex.encodeHex(this.decode2bytes(bytes)));
    }
    
    @Override
    public byte[] decode2bytes(String str) {
        return this.decode2bytes(DatatypeConverter.parseHexBinary(str));
    }

    @Override
    public byte[] decode2bytes(byte[] bytes) {
        return this.decode24bytesProcess(bytes); 
    }

    @Override
    public String encode(String str) {
         return this.encode(DatatypeConverter.parseHexBinary(str));
    }
    
    @Override
    public String encode(byte[] bytes) {
        return new String(Hex.encodeHex(this.encode2bytes(bytes)));
    }

    @Override
    public byte[] encode2bytes(String str) {
        return this.encode2bytes(DatatypeConverter.parseHexBinary(str));
    }
    
    @Override
    public byte[] encode2bytes(byte[] bytes) {
        return this.encode24bytesProcess(bytes);
    }

}
