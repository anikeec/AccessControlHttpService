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
 */
public class Gost28147Encryptor implements Encryptor {
    
    private final String PROPERTIES_FILE_NAME = "security.properties";
    private final String SECUR_KEY_PROPERTY = "gost28147.key";
    private final int KEY_BLOCK_ROWS = 8;
    private final int KEY_BLOCK_COLS = 4;
    private final int HANDLING_BUF_SIZE = 24;
    private final int BYTE_MASK = 0xFF;
    
    private int[][] keyBlock;
    private int[][] sBlock = {
            {1,15,13,0,5,7,10,4,9,2,3,14,6,11,8,12},
            {13,11,4,1,3,15,5,9,0,10,14,7,6,8,2,12},
            {4,11,10,0,7,2,1,13,3,6,8,5,9,12,15,14},
            {6,12,7,1,5,15,13,8,4,10,9,14,0,3,11,2},
            {7,13,10,1,0,8,9,15,14,4,6,12,11,2,5,3},
            {5,8,1,13,10,3,4,2,14,15,12,7,6,0,9,11},
            {14,11,4,12,6,13,15,10,2,3,8,1,0,7,5,9},
            {4,10,9,2,13,8,0,14,6,11,1,12,7,15,5,3}
    };
    
    /*------------------------------------------------------------------------*/
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
    
    /*------------------------------------------------------------------------*/
    public Gost28147Encryptor() {
        keyBlock = new int[KEY_BLOCK_ROWS][KEY_BLOCK_COLS];
        loadSecurityKey(keyBlock);
    }
    
    /*------------------------------------------------------------------------*/
    private void loadSecurityKey(int[][] keyBlock) {        
        String keyStr = 
                PropertyLoader.getPropertyFromFile(PROPERTIES_FILE_NAME, 
                                                    SECUR_KEY_PROPERTY);
        byte[] keyBytes = DatatypeConverter.parseHexBinary(keyStr);
        int index = 0;
        for(int row=0; row<KEY_BLOCK_ROWS; row++) {        
            for(int col=0; col<KEY_BLOCK_COLS; col++) {               
                keyBlock[row][col] = ((int)keyBytes[index++]) & 0xFF;
            }
        }
    }
    
    /*------------------------------------------------------------------------*/
    private void funcF1(int[] dataBuffer) {
        dataBuffer[0]=(((sBlock[6][dataBuffer[0]>>4])<<4) + sBlock[7][dataBuffer[0] & 15]);
        dataBuffer[1]=(((sBlock[4][dataBuffer[1]>>4])<<4) + sBlock[5][dataBuffer[1] & 15]);
        dataBuffer[2]=(((sBlock[2][dataBuffer[2]>>4])<<4) + sBlock[3][dataBuffer[2] & 15]);
        dataBuffer[3]=(((sBlock[0][dataBuffer[3]>>4])<<4) + sBlock[1][dataBuffer[3] & 15]);

        int rezerv_byte=dataBuffer[3];
        dataBuffer[3]=dataBuffer[2];
        dataBuffer[2]=dataBuffer[1];
        dataBuffer[1]=dataBuffer[0];
        dataBuffer[0]=rezerv_byte;

        int ddd0=((dataBuffer[0]<<3) & 248);
        int ddd1=((dataBuffer[1]<<3) & 248);
        int ddd2=((dataBuffer[2]<<3) & 248);
        int ddd3=((dataBuffer[3]<<3) & 248);

        int ddt0=(dataBuffer[0]>>5);
        int ddt1=(dataBuffer[1]>>5);
        int ddt2=(dataBuffer[2]>>5);
        int ddt3=(dataBuffer[3]>>5);

        dataBuffer[0]=(ddd0 + ddt3);
        dataBuffer[1]=(ddd1 + ddt0);
        dataBuffer[2]=(ddd2 + ddt1);
        dataBuffer[3]=(ddd3 + ddt2);
    }
    
    /*------------------------------------------------------------------------*/
    private void addc4byteN1(Byte keyNumber, int[] bufferN1, int[] dataBuffer) {
        int ddd0=(bufferN1[0] + keyBlock[keyNumber][0]);
        dataBuffer[0]=(ddd0 & BYTE_MASK);

        int ddd1=(bufferN1[1] + keyBlock[keyNumber][1] + (ddd0>>8));
        dataBuffer[1]=(ddd1 & BYTE_MASK);

        int ddd2=(bufferN1[2] + keyBlock[keyNumber][2] + (ddd1>>8));
        dataBuffer[2]=(ddd2 & BYTE_MASK);

        int ddd3=(bufferN1[3] + keyBlock[keyNumber][3] + (ddd2>>8));
        dataBuffer[3]=(ddd3 & BYTE_MASK);
    }    
    
