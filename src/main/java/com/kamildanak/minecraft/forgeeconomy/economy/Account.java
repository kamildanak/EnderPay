package com.kamildanak.minecraft.forgeeconomy.economy;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.*;

import java.io.IOException;

public class Account {
    private String name;
    private long balance;
    private int dailyLimit;
    private int transactionLimit;
    private int noPinLimit;
    private String pin;

    public Account(String name)
    {
        this.name = name;
        this.balance = 11;
        this.dailyLimit = 0;
        this.transactionLimit = 0;
        this.noPinLimit = 0;
        this.pin = "";
    }

    public Account(PacketBuffer buffer) {
        read(buffer);
    }

    protected void read(PacketBuffer buffer) {
        name = buffer.readStringFromBuffer(10);
        balance = buffer.readLong();
        dailyLimit = buffer.readInt();
        transactionLimit = buffer.readInt();
        noPinLimit = buffer.readInt();
        pin = buffer.readStringFromBuffer(10);
    }

    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeString(name);
        buffer.writeLong(balance);
        buffer.writeInt(dailyLimit);
        buffer.writeInt(transactionLimit);
        buffer.writeInt(noPinLimit);
        buffer.writeString(pin);
    }

    public String getName(){
        return name;
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
    public void addBalance(long v){
        balance += v;
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

    private String getMD5(String string) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(string.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        return new String(Hex.encodeHex(resultByte));
    }
}
