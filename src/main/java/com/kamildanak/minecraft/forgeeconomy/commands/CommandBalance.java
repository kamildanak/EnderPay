package com.kamildanak.minecraft.forgeeconomy.commands;

import com.kamildanak.minecraft.forgeeconomy.economy.Account;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandBalance extends CommandBase {
    @Override
    public String getCommandName() {
        return "balance";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.balance.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            notifyCommandListener(sender, this, "commands.balance.success",
                    new Object[]{Account.get((EntityPlayer) sender).getBalance()});
            return;
        }
        throw new WrongUsageException("commands.balance.usage", new Object[0]);
    }
}
