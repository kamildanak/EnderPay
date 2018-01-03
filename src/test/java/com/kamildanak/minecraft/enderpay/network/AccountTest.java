package com.kamildanak.minecraft.enderpay.network;

import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.economy.IDayHelper;
import com.kamildanak.minecraft.enderpay.economy.IPlayerHelper;
import com.kamildanak.minecraft.enderpay.proxy.DummySettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountTest {
    private DummySettings settings;
    private IDayHelper dayHelper;
    private IPlayerHelper playerHelper;
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void prepareAccountClass()
    {
        settings = new DummySettings();
        settings.maxLoginDelta = 0;
        settings.basicIncome = false;
        settings.basicIncomeAmount = 0;
        settings.startBalance = 0;
        settings.daysAfterBanknotesExpires = 0;
        settings.resetLoginDelta = 0;
        settings.stampedMoney = false;
        settings.stampedMoneyPercent = 0;
        dayHelper = mock(IDayHelper.class);
        playerHelper = mock(IPlayerHelper.class);
        when(playerHelper.isPlayerLoggedIn(any())).thenReturn(true);
        Account.setInterfaces(settings, dayHelper, playerHelper);
        Account.setLocation(folder.getRoot());
    }

    @Test
    public void IsStartBalanceCorrect()
    {
        settings.startBalance = 100;
        when(dayHelper.getCurrentDay()).thenReturn((long) 0);
        assertEquals(settings.startBalance, Account.get(UUID.randomUUID()).getBalance());
        when(dayHelper.getCurrentDay()).thenReturn((long) 10);
        assertEquals(settings.startBalance, Account.get(UUID.randomUUID()).getBalance());
        when(dayHelper.getCurrentDay()).thenReturn((long) 999999999);
        assertEquals(settings.startBalance, Account.get(UUID.randomUUID()).getBalance());
    }

    @Test
    public void IsBasicIncomeComputed()
    {
        settings.startBalance = 0;
        settings.basicIncome = true;
        settings.basicIncomeAmount = 100;
        settings.maxLoginDelta = 200;
        settings.resetLoginDelta = 200;
        when(dayHelper.getCurrentDay()).thenReturn((long) 0);
        Account account = Account.get(UUID.randomUUID());
        assertEquals(0, account.getBalance());
        when(dayHelper.getCurrentDay()).thenReturn((long) 100);
        account.update();
        assertEquals(100*100, account.getBalance());
    }

    @Test
    public void IsBasicIncomeComputedLongLoginDelta()
    {
        settings.startBalance = 0;
        settings.basicIncome = true;
        settings.basicIncomeAmount = 10;
        settings.maxLoginDelta = Integer.MAX_VALUE;
        settings.resetLoginDelta = Integer.MAX_VALUE;
        when(dayHelper.getCurrentDay()).thenReturn((long) 0);
        Account account = Account.get(UUID.randomUUID());
        assertEquals(0, account.getBalance());
        when(dayHelper.getCurrentDay()).thenReturn((long) Integer.MAX_VALUE-1);
        account.update();
        assertEquals(10*(long)(Integer.MAX_VALUE-1), account.getBalance());
    }

    @Test
    public void IsBasicIncomeNotComputedAfterMaxLoginDelta()
    {
        settings.startBalance = 0;
        settings.basicIncome = true;
        settings.basicIncomeAmount = 10;
        settings.maxLoginDelta = 10;
        settings.resetLoginDelta = Integer.MAX_VALUE;
        when(dayHelper.getCurrentDay()).thenReturn((long) 0);
        Account account = Account.get(UUID.randomUUID());
        assertEquals(0, account.getBalance());
        when(dayHelper.getCurrentDay()).thenReturn((long) Integer.MAX_VALUE-1);
        account.update();
        assertEquals(10*10, account.getBalance());
    }

    @Test
    public void IsBalanceResetAfterResetLoginDelta()
    {
        settings.startBalance = 0;
        settings.basicIncome = true;
        settings.basicIncomeAmount = 10;
        settings.maxLoginDelta = 10;
        settings.resetLoginDelta = 20;
        when(dayHelper.getCurrentDay()).thenReturn(0L);
        Account account = Account.get(UUID.randomUUID());
        assertEquals(0, account.getBalance());
        when(dayHelper.getCurrentDay()).thenReturn(21L);
        account.update();
        assertEquals(0, account.getBalance());

        when(dayHelper.getCurrentDay()).thenReturn(0L);
        account = Account.get(UUID.randomUUID());
        assertEquals(0, account.getBalance());
        when(dayHelper.getCurrentDay()).thenReturn(19L);
        account.update();
        assertEquals(10*10, account.getBalance());
    }

    @Test
    public void IsStampedMoneyComputed()
    {
        settings.startBalance = 0;
        settings.stampedMoney = true;
        settings.stampedMoneyPercent = 1;
        settings.maxLoginDelta = Integer.MAX_VALUE;
        settings.resetLoginDelta = Integer.MAX_VALUE;
        Account account = Account.get(UUID.randomUUID());
        assertEquals(settings.startBalance, account.getBalance());
        account.setBalance(Long.MAX_VALUE);
        when(dayHelper.getCurrentDay()).thenReturn((long) 2000);
        account.update();
        assertEquals(17190120, account.getBalance()/1000);
    }

    @Test
    public void CanBasicIncomeBeTurnedOff()
    {
        settings.startBalance = 0;
        settings.basicIncome = false;
        settings.basicIncomeAmount = 100;
        when(dayHelper.getCurrentDay()).thenReturn((long) 0);
        Account account = Account.get(UUID.randomUUID());
        assertEquals(account.getBalance(), 0);
        when(dayHelper.getCurrentDay()).thenReturn((long) 100);
        assertEquals(account.getBalance(), 0);
    }
}
