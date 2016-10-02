package com.kamildanak.minecraft.forgeeconomy.item;

import com.kamildanak.minecraft.forgeeconomy.ForgeEconomy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBanknote extends Item {
    //long face_value = 0;
    //long date_issued = 0; //equal to lastCount of issuing player
    //int tax = 0;
    //count tax every x o clock - full days from epoch
    //time offset option
    //send time to player with balance message

    // automatyczne mergowanie banknotów w inventory vending block w jeden, podczas operacji vend
    // api: get curent value, get current tax,
    public ItemBanknote()
    {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (!playerIn.capabilities.isCreativeMode || ForgeEconomy.consumeBanknotesInCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        /*
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        */
        if (!worldIn.isRemote)
        {
            /*if (itemStackIn.hasTagCompound()) {
                NBTTagCompound nbtTagCompound = itemStackIn.getTagCompound().getCompoundTag("Banknotes");
                if (nbtTagCompound != null)
                {

                }
            }*/
            //Account.get(playerIn).addBalance();
        }

        //playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        /*
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Fireworks");

            if (nbttagcompound != null)
            {
                if (nbttagcompound.hasKey("Flight", 99))
                {
                    tooltip.add(I18n.translateToLocal("item.fireworks.flight") + " " + nbttagcompound.getByte("Flight"));
                }

                NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);

                if (nbttaglist != null && !nbttaglist.hasNoTags())
                {
                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        List<String> list = Lists.<String>newArrayList();
                        ItemFireworkCharge.addExplosionInfo(nbttagcompound1, list);

                        if (!list.isEmpty())
                        {
                            for (int j = 1; j < ((List)list).size(); ++j)
                            {
                                list.set(j, "  " + (String)list.get(j));
                            }

                            tooltip.addAll(list);
                        }
                    }
                }
            }
        }*/
    }
}
