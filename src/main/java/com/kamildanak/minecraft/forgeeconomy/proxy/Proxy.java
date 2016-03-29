package com.kamildanak.minecraft.forgeeconomy.proxy;


import com.kamildanak.minecraft.forgeeconomy.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Proxy {
    public void preInit() {
    }

    public void init() {
    }

    public void registerPackets() {
        //PacketDispatcher.registerMessage();
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
        return ctx.getServerHandler().playerEntity.getServerForPlayer();
    }
}
