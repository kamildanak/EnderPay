package com.kamildanak.minecraft.enderpay.proxy;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.gui.hud.BalanceHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@SuppressWarnings("unused")
public class ProxyClient extends Proxy {
    private Minecraft mc;

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(new BalanceHUD(Minecraft.getMinecraft()));
        registerRenderers();
    }

    private void registerRenderers() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(EnderPay.itemBlankBanknote, 0, new ModelResourceLocation(EnderPay.modID + ":" + "blank_banknote", "inventory"));
        renderItem.getItemModelMesher().register(EnderPay.itemFilledBanknote, 0, new ModelResourceLocation(EnderPay.modID + ":" + "filled_banknote", "inventory"));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? mc.player : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return (ctx.side.isClient() ? mc : super.getThreadFromContext(ctx));
    }

    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }
}
