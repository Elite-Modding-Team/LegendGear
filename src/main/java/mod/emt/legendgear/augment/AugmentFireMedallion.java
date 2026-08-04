package mod.emt.legendgear.augment;

import com.google.common.collect.Multimap;
import mod.emt.legendgear.entity.LGEntityFireBolt;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class AugmentFireMedallion implements IMedallionAugment
{
    @Override
    public void executeAbility(EntityPlayer player, EnumHand hand, ItemStack tool)
    {
        if (player.world.isRemote)
        {
            return;
        }

        double attackDamage = 1.0D;
        Multimap<String, AttributeModifier> modifiers = tool.getAttributeModifiers(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND);
        for (AttributeModifier modifier : modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
        {
            attackDamage += modifier.getAmount();
        }

        player.world.spawnEntity(new LGEntityFireBolt(player.world, player, attackDamage));
        player.world.playSound(null, player.posX, player.posY, player.posZ, LGSoundEvents.ITEM_AUGMENT_FIRE_BOLT.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 2.0F);
    }

    @Override
    public int getChargeTime()
    {
        return 15;
    }

    @Override
    public int getActivationWindow()
    {
        return 20;
    }
}
