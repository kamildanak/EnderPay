package com.kamildanak.minecraft.enderpay.sponge;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Set;

public class EnderPayTransferResult extends EnderPayTransactionResult implements TransferResult {

    private final Account accountTo;

    public EnderPayTransferResult(Account account, Account accountTo, Currency currency, BigDecimal amount,
                                  Set<Context> contexts, ResultType result) {
        super(account, currency, amount, contexts, result, TransactionTypes.TRANSFER);
        this.accountTo = accountTo;
    }

    @Override
    public Account getAccountTo() {
        return this.accountTo;
    }

}
