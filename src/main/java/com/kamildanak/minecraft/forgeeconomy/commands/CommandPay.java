package com.kamildanak.minecraft.forgeeconomy.commands;

import com.kamildanak.minecraft.forgeeconomy.economy.Account;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandPay extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "pay";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nullable ICommandSender sender) {
        return "commands.pay.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 1) {
            EntityPlayer entityplayer = getPlayer(server, sender, args[0]);
            Account account = Account.get(entityplayer);
            long amount = parseLong(args[1]);
            if (amount<0)
                //noinspection RedundantArrayCreation
                throw new NumberInvalidException("commands.pay.number_must_be_positive", new Object[0]);
            Account senderAccount = Account.get((EntityPlayer) sender);
            if (senderAccount.getBalance() < amount)
                throw new InsufficientCreditException();
            senderAccount.addBalance(-amount);
            account.addBalance(amount);
            return;
        }
        //noinspection RedundantArrayCreation
        throw new WrongUsageException("commands.pay.usage", new Object[0]);
    }

    @Override
    @Nonnull
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
