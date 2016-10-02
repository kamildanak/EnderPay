package com.kamildanak.minecraft.forgeeconomy.gui;

import com.kamildanak.minecraft.forgeeconomy.gui.lib.GuiScreenPlus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiBanknote extends GuiScreenPlus {
    public GuiBanknote(World world, BlockPos blockPos, EntityPlayer player)
    {
        super(166, 120, "forgeeconomy:textures/banknote-gui.png");
    }
}
