package com.kamildanak.minecraft.forgeeconomy.events;

import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import com.kamildanak.minecraft.forgeeconomy.economy.AccountPlayerInfo;
import com.kamildanak.minecraft.forgeeconomy.network.PacketDispatcher;
import com.kamildanak.minecraft.forgeeconomy.network.client.MessageBalance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class EventHandler {
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
            String[] currencies = ForgeEconomy.currencies;
            int dCurrencyID = Arrays.asList(currencies).indexOf(ForgeEconomy.defaultCurrency);
            long[] balances = new long[currencies.length];
            AccountPlayerInfo aPI = AccountPlayerInfo.get((EntityPlayer) event.entity);
            int i = 0;
            for(String currency : currencies){
                balances[i] = aPI.getAccount(currency).getBalance();
            }
            PacketDispatcher.sendTo(new MessageBalance(currencies, balances, dCurrencyID), (EntityPlayerMP) event.entity);
        }
    }
}
