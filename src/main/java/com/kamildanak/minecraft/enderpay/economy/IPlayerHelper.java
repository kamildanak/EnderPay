package com.kamildanak.minecraft.enderpay.economy;

import java.util.UUID;

public interface IPlayerHelper {
    void send(UUID uuid, long balance);

    boolean isPlayerLoggedIn(UUID uuid);
}
