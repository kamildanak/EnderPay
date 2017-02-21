package com.kamildanak.minecraft.enderpay.proxy;


import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.client.MessageBalance;
import com.kamildanak.minecraft.enderpay.network.client.MessageSettings;
import com.kamildanak.minecraft.enderpay.network.server.MessageIssueBanknote;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Proxy {
    public void preInit() {
    }

    public void init() {
    }

    public void registerPackets() {
        PacketDispatcher.registerMessage(MessageBalance.class);
        PacketDispatcher.registerMessage(MessageIssueBanknote.class);
        PacketDispatcher.registerMessage(MessageSettings.class);
    }

    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    /**
     * Returns the current thread based on side during message handling,
     * used for ensuring that the message is being handled by the main thread
     */
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity.getServerWorld();
    }


    public void registerCraftingRecipes() {
        if (EnderPay.settings.isRegisterBanknoteRecipe()) {
            CraftingManager.getInstance().addRecipe(new ItemStack(EnderPay.itemBlankBanknote, 1),
                    "PGP", "GIG", "PGP",
                    'P', new ItemStack(Items.PAPER, 1),
                    'G', new ItemStack(Items.DYE, 1, 2),
                    'I', new ItemStack(Items.DYE, 1, 0));
        }
    }

    public boolean isSinglePlayer() {
        return false;
    }
}
