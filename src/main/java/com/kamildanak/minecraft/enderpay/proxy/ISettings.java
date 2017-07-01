package com.kamildanak.minecraft.enderpay.proxy;

import com.kamildanak.minecraft.enderpay.gui.hud.Anchor;
import com.kamildanak.minecraft.enderpay.gui.hud.Position;

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

    Position getPosition();

    Anchor getAnchor();

    int getPvpMoneyDrop();
}