    /*------------------------------------------------------------------------*/
    private void addc4byteN2(Byte keyNumber, int[] bufferN2, int[] dataBuffer) {
        int ddd0=(bufferN2[0] + keyBlock[keyNumber][0]);
        dataBuffer[0]=(ddd0 & BYTE_MASK);

        int ddd1=(bufferN2[1] + keyBlock[keyNumber][1] + (ddd0>>8));
        dataBuffer[1]=(ddd1 & BYTE_MASK);

        int ddd2=(bufferN2[2] + keyBlock[keyNumber][2] + (ddd1>>8));
        dataBuffer[2]=(ddd2 & BYTE_MASK);

        int ddd3=(bufferN2[3] + keyBlock[keyNumber][3] + (ddd2>>8));
        dataBuffer[3]=(ddd3 & BYTE_MASK);
    }    
    
    /*------------------------------------------------------------------------*/
    private void xor4byteN1(int[] bufferN1, int[] dataBuffer) {
        bufferN1[0]=(bufferN1[0] ^ dataBuffer[0]);
        bufferN1[1]=(bufferN1[1] ^ dataBuffer[1]);
        bufferN1[2]=(bufferN1[2] ^ dataBuffer[2]);
        bufferN1[3]=(bufferN1[3] ^ dataBuffer[3]);
    }
    
    /*------------------------------------------------------------------------*/
    private void xor4byteN2(int[] bufferN2, int[] dataBuffer) {
        bufferN2[0]=(bufferN2[0] ^ dataBuffer[0]);
        bufferN2[1]=(bufferN2[1] ^ dataBuffer[1]);
        bufferN2[2]=(bufferN2[2] ^ dataBuffer[2]);
        bufferN2[3]=(bufferN2[3] ^ dataBuffer[3]);
    }
    
    /*------------------------------------------------------------------------*/
    private void func01(Byte keyNumber, int[] bufferN1, int[] bufferN2, int[] dataBuffer) {
        addc4byteN1(keyNumber, bufferN1, dataBuffer);
        funcF1(dataBuffer);
        xor4byteN2(bufferN2, dataBuffer);
    }
    
    /*------------------------------------------------------------------------*/
    private void func02(Byte keyNumber, int[] bufferN1, int[] bufferN2, int[] dataBuffer) {
        addc4byteN2(keyNumber, bufferN2, dataBuffer);
        funcF1(dataBuffer);
        xor4byteN1(bufferN1, dataBuffer);
    }
    
    /*------------------------------------------------------------------------*/
    private void decrypt01234567(Byte keyNumber, int[] bufferN1, int[] bufferN2, int[] dataBuffer) {
        keyNumber=0;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber++;
    }
    
