package com.kamildanak.minecraft.enderpay.proxy;

public class DummySettings implements ISettings{
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
    private int pvpMoneyDrop;

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
                         boolean positionRelative,
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
        this.positionRelative = positionRelative;
        this.pvpMoneyDrop = pvpMoneyDrop;
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
    public boolean isPositionRelative() {
        return positionRelative;
    }

    @Override
    public int getPvpMoneyDrop() {
        return pvpMoneyDrop;
    }
}
