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

@Mod(modid=ForgeEconomy.MODID, name=ForgeEconomy.MODNAME, version=ForgeEconomy.VERSION)
public class ForgeEconomy {
    public static final String MODID = "forgeeconomy";
    public static final String MODNAME = "forgeeconomy";
    public static final String VERSION = "0.1";

    @Mod.Instance(MODID)
    public static ForgeEconomy instance;

    public static GuiHandler guiHandler;
    public static CreativeTabs tabEconomy;

    static Configuration config;

    @SidedProxy(clientSide = "com.kamildanak.minecraft.forgeeconomy.ProxyClient", serverSide = "com.kamildanak.minecraft.forgeeconomy.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
