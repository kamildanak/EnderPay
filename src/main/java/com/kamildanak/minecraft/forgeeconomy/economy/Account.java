package com.kamildanak.minecraft.forgeeconomy.economy;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class Account {
    private String name;
    private long balance;
    private int dailyLimit;
    private int transactionLimit;


    public Account(String name)
    {
        this.name = name;
        this.balance = 11;
        this.dailyLimit = 0;
        this.transactionLimit = 0;
    }

    public Account(PacketBuffer buffer) {
        read(buffer);
    }

    protected void read(PacketBuffer buffer) {
        name = buffer.readStringFromBuffer(10);
        balance = buffer.readLong();
        dailyLimit = buffer.readInt();
        transactionLimit = buffer.readInt();
    }

    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeString(name);
        buffer.writeLong(balance);
        buffer.writeInt(dailyLimit);
        buffer.writeInt(transactionLimit);
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
}
