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
    private final int[][] sBlock = {
            {1,15,13,0,5,7,10,4,9,2,3,14,6,11,8,12},
            {13,11,4,1,3,15,5,9,0,10,14,7,6,8,2,12},
            {4,11,10,0,7,2,1,13,3,6,8,5,9,12,15,14},
            {6,12,7,1,5,15,13,8,4,10,9,14,0,3,11,2},
            {7,13,10,1,0,8,9,15,14,4,6,12,11,2,5,3},
            {5,8,1,13,10,3,4,2,14,15,12,7,6,0,9,11},
            {14,11,4,12,6,13,15,10,2,3,8,1,0,7,5,9},
            {4,10,9,2,13,8,0,14,6,11,1,12,7,15,5,3}
    };
    
    private final int[] crc16Array = {
        0X0000, 0XC0C1, 0XC181, 0X0140, 0XC301, 0X03C0, 0X0280, 0XC241,
        0XC601, 0X06C0, 0X0780, 0XC741, 0X0500, 0XC5C1, 0XC481, 0X0440,
        0XCC01, 0X0CC0, 0X0D80, 0XCD41, 0X0F00, 0XCFC1, 0XCE81, 0X0E40,
        0X0A00, 0XCAC1, 0XCB81, 0X0B40, 0XC901, 0X09C0, 0X0880, 0XC841,
        0XD801, 0X18C0, 0X1980, 0XD941, 0X1B00, 0XDBC1, 0XDA81, 0X1A40,
        0X1E00, 0XDEC1, 0XDF81, 0X1F40, 0XDD01, 0X1DC0, 0X1C80, 0XDC41,
        0X1400, 0XD4C1, 0XD581, 0X1540, 0XD701, 0X17C0, 0X1680, 0XD641,
        0XD201, 0X12C0, 0X1380, 0XD341, 0X1100, 0XD1C1, 0XD081, 0X1040,
        0XF001, 0X30C0, 0X3180, 0XF141, 0X3300, 0XF3C1, 0XF281, 0X3240,
        0X3600, 0XF6C1, 0XF781, 0X3740, 0XF501, 0X35C0, 0X3480, 0XF441,
        0X3C00, 0XFCC1, 0XFD81, 0X3D40, 0XFF01, 0X3FC0, 0X3E80, 0XFE41,
        0XFA01, 0X3AC0, 0X3B80, 0XFB41, 0X3900, 0XF9C1, 0XF881, 0X3840,
        0X2800, 0XE8C1, 0XE981, 0X2940, 0XEB01, 0X2BC0, 0X2A80, 0XEA41,
        0XEE01, 0X2EC0, 0X2F80, 0XEF41, 0X2D00, 0XEDC1, 0XEC81, 0X2C40,
        0XE401, 0X24C0, 0X2580, 0XE541, 0X2700, 0XE7C1, 0XE681, 0X2640,
        0X2200, 0XE2C1, 0XE381, 0X2340, 0XE101, 0X21C0, 0X2080, 0XE041,
        0XA001, 0X60C0, 0X6180, 0XA141, 0X6300, 0XA3C1, 0XA281, 0X6240,
        0X6600, 0XA6C1, 0XA781, 0X6740, 0XA501, 0X65C0, 0X6480, 0XA441,
        0X6C00, 0XACC1, 0XAD81, 0X6D40, 0XAF01, 0X6FC0, 0X6E80, 0XAE41,
        0XAA01, 0X6AC0, 0X6B80, 0XAB41, 0X6900, 0XA9C1, 0XA881, 0X6840,
        0X7800, 0XB8C1, 0XB981, 0X7940, 0XBB01, 0X7BC0, 0X7A80, 0XBA41,
        0XBE01, 0X7EC0, 0X7F80, 0XBF41, 0X7D00, 0XBDC1, 0XBC81, 0X7C40,
        0XB401, 0X74C0, 0X7580, 0XB541, 0X7700, 0XB7C1, 0XB681, 0X7640,
        0X7200, 0XB2C1, 0XB381, 0X7340, 0XB101, 0X71C0, 0X7080, 0XB041,
        0X5000, 0X90C1, 0X9181, 0X5140, 0X9301, 0X53C0, 0X5280, 0X9241,
        0X9601, 0X56C0, 0X5780, 0X9741, 0X5500, 0X95C1, 0X9481, 0X5440,
        0X9C01, 0X5CC0, 0X5D80, 0X9D41, 0X5F00, 0X9FC1, 0X9E81, 0X5E40,
        0X5A00, 0X9AC1, 0X9B81, 0X5B40, 0X9901, 0X59C0, 0X5880, 0X9841,
        0X8801, 0X48C0, 0X4980, 0X8941, 0X4B00, 0X8BC1, 0X8A81, 0X4A40,
        0X4E00, 0X8EC1, 0X8F81, 0X4F40, 0X8D01, 0X4DC0, 0X4C80, 0X8C41,
        0X4400, 0X84C1, 0X8581, 0X4540, 0X8701, 0X47C0, 0X4680, 0X8641,
        0X8201, 0X42C0, 0X4380, 0X8341, 0X4100, 0X81C1, 0X8081, 0X4040
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
                new PropertyLoader().getPropertyFromFile(PROPERTIES_FILE_NAME, 
                                                    SECUR_KEY_PROPERTY);
        if(keyStr == null)
            return;
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
    private void applyMaskToBuffer(int[] buffer) {
        for(int i=0; i<buffer.length; i++) {
            buffer[i] &= BYTE_MASK;
        }
    }
    
    /*------------------------------------------------------------------------*/
    private byte[] decode24bytesProcess(byte[] input) {
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
        applyMaskToBuffer(bufferN1);
        applyMaskToBuffer(bufferN2);
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
        applyMaskToBuffer(bufferN1);
        applyMaskToBuffer(bufferN2);
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
        applyMaskToBuffer(bufferN1);
        applyMaskToBuffer(bufferN2);
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
    private byte[] encode24bytesProcess(byte[] input) {
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
        applyMaskToBuffer(bufferN1);
        applyMaskToBuffer(bufferN2);
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
        applyMaskToBuffer(bufferN1);
        applyMaskToBuffer(bufferN2);
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
        applyMaskToBuffer(bufferN1);
        applyMaskToBuffer(bufferN2);
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

    /*------------------------------------------------------------------------*/
    @Override
    public boolean crc16Check(byte[] buffer) {
        if(buffer.length != HANDLING_BUF_SIZE)
            throw new IllegalArgumentException("Input buffer length incorrect.");
        
        int crc16 = 0;
        for(int i=0; i<HANDLING_BUF_SIZE; i++) {
            int temp = (crc16 & BYTE_MASK) ^ ((int)buffer[i] & BYTE_MASK);
            crc16 = (crc16 >> 8) ^ crc16Array[temp];
        }
        
        if(crc16 == 0)
            return true;
        else 
            return false;
    }
    
}
