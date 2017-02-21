package com.kamildanak.minecraft.enderpay.network.client;

import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.gui.hud.BalanceHUD;
import com.kamildanak.minecraft.enderpay.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class MessageBalance extends AbstractMessage.AbstractClientMessage<MessageBalance> {
    private long balance;
    private long date;


    @SuppressWarnings("unused")
    public MessageBalance() {
    }

    public MessageBalance(long balance) {
        this.balance = balance;
        this.date = Utils.getCurrentDay();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        balance = buffer.readLong();
        date = buffer.readLong();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeLong(balance);
        buffer.writeLong(date);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BalanceHUD.setBalance(balance);
        BalanceHUD.setDate(date);
    }
}
