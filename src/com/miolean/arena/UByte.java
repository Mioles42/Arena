package com.miolean.arena;

import java.io.Serializable;

public final class UByte implements Serializable {

    private byte value;

    public UByte(int in) {
        value = (byte) (in - 128);
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
        return "" + (value+128);
    }

    public static UByte ub(int value) {
        return new UByte(value);
    }
}
