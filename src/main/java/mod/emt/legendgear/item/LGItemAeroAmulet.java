package mod.emt.legendgear.item;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Instead of durability and being able to break, it will recharge with extra wind medallions
public class LGItemAeroAmulet extends Item implements IBauble {
    public LGItemAeroAmulet() {
        super();
        setMaxDamage(600);
        setMaxStackSize(1);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.AMULET;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.aero_amulet"));
    }
}
