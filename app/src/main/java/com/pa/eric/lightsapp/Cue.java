package com.pa.eric.lightsapp;

/**
 * Created by eric on 4/19/15.
 */
public class Cue {

    private char num;
    private String cue;

    public Cue(char noteNum, String cue) {
        this.num = noteNum;
        this.cue = cue;
    }

    @Override
    public String toString() {
        return cue;
    }

    public char getNum() {
        return num;
    }
}
