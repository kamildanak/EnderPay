package com.kamildanak.minecraft.enderpay.proxy;

import com.kamildanak.minecraft.enderpay.gui.hud.Anchor;
import com.kamildanak.minecraft.enderpay.gui.hud.Position;
import com.kamildanak.minecraft.enderpay.network.client.MessageSettings;
import net.minecraftforge.common.config.Configuration;

public class Settings implements ISettings{
    public Configuration config;
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
    private Position position;
    private Anchor anchor;
    private int pvpMoneyDrop;
    private boolean useGuiConfigFromServer;

    @SuppressWarnings("WeakerAccess")
    public Settings() {

    }

    public void loadConfig(Configuration config) {
        this.config = config;
        currencyNameSingular = config.getString("currency name (singular)", "general", "credit",
                "Currency name (displayed in HUD, max 20 char)");
        currencyNameMultiple = config.getString("currency name (multiple)", "general", "credits",
                "Currency name (displayed in HUD, max 20 char)");
        maxLoginDelta = config.getInt("maxLoginDelta", "basicIncome", 6, -1, 200,
                "Maximum number of day since last login the player will be payed for. (set to -1 to disable)");
        if (maxLoginDelta == 0) maxLoginDelta = -1;

        basicIncome = config.getBoolean("enabled", "basicIncome", true,
                "Each day give set amount of credits to each player to stimulate economy");
        basicIncomeAmount = config.getInt("amount", "basicIncome", 50, 0, 10000,
                "Amount of credits to give each player each day");
        stampedMoney = config.getBoolean("enabled", "stampedMoney", true,
                "Take % of players money each day to stimulate economy");
        stampedMoneyPercent = config.getInt("percent", "stampedMoney", 1, 0, 100,
                "What percentage of players money should be taken each day");
        if (stampedMoneyPercent == 0) stampedMoney = false;
        startBalance = config.getInt("startBalance", "general", 100, 0, 10000,
                "Amount of credits given to new players joining the server");

        consumeBanknotesInCreativeMode = config.getBoolean("consumeBanknotesInCreativeMode", "general", true,
                "Should banknotes be consumed when used by player in creative mode");

        daysAfterBanknotesExpires = config.getInt("daysAfterBanknotesExpires", "basicIncome", 10, 1, 200,
                "Number of days after banknote no longer has value");

        resetLoginDelta = config.getInt("resetLoginDelta", "basicIncome", 100, -1, Integer.MAX_VALUE,
                "Number of days of inactivity after account balance will be set to startBalance (set to -1 to disable)");
        if (resetLoginDelta == 0) resetLoginDelta = -1;

        dayLength = config.getInt("dayLength", "basicIncome", 24 * 60, 1, 24 * 60 * 365,
                "Day length in minutes");

        registerBanknoteRecipe = config.getBoolean("registerBanknoteRecipe", "general", true,
                "Set to true to allow crafting banknotes [temporary disabled]");

        String positionHUD = config.getString("position", "gui", "hud_above_right",
                "Position of HUD", new String[]
                        {"top_left", "middle_left", "bottom_left",
                                "top_middle", "middle_middle", "bottom_middle",
                                "top_right", "middle_right", "bottom_right",
                                "hud_above_left", "hud_above_middle", "hud_above_right"});

        position = Position.byName(positionHUD);

        String anchorHUD = config.getString("anchorHUD", "gui", "right",
                "HUD anchor position", new String[]{"left", "centre", "right"});
        anchor = Anchor.byName(anchorHUD);

        xOffset = config.getInt("xOffset", "gui", 0, -10000, 10000,
                "HUD x offset in scaled pixels");

        yOffset = config.getInt("yOffset", "gui", 0, -10000, 10000,
                "HUD y offset in scaled pixels");

        useGuiConfigFromServer = config.getBoolean("useGuiConfigFromServer", "gui", true,
                "Use HUD position provided by server");

        pvpMoneyDrop = config.getInt("pvpMoneyDrop", "general", 0, -2147483647,
                100, "What percentage (0-100) or what amount (pvpMoneyDrop<0)" +
                        " of players money should be transferred to slayer");
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

    public Position getPosition() {
        return position;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public int getPvpMoneyDrop() {
        return pvpMoneyDrop;
    }

    public void setSettings(MessageSettings settings) {
    }

    boolean isUseGuiConfigFromServer() {
        return useGuiConfigFromServer;
    }

    public void reloadConfig() {
        loadConfig(config);
    }
}
