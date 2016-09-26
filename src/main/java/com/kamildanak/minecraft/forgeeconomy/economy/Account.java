package com.kamildanak.minecraft.forgeeconomy.economy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import com.kamildanak.minecraft.forgeeconomy.network.PacketDispatcher;
import com.kamildanak.minecraft.forgeeconomy.network.client.MessageBalance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Account {
    private static HashMap<String, Account> objects = new HashMap<>();
    private static File location;
    private boolean changed;

    private UUID uuid;
    private long balance;

    private Account(UUID uuid) {
        this.uuid = uuid;
        this.balance = 0;
        this.changed = true;
    }

    public static Account get(EntityPlayer player) {
        return get(player.getUniqueID());
    }

    private static Account get(UUID uuid) {
        Account account = objects.get(uuid.toString());
        if (account != null) return account;

        if (location == null) return null;
        //noinspection ResultOfMethodCallIgnored
        location.mkdirs();

        account = new Account(uuid);
        objects.put(uuid.toString(), account);

        File file = account.getFile();
        if (!file.exists()) return account;

        try {
            account.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return account;
    }

    public static void writeAll() throws IOException {
        for (Account account : objects.values())
            if (account.changed) account.write();
    }

    public static void clear() {
        Account.location = null;
        Account.objects.clear();
    }

    public static void setLocation(File location) {
        Account.location = location;
    }

    private File getFile() {
        return new File(location, uuid + ".json");
    }

    private void read() throws IOException {
        read(getFile());
    }

    private void read(File file) throws IOException {
        changed = false;

        JsonParser jsonParser = new JsonParser();
        try {

            Object obj = jsonParser.parse(new FileReader(file));
            JsonObject jsonObject = (JsonObject) obj;
            balance = jsonObject.get("balance").getAsLong();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write() throws IOException {
        write(getFile());
    }

    private void write(File location) throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("balance", balance);
        try (FileWriter file = new FileWriter(location)) {
            String str = obj.toString();
            file.write(str);
        }
        changed = false;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long v) {
        balance = v;
        changed = true;
        PacketDispatcher.sendTo(new MessageBalance(balance), getPlayerMP());
    }

    public void addBalance(long v) {
        setBalance(balance + v);
    }

    private EntityPlayerMP getPlayerMP() {
        return ForgeEconomy.minecraftServer.getPlayerList().getPlayerByUUID(uuid);
    }
}
