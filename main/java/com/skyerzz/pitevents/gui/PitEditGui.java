package com.skyerzz.pitevents.gui;

import com.skyerzz.pitevents.Event;
import com.skyerzz.pitevents.PitEvents;
import com.skyerzz.pitevents.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;

/**
 * Created by sky on 15-7-2018.
 */
public class PitEditGui extends GuiScreen {


    private static final ResourceLocation icons = new ResourceLocation(PitEvents.MODID, "textures/overlay/IconsPitEvents.png");

    private static final int CARE_PACKAGE = 0, TDM = 1, KOTH = 2, DOUBLE_COINS = 3, MATHS = 4, ROBBERY = 5, BEAST = 6, RAGE_PIT = 7, RAFFLE = 8, UNKNOWN = 56;
    private static final int Ctime = 0xFFFF0000, Cbrack = 0xFFb2a4a4, Cloc = 0xFF4eb218, Cname = 0xFF11ddda;

    private long timeSinceLastScale = 0;
    private float newXPerc = -1, newYPerc = -1;

    private Event testEvent;

    public PitEditGui(){
        testEvent = new Event("minor", "Test Event", "Some Location", 599);
        newXPerc = Settings.getWidthPercentage();
        newYPerc = Settings.getHeightPercentage();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        ScaledResolution res = new ScaledResolution(this.mc);

        int midX = res.getScaledWidth()/2;
        int midY = res.getScaledHeight()/2;

        int textWidth = mc.fontRendererObj.getStringWidth("LeftClick and Drag to move");
        int textHeight = mc.fontRendererObj.FONT_HEIGHT;
        int paddingheight = 4;

        midX -= (textWidth/2);
        midY -= textHeight/2;
        this.drawString(mc.fontRendererObj,  "LeftClick and Drag to move", midX, midY, 0xFFFFFFFF);

        drawEmbattlementStatus(res.getScaledWidth(), res.getScaledHeight(), 0);
        drawEvent(testEvent, res.getScaledWidth(), res.getScaledHeight(), paddingheight+textHeight);

        //this.drawHoveringText(Arrays.asList(new String[]{"LeftClick and Drag to move"}), midX, midY);
    }

