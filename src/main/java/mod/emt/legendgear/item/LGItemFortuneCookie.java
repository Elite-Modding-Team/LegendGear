package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.base.LGItemFood;

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
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.fortune_cookie"));
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            // TODO: Custom fortunes via a config list?
            // 1/3 chance to give a useful tip, otherwise give us a generic message
            /*if (world.rand.nextInt(2) == 0)
            {
                player.sendMessage(new TextComponentTranslation("fortunes." + LegendGear.MOD_ID + ".hint." + world.rand.nextInt(23)).setStyle(new Style().setItalic(true).setColor(TextFormatting.GOLD)));
            } else
            {
                player.sendMessage(new TextComponentTranslation("fortunes." + LegendGear.MOD_ID + ".generic." + world.rand.nextInt(51)).setStyle(new Style().setItalic(true)));
            }*/

            player.sendMessage(new TextComponentTranslation("fortunes." + LegendGear.MOD_ID + ".generic." + world.rand.nextInt(51)).setStyle(new Style().setItalic(true)));

            // TODO: Advancement?
            //player.addStat((StatBase)LegendGear2.achievementFortunate, 1);
        }

        player.playSound(LGSoundEvents.ITEM_FORTUNE_COOKIE_USE.getSoundEvent(), 2.0F, 0.8F + world.rand.nextFloat() * 0.4F);
        super.onFoodEaten(stack, world, player);
    }
}