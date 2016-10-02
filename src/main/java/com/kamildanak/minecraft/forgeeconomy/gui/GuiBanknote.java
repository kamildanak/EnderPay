package com.kamildanak.minecraft.forgeeconomy.gui;

import com.kamildanak.minecraft.forgeeconomy.gui.lib.GuiScreenPlus;
import com.kamildanak.minecraft.forgeeconomy.gui.lib.elements.GuiEditBigInteger;
import com.kamildanak.minecraft.forgeeconomy.gui.lib.elements.GuiExButton;
import com.kamildanak.minecraft.forgeeconomy.gui.lib.elements.GuiLabel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.math.BigInteger;

public class GuiBanknote extends GuiScreenPlus {
    public GuiBanknote(World world, BlockPos blockPos, EntityPlayer player)
    {
        super(146, 65, "forgeeconomy:textures/banknote-gui.png");
        addChild(new GuiLabel(9, 9, "gui.forgeeconomy:gui_banknote.number_of_credits"));
        addChild(new GuiEditBigInteger(9, 20, 127, 10, BigInteger.ZERO, BigInteger.valueOf(Long.MAX_VALUE)));
        addChild(new GuiExButton(9, 36, 60, 20, "gui.forgeeconomy:gui_banknote.issue")
        {
            @Override
            public void onClick() {
                mc.thePlayer.closeScreen();
            }
        });
        addChild(new GuiExButton(76, 36, 60, 20, "gui.forgeeconomy:gui_banknote.cancel"){
            @Override
            public void onClick() {
                mc.thePlayer.closeScreen();
            }
        });
    }
}
