package com.kamildanak.minecraft.enderpay.gui.hud;

public enum Anchor {
    LEFT, CENTRE, RIGHT;

    public static Anchor byName(String anchorHUD) {
        switch (anchorHUD) {
            case "left":
                return Anchor.LEFT;
            case "centre":
                return Anchor.CENTRE;
            default:
                return Anchor.RIGHT;
        }
    }
}
