package com.skyerzz.pitevents;

import com.skyerzz.pitevents.gui.PitEditGui;
import com.skyerzz.pitevents.music.SongPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sky on 15-7-2018.
 */
public class PitEventsCommand implements ICommand{

    private List<String> alias;
    private List<String> compl;

    public PitEventsCommand(){
        alias = new ArrayList<>();
        compl = new ArrayList<>(Arrays.asList("editlayout", "togglesound"));
    }

    @Override
    public String getCommandName() {
        return "pitevents";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "pitevents [togglesound|editlayout]";
    }

    @Override
    public List<String> getCommandAliases() {
        return alias;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1){
            sender.addChatMessage(new ChatComponentText("\u00A7cInvalid use! Type \"/pitevents help\" for help"));
            return;
        }
        switch(args[0].toLowerCase()){
            case "help":
                sender.addChatMessage(new ChatComponentText("\u00A77Usage: \u00A7c/pitevents togglesound \u00A76- Toggles the sound effects"));
                sender.addChatMessage(new ChatComponentText("\u00A77Usage: \u00A7c/pitevents editlayout \u00A76- Lets you edit the layout"));
                sender.addChatMessage(new ChatComponentText("\u00A77Usage: \u00A7c/pitevents resetlayout \u00A76- resets any current events and embattlement status"));
                break;
            case "layout":
            case "edit":
            case "editlayout":
                MinecraftForge.EVENT_BUS.register(this);
                Minecraft.getMinecraft().displayGuiScreen(new PitEditGui());
                break;
            case "togglesound":
            case "sound":
                Settings.setSound(!Settings.hasSound());
                sender.addChatMessage(new ChatComponentText("\u00A76Turned sound " + (Settings.hasSound() ? "\u00A72ON" : "\u00A7cOFF")));
                break;
            case "testsound":
                SongPlayer.playStartingSong();
                break;
            case "resetevents":
                PitEventHandler.getInstance().reset();
                break;
            case "toggleevents":
                Settings.setShowEvents(!Settings.doShowEvents());
                sender.addChatMessage(new ChatComponentText("\u00A76Turned event display " + (Settings.doShowEvents() ? "\u00A72ON" : "\u00A7cOFF")));
                break;
            case "toggleembattlement":
            case "toggleidle":
            case "togglecombat":
                Settings.setShowEmbattle(!Settings.doShowEmbattle());
                sender.addChatMessage(new ChatComponentText("\u00A76Turned embattlement display " + (Settings.doShowEmbattle() ? "\u00A72ON" : "\u00A7cOFF")));
                break;
            default:
                sender.addChatMessage(new ChatComponentText("\u00A7cInvalid use! Type \"/pitevents help\" for help"));
                break;

        }
    }

    private int tick = 0;

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent e){
        if(tick++>1){
            System.out.println("Found tick event! executing GUI & unsbscribing");
            MinecraftForge.EVENT_BUS.unregister(this);
            Minecraft.getMinecraft().displayGuiScreen(new PitEditGui());
            tick=0;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return compl;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
