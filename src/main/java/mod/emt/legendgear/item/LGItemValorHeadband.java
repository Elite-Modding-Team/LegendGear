package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.util.TooltipHelper;

// TODO: Instead of durability and being able to break, it will recharge with extra fire medallions
public class LGItemValorHeadband extends ItemArmor
{
    protected static final String TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/models/armor/valor_headband.png").toString();

    public LGItemValorHeadband(ArmorMaterial material, int renderIndex)
    {
        super(material, renderIndex, EntityEquipmentSlot.HEAD);
        this.setMaxDamage(4000);
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.valor_headband"));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return TEXTURE;
    }
}