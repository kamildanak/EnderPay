package com.kamildanak.minecraft.enderpay;

import com.kamildanak.minecraft.enderpay.commands.CommandBalance;
import com.kamildanak.minecraft.enderpay.commands.CommandPay;
import com.kamildanak.minecraft.enderpay.commands.CommandWallet;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.events.EventHandler;
import com.kamildanak.minecraft.enderpay.gui.GuiBanknote;
import com.kamildanak.minecraft.enderpay.gui.lib.GuiHandler;
import com.kamildanak.minecraft.enderpay.inventory.DummyContainer;
import com.kamildanak.minecraft.enderpay.item.ItemBlankBanknote;
import com.kamildanak.minecraft.enderpay.item.ItemFilledBanknote;
import com.kamildanak.minecraft.enderpay.proxy.Proxy;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod(modid = EnderPay.modID, name = EnderPay.modName, version = EnderPay.version,
        acceptedMinecraftVersions = "[1.10,1.10.2]")
public class EnderPay {
    public static final String modID = "enderpay";
    static final String modName = "enderpay";
    static final String version = "0.1";

    @Mod.Instance(modID)
    @SuppressWarnings("unused")
    public static EnderPay instance;

    public static GuiHandler guiBanknote;
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
    public static boolean registerBanknoteRecipe;
    public static int daysAfterBanknotesExpires;
    public static int resetLoginDelta;
    public static MinecraftServer minecraftServer;
    public static Item itemBlankBanknote;
    public static Item itemFilledBanknote;
    @SidedProxy(clientSide = "com.kamildanak.minecraft.enderpay.proxy.ProxyClient", serverSide = "com.kamildanak.minecraft.enderpay.proxy.Proxy")
    public static Proxy proxy;
    static int dayLength;
    private static Configuration config;

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

        maxLoginDelta = (1000 * 60 * 60) * config.getInt("maxLoginDelta", "basicIncome", 6, 1, 20,
                "Maximum number of day since last login the player will be payed for. ");

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

        daysAfterBanknotesExpires = config.getInt("daysAfterBanknotesExpires", "basicIncome", 10, 1, 100,
                "Number of days after banknote no longer has value");

        resetLoginDelta = config.getInt("resetLoginDelta", "basicIncome", 100, 1, 100,
                "Number of days of inactivity after account balance will be set to startBalance");

        dayLength = config.getInt("dayLength", "basicIncome", 24 * 60, 1, 24 * 60 * 365,
                "Day length in minutes");

        registerBanknoteRecipe = config.getBoolean("registerBanknoteRecipe", "general", true,
                "Set to true to allow crafting banknotes");

        itemBlankBanknote = new ItemBlankBanknote("blank_banknote");
        GameRegistry.register(itemBlankBanknote);

        itemFilledBanknote = new ItemFilledBanknote("filled_banknote");
        GameRegistry.register(itemFilledBanknote);

        proxy.init();
        proxy.registerPackets();
        proxy.registerCraftingRecipes();
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        guiBanknote = new GuiHandler("wrench") {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return new DummyContainer();
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return new GuiBanknote(world, new BlockPos(x, y, z), player);
            }
        };
        GuiHandler.register(this);
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

        Account.setLocation(new File(file, "EnderPay-accounts"));

        registerCommands(event);
    }

    private void registerCommands(FMLServerStartingEvent event) {
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
