package com.kamildanak.minecraft.enderpay.proxy;

import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.gui.hud.Anchor;
import com.kamildanak.minecraft.enderpay.gui.hud.Position;
import com.kamildanak.minecraft.enderpay.network.client.MessageSettings;

@SuppressWarnings("unused")
public class SettingsClient extends Settings implements ISettings {
    private MessageSettings message;

    public SettingsClient() {
        super();
    }

    public String getCurrencyNameSingular() {
        if (Utils.isClient() && message != null) return message.getCurrencyNameSingular();
        return super.getCurrencyNameSingular();
    }

    public String getCurrencyNameMultiple() {
        if (Utils.isClient() && message != null) return message.getCurrencyNameMultiple();
        return super.getCurrencyNameMultiple();
    }

    public long getMaxLoginDelta() {
        if (Utils.isClient() && message != null) return message.getMaxLoginDelta();
        return super.getMaxLoginDelta();
    }

    public boolean isBasicIncome() {
        if (Utils.isClient() && message != null) return message.isBasicIncome();
        return super.isBasicIncome();
    }

    public int getBasicIncomeAmount() {
        if (Utils.isClient() && message != null) return message.getBasicIncomeAmount();
        return super.getBasicIncomeAmount();
    }

    public boolean isStampedMoney() {
        if (Utils.isClient() && message != null) return message.isStampedMoney();
        return super.isStampedMoney();
    }

    public int getStampedMoneyPercent() {
        if (Utils.isClient() && message != null) return message.getStampedMoneyPercent();
        return super.getStampedMoneyPercent();
    }

    public int getStartBalance() {
        if (Utils.isClient() && message != null) return message.getStartBalance();
        return super.getStartBalance();
    }

    public boolean isConsumeBanknotesInCreativeMode() {
        if (Utils.isClient() && message != null) return message.isConsumeBanknotesInCreativeMode();
        return super.isConsumeBanknotesInCreativeMode();
    }

    public boolean isRegisterBanknoteRecipe() {
        if (Utils.isClient() && message != null) return message.isRegisterBanknoteRecipe();
        return super.isRegisterBanknoteRecipe();
    }

    public int getDaysAfterBanknotesExpires() {
        if (Utils.isClient() && message != null) return message.getDaysAfterBanknotesExpires();
        return super.getDaysAfterBanknotesExpires();
    }

    public int getResetLoginDelta() {
        if (Utils.isClient() && message != null) return message.getResetLoginDelta();
        return super.getResetLoginDelta();
    }

    public int getDayLength() {
        if (Utils.isClient() && message != null) return message.getDayLength();
        return super.getDayLength();
    }

    public int getxOffset() {
        if (Utils.isClient() && message != null) return message.getxOffset();
        return super.getxOffset();
    }

    public int getyOffset() {
        if (Utils.isClient() && message != null) return message.getyOffset();
        return super.getyOffset();
    }

    public Position getPosition() {
        if (Utils.isClient() && message != null) return message.getPosition();
        return super.getPosition();
    }

    public Anchor getAnchor() {
        if (Utils.isClient() && message != null) return message.getAnchor();
        return super.getAnchor();
    }

    public int getPvpMoneyDrop() {
        if (Utils.isClient() && message != null) return message.getPvpMoneyDrop();
        return super.getPvpMoneyDrop();
    }

    public void setSettings(MessageSettings message) {
        this.message = message;
    }
}
