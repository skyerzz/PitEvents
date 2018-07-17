package com.skyerzz.pitevents;

/**
 * Created by sky on 13-7-2018.
 */
public class Event {

    private String type;
    private String name;
    private String location;
    private int seconds = -1;

    public Event(String type, String name){
        this(type, name, null, -1);
    }

    public Event(String type, String name, String location){
        this(type, name, location, -1);
    }

    public Event(String type, String name, String location, int seconds){
        this.type = type.trim();
        this.name = name.trim();
        if(location!=null) {
            this.location = location.trim();
        }
        this.seconds = seconds;
    }

    /**
     * decreases time by 1 and returns if event is still active
     * @return
     */
    public boolean tick(){
        //System.out.println("TICK FOR " + name + " from " + seconds);
        if(seconds>0){
            seconds--;
        }
        return seconds==0;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public String getLocation(){
        return location;
    }

    public int getTimeLeft(){ return seconds; }

    @Override
    public String toString(){
        return "[" + type + "] " + name + (location!=null ? " at " + location : "") + " for " + seconds;
    }
}
