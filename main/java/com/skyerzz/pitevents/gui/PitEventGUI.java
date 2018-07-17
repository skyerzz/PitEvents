package com.skyerzz.pitevents.gui;

import com.skyerzz.pitevents.Event;
import com.skyerzz.pitevents.PitEventHandler;
import com.skyerzz.pitevents.PitEvents;
import com.skyerzz.pitevents.Settings;
import javafx.scene.control.TextFormatter;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Created by sky on 13-7-2018.
 */
public class PitEventGUI {

    private static final ResourceLocation icons = new ResourceLocation(PitEvents.MODID, "textures/overlay/IconsPitEvents.png");

    private static final int CARE_PACKAGE = 0, TDM = 1, KOTH = 2, DOUBLE_COINS = 3, MATHS = 4, ROBBERY = 5, BEAST = 6, RAGE_PIT = 7, RAFFLE = 8, UNKNOWN = 56;

    private static final int Ctime = 0xFFFF0000, Cbrack = 0xFFb2a4a4, Cloc = 0xFF4eb218, Cname = 0xFF11ddda;


    public PitEventGUI(){
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDraw(RenderGameOverlayEvent.Post event) {
        if(PitEventHandler.getInstance().isInPitGameMode() && event.type== RenderGameOverlayEvent.ElementType.ALL) {
            ScaledResolution res = event.resolution;
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();
            int stringHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
            int y = 0;
            if(Settings.doShowEmbattle()) {
                drawEmbattlementStatus(width, height, 0);
                y = 4+stringHeight;
            }
            if(Settings.doShowEvents()) {
                for (Event e : PitEventHandler.getInstance().getEvents()) {
                    drawEvent(e, width, height, y);
                    y += 4+stringHeight;
                }
            }
        }
    }

    public void drawEmbattlementStatus(int fullwidth, int fullheight, int yOffset) {
        Minecraft mc = Minecraft.getMinecraft();


        float newXPerc = Settings.getWidthPercentage();
        float newYPerc = Settings.getHeightPercentage();
        String status = "Status:";
        int timeToIdle = PitEventHandler.getInstance().getTimeToIdle();
        String embattleStatus = timeToIdle > 0 ? "Embattled" : "Idle";
        String time = timeToIdle + "";
        int statusLength = mc.fontRendererObj.getStringWidth(status);
        int embattleLength = mc.fontRendererObj.getStringWidth(embattleStatus);
        int timeLength = mc.fontRendererObj.getStringWidth(time);
        int bracketLength = mc.fontRendererObj.getStringWidth("(");
        int defaultPadding = mc.fontRendererObj.getStringWidth(" ");
        int y = (int) (fullheight * newYPerc) + yOffset;
        if (newXPerc < 0.5f) {
            int startX = (int) (newXPerc * fullwidth);
            mc.fontRendererObj.drawString(status, startX, y, Cbrack);
            startX += statusLength + defaultPadding;
            mc.fontRendererObj.drawString(embattleStatus, startX, y, timeToIdle> 0 ? Ctime : Cloc);
            startX += embattleLength + defaultPadding;
            if (timeToIdle > 0) {
                mc.fontRendererObj.drawString("(", startX, y, Cbrack);
                startX += bracketLength;
                mc.fontRendererObj.drawString(time, startX, y, Cname);
                startX += timeLength;
                mc.fontRendererObj.drawString(")", startX, y, Cbrack);
                startX += bracketLength + defaultPadding;
            }
        } else {
            int startX = (int) (newXPerc * fullwidth);
            //back to front for nice stuffs!
            if (timeToIdle > 0) {
                startX -= (bracketLength);
                mc.fontRendererObj.drawString(")", startX, y, Cbrack);
                startX -= (timeLength);
                mc.fontRendererObj.drawString(time, startX, y, Cname);
                startX -= (bracketLength);
                mc.fontRendererObj.drawString("(", startX, y, Cbrack);
            }

            startX -= (embattleLength + defaultPadding);
            mc.fontRendererObj.drawString(embattleStatus, startX, y, timeToIdle> 0 ? Ctime : Cloc);

            startX -= (statusLength + defaultPadding);
            mc.fontRendererObj.drawString(status, startX, y, Cbrack);
        }
    }

    private void drawEvent(Event e, int fullwidth, int fullHeight, int yOffset){
        Minecraft mc = Minecraft.getMinecraft();

        float newXPerc = Settings.getWidthPercentage();
        float newYPerc = Settings.getHeightPercentage();
        int drawItem = UNKNOWN;
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

        int y = (int) (fullHeight* newYPerc) + yOffset;
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


    private void drawImg(Minecraft mc, int option, int x, int y){
        int startx = 0+(32*(option%8));
        int starty = 0+(32*(option/8));
        mc.getTextureManager().bindTexture(icons);
        //enableAlpha(1f);
        GL11.glScaled(0.5, 0.5, 0.5);
        GlStateManager.color(1f, 1f, 1f);
        mc.ingameGUI.drawTexturedModalRect(x*2, y*2, startx, starty, 32, 32);
        GL11.glScaled(2, 2, 2);
        //disableAlpha(1f);
        mc.getTextureManager().bindTexture(Gui.icons);
    }

    public static void enableAlpha(float alpha)
    {
        GlStateManager.enableBlend();

        if (alpha == 1f)
            return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableAlpha(float alpha)
    {
        GlStateManager.disableBlend();

        if (alpha == 1f)
            return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
