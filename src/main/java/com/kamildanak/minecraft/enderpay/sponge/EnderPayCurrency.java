package com.kamildanak.minecraft.enderpay.sponge;

import com.kamildanak.minecraft.enderpay.EnderPay;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class EnderPayCurrency implements Currency {

    public static final EnderPayCurrency INSTANCE = new EnderPayCurrency();

    private EnderPayCurrency() {}

    @Override
    public Text getDisplayName() {
        return Text.of(EnderPay.settings.getCurrencyNameSingular());
    }

    @Override
    public Text getPluralDisplayName() {
        return Text.of(EnderPay.settings.getCurrencyNameMultiple());
    }

    @Override
    public Text getSymbol() {
        return Text.of(EnderPay.settings.getCurrencyNameMultiple());
    }

    @Override
    public Text format(BigDecimal amount, int numFractionDigits) {
        return Text.of(amount.round(new MathContext(numFractionDigits, RoundingMode.HALF_UP)).toPlainString(), " ", this.getSymbol());
    }

    @Override
    public int getDefaultFractionDigits() {
        return 0;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public String getId() {
        return EnderPay.modID + ":credits";
    }

    @Override
    public String getName() {
        return EnderPay.settings.getCurrencyNameMultiple();
    }

}
