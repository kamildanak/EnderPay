package com.kamildanak.minecraft.enderpay.network.server;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentTranslation;
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
        if (currentItem != null && currentItem.isItemEqual(new ItemStack(EnderPay.itemBlankBanknote))) {

            Account account = Account.get(player);
            if (amount <= 0) {
                //noinspection RedundantArrayCreation
                player.addChatMessage(new TextComponentTranslation("exception.enderpay.number_must_be_positive",
                        new Object[0]));
                return;
            }
            if (account.getBalance() < amount) {
                //noinspection RedundantArrayCreation
                player.addChatMessage(new TextComponentTranslation("exception.enderpay.insufficient_credit",
                        new Object[0]));
                return;
            }
            account.addBalance(-amount);
            ItemStack newBanknote = new ItemStack(EnderPay.itemFilledBanknote);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("Amount", amount);
            tag.setLong("DateIssued", Utils.getCurrentDay());
            newBanknote.setTagCompound(tag);
            if(!player.isCreative() || EnderPay.consumeBanknotesInCreativeMode)
                player.inventory.decrStackSize(currentItemIndex,1);
            player.inventory.addItemStackToInventory(newBanknote);
        }
    }
}
