package com.kamildanak.minecraft.forgeeconomy.commands;

import net.minecraft.command.CommandException;

public class InsufficientCreditException extends CommandException {

    public InsufficientCreditException() {
        this("commands.pay.insufficient_credit", new Object[0]);
    }

    public InsufficientCreditException(String message, Object... objects) {
        super(message, objects);
    }
}