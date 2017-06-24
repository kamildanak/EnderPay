package com.kamildanak.minecraft.enderpay.events;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.client.MessageBalance;
import com.kamildanak.minecraft.enderpay.network.client.MessageSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class EventHandler {
    private static long lastTickEvent = 0;

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote) {
            Account account = Account.get((EntityPlayer) event.getEntity());
            account.update();
            long balance = account.getBalance();
            PacketDispatcher.sendTo(new MessageBalance(balance), (EntityPlayerMP) event.getEntity());
            PacketDispatcher.sendTo(new MessageSettings(EnderPay.settings), (EntityPlayerMP) event.getEntity());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        Account account = Account.get(event.getEntityPlayer());
        try {
            account.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        long now = Utils.getCurrentDay();
        if (lastTickEvent == now) return;
        lastTickEvent = now;
        MinecraftServer server = EnderPay.minecraftServer;
        if (server == null) return;
        for (EntityPlayerMP entityPlayer : server.getPlayerList().getPlayers()) {
            Account account = Account.get(entityPlayer);
            if (account.update())
                PacketDispatcher.sendTo(new MessageBalance(account.getBalance()), entityPlayer);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingDeathEvent(LivingDeathEvent event) {
        int moneyDropValue = EnderPay.settings.getMoneyDropValue();
        if (moneyDropValue == 0) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer) || entity.world.isRemote) return;
        Entity killer = event.getSource().getTrueSource();
        if (!(killer instanceof EntityPlayerMP)) return;

        Account account = Account.get((EntityPlayer) entity);
        if (account.getBalance() <= 0) return;
        long amountTaken = (moneyDropValue > 0) ?
                (account.getBalance() * EnderPay.settings.getMoneyDropValue()) / 100 :
                Math.max(Math.min(account.getBalance(), -EnderPay.settings.getMoneyDropValue()), 0);
        account.addBalance(-amountTaken);

        long balance = account.getBalance();
        PacketDispatcher.sendTo(new MessageBalance(balance), (EntityPlayerMP) entity);

        Account killerAccount = Account.get((EntityPlayer) killer);
        killerAccount.addBalance(amountTaken);
        long balance2 = killerAccount.getBalance();
        PacketDispatcher.sendTo(new MessageBalance(balance2), (EntityPlayerMP) killer);
    }
}
