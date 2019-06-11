package com.kamildanak.minecraft.enderpay.sponge;

import com.google.common.collect.ImmutableSet;
import com.kamildanak.minecraft.enderpay.economy.Account;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class EnderPayEconomyService implements EconomyService {

    @Override
    public Currency getDefaultCurrency() {
        return EnderPayCurrency.INSTANCE;
    }

    @Override
    public Set<Currency> getCurrencies() {
        return ImmutableSet.of(EnderPayCurrency.INSTANCE);
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return Account.get(uuid) != null;
    }

    @Override
    public boolean hasAccount(String identifier) {
        return false;
    }

    @Override
    public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) {
        Account account = Account.get(uuid);
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(new EnderPayUniqueAccount(uuid, account));
    }

    @Override
    public Optional<org.spongepowered.api.service.economy.account.Account> getOrCreateAccount(String identifier) {
        return Optional.empty();
    }

    @Override
    public void registerContextCalculator(
            ContextCalculator<org.spongepowered.api.service.economy.account.Account> calculator) {

    }

}
