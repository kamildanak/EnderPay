package com.kamildanak.minecraft.forgeeconomy.economy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class AccountPlayerInfo {
    private static HashMap<String, AccountPlayerInfo> objects = new HashMap<String, AccountPlayerInfo>();
    private static File location;

    private HashMap<String, Account> accounts = new HashMap<String, Account>();
    private String username;
    private boolean changed;

    AccountPlayerInfo(String n) {
        username = n.replaceAll("[^\\p{L}\\p{Nd}_]", "");
    }

    public static AccountPlayerInfo get(EntityPlayer player) {
        return get(EntityPlayer.getUUID(player.getGameProfile()).toString());
    }

    public static AccountPlayerInfo get(String nn) {
        AccountPlayerInfo info = objects.get(nn);
        if (info != null) return info;

        if (location == null) return null;
        location.mkdirs();

        info = new AccountPlayerInfo(nn);
        objects.put(nn, info);

        File file = info.getFile();
        if (!file.exists()) return info;

        try {
            info.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return info;
    }

    private File getFile() {
        return new File(location, username + ".dat");
    }

    public static void writeAll() throws IOException {
        for (AccountPlayerInfo info : objects.values()) {
            if (info.changed)
                info.write(info.getFile());
        }
    }

    public Account getAccount(String name) {
        if(!accounts.containsKey(name))
            addAccount(name);
        return accounts.get(name);
    }

    private void addAccount(Account account)
    {
        accounts.put(account.getName(), account);
        changed = true;
    }

    private void addAccount(String name) {
        accounts.put(name, new Account(name));
        changed = true;
    }

    private void removeAccount(String name) {
        accounts.remove(name);
        changed = true;
    }

    private void read(File file) throws IOException {
        changed = false;
        accounts.clear();
        FileInputStream fis = new FileInputStream(file);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(IOUtils.toByteArray(fis));
        PacketBuffer packetBuffer = new PacketBuffer(buffer);
        int count = packetBuffer.readInt();
        for (int i = 0; i < count; i++) {
            addAccount(new Account(packetBuffer));
        }
    }

    private void write(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);

        ByteBuf buffer = Unpooled.buffer();
        PacketBuffer packetBuffer = new PacketBuffer(buffer);

        packetBuffer.writeInt(accounts.size());
        for(Account account : accounts.values()) account.write(packetBuffer);
        byte[] bytes = new byte[packetBuffer.readableBytes()];
        fos.write(packetBuffer.array());
        changed = false;
    }

    public static void clear() {
        AccountPlayerInfo.location = null;
        AccountPlayerInfo.objects.clear();
    }

    public static void setLocation(File location)
    {
        AccountPlayerInfo.location = location;
    }
}
