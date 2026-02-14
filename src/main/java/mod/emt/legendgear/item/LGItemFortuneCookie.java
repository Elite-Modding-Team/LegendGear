package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

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

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            // 1/3 chance to give a useful tip, otherwise give us a generic message
            if (world.rand.nextInt(3) == 0)
            {
                player.sendMessage(new TextComponentTranslation("fortunes.legendgear.hint." + world.rand.nextInt(countMessages("fortunes.legendgear.hint"))).setStyle(new Style().setItalic(true).setColor(TextFormatting.GOLD)));
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8F, 1.2F);
                // TODO: Advancement
            }
            else
            {
                player.sendMessage(new TextComponentTranslation("fortunes.legendgear.generic." + world.rand.nextInt(countMessages("fortunes.legendgear.generic"))).setStyle(new Style().setItalic(true)));
                player.world.playSound(null, player.posX, player.posY, player.posZ, LGSoundEvents.ITEM_FORTUNE_COOKIE_USE.getSoundEvent(), SoundCategory.PLAYERS, 2.0F, 0.8F + world.rand.nextFloat() * 0.4F);
            }
        }

        super.onFoodEaten(stack, world, player);
    }

    private int countMessages(String translateKey)
    {
        int i;
        for (i = 0; !I18n.format(translateKey + "." + i).contains(translateKey); i++) ;
        return i;
    }
}
