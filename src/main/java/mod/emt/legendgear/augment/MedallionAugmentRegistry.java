package mod.emt.legendgear.augment;

import java.util.HashMap;
import java.util.Map;

import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class MedallionAugmentRegistry
{
    private static final Map<String, IMedallionAugment> AUGMENTS = new HashMap<>();

    public static void register(String key, IMedallionAugment ability)
    {
        AUGMENTS.put(key, ability);
    }

    public static IMedallionAugment get(String key)
    {
        return AUGMENTS.get(key);
    }

    public static void trigger(EntityPlayer player, EnumHand hand, ItemStack tool)
    {
        String medallionKey = MedallionAugmentHelper.getAugment(tool);
        if (medallionKey != null)
        {
            IMedallionAugment ability = get(medallionKey);
            if (ability != null)
            {
                ability.executeAbility(player, hand, tool);
            }
        }
    }
}
