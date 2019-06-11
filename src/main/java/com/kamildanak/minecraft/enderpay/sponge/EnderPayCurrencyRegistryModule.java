package com.kamildanak.minecraft.enderpay.sponge;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.RegistrationPhase;
import org.spongepowered.api.registry.util.DelayedRegistration;
import org.spongepowered.api.service.economy.Currency;

import java.util.Collection;
import java.util.Optional;

public class EnderPayCurrencyRegistryModule implements CatalogRegistryModule<Currency> {

    @Override
    public Optional<Currency> getById(String id) {
        if (id.equals(EnderPayCurrency.INSTANCE.getId())) {
            return Optional.of(EnderPayCurrency.INSTANCE);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Currency> getAll() {
        return ImmutableList.of(EnderPayCurrency.INSTANCE);
    }

    @Override
    @DelayedRegistration(RegistrationPhase.INIT)
    public void registerDefaults() {}

}
