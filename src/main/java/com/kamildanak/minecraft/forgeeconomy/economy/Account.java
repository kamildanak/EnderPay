package com.kamildanak.minecraft.forgeeconomy.economy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import com.kamildanak.minecraft.forgeeconomy.network.PacketDispatcher;
import com.kamildanak.minecraft.forgeeconomy.network.client.MessageBalance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.nio.charset.Charset;
import java.security.*;

import java.util.HashMap;
import java.util.UUID;

public class Account {
    private static HashMap<String, Account> objects = new HashMap<>();
    private static File location;
    private boolean changed;

    private UUID uuid;
    private long balance;
    private int dailyLimit;
    private int transactionLimit;
    private int noPinLimit;
    private String pin;

    public static Account get(EntityPlayer player) {
        return get(player.getUniqueID());
    }

    private static Account get(UUID uuid)
    {
        Account account = objects.get(uuid.toString());
        if(account!=null) return account;

        if (location == null) return null;
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

    private File getFile() {
        return new File(location, uuid + ".json");
    }

    private Account(UUID uuid)
    {
        this.uuid = uuid;
        this.balance = 0;
        this.dailyLimit = 0;
        this.transactionLimit = 0;
        this.noPinLimit = 0;
        this.pin = "";
        this.changed = true;
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
            dailyLimit = jsonObject.get("dailyLimit").getAsInt();
            transactionLimit = jsonObject.get("transactionLimit").getAsInt();
            noPinLimit = jsonObject.get("noPinLimit").getAsInt();
            pin = jsonObject.get("pin").getAsString();

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
        obj.addProperty("dailyLimit", dailyLimit);
        obj.addProperty("transactionLimit", transactionLimit);
        obj.addProperty("noPinLimit", noPinLimit);
        obj.addProperty("pin", pin);
        try (FileWriter file = new FileWriter(location)) {
            String str = obj.toString();
            file.write(str);
        }
        changed = false;
    }

    public static void writeAll() throws IOException {
        for (Account account : objects.values())
            if (account.changed) account.write();
    }

    public static void clear(){
        Account.location = null;
        Account.objects.clear();
    }

    public static void setLocation(File location)
    {
        Account.location = location;
    }


    public int getDailyLimit(){
        return dailyLimit;
    }

    public int getTransactionLimit(){
        return transactionLimit;
    }

    public long getBalance(){
        return balance;
    }
    public void addBalance(long v) {
        setBalance(balance + v);
    }
    public void setBalance(long v){
        balance = v;
        changed = true;
        PacketDispatcher.sendTo(new MessageBalance(balance), getPlayerMP());
    }

    public int getNoPinLimit() { return noPinLimit; }

    public boolean checkPin(String pinToCheck)
    {
        try {
            return pin.equals(getMD5(pinToCheck));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public void setPin(String newPin) throws NoSuchAlgorithmException {
        pin = getMD5(newPin);
    }

    private EntityPlayerMP getPlayerMP()
    {
        return ForgeEconomy.minecraftServer.getPlayerList().getPlayerByUUID(uuid);
    }

    private String getMD5(String string) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(string.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        return new String(Hex.encodeHex(resultByte));
    }
}
