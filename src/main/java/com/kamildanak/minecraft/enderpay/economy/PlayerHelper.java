package com.kamildanak.minecraft.enderpay.economy;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.client.MessageBalance;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerHelper implements IPlayerHelper {
    @Nullable
    private EntityPlayerMP getPlayerMP(UUID uuid) {
        EntityPlayerMP entityPlayerMP = EnderPay.minecraftServer.getPlayerList().getPlayerByUUID(uuid);
        //noinspection ConstantConditions
        return (entityPlayerMP != null) ? entityPlayerMP : null;
    }

    @Override
    public void send(UUID uuid, long balance) {
        PacketDispatcher.sendTo(new MessageBalance(balance), getPlayerMP(uuid));
    }

    @Override
    public boolean isPlayerLoggedIn(UUID uuid) {
        return getPlayerMP(uuid) != null;
    }
}
