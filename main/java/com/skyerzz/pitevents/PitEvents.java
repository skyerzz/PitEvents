package com.skyerzz.pitevents;

import com.skyerzz.pitevents.gui.PitEventGUI;
import com.skyerzz.pitevents.music.SongPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PitEvents.MODID, version = PitEvents.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8,1.8.9]", name = "PitEvents")
public class PitEvents
{
    public static final String MODID = "pitevents";
    public static final String VERSION = "1.1";

    public static Configuration config;


    public static void setConfigValue(String value, float option)
    {
        config.get(config.CATEGORY_GENERAL, value, option).set(option);
        config.save();
    }

    public static void setConfigValue(String value, boolean option)
    {
        config.get(config.CATEGORY_GENERAL, value, option).set(option);
        config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Settings.initialise();
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(PitEventHandler.getInstance());
        new PitEventGUI();
        SongPlayer.getInstance();
        ClientCommandHandler.instance.registerCommand(new PitEventsCommand());

        //PitEventHandler.getInstance().addEvent(new Event("Test", "Some String", "Some location", -1));
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent e)
    {
        config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();
    }

}
