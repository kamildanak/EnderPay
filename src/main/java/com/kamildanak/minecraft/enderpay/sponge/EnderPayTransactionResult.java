package com.kamildanak.minecraft.enderpay.sponge;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.Set;

public class EnderPayTransactionResult implements TransactionResult {

    private final Account account;
    private final Currency currency;
    private final BigDecimal amount;
    private final Set<Context> contexts;
    private final ResultType result;
    private final TransactionType transactionType;

    public EnderPayTransactionResult(Account account, Currency currency, BigDecimal amount, Set<Context> contexts,
                                     ResultType result, TransactionType transactionType) {
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        this.contexts = contexts;
        this.result = result;
        this.transactionType = transactionType;
    }

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public Currency getCurrency() {
        return this.currency;
    }

    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public Set<Context> getContexts() {
        return this.contexts;
    }

    @Override
    public ResultType getResult() {
        return this.result;
    }

    @Override
    public TransactionType getType() {
        return this.transactionType;
    }

}
