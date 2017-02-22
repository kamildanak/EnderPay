package com.kamildanak.minecraft.enderpay.item;

import com.kamildanak.minecraft.enderpay.EnderPay;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlankBanknote extends Item {
    public ItemBlankBanknote(String name) {
        this.setItemName(name);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, @Nonnull EnumHand handIn) {
        EnderPay.guiBanknote.open(player, worldIn, player.getPosition());
        //noinspection unchecked
        return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
    }

    private void setItemName(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
    }
}
