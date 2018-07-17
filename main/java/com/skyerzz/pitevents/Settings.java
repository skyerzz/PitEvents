package com.skyerzz.pitevents;

/**
 * Created by sky on 15-7-2018.
 */
public class Settings {

    private static boolean doSound = true, showEmbattle = true, showEvents = true;

    private static float heightPercentage = 0.1f, widthPercentage = 0.95f;

    public static void initialise(){
        doSound = PitEvents.config.get(PitEvents.config.CATEGORY_GENERAL, "sound", true).getBoolean();
        heightPercentage = (float) PitEvents.config.get(PitEvents.config.CATEGORY_GENERAL, "heightPerc", 0.1f).getDouble();
        widthPercentage = (float) PitEvents.config.get(PitEvents.config.CATEGORY_GENERAL, "widthPerc", 0.95f).getDouble();
        showEmbattle = PitEvents.config.get(PitEvents.config.CATEGORY_GENERAL, "showEmbattle", true).getBoolean();
        showEvents = PitEvents.config.get(PitEvents.config.CATEGORY_GENERAL, "showEvents", true).getBoolean();
    }

    public static void setSound(boolean value){
        doSound = value;
        PitEvents.setConfigValue("sound", value);
    }

    public static boolean hasSound(){ return doSound; }

    public static boolean doShowEmbattle(){ return showEmbattle; }

    public static boolean doShowEvents(){ return showEvents; }

    public static float getHeightPercentage(){ return heightPercentage;}

    public static float getWidthPercentage(){ return widthPercentage;}

    public static void setShowEmbattle(boolean value){
        showEmbattle = value;
        PitEvents.setConfigValue("showEmbattle", value);
    }
    public static void setShowEvents(boolean value){
        showEvents = value;
        PitEvents.setConfigValue("showEvents", value);
    }

    public static void setHeightPercentage(float value){
        heightPercentage = value;
        PitEvents.setConfigValue("heightPerc", value);
    }

    public static void setWidthPercentage(float value){
        widthPercentage = value;
        PitEvents.setConfigValue("widthPerc", value);
    }
}
