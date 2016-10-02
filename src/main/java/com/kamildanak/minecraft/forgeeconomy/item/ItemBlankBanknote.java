package com.kamildanak.minecraft.forgeeconomy.item;

import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlankBanknote extends Item {
    public ItemBlankBanknote(String name)
    {
        this.setItemName(name);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ForgeEconomy.guiBanknote.open(playerIn, worldIn, pos);
        return EnumActionResult.SUCCESS;
    }

    private void setItemName(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
