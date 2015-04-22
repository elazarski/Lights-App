package com.pa.eric.lightsapp;

import java.util.ArrayList;

/**
 * Created by eric on 4/19/15.
 */
public class Song {
    private String name;
    private int bpm;
    private String mood;

    private ArrayList<Cue> cues;

    public Song(String name, int bpm, String mood, ArrayList<Cue> cues) {
        this.name = name;
        this.bpm = bpm;
        this.mood = mood;
        this.cues = cues;
    }

    public Cue findCue(char num) {
        for (Cue cue : cues) {
            if (cue.getNum() == num) return cue;
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public int getBpm() {
        return bpm;
    }

    public String getMood() {
        return mood;
    }

    public Cue getFirstCue() {
        return cues.get(0);
    }

    public Cue getFinalCue() {
        return cues.get(cues.size() - 1);
    }

}
