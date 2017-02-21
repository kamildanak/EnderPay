package com.kamildanak.minecraft.enderpay.api;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.item.ItemBlankBanknote;
import com.kamildanak.minecraft.enderpay.item.ItemFilledBanknote;
import net.minecraft.item.ItemStack;

import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class EnderPayApi {
    public static long getBalance(UUID uuid) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        account.addBalance(amount);
    }

    public static void takeFromBalance(UUID uuid, long amount) throws InsufficientCreditException, NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        if (account.getBalance() < amount) throw new InsufficientCreditException();
        account.addBalance(-amount);
    }

    public static void takeFromBalanceNegative(UUID uuid, long amount) throws NoSuchAccountException {
        addToBalance(uuid, -amount);
    }

    public static ItemStack getBanknote(long creditsAmount) {
        return ItemFilledBanknote.getItemStack(creditsAmount);
    }

    public static long getBanknoteOriginalValue(ItemStack itemStack) throws NotABanknoteException {
        if (!isBlankBanknote(itemStack) && !isFilledBanknote(itemStack)) throw new NotABanknoteException();
        if (!isValidFilledBanknote(itemStack)) return 0;
        //noinspection ConstantConditions - itemStack.getTagCompund() == null checked in isValidFilledBanknote()
        return itemStack.getTagCompound().getLong("Amount");
    }

    public static long getBanknoteCurrentValue(ItemStack itemStack) throws NotABanknoteException {
        long amount = getBanknoteOriginalValue(itemStack);
        if (amount <= 0) return 0;
        long dayAfter = Utils.daysAfterDate(ItemFilledBanknote.getDateIssued(itemStack));
        if (dayAfter < 0) return amount;
        if (ItemFilledBanknote.isExpired(ItemFilledBanknote.getDateIssued(itemStack))) {
            amount = 0;
        } else {
            amount -= Math.ceil((double) (dayAfter * (amount * EnderPay.settings.getStampedMoneyPercent())) / 100);
        }
        return amount;
    }

    public static boolean isValidFilledBanknote(ItemStack itemStack) {
        return itemStack != null &&
                itemStack.getItem() instanceof ItemFilledBanknote &&
                itemStack.getTagCompound() != null &&
                itemStack.getTagCompound().hasKey("Amount") &&
                itemStack.getTagCompound().hasKey("DateIssued");
    }

    public static boolean isFilledBanknote(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ItemFilledBanknote;
    }

    public static boolean isBlankBanknote(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ItemBlankBanknote;
    }

    @Deprecated
    public static String getCurrencyNameSingular() {
        return EnderPay.settings.getCurrencyNameSingular();
    }

    @Deprecated
    public static String getCurrencyNameMultiple() {
        return EnderPay.settings.getCurrencyNameMultiple();
    }

    public static String getCurrencyName(long amount) {
        if (amount == 1) return EnderPay.settings.getCurrencyNameSingular();
        return EnderPay.settings.getCurrencyNameMultiple();
    }
}
