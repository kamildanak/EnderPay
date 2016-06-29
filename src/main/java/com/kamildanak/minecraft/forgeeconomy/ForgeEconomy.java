package com.kamildanak.minecraft.forgeeconomy;

import com.kamildanak.minecraft.forgeeconomy.economy.Account;
import com.kamildanak.minecraft.forgeeconomy.economy.AccountPlayerInfo;
import com.kamildanak.minecraft.forgeeconomy.events.EventHandler;
import com.kamildanak.minecraft.forgeeconomy.gui.GuiHandler;
import com.kamildanak.minecraft.forgeeconomy.proxy.Proxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mod(modid=ForgeEconomy.MODID, name=ForgeEconomy.MODNAME, version=ForgeEconomy.VERSION)
public class ForgeEconomy {
    public static final String MODID = "forgeeconomy";
    public static final String MODNAME = "forgeeconomy";
    public static final String VERSION = "0.1";

    @Mod.Instance(MODID)
    public static ForgeEconomy instance;

    public static GuiHandler guiHandler;
    public static CreativeTabs tabEconomy;

    public static String defaultCurrency;
    public static String[] currencies;
    private Item itemWrench;
    private static Configuration config;

    @SidedProxy(clientSide = "com.kamildanak.minecraft.forgeeconomy.proxy.ProxyClient", serverSide = "com.kamildanak.minecraft.forgeeconomy.proxy.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        defaultCurrency = config.getString("general", "default currency", "credits",
                "Default currency (one displayed in HUD)");
        currencies = config.getStringList("general", "currency list", new String[] {"credits"},
                "List of currencies in which accounts may be opened");

        List<String> currenciesList = Arrays.asList(currencies);
        if(!currenciesList.contains(defaultCurrency)) currenciesList.add(defaultCurrency);
        currencies = (String[]) currenciesList.toArray();

        proxy.init();
        proxy.registerPackets();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        config.save();
    }

    @Mod.EventHandler
    public void onServerStop(FMLServerStoppingEvent evt) {
        try {
            AccountPlayerInfo.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onLoadingWorld(FMLServerStartingEvent evt) {
        AccountPlayerInfo.clear();

        File file = getWorldDir(evt.getServer().getEntityWorld());
        if (file == null) return;

        AccountPlayerInfo.setLocation(new File(file, "economy-accounts"));
    }

    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler)) return null;
        return ((SaveHandler) handler).getWorldDirectory();
    }
}
