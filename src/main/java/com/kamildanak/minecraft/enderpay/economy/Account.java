package com.kamildanak.minecraft.enderpay.economy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kamildanak.minecraft.enderpay.EnderPay;
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
    private long lastCountLogin;
    private long lastCountActivity;

    private Account(UUID uuid) {
        this.uuid = uuid;
        this.balance = EnderPay.startBalance;
        long now = System.currentTimeMillis();
        this.lastLogin = now;
        this.lastCountLogin = now;
        this.lastCountActivity = now;
        this.changed = true;
    }

    public void update() {
        long now = System.currentTimeMillis();
        long day = 1000*60*60*24;

        long activityDelta = now-this.lastCountActivity;
        long activityDeltaDays = activityDelta/day;
        this.lastCountActivity = now - activityDelta + activityDeltaDays*day;

        if(EnderPay.stampedMoney) {
            if (activityDeltaDays > 100)
                this.balance = 0;
            else {
                for (int i = 0; i < activityDeltaDays; i++)
                    this.balance -= (this.balance * EnderPay.stampedMoneyPercent) / 100;
            }
        }

        if(EnderPay.basicIncome) {
            if (getPlayerMP() != null) {
                long loginDelta = countDelta(now, this.lastCountLogin, this.lastLogin);
                long loginDeltaDays = loginDelta / day;
                this.lastLogin = now;
                this.lastCountLogin = now - loginDelta + loginDeltaDays * day;
                this.balance += loginDeltaDays * EnderPay.basicIncomeAmount;
            }
        }
    }

    private long countDelta(long now, long lastCount, long last)
    {
        long loginDelta = now-lastCount;
        if(loginDelta> EnderPay.maxLoginDelta)
            loginDelta = now-last;
        if(loginDelta> EnderPay.maxLoginDelta)
            loginDelta= EnderPay.maxLoginDelta;
        return loginDelta;
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

    public void writeIfChanged() throws IOException {
        if(changed) write();
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
            lastLogin = jsonObject.get("lastLogin").getAsLong();
            lastCountLogin = jsonObject.get("lastCountLogin").getAsLong();
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
        obj.addProperty("lastCountLogin", lastCountLogin);
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
        return (entityPlayerMP!=null) ? entityPlayerMP : null;
    }
}
