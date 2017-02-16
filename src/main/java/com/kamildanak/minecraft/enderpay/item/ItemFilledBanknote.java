package com.kamildanak.minecraft.enderpay.item;

import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.gui.hud.BalanceHUD;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFilledBanknote extends Item {
    public ItemFilledBanknote(String name) {
        this.setItemName(name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.maxStackSize = 1;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            int currentItemIndex = playerIn.inventory.currentItem;
            if(!stack.isItemEqual(playerIn.inventory.getStackInSlot(currentItemIndex))) return EnumActionResult.FAIL;
            playerIn.inventory.decrStackSize(currentItemIndex, 1);
            if (stack.getTagCompound() == null) return EnumActionResult.FAIL;
            if (!stack.getTagCompound().hasKey("Amount")) return EnumActionResult.FAIL;
            long amount = stack.getTagCompound().getLong("Amount");
            Account.get(playerIn).addBalance(amount);
        }
        //EnderPay.guiBanknote.open(playerIn, worldIn, pos);
        return EnumActionResult.SUCCESS;
    }

    private void setItemName(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        //tooltip.add("V:");
        if (stack.getTagCompound() == null) return;
        if (!stack.getTagCompound().hasKey("Amount")) return;
        long amount = stack.getTagCompound().getLong("Amount");
        //tooltip.add("Vallue:");
        tooltip.add(Utils.format(amount) + BalanceHUD.getCurrency());
    }
}
