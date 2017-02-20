package com.kamildanak.minecraft.enderpay.economy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.client.MessageBalance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
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
    private long lastLogin;
    private long lastCountActivity;

    private Account(UUID uuid) {
        this.uuid = uuid;
        this.balance = EnderPay.startBalance;
        long now = Utils.getCurrentDay();
        this.lastLogin = now;
        this.lastCountActivity = now;
        this.changed = true;
    }

    public static Account get(EntityPlayer player) {
        return get(player.getUniqueID());
    }

    @Nullable
    public static Account get(UUID uuid) {
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

    public static void clear() {
        Account.location = null;
        Account.objects.clear();
    }

    public static void setLocation(File location) {
        Account.location = location;
    }

    public boolean update() {
        long balanceBefore = this.balance;
        long now = Utils.getCurrentDay();
        long activityDeltaDays = now - this.lastCountActivity;
        this.lastCountActivity = now;

        if (activityDeltaDays == 0) return false;

        if (EnderPay.stampedMoney) {
            if (activityDeltaDays <= EnderPay.resetLoginDelta) {
                for (int i = 0; i < activityDeltaDays; i++)
                    this.balance -= Math.ceil((double) (this.balance * EnderPay.stampedMoneyPercent) / 100);
            }
        }
        if (EnderPay.basicIncome && getPlayerMP() != null) {
            long loginDeltaDays = (now - this.lastLogin);
            if (loginDeltaDays > EnderPay.maxLoginDelta) loginDeltaDays = EnderPay.maxLoginDelta;
            this.lastLogin = now;
            this.balance += loginDeltaDays * EnderPay.basicIncomeAmount;
        }
        if (activityDeltaDays > EnderPay.resetLoginDelta) {
            this.balance = EnderPay.startBalance;
        }
        return balanceBefore != balance;
    }

    public void writeIfChanged() throws IOException {
        if (changed) write();
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
            lastLogin = jsonObject.get("lastLogin").getAsLong();
            lastCountActivity = jsonObject.get("lastCountActivity").getAsLong();

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
        obj.addProperty("lastLogin", lastLogin);
        obj.addProperty("lastCountActivity", lastCountActivity);
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

    @Nullable
    private EntityPlayerMP getPlayerMP() {
        EntityPlayerMP entityPlayerMP = EnderPay.minecraftServer.getPlayerList().getPlayerByUUID(uuid);
        //noinspection ConstantConditions
        return (entityPlayerMP != null) ? entityPlayerMP : null;
    }
}
