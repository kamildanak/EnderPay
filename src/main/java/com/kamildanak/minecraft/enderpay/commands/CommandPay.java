package com.kamildanak.minecraft.enderpay.commands;

import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.client.MessageBalance;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandPay extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "pay";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return "commands.pay.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 1) {
            EntityPlayerMP entityplayer = getPlayer(server, sender, args[0]);
            Account account = Account.get(entityplayer);
            account.update();
            long amount = parseLong(args[1]);
            if (amount < 0)
                //noinspection RedundantArrayCreation
                throw new NumberInvalidException("commands.pay.number_must_be_positive", new Object[0]);
            Account senderAccount = Account.get((EntityPlayerMP) sender);
            if (senderAccount.getBalance() < amount)
                throw new InsufficientCreditException();
            senderAccount.addBalance(-amount);
            account.addBalance(amount);
            PacketDispatcher.sendTo(new MessageBalance(senderAccount.getBalance()), (EntityPlayerMP) sender);
            PacketDispatcher.sendTo(new MessageBalance(account.getBalance()), entityplayer);
            return;
        }
        //noinspection RedundantArrayCreation
        throw new WrongUsageException("commands.pay.usage", new Object[0]);
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
