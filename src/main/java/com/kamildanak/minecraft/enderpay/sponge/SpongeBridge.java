package com.kamildanak.minecraft.enderpay.sponge;

import com.kamildanak.minecraft.enderpay.EnderPay;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

public class SpongeBridge {

    private SpongeBridge() {}

    public static void init() {
        Sponge.getRegistry().registerModule(Currency.class, new EnderPayCurrencyRegistryModule());
        Sponge.getServiceManager().setProvider(EnderPay.instance, EconomyService.class, new EnderPayEconomyService());
    }

}
