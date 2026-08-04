package mod.emt.legendgear.event;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID, value = Side.CLIENT)
public class LGAugmentTooltipEvent
{
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event)
    {
        try
        {
            ItemStack stack = event.getItemStack();
            if (stack != null && !stack.isEmpty())
            {
                String medallionKey = MedallionAugmentHelper.getAugment(stack);
                if (medallionKey != null)
                {
                    String translationKey = "augment.legendgear." + medallionKey;
                    String tooltipLine;

                    if (I18n.canTranslate(translationKey))
                    {
                        String localizedName = I18n.translateToLocal(translationKey);
                        tooltipLine = TextFormatting.GOLD + localizedName;
                    } else
                    {
                        tooltipLine = TextFormatting.RED + "Missing Lang: " + translationKey;
                    }

                    event.getToolTip().add(1, tooltipLine);
                }
            }
        } catch (Exception ignored)
        {
        }
    }
}
