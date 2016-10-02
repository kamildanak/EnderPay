package com.kamildanak.minecraft.forgeeconomy.proxy;

import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import com.kamildanak.minecraft.forgeeconomy.gui.hud.BalanceHUD;
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

    private void registerRenderers()
    {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(ForgeEconomy.itemBlankBanknote, 0, new ModelResourceLocation(ForgeEconomy.modID + ":" + "blank_banknote", "inventory"));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? mc.thePlayer : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return (ctx.side.isClient() ? mc : super.getThreadFromContext(ctx));
    }
}
