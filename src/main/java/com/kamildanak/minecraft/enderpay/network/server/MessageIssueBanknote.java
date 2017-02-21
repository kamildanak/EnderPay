package com.kamildanak.minecraft.enderpay.network.server;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.item.ItemFilledBanknote;
import com.kamildanak.minecraft.enderpay.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class MessageIssueBanknote extends AbstractMessage.AbstractServerMessage<MessageIssueBanknote> {
    private long amount;

    @SuppressWarnings("unused")
    public MessageIssueBanknote() {
    }

    public MessageIssueBanknote(long amount) {
        this.amount = amount;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        amount = buffer.readLong();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeLong(amount);
    }

    @Override
    public void process(EntityPlayer player, Side side) {

        int currentItemIndex = player.inventory.currentItem;
        ItemStack currentItem = player.inventory.getStackInSlot(currentItemIndex);
        if (!currentItem.isEmpty() && currentItem.isItemEqual(new ItemStack(EnderPay.itemBlankBanknote))) {

            Account account = Account.get(player);
            if (amount <= 0) {
                //noinspection RedundantArrayCreation
                player.sendStatusMessage((new TextComponentTranslation("exception.enderpay.number_must_be_positive", new Object[0]))
                        .setStyle((new Style()).setColor(TextFormatting.RED)), true);
                return;
            }
            if (account.getBalance() < amount) {
                //noinspection RedundantArrayCreation
                player.sendStatusMessage((new TextComponentTranslation("exception.enderpay.insufficient_credit", new Object[0]))
                        .setStyle((new Style()).setColor(TextFormatting.RED)), true);
                return;
            }
            account.addBalance(-amount);
            ItemStack newBanknote = ItemFilledBanknote.getItemStack(amount);
            if (!player.isCreative() || EnderPay.settings.isConsumeBanknotesInCreativeMode())
                player.inventory.decrStackSize(currentItemIndex, 1);
            player.inventory.addItemStackToInventory(newBanknote);
        }
    }
}
