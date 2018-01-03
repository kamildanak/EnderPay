package com.kamildanak.minecraft.enderpay.economy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kamildanak.minecraft.enderpay.proxy.ISettings;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Account {
    private static HashMap<String, Account> objects = new HashMap<>();
    private static ISettings settings;
    private static IDayHelper dayHelper;
    private static IPlayerHelper playerHelper;
    private static File location;
    private boolean changed;

    private UUID uuid;
    private long balance;
    private long lastLogin;
    private long lastCountActivity;

    private Account(UUID uuid) {
        this.uuid = uuid;
        this.balance = settings.getStartBalance();
        long now = dayHelper.getCurrentDay();
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
        long now = dayHelper.getCurrentDay();
        long activityDeltaDays = now - this.lastCountActivity;
        this.lastCountActivity = now;

        if (activityDeltaDays == 0) return false;

        if (settings.isStampedMoney()) {
            if (activityDeltaDays <= settings.getResetLoginDelta()) {
                if (activityDeltaDays>2000) {
                    this.balance = (long) Math.ceil(this.balance * Math.pow((1 - ((double) settings.getStampedMoneyPercent()) / 100), activityDeltaDays));
                } else {
                    for (int i = 0; i < activityDeltaDays; i++)
                        this.balance -= Math.ceil((double) (this.balance * settings.getStampedMoneyPercent()) / 100);
                }
            }
        }
        if (settings.isBasicIncome() && playerHelper.isPlayerLoggedIn(uuid)) {
            long loginDeltaDays = (now - this.lastLogin);
            if (settings.getMaxLoginDelta() > 0 && loginDeltaDays > settings.getMaxLoginDelta())
                loginDeltaDays = settings.getMaxLoginDelta();
            this.lastLogin = now;
            this.balance += loginDeltaDays * settings.getBasicIncomeAmount();
        }
        if (settings.getResetLoginDelta() > 0 && activityDeltaDays > settings.getResetLoginDelta()) {
            this.balance = settings.getStartBalance();
        }
        return activityDeltaDays > 0;
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
        playerHelper.send(uuid, balance);
    }

    public void addBalance(long v) {
        setBalance(balance + v);
    }

    public static void setInterfaces(ISettings settings, IDayHelper dayHelper, IPlayerHelper playerHelper) {
        Account.settings = settings;
        Account.dayHelper = dayHelper;
        Account.playerHelper = playerHelper;
    }
}
