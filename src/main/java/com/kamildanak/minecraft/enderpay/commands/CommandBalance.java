package com.kamildanak.minecraft.enderpay.commands;

import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.client.MessageBalance;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandBalance extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "balance";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return "commands.balance.usage";
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, @Nullable String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            //noinspection RedundantArrayCreation
            Account account = Account.get((EntityPlayer) sender);
            account.update();
            long balance = account.getBalance();
            if (sender instanceof EntityPlayerMP) {
                PacketDispatcher.sendTo(new MessageBalance(balance), (EntityPlayerMP) sender);
            }
            notifyCommandListener(sender, this, "commands.balance.success",
                    balance);
            return;
        }
        //noinspection RedundantArrayCreation
        throw new WrongUsageException("commands.balance.usage", new Object[0]);
    }
}
