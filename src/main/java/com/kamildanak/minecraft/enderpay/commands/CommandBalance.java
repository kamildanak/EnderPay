package com.kamildanak.minecraft.enderpay.commands;

import com.kamildanak.minecraft.enderpay.economy.Account;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandBalance extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "balance";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nullable ICommandSender sender) {
        return "commands.balance.usage";
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, @Nullable String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            //noinspection RedundantArrayCreation
            notifyCommandListener(sender, this, "commands.balance.success",
                    new Object[]{Account.get((EntityPlayer) sender).getBalance()});
            return;
        }
        //noinspection RedundantArrayCreation
        throw new WrongUsageException("commands.balance.usage", new Object[0]);
    }
}
