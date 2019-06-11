package com.kamildanak.minecraft.enderpay.sponge;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.economy.Account;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EnderPayUniqueAccount implements UniqueAccount {

    private static final BigDecimal LONG_MAX = BigDecimal.valueOf(Long.MAX_VALUE);

    private final Account account;
    private final UUID id;

    public EnderPayUniqueAccount(UUID id, Account account) {
        this.account = account;
        this.id = id;
    }

    @Override
    public Text getDisplayName() {
        return Text.of(id.toString());
    }

    @Override
    public BigDecimal getDefaultBalance(Currency currency) {
        if (!(checkNotNull(currency) instanceof EnderPayCurrency)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(EnderPay.settings.getStartBalance());
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        checkNotNull(contexts);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency)) {
            return false;
        }
        return this.account.getBalance() != 0;
    }

    @Override
    public BigDecimal getBalance(Currency currency, Set<Context> contexts) {
        checkNotNull(contexts);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(this.account.getBalance());
    }

    @Override
    public Map<Currency, BigDecimal> getBalances(Set<Context> contexts) {
        checkNotNull(contexts);
        return ImmutableMap.of(EnderPayCurrency.INSTANCE, BigDecimal.valueOf(this.account.getBalance()));
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        checkNotNull(contexts);
        checkNotNull(cause);
        checkNotNull(amount);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency) || amount.compareTo(BigDecimal.ZERO) < 0) {
            return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.FAILED,
                    TransactionTypes.DEPOSIT);
        }
        if (amount.compareTo(LONG_MAX) > 0) {
            return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.ACCOUNT_NO_SPACE,
                    TransactionTypes.DEPOSIT);
        }
        long toAdd = amount.round(new MathContext(0, RoundingMode.HALF_UP)).longValue();
        this.account.setBalance(toAdd);
        return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.SUCCESS,
                TransactionTypes.DEPOSIT);
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause, Set<Context> contexts) {
        checkNotNull(cause);
        checkNotNull(contexts);
        this.account.setBalance(EnderPay.settings.getStartBalance());
        return ImmutableMap.of(EnderPayCurrency.INSTANCE, new EnderPayTransactionResult(this,
                EnderPayCurrency.INSTANCE, BigDecimal.valueOf(EnderPay.settings.getStartBalance()), contexts,
                ResultType.SUCCESS, TransactionTypes.DEPOSIT));
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> contexts) {
        checkNotNull(cause);
        checkNotNull(contexts);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency)) {
            return new EnderPayTransactionResult(this, currency, BigDecimal.ZERO, contexts, ResultType.FAILED,
                    TransactionTypes.DEPOSIT);
        }
        this.account.setBalance(EnderPay.settings.getStartBalance());
        return new EnderPayTransactionResult(this, currency, BigDecimal.valueOf(EnderPay.settings.getStartBalance()),
                contexts, ResultType.SUCCESS, TransactionTypes.DEPOSIT);
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        checkNotNull(cause);
        checkNotNull(amount);
        checkNotNull(contexts);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency)) {
            return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.FAILED,
                    TransactionTypes.DEPOSIT);
        }
        if (BigDecimal.valueOf(this.account.getBalance()).add(amount).compareTo(LONG_MAX) > 0) {
            return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.ACCOUNT_NO_SPACE,
                    TransactionTypes.DEPOSIT);
        }
        long toAdd = amount.round(new MathContext(0, RoundingMode.HALF_UP)).longValue();
        this.account.addBalance(toAdd);
        return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.SUCCESS,
                TransactionTypes.DEPOSIT);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        checkNotNull(amount);
        checkNotNull(cause);
        checkNotNull(contexts);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency) || amount.compareTo(BigDecimal.ZERO) < 0) {
            return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.FAILED,
                    TransactionTypes.WITHDRAW);
        }
        if (amount.compareTo(BigDecimal.valueOf(this.account.getBalance())) > 0) {
            return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.ACCOUNT_NO_FUNDS,
                    TransactionTypes.WITHDRAW);
        }
        long toSubtract = amount.round(new MathContext(0, RoundingMode.HALF_UP)).longValue();
        this.account.addBalance(-toSubtract);
        return new EnderPayTransactionResult(this, currency, amount, contexts, ResultType.SUCCESS,
                TransactionTypes.WITHDRAW);
    }

    @Override
    public TransferResult transfer(org.spongepowered.api.service.economy.account.Account to, Currency currency,
                                   BigDecimal amount, Cause cause, Set<Context> contexts) {
        checkNotNull(to);
        checkNotNull(amount);
        checkNotNull(cause);
        checkNotNull(contexts);
        if (!(checkNotNull(currency) instanceof EnderPayCurrency)) {
            return new EnderPayTransferResult(this, to, currency, amount, contexts, ResultType.FAILED);
        }
        TransactionResult withdrawResult = this.withdraw(currency, amount, cause, contexts);
        if (withdrawResult.getResult() != ResultType.SUCCESS) {
            return new EnderPayTransferResult(this, to, currency, amount, contexts, withdrawResult.getResult());
        }
        TransactionResult depositResult = to.deposit(currency, amount, cause, contexts);
        if (depositResult.getResult() != ResultType.SUCCESS) {
            this.deposit(currency, amount, cause, contexts);
            return new EnderPayTransferResult(this, to, currency, amount, contexts, depositResult.getResult());
        }
        return new EnderPayTransferResult(this, to, currency, amount, contexts, ResultType.SUCCESS);
    }

    @Override
    public String getIdentifier() {
        return this.getUniqueId().toString();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return ImmutableSet.of();
    }

    @Override
    public UUID getUniqueId() {
        return this.id;
    }

}
