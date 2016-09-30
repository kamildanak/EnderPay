package com.kamildanak.minecraft.forgeeconomy.api;

import com.kamildanak.minecraft.forgeeconomy.economy.Account;

import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ForgeEconomyApi {
    public static long getBalance(UUID uuid) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if(account == null) throw new NoSuchAccountException();
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount)  throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if(account == null) throw new NoSuchAccountException();
        account.addBalance(amount);
    }

    public static void takeFromBalance(UUID uuid, long amount) throws InsufficientCreditException, NoSuchAccountException{
        Account account = Account.get(uuid);
        if(account == null) throw new NoSuchAccountException();
        if (account.getBalance() < amount) throw new InsufficientCreditException();
        account.addBalance(-amount);
    }

    public static void takeFromBalanceNegative(UUID uuid, long amount) throws NoSuchAccountException {
        addToBalance(uuid, -amount);
    }
}
