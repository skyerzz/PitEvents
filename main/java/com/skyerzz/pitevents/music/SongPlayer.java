package com.skyerzz.pitevents.music;

import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * Created by sky on 14-7-2018.
 */
public class SongPlayer {

    public static final String bass = "note.bass", bassattack = "note.bassattack", bd = "note.bd", harp = "note.harp", hat = "note.hat", pling = "note.pling", snare  = "note.snare";
    public static final float GES = 0, G = 0.529732f, AES = 0.561231f, A = 0.594604f, BES=0.629961f, B= 0.667420f, C=0.707107f, DES = 0.749154f, D = 0.793701f, ES =  0.840896f, E = 0.890899f, F = 0.943874f;
    public static final float GES_HIGH = 1, G_HIGH = 1.059463f, AES_HIGH = 1.122462f, A_HIGH = 1.189207f, BES_HIGH= 1.259921f, B_HIGH= 1.334840f, C_HIGH=1.414214f, DES_HIGH= 1.498307f, D_HIGH = 1.587401f, ES_HIGH =  1.681793f, E_HIGH = 1.781797f, F_HIGH = 1.887749f, G_HIGH_MAX = 2f;

    ArrayList<NoteList> song = new ArrayList<>();

    private static SongPlayer instance;

    int currentNote = -1;

    private SongPlayer(){
        initialise();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static SongPlayer getInstance(){
        if(instance==null){
            instance = new SongPlayer();
        }
        return instance;
    }

    private void initialise(){
        song.add(new NoteList(new Note(pling, G, 1f), new Note(pling, B, 1f), new Note(pling, D, 1f), new Note(pling, F, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, A_HIGH, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, C_HIGH, 1f), new Note(bd, C, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, A_HIGH, 1f), new Note(snare, G, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, A, 1f), new Note(pling, C, 1f), new Note(pling, E, 1f), new Note(pling, C_HIGH, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, D_HIGH, 1f), new Note(bd, C, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, C_HIGH, 1f), new Note(snare, G, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, D_HIGH, 1f), new Note(bd, C, 1f)));
        song.add(null);
        song.add(null);
        song.add(new NoteList(new Note(pling, F, 1f), new Note(pling, A_HIGH, 1f), new Note(pling, C_HIGH, 1f), new Note(pling, E_HIGH, 1f), new Note(bd, C, 1f)));
        currentNote = song.size();
    }

    public static void playStartingSong(){
        instance.currentNote = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if(e.phase== TickEvent.Phase.END && e.side== Side.CLIENT) {
            if (currentNote < song.size()){
                if(song.get(currentNote)!=null) {
                    song.get(currentNote).play();
                }
                currentNote++;
            }
        }
    }
}
