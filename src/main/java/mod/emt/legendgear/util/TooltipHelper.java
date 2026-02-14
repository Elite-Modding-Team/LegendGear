package mod.emt.legendgear.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class TooltipHelper
{
    public static void addWrappedTooltip(List<String> tooltip, TextFormatting formatting, String text)
    {
        List<String> tooltipWrapped = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, 250);
        if (tooltipWrapped.size() > 1 && !GuiScreen.isShiftKeyDown())
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.shift"));
            return;
        }
        for (String line : tooltipWrapped)
        {
            tooltip.add(formatting + line);
        }
    }
}
