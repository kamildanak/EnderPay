package com.kamildanak.minecraft.enderpay.gui;

import com.kamildanak.minecraft.enderpay.gui.lib.GuiScreenPlus;
import com.kamildanak.minecraft.enderpay.gui.lib.elements.GuiEditBigInteger;
import com.kamildanak.minecraft.enderpay.gui.lib.elements.GuiExButton;
import com.kamildanak.minecraft.enderpay.gui.lib.elements.GuiLabel;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.server.MessageIssueBanknote;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.math.BigInteger;

public class GuiBanknote extends GuiScreenPlus {
    private GuiEditBigInteger editBigInteger;
    public GuiBanknote(World world, BlockPos blockPos, EntityPlayer player)
    {
        super(146, 65, "enderpay:textures/banknote-gui.png");
        addChild(new GuiLabel(9, 9, "gui.enderpay:gui_banknote.number_of_credits"));
        addChild(editBigInteger = new GuiEditBigInteger(9, 20, 127, 10,
                BigInteger.ZERO, BigInteger.valueOf(Long.MAX_VALUE)));
        addChild(new GuiExButton(9, 36, 60, 20, "gui.enderpay:gui_banknote.issue")
        {
            @Override
            public void onClick() {
                PacketDispatcher.sendToServer(new MessageIssueBanknote(editBigInteger.getValue().longValue()));
                mc.player.closeScreen();
            }
        });
        addChild(new GuiExButton(76, 36, 60, 20, "gui.enderpay:gui_banknote.cancel"){
            @Override
            public void onClick() {
                mc.player.closeScreen();
            }
        });
    }
}
