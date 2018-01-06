package com.kamildanak.minecraft.enderpay.gui;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.Utils;
import com.kamildanak.minecraft.enderpay.network.PacketDispatcher;
import com.kamildanak.minecraft.enderpay.network.server.MessageIssueBanknote;
import com.kamildanak.minecraft.foamflower.gui.GuiScreenPlus;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiEditBigInteger;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiExButton;
import com.kamildanak.minecraft.foamflower.gui.elements.GuiLabel;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.math.BigInteger;

public class GuiBanknote extends GuiScreenPlus {
    private GuiEditBigInteger editBigInteger;
    private boolean expires;
    public GuiBanknote(World world, BlockPos blockPos, EntityPlayer player)
    {
        super(146, isExpireButtonVisible(player) ? 90:65, isExpireButtonVisible(player) ?
                "enderpay:textures/banknote-gui-big.png" : "enderpay:textures/banknote-gui.png");
        addChild(new GuiLabel(9, 9, "gui.enderpay:gui_banknote.number_of_credits"));
        addChild(editBigInteger = new GuiEditBigInteger(9, 21, 127, 10,
                BigInteger.ZERO, BigInteger.valueOf(Long.MAX_VALUE)));
        addChild(new GuiExButton(9, 36, 60, 20, "gui.enderpay:gui_banknote.issue")
        {
            @Override
            public void onClick() {
                PacketDispatcher.sendToServer(new MessageIssueBanknote(editBigInteger.getValue().longValue(), expires));
                mc.player.closeScreen();
            }
        });
        addChild(new GuiExButton(76, 36, 60, 20, "gui.enderpay:gui_banknote.cancel"){
            @Override
            public void onClick() {
                mc.player.closeScreen();
            }
        });
        expires = true;
        if (isExpireButtonVisible(player)) {
            expires = false;
            GuiExButton exButton;
            addChild(exButton = new GuiExButton(9, 62, 127, 20, "") {
                @Override
                public void onClick() {
                    expires = !expires;
                    setCaption(I18n.format("gui.enderpay:gui_banknote.expires").trim() + ": " +
                            (expires ? I18n.format("gui.enderpay.yes").trim() :
                                    I18n.format("gui.enderpay.no").trim()));
                }
            });
            exButton.onClick();
        }
    }

    private static boolean isExpireButtonVisible(EntityPlayer player)
    {
        return EnderPay.settings.isStampedMoney() && Utils.isOP(player);
    }
}
