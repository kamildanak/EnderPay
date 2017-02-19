package com.kamildanak.minecraft.enderpay.item;

import com.kamildanak.minecraft.enderpay.EnderPay;
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
        if (stack.getTagCompound() == null) return;
        if (!stack.getTagCompound().hasKey("Amount")) {
            tooltip.add(Utils.format(0) + " " + BalanceHUD.getCurrency());
            return;
        }
        long amount = stack.getTagCompound().getLong("Amount");
        if (EnderPay.stampedMoney) {
            if (!stack.getTagCompound().hasKey("DateIssued")) return;
            long dateIssued = stack.getTagCompound().getLong("DateIssued");
            tooltip.add("Original value: " + Utils.format(amount) + " " + BalanceHUD.getCurrency());

            amount = getCurrentValue(amount, dateIssued);
            if (amount == 0) {
                tooltip.add("Expired");
            } else {
                tooltip.add("Current value: " + Utils.format(amount) + " " + BalanceHUD.getCurrency());
                tooltip.add("Expires in " +
                        Long.toString(EnderPay.daysAfterBanknotesExpires - daysAfterIssued(dateIssued)) + " days");
            }
        } else {
            tooltip.add(Utils.format(amount) + " " + BalanceHUD.getCurrency());
        }
    }

    private long getCurrentValue(long amount, long dateIssued) {
        if (isExpired(dateIssued)) {
            amount = 0;
        } else {
            for (int i = 0; i < daysAfterIssued(dateIssued); i++) {
                amount -= (amount * EnderPay.stampedMoneyPercent) / 100;
            }
        }
        return amount;
    }

    private boolean isExpired(long dateIssued) {
        return daysAfterIssued(dateIssued) >= EnderPay.daysAfterBanknotesExpires;
    }

    private long daysAfterIssued(long dateIssued) {
        long now = Utils.getCurrentTime();
        long timeDelta = now - dateIssued;
        return Utils.timeToDays(timeDelta);
    }
}
