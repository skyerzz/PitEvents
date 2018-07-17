package com.skyerzz.pitevents;

import com.skyerzz.pitevents.music.SongPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * Created by sky on 13-7-2018.
 */
public class PitEventHandler {

    private final int cooldownmilliseconds = 15000; //15 seconds from fighting to idle

    private static PitEventHandler instance;

    private boolean isInPitGamemode = false;

    private ArrayList<Event> events;

    private PitEventHandler(){
        events = new ArrayList<Event>();
    }

    public static PitEventHandler getInstance(){
        if(instance==null){
            instance = new PitEventHandler();
        }
        return instance;
    }

    public boolean isInPitGameMode(){
        return isInPitGamemode;
    }

    public ArrayList<Event> getEvents(){
        return events;
    }

    public void addEvent(Event event){
        for(Event e:events){
            if(e.getName().equals(event.getName()) && e.getLocation().equals(event.getLocation())){
                return; //we already have this one in our array;
            }
        }
        System.out.println("[PitEvents] Adding event " + event.toString());
        events.add(event);
        SongPlayer.playStartingSong();
    }

    public void removeCarePackageEvent(){
        Event toRemove = null;
        for(Event e: events){
            if(e.getName().equalsIgnoreCase("Care Package")){
                toRemove = e;
                System.out.println("[PitEvents] Removed care package event: " + e.toString());
                break;
            }
        }
        if(toRemove!=null){
            events.remove(toRemove);
        }
    }

    public void removeMathsEvent(){
        Event toRemove = null;
        for(Event e: events){
            if(e.getName().equalsIgnoreCase("Quick Maths")){
                toRemove = e;
                System.out.println("[PitEvents] Removed Quick Maths event: " + e.toString());
                break;
            }
        }
        if(toRemove!=null){
            events.remove(toRemove);
        }
    }

    public void removeEvent(String name, String location){
        Event toRemove = null;
        for(Event e: events){
            if(e.getName().equalsIgnoreCase(name) && e.getLocation().equalsIgnoreCase(location)){
                toRemove = e;
                System.out.println("[PitEvents] Removed event: " + e.toString());
                break;
            }
        }
        if(toRemove!=null){
            events.remove(toRemove);
        }
    }

    public int getTimeToIdle(){
        if(lastDamageTaken==-1){
            return 0;
        }
        long time = System.currentTimeMillis();
        long diff = time-lastDamageTaken;
        return (int) Math.ceil((cooldownmilliseconds-diff)/1000);
    }

    public void reset(){
        events.clear();
        lastTickTime = -1;
        lastPlayertickTime = -1;
        lastDamageTaken = -1;
        lastHealth = -1;
    }

    private long lastTickTime = -1;
    private long lastPlayertickTime = -1;
    private long lastDamageTaken = -1;
    private float lastHealth = -1;

    @SubscribeEvent
    public void loop(TickEvent.ClientTickEvent e){
        if(e.side== Side.CLIENT && e.phase== TickEvent.Phase.END){
            long time = System.currentTimeMillis();
            if(lastTickTime +1000 < time){
                if(Minecraft.getMinecraft()!=null && Minecraft.getMinecraft().theWorld!=null && Minecraft.getMinecraft().theWorld.getScoreboard()!=null && Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1)!=null) {
                    ScoreObjective objective = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                    if (objective.getName().equalsIgnoreCase("pit")) {
                        isInPitGamemode = true;
                    } else {
                        isInPitGamemode = false;
                        PitEventHandler.getInstance().reset();
                    }
                }
                lastTickTime = time;
                //System.out.println("Second");
                Event toRemove = null;
                //System.out.println(events.toString());
                for(Event ev: events){
                    if(ev.tick()){
                        System.out.println("[PitEvents] Removing event " + ev.getName() + " : " + ev.getLocation());
                        toRemove = ev;
                        break;
                    }
                }
                if(toRemove!=null){
                    events.remove(toRemove);
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.PlayerTickEvent e){ // only way to get the damned hp
        long time = System.currentTimeMillis();
        if(isInPitGamemode && lastPlayertickTime +50 < time && e.player.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())){
            lastPlayertickTime = time;
            float health = e.player.getHealth();
            if(lastHealth>health){
                lastDamageTaken = time;
            }
            lastHealth = health;
        }

    }
}
