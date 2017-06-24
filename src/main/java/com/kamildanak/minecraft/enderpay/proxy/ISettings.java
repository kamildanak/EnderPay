package com.kamildanak.minecraft.enderpay.proxy;

public interface ISettings {
    String getCurrencyNameSingular();

    String getCurrencyNameMultiple();

    long getMaxLoginDelta();

    boolean isBasicIncome();

    int getBasicIncomeAmount();

    boolean isStampedMoney();

    int getStampedMoneyPercent();

    int getStartBalance();

    boolean isConsumeBanknotesInCreativeMode();

    boolean isRegisterBanknoteRecipe();

    int getDaysAfterBanknotesExpires();

    int getResetLoginDelta();

    int getDayLength();

    int getxOffset();

    int getyOffset();

    boolean isPositionRelative();

    int getMoneyDropValue();
}
