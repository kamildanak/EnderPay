package com.kamildanak.minecraft.enderpay.proxy;

import com.kamildanak.minecraft.enderpay.gui.hud.Anchor;
import com.kamildanak.minecraft.enderpay.gui.hud.Position;

public class DummySettings implements ISettings{
    public Anchor anchor;
    public String currencyNameSingular;
    public String currencyNameMultiple;
    public long maxLoginDelta;
    public boolean basicIncome;
    public int basicIncomeAmount;
    public boolean stampedMoney;
    public int stampedMoneyPercent;
    public int startBalance;
    public boolean consumeBanknotesInCreativeMode;
    public boolean registerBanknoteRecipe;
    public int daysAfterBanknotesExpires;
    public int resetLoginDelta;
    public int dayLength;
    public int xOffset;
    public int yOffset;
    public Position position;
    public int pvpMoneyDrop;

    public DummySettings(String currencyNameSingular,
                         String currencyNameMultiple,
                         long maxLoginDelta,
                         boolean basicIncome,
                         int basicIncomeAmount,
                         boolean stampedMoney,
                         int stampedMoneyPercent,
                         int startBalance,
                         boolean consumeBanknotesInCreativeMode,
                         boolean registerBanknoteRecipe,
                         int daysAfterBanknotesExpires,
                         int resetLoginDelta,
                         int dayLength,
                         int xOffset,
                         int yOffset,
                         Position position,
                         Anchor anchor,
                         int pvpMoneyDrop) {
        this.currencyNameSingular = currencyNameSingular;
        this.currencyNameMultiple = currencyNameMultiple;
        this.maxLoginDelta = maxLoginDelta;
        this.basicIncome = basicIncome;
        this.basicIncomeAmount = basicIncomeAmount;
        this.stampedMoney = stampedMoney;
        this.stampedMoneyPercent = stampedMoneyPercent;
        this.startBalance = startBalance;
        this.consumeBanknotesInCreativeMode = consumeBanknotesInCreativeMode;
        this.registerBanknoteRecipe = registerBanknoteRecipe;
        this.daysAfterBanknotesExpires = daysAfterBanknotesExpires;
        this.resetLoginDelta = resetLoginDelta;
        this.dayLength = dayLength;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.position = position;
        this.anchor = anchor;
        this.pvpMoneyDrop = pvpMoneyDrop;
    }

    public DummySettings() {

    }

    @Override
    public String getCurrencyNameSingular() {
        return currencyNameSingular;
    }

    @Override
    public String getCurrencyNameMultiple() {
        return currencyNameMultiple;
    }

    @Override
    public long getMaxLoginDelta() {
        return maxLoginDelta;
    }

    @Override
    public boolean isBasicIncome() {
        return basicIncome;
    }

    @Override
    public int getBasicIncomeAmount() {
        return basicIncomeAmount;
    }

    @Override
    public boolean isStampedMoney() {
        return stampedMoney;
    }

    @Override
    public int getStampedMoneyPercent() {
        return stampedMoneyPercent;
    }

    @Override
    public int getStartBalance() {
        return startBalance;
    }

    @Override
    public boolean isConsumeBanknotesInCreativeMode() {
        return consumeBanknotesInCreativeMode;
    }

    @Override
    public boolean isRegisterBanknoteRecipe() {
        return registerBanknoteRecipe;
    }

    @Override
    public int getDaysAfterBanknotesExpires() {
        return daysAfterBanknotesExpires;
    }

    @Override
    public int getResetLoginDelta() {
        return resetLoginDelta;
    }

    @Override
    public int getDayLength() {
        return dayLength;
    }

    @Override
    public int getxOffset() {
        return xOffset;
    }

    @Override
    public int getyOffset() {
        return yOffset;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Anchor getAnchor() {
        return anchor;
    }

    @Override
    public int getPvpMoneyDrop() {
        return pvpMoneyDrop;
    }
}
