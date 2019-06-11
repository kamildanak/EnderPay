package com.kamildanak.minecraft.enderpay;

import com.kamildanak.minecraft.enderpay.commands.CommandBalance;
import com.kamildanak.minecraft.enderpay.commands.CommandPay;
import com.kamildanak.minecraft.enderpay.commands.CommandWallet;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.economy.DayHelper;
import com.kamildanak.minecraft.enderpay.economy.PlayerHelper;
import com.kamildanak.minecraft.enderpay.events.EventHandler;
import com.kamildanak.minecraft.enderpay.gui.GuiBanknote;
import com.kamildanak.minecraft.enderpay.item.ItemBlankBanknote;
import com.kamildanak.minecraft.enderpay.item.ItemFilledBanknote;
import com.kamildanak.minecraft.enderpay.proxy.Proxy;
import com.kamildanak.minecraft.enderpay.proxy.Settings;
import com.kamildanak.minecraft.enderpay.sponge.SpongeBridge;
import com.kamildanak.minecraft.foamflower.gui.GuiHandler;
import com.kamildanak.minecraft.foamflower.inventory.DummyContainer;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = EnderPay.modID, name = EnderPay.modName, version = EnderPay.VERSION,
        acceptedMinecraftVersions = EnderPay.ACCEPTED_VERSIONS,
        guiFactory = "com.kamildanak.minecraft.enderpay.gui.EnderPayGuiFactory",
        dependencies = "required-after:foamflower")
public class EnderPay {
    public static final String modID = "enderpay";
    static final String VERSION = "{@enderPayVersion}";
    static final String ACCEPTED_VERSIONS = "{@mcVersion}";
    static final String modName = "EnderPay Economy Api";
    @Mod.Instance(modID)
    @SuppressWarnings("unused")
    public static EnderPay instance;

    public static GuiHandler guiBanknote;

    public static MinecraftServer minecraftServer;
    public static Item itemBlankBanknote;
    public static Item itemFilledBanknote;
    @SidedProxy(clientSide = "com.kamildanak.minecraft.enderpay.proxy.ProxyClient", serverSide = "com.kamildanak.minecraft.enderpay.proxy.Proxy")
    @SuppressWarnings("unused")
    public static Proxy proxy;
    @SidedProxy(clientSide = "com.kamildanak.minecraft.enderpay.proxy.SettingsClient", serverSide = "com.kamildanak.minecraft.enderpay.proxy.Settings")
    @SuppressWarnings("unused")
    public static Settings settings;
    private static Configuration config;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        proxy.preInit();

        settings.loadConfig(config);
        Account.setInterfaces(settings, new DayHelper(), new PlayerHelper());
        itemBlankBanknote = new ItemBlankBanknote("blank_banknote");
        itemFilledBanknote = new ItemFilledBanknote("filled_banknote");
        if (Loader.isModLoaded("spongeforge")) {
            SpongeBridge.init();
        }
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        proxy.init();
        proxy.registerPackets();
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
