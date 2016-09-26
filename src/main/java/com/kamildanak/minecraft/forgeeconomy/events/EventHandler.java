package com.kamildanak.minecraft.forgeeconomy.events;

import com.kamildanak.minecraft.forgeeconomy.economy.Account;
import com.kamildanak.minecraft.forgeeconomy.network.PacketDispatcher;
import com.kamildanak.minecraft.forgeeconomy.network.client.MessageBalance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().worldObj.isRemote) {

            Account account = Account.get((EntityPlayer) event.getEntity());
            long balance = account.getBalance();
            PacketDispatcher.sendTo(new MessageBalance(balance), (EntityPlayerMP) event.getEntity());
        }
    }
}
