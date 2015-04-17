package com.pa.eric.lightsapp;

/**
 * Created by eric on 4/14/15.
 */
public class MidiEvent {

    private char type;
    private char channel;
    private char num;

    public MidiEvent(char[] data) {
        type = data[0];
        channel = data[1];
        num = data[2];
    }

    public char getType() { return type; }
    public char getChannel() { return channel; }
    public char getNum() { return num; }

    public String toString() {
        return "Type: " + (int)type + ", Channel: " + (int)channel + ", Note: " + (int)num;
    }
}
