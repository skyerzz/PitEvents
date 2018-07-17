package com.skyerzz.pitevents.music;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Created by sky on 14-7-2018.
 */
public class Note {

    String name;
    float pitch;
    float volume;

    public Note(String name, float pitch, float volume){
        this.name = name;
        this.pitch = pitch;
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void play(){
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.playSound(name, volume, pitch);
    }
}
