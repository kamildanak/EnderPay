package com.kamildanak.minecraft.enderpay.item;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.gui.hud.BalanceHUD;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
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

    public static boolean isExpired(long dateIssued) {
        return Utils.daysAfterDate(dateIssued) >= EnderPay.settings.getDaysAfterBanknotesExpires();
    }

    public static ItemStack getItemStack(long creditsAmount) {
        ItemStack newBanknote = new ItemStack(EnderPay.itemFilledBanknote);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("Amount", creditsAmount);
        tag.setLong("DateIssued", Utils.getCurrentDay());
        newBanknote.setTagCompound(tag);
        return newBanknote;
    }

    public static long getDateIssued(ItemStack stack) {
        if (stack.getTagCompound() == null) return 0;
        return stack.getTagCompound().getLong("DateIssued");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
        if (!worldIn.isRemote) {
            int currentItemIndex = playerIn.inventory.currentItem;
            ItemStack stack = playerIn.getHeldItem(hand);
            if (!stack.isItemEqual(playerIn.inventory.getStackInSlot(currentItemIndex)))
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
            playerIn.inventory.decrStackSize(currentItemIndex, 1);
            if (stack.getTagCompound() == null)
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
            if (!stack.getTagCompound().hasKey("Amount"))
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
            long amount = stack.getTagCompound().getLong("Amount");
            Account.get(playerIn).addBalance(amount);
        }
        //EnderPay.guiBanknote.open(playerIn, worldIn, pos);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
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
        if (EnderPay.settings.isStampedMoney()) {
            if (!stack.getTagCompound().hasKey("DateIssued")) return;
            long dateIssued = stack.getTagCompound().getLong("DateIssued");
            tooltip.add("Original value: " + Utils.format(amount) + " " + BalanceHUD.getCurrency());

            amount = getCurrentValue(amount, dateIssued);
            if (amount == 0) {
                tooltip.add("Expired");
            } else {
                tooltip.add("Current value: " + Utils.format(amount) + " " + BalanceHUD.getCurrency());
                tooltip.add("Expires in " +
                        Long.toString(EnderPay.settings.getDaysAfterBanknotesExpires() - Utils.daysAfterDate(dateIssued)) + " days");
            }
        } else {
            tooltip.add(Utils.format(amount) + " " + BalanceHUD.getCurrency());
        }
    }

    private long getCurrentValue(long amount, long dateIssued) {
        long dayAfter = Utils.daysAfterDate(dateIssued);
        if (dayAfter < 0) return amount;
        if (isExpired(dateIssued)) {
            amount = 0;
        } else {
            amount -= Math.ceil((double) (dayAfter * (amount * EnderPay.settings.getStampedMoneyPercent())) / 100);
        }
        return amount;
    }
}
