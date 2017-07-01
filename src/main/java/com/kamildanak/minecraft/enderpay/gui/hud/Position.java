package com.kamildanak.minecraft.enderpay.gui.hud;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.PlayerCapabilities;

import javax.vecmath.Point2i;

public enum Position {
    TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT,
    TOP_MIDDLE, MIDDLE_MIDDLE, BOTTOM_MIDDLE,
    TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT,
    HUD_ABOVE_LEFT, HUD_ABOVE_MIDDLE, HUD_ABOVE_RIGHT;

    public static Position byName(String name) {
        switch (name) {
            case "top_left":
                return TOP_LEFT;
            case "middle_left":
                return MIDDLE_LEFT;
            case "bottom_left":
                return BOTTOM_LEFT;
            case "top_middle":
                return TOP_MIDDLE;
            case "middle_middle":
                return MIDDLE_MIDDLE;
            case "bottom_middle":
                return BOTTOM_MIDDLE;
            case "top_right":
                return TOP_RIGHT;
            case "middle_right":
                return MIDDLE_RIGHT;
            case "bottom_right":
                return BOTTOM_RIGHT;
            case "hud_above_left":
                return HUD_ABOVE_LEFT;
            case "hud_above_middle":
                return HUD_ABOVE_MIDDLE;
            case "hud_above_right":
            default:
                return HUD_ABOVE_RIGHT;
        }
    }

    public Point2i getPoint(ScaledResolution resolution, Minecraft mc) {
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        switch (this) {
            case TOP_LEFT:
                return new Point2i(0, 0);
            case MIDDLE_LEFT:
                return new Point2i(0, height / 2);
            case BOTTOM_LEFT:
                return new Point2i(0, height);
            case TOP_MIDDLE:
                return new Point2i(width / 2, 0);
            case MIDDLE_MIDDLE:
                return new Point2i(width / 2, height / 2);
            case BOTTOM_MIDDLE:
                return new Point2i(width / 2, height);
            case TOP_RIGHT:
                return new Point2i(width, 0);
            case MIDDLE_RIGHT:
                return new Point2i(width, height / 2);
            case BOTTOM_RIGHT:
                return new Point2i(width, height);
            case HUD_ABOVE_LEFT:
                return new Point2i(width / 2 - 91, height - 33 - countYOFF(mc.player, true, false));
            case HUD_ABOVE_MIDDLE:
                return new Point2i(width / 2, height - 33 - countYOFF(mc.player, true, true));
            case HUD_ABOVE_RIGHT:
            default:
                return new Point2i(width / 2 + 91, height - 33 - countYOFF(mc.player, false, true));
        }
    }

    private int countYOFF(EntityPlayerSP p, boolean left, boolean right) {
        PlayerCapabilities pc = p.capabilities;
        boolean up = (right && p.isInsideOfMaterial(Material.WATER) && !p.canBreatheUnderwater()) ||
                (left && p.getTotalArmorValue() > 0);
        return pc.isCreativeMode ? 0 : 17 + (up ? 10 : 0);
    }
}
