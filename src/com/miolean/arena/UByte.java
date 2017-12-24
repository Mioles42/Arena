package com.miolean.arena;

import java.io.Serializable;

public final class UByte implements Serializable {

    private byte value;

    public UByte(int in) {
        if(in < 0) value = -128;
        else if(in > 255) value = 127;
        else value = (byte) (in - 128);
    }
    public UByte(short in) {
        value = (byte) (in - 128);
    }
    public UByte(long in) {
        value = (byte) (in - 128);
    }

    public int val() {
        return value + 128;
    }

    @Override
    public String toString() {
        return "" + Integer.toHexString(value+128);
    }

    public static UByte ub(int value) {
        return new UByte(value);
    }

    public static UByte[][] ubDeepCopy(UByte[][] array) {
        UByte[][] result = new UByte[array.length][array[0].length];
        for(int i = 0; i < result.length; i++) {
            if(array[i] != null) {
                result[i] = new UByte[array[i].length];
                for(int j = 0; j < result[i].length; j++) {
                    result[i][j] = array[i][j];
                }
            }
        }
        return result;
    }
}
