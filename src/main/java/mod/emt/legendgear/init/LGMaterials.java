package mod.emt.legendgear.init;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class LGMaterials
{
    // Tools
    public static final Item.ToolMaterial TOOL_STARGLASS = EnumHelper.addToolMaterial("legendgear_tool_starglass", 2, 216, 15.0F, 2.0F, 20);

    // Armor
    public static final ItemArmor.ArmorMaterial ARMOR_SPECIAL = EnumHelper.addArmorMaterial("legendgear_armor_special", "special", 5, new int[] {1, 0, 0, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
}
