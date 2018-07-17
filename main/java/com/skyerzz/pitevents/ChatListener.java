package com.skyerzz.pitevents;

import com.skyerzz.pitevents.music.SongPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sky on 13-7-2018.
 */
public class ChatListener {

    Pattern event = Pattern.compile("^(MINOR|MAJOR) EVENT!\\s((.*?)($|starting now|in(.*?)($|ended|((for\\s|)(\\d)\\smin(.*?)))))$");
    Pattern maths = Pattern.compile("^QUICK\\sMATHS!\\sSolve:\\s(.*)$");
    Pattern looted = Pattern.compile("^LOOTED.*?care package");

    public ChatListener(){
        System.out.println("chat <3");
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e){
        if(!PitEventHandler.getInstance().isInPitGameMode() || !(e.type==0)){
            //if its not of type 0, its not text chat (rather compass messages and such)
            //if it does not contain : , its not a player chat message.
            //since we dont need either, we simply return.
            //if they arent in a pit gamemode, we dont need to read their chat either!
            return;
        }
        String message = e.message.getUnformattedText().trim();
        message = message.replaceAll("\u00A7[\\w\\d]", "");
        Matcher m = event.matcher(message);
        boolean matches = m.matches();
        if(matches){
            System.out.println("[PitEvents] Found event : " + message);
            if(message.toLowerCase().contains("everyone gets a bounty")){
                SongPlayer.playStartingSong();
                return; // we dont want this one to display. We want to sound it though!
            }
            for(int i = 0; i < m.groupCount(); i++){
                System.out.println("[PitEvents] Group: " + i + " message: " + m.group(i));
            }
            if(m.group(6)!=null && m.group(6).trim().equalsIgnoreCase("ended")){
                //in case we still got one running, lets end
                PitEventHandler.getInstance().removeEvent(m.group(3).trim(), m.group(5).trim());
                return; //we dont need to store this one
            }
            int duration = -1;
            try{
                int x = Integer.parseInt(m.group(9).trim());
                duration = (x*60);
            }catch(NumberFormatException | NullPointerException ex){
                //dont do shit, there was no time;
            }
            if(message.toLowerCase().contains("starting now")){
                //we assume its 5 minutes.
                if(m.group(3).trim().toLowerCase().equals("rage pit")){
                    duration = 240;
                }else {
                    duration = 300;
                }
            }
            PitEventHandler.getInstance().addEvent(new Event(m.group(1), m.group(3), m.group(5), duration));
        }else if(message.contains("QUICK MATHS")){
            System.out.println("QUITCK MATHS?!");
            if(message.contains("QUICK MATHS OVER")){
                System.out.println("Aww its over, RIP quick maths");
                PitEventHandler.getInstance().removeMathsEvent();
            }else{
                //its starting :hype:
                Matcher mathMatcher = maths.matcher(message);
                if(mathMatcher.matches()){
                    for(int i = 0; i < mathMatcher.groupCount(); i++){
                        System.out.println("[PitEvents] Maths! Group: " + i + " message: " + mathMatcher.group(i));
                    }
                    PitEventHandler.getInstance().addEvent(new Event("Maths", "QUICK MATHS", mathMatcher.group(1), -1));
                }else{
                    System.out.println("[PitEvents] Couldnt find the correct QUICK MATHS sum?? " + message);
                }

            }
        }else if(message.contains("EVENT!")){
            System.out.println("No Match for \"" + message + "\".");
        }else{
            Matcher lootm = looted.matcher(message);
            boolean lootmatch = lootm.matches();
            if(lootmatch){
                System.out.println("[PitEvents] Matched for care package looted!");
                PitEventHandler.getInstance().removeCarePackageEvent();
            }
        }
    }
}