    public void drawEmbattlementStatus(int fullwidth, int fullheight, int yOffset){
        String status = "Status:";
        String embattle = "Embattled";
        String time = "15";
        int statusLength = mc.fontRendererObj.getStringWidth(status);
        int embattleLength = mc.fontRendererObj.getStringWidth(embattle);
        int timeLength = mc.fontRendererObj.getStringWidth(time);
        int bracketLength = mc.fontRendererObj.getStringWidth("(");
        int defaultPadding = mc.fontRendererObj.getStringWidth(" ");
        int y = (int) (fullheight * newYPerc) + yOffset;
        if(newXPerc<0.5f){
            int startX = (int) (newXPerc*fullwidth);
            this.drawString(mc.fontRendererObj, status, startX, y, Cbrack);
            startX += statusLength + defaultPadding;
            this.drawString(mc.fontRendererObj, embattle, startX, y, Ctime);
            startX += embattleLength + defaultPadding;
            this.drawString(mc.fontRendererObj, "(", startX, y, Cbrack);
            startX += bracketLength;
            this.drawString(mc.fontRendererObj, time, startX, y, Cname);
            startX += timeLength;
            this.drawString(mc.fontRendererObj, ")", startX, y, Cbrack);
            startX += bracketLength +defaultPadding;
        }else{
            int startX = (int) (newXPerc*fullwidth);
            //back to front for nice stuffs!
            startX -= (bracketLength);
            this.drawString(mc.fontRendererObj, ")", startX, y, Cbrack);
            startX -= (timeLength);
            this.drawString(mc.fontRendererObj, time, startX, y, Cname);
            startX -= (bracketLength);
            this.drawString(mc.fontRendererObj, "(", startX, y, Cbrack);

            startX -= (embattleLength + defaultPadding);
            this.drawString(mc.fontRendererObj, embattle, startX, y, Ctime);

            startX -= (statusLength + defaultPadding);
            this.drawString(mc.fontRendererObj, status, startX, y, Cbrack);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int buttonHeld, long timeSinceLastClick){
        if(buttonHeld==0 && timeSinceLastClick-timeSinceLastScale > 25){//leftclick + timedelay to make it smoother
            //System.out.println("ClickMove on " + mouseX + ":" + mouseY);
            ScaledResolution res = new ScaledResolution(this.mc);
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();

            float percWidth = ((float) mouseX/(float) width);
            float percHeight = ((float) mouseY/(float) height);

            System.out.println("X% " + percWidth + "   Y%  " + percHeight);
            newXPerc = percWidth;
            newYPerc = percHeight;
            //System.out.println("SCALING x " + mouseX + " y " + mouseY  + " time " + timeSinceLastClick);
        }
    }

    @Override
    public void onGuiClosed(){
        System.out.println( "Closed gui");
        Settings.setHeightPercentage(newYPerc);
        Settings.setWidthPercentage(newXPerc);
    }

    private void drawEvent(Event e, int fullwidth, int fullHeight, int yOffset){
        int drawItem = 0;
        switch(e.getName().toLowerCase()){
            case "care package": drawItem = CARE_PACKAGE;break;
            case "team deathmatch": drawItem = TDM;break;
            case "koth": drawItem = KOTH;break;
            case "2x rewards": drawItem = DOUBLE_COINS;break;
            case "quick maths": drawItem = MATHS;break;
            case "robbery": drawItem = ROBBERY;break;
            case "beast": drawItem = BEAST;break;
            case "rage pit": drawItem = RAGE_PIT;break;
            case "raffle": drawItem = RAFFLE;break;
        }
        String timeLeft = null;
        if(e.getTimeLeft()>=0){
            timeLeft = getCountString(e.getTimeLeft());
        }
        String loc = e.getLocation();
        String ev = e.getName();

        int y = (int) (fullHeight* newYPerc) + yOffset; // we draw the idle timer above it
        int startX = (int) (fullwidth * newXPerc);


        int defaultPadding = mc.fontRendererObj.getStringWidth(" ");

        int trackX = startX;

        if(newXPerc < 0.5f){
            //its left side so we draw from left to right to align nicely
            trackX = startX;
            drawImg(mc, drawItem, trackX, y-4); //1/4th of the image up to center it with text
            trackX += 16 + defaultPadding; //16x16 image
            if(ev!=null){
                int evWidth = mc.fontRendererObj.getStringWidth(ev);

                mc.fontRendererObj.drawString(ev, trackX, y, Cname);

                trackX += evWidth + defaultPadding;
            }
            if(loc!=null) {
                int locWidth = mc.fontRendererObj.getStringWidth(loc);
                int atWidth = mc.fontRendererObj.getStringWidth("@");

                mc.fontRendererObj.drawString("@", trackX, y, Cbrack);
                trackX += atWidth + defaultPadding;

                mc.fontRendererObj.drawString(loc, trackX, y, Cloc);
                trackX += locWidth + defaultPadding;
            }
            if(timeLeft!=null) {
                int bracketWith = mc.fontRendererObj.getStringWidth("]");
                int timeLeftWidth = mc.fontRendererObj.getStringWidth(timeLeft);


                mc.fontRendererObj.drawString("[", trackX, y, Cbrack);
                trackX += bracketWith;

                mc.fontRendererObj.drawString(timeLeft, trackX, y, Ctime);
                trackX += timeLeftWidth;

                mc.fontRendererObj.drawString("]", trackX, y, Cbrack);
                trackX += bracketWith+defaultPadding;
            }
        }else {
            //right side, draw backwards!
            trackX = startX;
            if (timeLeft != null) {
                int bracketWith = mc.fontRendererObj.getStringWidth("]");
                int timeLeftWidth = mc.fontRendererObj.getStringWidth(timeLeft);

                trackX -= bracketWith;
                mc.fontRendererObj.drawString("]", trackX, y, Cbrack);

                trackX -= timeLeftWidth;
                mc.fontRendererObj.drawString(timeLeft, trackX, y, Ctime);

                trackX -= bracketWith;
                mc.fontRendererObj.drawString("[", trackX, y, Cbrack);

                trackX -= defaultPadding;
            }
            if (loc != null) {
                int locWidth = mc.fontRendererObj.getStringWidth(loc);
                int atWidth = mc.fontRendererObj.getStringWidth("@");

                trackX -= locWidth;
                mc.fontRendererObj.drawString(loc, trackX, y, Cloc);

                trackX -= atWidth;
                trackX -= defaultPadding;
                mc.fontRendererObj.drawString("@", trackX, y, Cbrack);

                trackX -= defaultPadding;
            }
            if (ev != null) {
                int evWidth = mc.fontRendererObj.getStringWidth(ev);

                trackX -= evWidth;
                mc.fontRendererObj.drawString(ev, trackX, y, Cname);
                trackX -= defaultPadding;
            }
            trackX = trackX - 16; //16x16 image
            y = y - 4; //1/4th of the image up to center it with text
            drawImg(mc, drawItem, trackX, y);
        }
    }

    private void drawImg(Minecraft mc, int option, int x, int y){
        int startx = 0+(32*(option%8));
        int starty = 0+(32*(option/8));
        mc.getTextureManager().bindTexture(icons);
        //enableAlpha(1f);
        GL11.glScaled(0.5, 0.5, 0.5);
        GlStateManager.color(1f, 1f, 1f);
        this.drawTexturedModalRect(x*2, y*2, startx, starty, 32, 32);
        GL11.glScaled(2, 2, 2);
        //disableAlpha(1f);
        mc.getTextureManager().bindTexture(Gui.icons);
    }

    private String getCountString(int seconds){
        if(seconds<=0){
            return "";
        }
        int min = seconds/60;
        int sec = seconds%60;
        String m = min+"";
        String s = (sec<10) ? "0"+sec : sec+"";
        return m+":"+s;
    }
}
