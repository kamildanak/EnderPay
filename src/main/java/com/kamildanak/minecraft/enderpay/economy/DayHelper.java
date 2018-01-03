package com.kamildanak.minecraft.enderpay.economy;

import com.kamildanak.minecraft.enderpay.Utils;

public class DayHelper implements IDayHelper{
    @Override
    public long getCurrentDay() {
        return Utils.getCurrentDay();
    }
}
