package com.kamildanak.minecraft.forgeeconomy.network.client;

import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import com.kamildanak.minecraft.forgeeconomy.gui.BalanceHUD;
import com.kamildanak.minecraft.forgeeconomy.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class MessageBalance  extends AbstractMessage.AbstractClientMessage<MessageBalance>{
    private long balance;
    private String currencyName;


    @SuppressWarnings("unused")
    public MessageBalance() {}

    public MessageBalance(long balance)
    {
        this.balance = balance;
        this.currencyName = balance == 1 ? ForgeEconomy.currencyNameSingular : ForgeEconomy.currencyNameMultiple;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        balance = buffer.readLong();
        currencyName = buffer.readStringFromBuffer(20);
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeLong(balance);
        buffer.writeString(currencyName);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BalanceHUD.setBalanceAndCurrency(balance, currencyName);
    }
}
