package com.kamildanak.minecraft.forgeeconomy;

import com.kamildanak.minecraft.forgeeconomy.commands.CommandBalance;
import com.kamildanak.minecraft.forgeeconomy.commands.CommandPay;
import com.kamildanak.minecraft.forgeeconomy.commands.CommandWallet;
import com.kamildanak.minecraft.forgeeconomy.economy.Account;
import com.kamildanak.minecraft.forgeeconomy.events.EventHandler;
import com.kamildanak.minecraft.forgeeconomy.item.ItemBlankBanknote;
import com.kamildanak.minecraft.forgeeconomy.proxy.Proxy;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod(modid=ForgeEconomy.modID, name=ForgeEconomy.modName, version=ForgeEconomy.version)
public class ForgeEconomy {
    public static final String modID = "forgeeconomy";
    static final String modName = "forgeeconomy";
    static final String version = "0.1";

    @Mod.Instance(modID)
    @SuppressWarnings("unused")
    public static ForgeEconomy instance;

    //public static GuiHandler guiHandler;
    //public static CreativeTabs tabEconomy;

    public static String currencyNameSingular;
    public static String currencyNameMultiple;
    public static long maxLoginDelta;
    public static boolean basicIncome;
    public static int basicIncomeAmount;
    public static boolean stampedMoney;
    public static int stampedMoneyPercent;
    public static int startBalance;
    public static boolean consumeBanknotesInCreativeMode;
    public static MinecraftServer minecraftServer;

    private static Configuration config;

    public static Item itemBlankBanknote;

    @SidedProxy(clientSide = "com.kamildanak.minecraft.forgeeconomy.proxy.ProxyClient", serverSide = "com.kamildanak.minecraft.forgeeconomy.proxy.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        proxy.preInit();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        currencyNameSingular = config.getString("currency name (singular)", "general", "credit",
                "Currency name (displayed in HUD, max 20 char)");

        currencyNameMultiple = config.getString("currency name (multiple)", "general", "credits",
                "Currency name (displayed in HUD, max 20 char)");

        maxLoginDelta = (1000*60*60)*config.getInt("maxLoginDelta", "basicIncome", 24*6, 24, 480,
                "Maximum number of day since last login the player will be payed for. (min 24h, max 480h (20days)");

        basicIncome = config.getBoolean("enabled", "basicIncome", true,
                "Each day give set amount of credits to each player to stimulate economy");

        basicIncomeAmount = config.getInt("amount", "basicIncome", 50, 0, 10000,
                "Amount of credits to give each player each day");

        stampedMoney = config.getBoolean("enabled", "stampedMoney", true,
                "Take % of players money each day to stimulate economy");

        stampedMoneyPercent = config.getInt("percent", "stampedMoney", 1, 0, 100,
                "What percentage of players money should be taken each day");

        startBalance = config.getInt("startBalance", "general", 100, 0, 10000,
                "Amount of credits given to new players joining the server");

        consumeBanknotesInCreativeMode = config.getBoolean("consumeBanknotesInCreativeMode", "general", true,
                "Should banknotes be consumed when used by player in creative mode");

        itemBlankBanknote = new ItemBlankBanknote("blank_banknote");
        GameRegistry.register(itemBlankBanknote);

        proxy.init();
        proxy.registerPackets();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        config.save();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.getEntityWorld());
        if (file == null) return;

        Account.setLocation(new File(file, "ForgeEconomy-accounts"));

        registerCommands(event);
    }

    private void registerCommands(FMLServerStartingEvent event)
    {
        MinecraftServer server = event.getServer();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new CommandWallet());
        manager.registerCommand(new CommandBalance());
        manager.registerCommand(new CommandPay());
    }

    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler)) return null;
        return handler.getWorldDirectory();
    }
}
