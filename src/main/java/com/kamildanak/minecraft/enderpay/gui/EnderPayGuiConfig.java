package com.kamildanak.minecraft.enderpay.gui;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.gui.hud.Anchor;
import com.kamildanak.minecraft.enderpay.gui.hud.Position;
import com.kamildanak.minecraft.enderpay.network.client.MessageSettings;
import com.kamildanak.minecraft.enderpay.proxy.SettingsClient;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;

import javax.vecmath.Point2i;

public class EnderPayGuiConfig extends GuiConfig {
    public EnderPayGuiConfig(GuiScreen parent) {
        super(parent,
                new ConfigElement(
                        EnderPay.settings.config.getCategory("gui"))
                        .getChildElements(),
                EnderPay.modID,
                false,
                false,
                "Use this config menu to set your desired HUD position");
        titleLine2 = "Green rectangle shows where the HUD will display ingame";
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        Position position = null;
        Anchor anchor = null;
        boolean fromServer = false;
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < this.entryList.getSize(); i++) {
            GuiConfigEntries.IConfigEntry configEntry = this.entryList.getListEntry(i);
            Object value = configEntry.getCurrentValue();
            try {
                switch (configEntry.getName()) {
                    case "anchorHUD":
                        if (value instanceof String) anchor = Anchor.byName((String) value);
                        break;
                    case "position":
                        if (value instanceof String) position = Position.byName((String) value);
                        break;
                    case "useGuiConfigFromServer":
                        if (value instanceof Boolean) fromServer = (Boolean) value;
                        break;
                    case "xOffset":
                        if (value instanceof String) xOffset = Integer.valueOf((String) value);
                        break;
                    case "yOffset":
                        if (value instanceof String) yOffset = Integer.valueOf((String) value);
                        break;
                }
            } catch (NumberFormatException ignored) {

            }
        }

        ScaledResolution resolution = new ScaledResolution(mc);
        Point2i point = null;

        int hudWidth = 70;
        int xOffsetAnchor = 0;

        if (fromServer && EnderPay.settings instanceof SettingsClient) {
            MessageSettings messageSettings = ((SettingsClient) EnderPay.settings).getMessage();
            if (messageSettings == null) return;
            point = messageSettings.getPosition().getPoint(resolution, mc);
            xOffset = messageSettings.getxOffset();
            yOffset = messageSettings.getyOffset();
            if (messageSettings.getAnchor() != Anchor.LEFT) {
                if (anchor == Anchor.RIGHT) xOffsetAnchor -= hudWidth;
                else xOffsetAnchor -= hudWidth / 2;
            }
        }
        if (!fromServer && position != null) {
            point = position.getPoint(resolution, mc);
            if (anchor != Anchor.LEFT) {
                if (anchor == Anchor.RIGHT) xOffsetAnchor -= hudWidth;
                else xOffsetAnchor -= hudWidth / 2;
            }
        }
        if (point == null) return;

        drawRect(point.getX() + xOffset + xOffsetAnchor, point.getY() + yOffset,
                point.getX() + xOffset + hudWidth + xOffsetAnchor, point.getY() + yOffset + 14,
                0x8800FF00);
        drawRect(point.getX() + xOffset, point.getY() + yOffset,
                point.getX() + xOffset + 14, point.getY() + yOffset + 14,
                0x88FF0000);
    }
}
