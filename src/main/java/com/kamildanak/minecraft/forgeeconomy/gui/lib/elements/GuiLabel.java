package com.kamildanak.minecraft.forgeeconomy.gui.lib.elements;

import net.minecraft.client.resources.I18n;

public class GuiLabel extends GuiElement{
    String caption;

    public GuiLabel(int x, int y, int w, int h, String caption) {
        super(x, y, w, h);
        this.caption = I18n.format(caption).trim();
    }

    public GuiLabel(int x, int y, String caption)
    {
        this(x, y, 0, 0, caption);
    }

    @Override
    public void render()
    {
        gui.drawString(caption, x, y, 0x404040);
    }
}
