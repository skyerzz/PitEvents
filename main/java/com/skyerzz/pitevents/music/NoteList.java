package com.skyerzz.pitevents.music;

import java.util.ArrayList;

/**
 * Created by sky on 14-7-2018.
 */
public class NoteList {

    ArrayList<Note> notes = new ArrayList<>();

    public NoteList(Note...note){
        for(Note n: note){
            notes.add(n);
        }
    }

    public void play(){
        for(Note n: notes){
            n.play();
        }
    }
}
