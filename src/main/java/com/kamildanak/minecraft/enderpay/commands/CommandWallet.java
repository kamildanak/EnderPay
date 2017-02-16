package com.kamildanak.minecraft.enderpay.commands;

import com.kamildanak.minecraft.enderpay.economy.Account;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandWallet extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "wallet";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nullable ICommandSender sender) {
        return "commands.wallet.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 1) {
            EntityPlayer entityplayer = getPlayer(server, sender, args[1]);
            Account account = Account.get(entityplayer);
            if ("balance".equals(args[0])) {
                sender.addChatMessage(new TextComponentTranslation("commands.wallet.balance.success",
                        entityplayer.getName(), account.getBalance()));
                return;
            }
            long amount = parseLong(args[2]);
            if ("set".equals(args[0])) {
                account.setBalance(amount);
                sender.addChatMessage(new TextComponentTranslation("commands.wallet.set.success",
                        entityplayer.getName(), account.getBalance()));
                return;
            }
            if ("give".equals(args[0])) {
                account.addBalance(amount);
                sender.addChatMessage(new TextComponentTranslation("commands.wallet.give.success",
                        amount, entityplayer.getName()));
                return;
            }
            if ("take".equals(args[0])) {
                account.addBalance(-amount);
                sender.addChatMessage(new TextComponentTranslation("commands.wallet.take.success",
                        amount, entityplayer.getName()));
                return;
            }
        }
        //noinspection RedundantArrayCreation
        throw new WrongUsageException("commands.wallet.usage", new Object[0]);
    }

    @Override
    @Nonnull
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            //noinspection RedundantArrayCreation
            return getListOfStringsMatchingLastWord(args, new String[]{"give", "take", "set", "balance"});
        }
        if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
