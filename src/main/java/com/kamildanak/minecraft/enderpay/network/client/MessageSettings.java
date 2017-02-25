package com.kamildanak.minecraft.enderpay.network.client;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.network.AbstractMessage;
import com.kamildanak.minecraft.enderpay.proxy.ISettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class MessageSettings extends AbstractMessage.AbstractClientMessage<MessageSettings> {
    private String currencyNameSingular;
    private String currencyNameMultiple;
    private long maxLoginDelta;
    private boolean basicIncome;
    private int basicIncomeAmount;
    private boolean stampedMoney;
    private int stampedMoneyPercent;
    private int startBalance;
    private boolean consumeBanknotesInCreativeMode;
    private boolean registerBanknoteRecipe;
    private int daysAfterBanknotesExpires;
    private int resetLoginDelta;
    private int dayLength;
    private int xOffset;
    private int yOffset;
    private boolean positionRelative;

    @SuppressWarnings("unused")
    public MessageSettings() {
    }

    public MessageSettings(ISettings settings) {
        this.currencyNameSingular = settings.getCurrencyNameSingular();
        this.currencyNameMultiple = settings.getCurrencyNameMultiple();
        this.maxLoginDelta = settings.getMaxLoginDelta();
        this.basicIncome = settings.isBasicIncome();
        this.basicIncomeAmount = settings.getBasicIncomeAmount();
        this.stampedMoney = settings.isStampedMoney();
        this.stampedMoneyPercent = settings.getStampedMoneyPercent();
        this.startBalance = settings.getStartBalance();
        this.consumeBanknotesInCreativeMode = settings.isConsumeBanknotesInCreativeMode();
        this.registerBanknoteRecipe = settings.isRegisterBanknoteRecipe();
        this.daysAfterBanknotesExpires = settings.getDaysAfterBanknotesExpires();
        this.resetLoginDelta = settings.getResetLoginDelta();
        this.dayLength = settings.getDayLength();
        this.xOffset = settings.getxOffset();
        this.yOffset = settings.getyOffset();
        this.positionRelative = settings.isPositionRelative();
    }


    public String getCurrencyNameSingular() {
        return currencyNameSingular;
    }

    public String getCurrencyNameMultiple() {
        return currencyNameMultiple;
    }

    public long getMaxLoginDelta() {
        return maxLoginDelta;
    }

    public boolean isBasicIncome() {
        return basicIncome;
    }

    public int getBasicIncomeAmount() {
        return basicIncomeAmount;
    }

    public boolean isStampedMoney() {
        return stampedMoney;
    }

    public int getStampedMoneyPercent() {
        return stampedMoneyPercent;
    }

    public int getStartBalance() {
        return startBalance;
    }

    public boolean isConsumeBanknotesInCreativeMode() {
        return consumeBanknotesInCreativeMode;
    }

    public boolean isRegisterBanknoteRecipe() {
        return registerBanknoteRecipe;
    }

    public int getDaysAfterBanknotesExpires() {
        return daysAfterBanknotesExpires;
    }

    public int getResetLoginDelta() {
        return resetLoginDelta;
    }

    public int getDayLength() {
        return dayLength;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public boolean isPositionRelative() {
        return positionRelative;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.currencyNameSingular = ByteBufUtils.readUTF8String(buffer);
        this.currencyNameMultiple = ByteBufUtils.readUTF8String(buffer);
        this.maxLoginDelta = buffer.readLong();
        this.basicIncome = buffer.readBoolean();
        this.basicIncomeAmount = buffer.readInt();
        this.stampedMoney = buffer.readBoolean();
        this.stampedMoneyPercent = buffer.readInt();
        this.startBalance = buffer.readInt();
        this.consumeBanknotesInCreativeMode = buffer.readBoolean();
        this.registerBanknoteRecipe = buffer.readBoolean();
        this.daysAfterBanknotesExpires = buffer.readInt();
        this.resetLoginDelta = buffer.readInt();
        this.dayLength = buffer.readInt();
        this.xOffset = buffer.readInt();
        this.yOffset = buffer.readInt();
        this.positionRelative = buffer.readBoolean();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        ByteBufUtils.writeUTF8String(buffer, this.currencyNameSingular);
        ByteBufUtils.writeUTF8String(buffer, this.currencyNameMultiple);
        buffer.writeLong(this.maxLoginDelta);
        buffer.writeBoolean(this.basicIncome);
        buffer.writeInt(this.basicIncomeAmount);
        buffer.writeBoolean(this.stampedMoney);
        buffer.writeInt(this.stampedMoneyPercent);
        buffer.writeInt(this.startBalance);
        buffer.writeBoolean(this.consumeBanknotesInCreativeMode);
        buffer.writeBoolean(this.registerBanknoteRecipe);
        buffer.writeInt(this.daysAfterBanknotesExpires);
        buffer.writeInt(this.resetLoginDelta);
        buffer.writeInt(this.dayLength);
        buffer.writeInt(this.xOffset);
        buffer.writeInt(this.yOffset);
        buffer.writeBoolean(this.positionRelative);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        EnderPay.settings.setSettings(this);
    }
}
