package com.kamildanak.minecraft.forgeeconomy.network.client;

import com.kamildanak.minecraft.forgeeconomy.gui.BalanceHUD;
import com.kamildanak.minecraft.forgeeconomy.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class MessageBalance  extends AbstractMessage.AbstractClientMessage<MessageBalance>{
    private String[] currencies;
    private int[] balances;
    private int d;

    public MessageBalance()
    {

    }
    public MessageBalance(String[] currencies, int[] balances, int d)
    {
        this.currencies = currencies;
        this.balances = balances;
        this.d = d;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        int n = buffer.readInt();
        currencies = new String[n];
        for(int i = 0; i < n; i++)
            currencies[i] = buffer.readStringFromBuffer(10);
        balances = new int[n];
        for(int i = 0; i < n; i++)
            balances[i] = buffer.readInt();
        d = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(currencies.length);
        for (String currency : currencies) buffer.writeString(currency);
        for (int balance : balances) buffer.writeInt(balance);
        buffer.writeInt(d);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BalanceHUD.setBalanceAndCurrency(balances[d], currencies[d]);
    }
}
