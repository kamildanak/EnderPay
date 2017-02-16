package com.kamildanak.minecraft.enderpay.commands;

import net.minecraft.command.CommandException;

class InsufficientCreditException extends CommandException {

    InsufficientCreditException() {
        //noinspection RedundantArrayCreation
        this("commands.pay.insufficient_credit", new Object[0]);
    }

    private InsufficientCreditException(String message, Object... objects) {
        super(message, objects);
    }
}