    /*------------------------------------------------------------------------*/
    private void decrypt76543210(Byte keyNumber, int[] bufferN1, int[] bufferN2, int[] dataBuffer) {
        keyNumber=7;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;

        func01(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;
        func02(keyNumber, bufferN1, bufferN2, dataBuffer);
        keyNumber--;
    }
    
    /*------------------------------------------------------------------------*/
    private void gostEncode(int[] bufferN1, int[] bufferN2) {
        Byte keyNumber = new Byte((byte)0);
        int[] dataBuffer = new int[4];
        
        decrypt01234567(keyNumber, bufferN1, bufferN2, dataBuffer);
        decrypt76543210(keyNumber, bufferN1, bufferN2, dataBuffer);
        decrypt76543210(keyNumber, bufferN1, bufferN2, dataBuffer);
        decrypt76543210(keyNumber, bufferN1, bufferN2, dataBuffer);

        int ddd0=bufferN1[0];
        int ddd1=bufferN1[1];
        int ddd2=bufferN1[2];
        int ddd3=bufferN1[3];

        bufferN1[0]=bufferN2[0];
        bufferN1[1]=bufferN2[1];
        bufferN1[2]=bufferN2[2];
        bufferN1[3]=bufferN2[3];

        bufferN2[0]=ddd0;
        bufferN2[1]=ddd1;
        bufferN2[2]=ddd2;
        bufferN2[3]=ddd3;
    }
    
    /*------------------------------------------------------------------------*/
    private void gostDecode(int[] bufferN1, int[] bufferN2) {
        Byte keyNumber = new Byte((byte)0);
        int[] dataBuffer = new int[4];
        
        decrypt01234567(keyNumber, bufferN1, bufferN2, dataBuffer);
        decrypt01234567(keyNumber, bufferN1, bufferN2, dataBuffer);
        decrypt01234567(keyNumber, bufferN1, bufferN2, dataBuffer);
        decrypt76543210(keyNumber, bufferN1, bufferN2, dataBuffer);
        
        int ddd0 = bufferN1[0];
        int ddd1 = bufferN1[1];
        int ddd2 = bufferN1[2];
        int ddd3 = bufferN1[3];
        
        bufferN1[0]=bufferN2[0];
        bufferN1[1]=bufferN2[1];
        bufferN1[2]=bufferN2[2];
        bufferN1[3]=bufferN2[3];

        bufferN2[0]=ddd0;
        bufferN2[1]=ddd1;
        bufferN2[2]=ddd2;
        bufferN2[3]=ddd3;
    }
    
    /*------------------------------------------------------------------------*/
    private int[] byteBuf2IntBuf(byte[] bytes) {
        int[] result = new int[bytes.length];
        for(int i=0; i<bytes.length; i++) {
            result[i] = ((int)bytes[i]) & BYTE_MASK;
        }
        return result;
    }
    
    /*------------------------------------------------------------------------*/
    public byte[] decode24bytesProcess(byte[] input) {
        if(input.length != HANDLING_BUF_SIZE)
            throw new IllegalArgumentException("Input message length incorrect.");
        
        int[] bufferN1 = new int[4];
        int[] bufferN2 = new int[4];
        byte[] decodebuff = new byte[HANDLING_BUF_SIZE];
        int[] inputBytes = byteBuf2IntBuf(input);
        
        bufferN1[0]=inputBytes[16];
        bufferN1[1]=inputBytes[17];
        bufferN1[2]=inputBytes[18];
        bufferN1[3]=inputBytes[19];
        bufferN2[0]=inputBytes[20];
        bufferN2[1]=inputBytes[21];
        bufferN2[2]=inputBytes[22];
        bufferN2[3]=inputBytes[23];
        for(int i=0; i<4; i++) {
            bufferN1[i] &= BYTE_MASK;
            bufferN2[i] &= BYTE_MASK;
        }
        gostDecode(bufferN1, bufferN2);
        decodebuff[16]=(byte)bufferN1[0];
        decodebuff[17]=(byte)bufferN1[1];
        decodebuff[18]=(byte)bufferN1[2];
        decodebuff[19]=(byte)bufferN1[3];
        decodebuff[20]=(byte)bufferN2[0];
        decodebuff[21]=(byte)bufferN2[1];
        decodebuff[22]=(byte)bufferN2[2];
        decodebuff[23]=(byte)bufferN2[3];

        bufferN1[0]=inputBytes[8];
        bufferN1[1]=inputBytes[9];
        bufferN1[2]=inputBytes[10];
        bufferN1[3]=inputBytes[11];
        bufferN2[0]=inputBytes[12];
        bufferN2[1]=inputBytes[13];
        bufferN2[2]=inputBytes[14];
        bufferN2[3]=inputBytes[15];
        for(int i=0; i<4; i++) {
            bufferN1[i] &= BYTE_MASK;
            bufferN2[i] &= BYTE_MASK;
        }
        gostDecode(bufferN1, bufferN2);
        decodebuff[8]=(byte)(bufferN1[0]^inputBytes[16]);
        decodebuff[9]=(byte)(bufferN1[1]^inputBytes[17]);
        decodebuff[10]=(byte)(bufferN1[2]^inputBytes[18]);
        decodebuff[11]=(byte)(bufferN1[3]^inputBytes[19]);
        decodebuff[12]=(byte)(bufferN2[0]^inputBytes[20]);
        decodebuff[13]=(byte)(bufferN2[1]^inputBytes[21]);
        decodebuff[14]=(byte)(bufferN2[2]^inputBytes[22]);
        decodebuff[15]=(byte)(bufferN2[3]^inputBytes[23]);

        bufferN1[0]=inputBytes[0];
        bufferN1[1]=inputBytes[1];
        bufferN1[2]=inputBytes[2];
        bufferN1[3]=inputBytes[3];
        bufferN2[0]=inputBytes[4];
        bufferN2[1]=inputBytes[5];
        bufferN2[2]=inputBytes[6];
        bufferN2[3]=inputBytes[7];
        for(int i=0; i<4; i++) {
            bufferN1[i] &= BYTE_MASK;
            bufferN2[i] &= BYTE_MASK;
        }
        gostDecode(bufferN1, bufferN2);
        decodebuff[0]=(byte)(bufferN1[0]^inputBytes[16]);
        decodebuff[1]=(byte)(bufferN1[1]^inputBytes[17]);
        decodebuff[2]=(byte)(bufferN1[2]^inputBytes[18]);
        decodebuff[3]=(byte)(bufferN1[3]^inputBytes[19]);
        decodebuff[4]=(byte)(bufferN2[0]^inputBytes[20]);
        decodebuff[5]=(byte)(bufferN2[1]^inputBytes[21]);
        decodebuff[6]=(byte)(bufferN2[2]^inputBytes[22]);
        decodebuff[7]=(byte)(bufferN2[3]^inputBytes[23]);
        
        return decodebuff;
    }
    
    /*------------------------------------------------------------------------*/
    public byte[] encode24bytesProcess(byte[] input) {
        if(input.length != HANDLING_BUF_SIZE)
            throw new IllegalArgumentException("Input message length incorrect.");
        
        int[] bufferN1 = new int[4];
        int[] bufferN2 = new int[4];
        byte[] decodebuff = new byte[HANDLING_BUF_SIZE];
        int[] inputBytes = byteBuf2IntBuf(input);
        
        bufferN1[0]=inputBytes[16];
        bufferN1[1]=inputBytes[17];
        bufferN1[2]=inputBytes[18];
        bufferN1[3]=inputBytes[19];
        bufferN2[0]=inputBytes[20];
        bufferN2[1]=inputBytes[21];
        bufferN2[2]=inputBytes[22];
        bufferN2[3]=inputBytes[23];
        for(int i=0; i<4; i++) {
            bufferN1[i] &= BYTE_MASK;
            bufferN2[i] &= BYTE_MASK;
        }
        gostEncode(bufferN1, bufferN2);
        decodebuff[16]=(byte)bufferN1[0];
        decodebuff[17]=(byte)bufferN1[1];
        decodebuff[18]=(byte)bufferN1[2];
        decodebuff[19]=(byte)bufferN1[3];
        decodebuff[20]=(byte)bufferN2[0];
        decodebuff[21]=(byte)bufferN2[1];
        decodebuff[22]=(byte)bufferN2[2];
        decodebuff[23]=(byte)bufferN2[3];

        bufferN1[0]=inputBytes[8]^decodebuff[16];
        bufferN1[1]=inputBytes[9]^decodebuff[17];
        bufferN1[2]=inputBytes[10]^decodebuff[18];
        bufferN1[3]=inputBytes[11]^decodebuff[19];
        bufferN2[0]=inputBytes[12]^decodebuff[20];
        bufferN2[1]=inputBytes[13]^decodebuff[21];
        bufferN2[2]=inputBytes[14]^decodebuff[22];
        bufferN2[3]=inputBytes[15]^decodebuff[23];
        for(int i=0; i<4; i++) {
            bufferN1[i] &= BYTE_MASK;
            bufferN2[i] &= BYTE_MASK;
        }
        gostEncode(bufferN1, bufferN2);
        decodebuff[8]=(byte)(bufferN1[0]);
        decodebuff[9]=(byte)(bufferN1[1]);
        decodebuff[10]=(byte)(bufferN1[2]);
        decodebuff[11]=(byte)(bufferN1[3]);
        decodebuff[12]=(byte)(bufferN2[0]);
        decodebuff[13]=(byte)(bufferN2[1]);
        decodebuff[14]=(byte)(bufferN2[2]);
        decodebuff[15]=(byte)(bufferN2[3]);

        bufferN1[0]=inputBytes[0]^decodebuff[16];
        bufferN1[1]=inputBytes[1]^decodebuff[17];
        bufferN1[2]=inputBytes[2]^decodebuff[18];
        bufferN1[3]=inputBytes[3]^decodebuff[19];
        bufferN2[0]=inputBytes[4]^decodebuff[20];
        bufferN2[1]=inputBytes[5]^decodebuff[21];
        bufferN2[2]=inputBytes[6]^decodebuff[22];
        bufferN2[3]=inputBytes[7]^decodebuff[23];
        for(int i=0; i<4; i++) {
            bufferN1[i] &= BYTE_MASK;
            bufferN2[i] &= BYTE_MASK;
        }
        gostEncode(bufferN1, bufferN2);
        decodebuff[0]=(byte)(bufferN1[0]);
        decodebuff[1]=(byte)(bufferN1[1]);
        decodebuff[2]=(byte)(bufferN1[2]);
        decodebuff[3]=(byte)(bufferN1[3]);
        decodebuff[4]=(byte)(bufferN2[0]);
        decodebuff[5]=(byte)(bufferN2[1]);
        decodebuff[6]=(byte)(bufferN2[2]);
        decodebuff[7]=(byte)(bufferN2[3]);
        
        return decodebuff;
    }

}
