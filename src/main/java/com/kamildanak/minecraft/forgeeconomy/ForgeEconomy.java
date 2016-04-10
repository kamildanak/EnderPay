package com.kamildanak.minecraft.forgeeconomy;

import com.kamildanak.minecraft.forgeeconomy.gui.GuiHandler;
import com.kamildanak.minecraft.forgeeconomy.proxy.Proxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

    static Configuration config;

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
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        config.save();
    }
}
