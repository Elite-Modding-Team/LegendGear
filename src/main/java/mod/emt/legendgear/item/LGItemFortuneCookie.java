package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.base.LGItemFood;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemFortuneCookie extends LGItemFood
{
    public LGItemFortuneCookie()
    {
        super(2, 0.1F, false);
        this.setAlwaysEdible();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.fortune_cookie"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        // 1/3 chance to give a useful tip, otherwise give us a generic message
        if (world.rand.nextInt(3) == 0)
        {
            player.sendMessage(new TextComponentTranslation("fortunes.legendgear.hint." + world.rand.nextInt(countMessages("fortunes.legendgear.hint"))).setStyle(new Style().setItalic(true).setColor(TextFormatting.GOLD)));
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.8F, 1.2F);
        }
        else
        {
            player.sendMessage(new TextComponentTranslation("fortunes.legendgear.generic." + world.rand.nextInt(countMessages("fortunes.legendgear.generic"))).setStyle(new Style().setItalic(true)));
            player.playSound(LGSoundEvents.ITEM_FORTUNE_COOKIE_USE.getSoundEvent(), 2.0F, 0.8F + world.rand.nextFloat() * 0.4F);
        }
        super.onFoodEaten(stack, world, player);
    }

    @SideOnly(Side.CLIENT)
    private int countMessages(String translateKey)
    {
        int i;
        for (i = 0; !I18n.format(translateKey + "." + i).contains(translateKey); i++) ;
        return i;
    }
}
