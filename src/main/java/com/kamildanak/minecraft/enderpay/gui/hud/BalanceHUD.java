package com.kamildanak.minecraft.enderpay.gui.hud;

import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.gui.lib.GuiExtended;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BalanceHUD extends GuiExtended {
    private Minecraft mc;
    private static Long balance = null;
    private static String currency;

    public BalanceHUD(Minecraft mc) {
        super(mc);
        this.mc = mc;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    @SuppressWarnings("unused")
    public void onRenderInfo(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable()) return;
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE &&
                event.getType() != RenderGameOverlayEvent.ElementType.HEALTHMOUNT)
            return;
        if (mc == null || mc.thePlayer == null || mc.theWorld == null) return;

        drawBalance();
    }

    private void drawBalance() {
        mc.mcProfiler.startSection("balance");
        ScaledResolution resolution = new ScaledResolution(mc);
        FontRenderer fontRenderer = this.mc.getRenderManager().getFontRenderer();
        //noinspection ConstantConditions //no, it is not constant
        if (fontRenderer == null) return;

        String text = (balance == null) ? "---" :
                fontRenderer.listFormattedStringToWidth(Utils.format(balance) + currency, 64).get(0);

        bind("enderpay:textures/icons.png");

        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        int cx = width / 2;
        int textLength = fontRenderer.getStringWidth(text);

        int drawHeight = height - 50 + (mc.thePlayer.capabilities.isCreativeMode ? 17 : 0) -
                (!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.isInsideOfMaterial(Material.WATER)
                        && !mc.thePlayer.canBreatheUnderwater() ? 10 : 0);
        drawTexturedModalRect(cx + 82 - textLength - 4 - 7, drawHeight - 1, 0, 0, 16, 11);
        drawString(fontRenderer, text, cx + 82 - textLength + 9 - 2, drawHeight + 1, 0xa0a0a0);

        mc.mcProfiler.endSection();
    }

    public static void setBalanceAndCurrency(long balance, String currency)
    {
        BalanceHUD.balance = balance;
        BalanceHUD.currency = currency;
    }

    public static String getCurrency() {
        return currency;
    }
}
