package mod.emt.legendgear.augment;

import com.google.common.collect.Multimap;
import mod.emt.legendgear.entity.LGEntityQuake;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class AugmentEarthMedallion implements IMedallionAugment
{
    @Override
    public void executeAbility(EntityPlayer player, EnumHand hand, ItemStack tool)
    {
        if (player.world.isRemote)
        {
            return;
        }

        LGEntityQuake quake = new LGEntityQuake(player.world, player.posX, player.posY, player.posZ, player, true, 6.0D);
        double attackDamage = 1.0D;
        Multimap<String, AttributeModifier> modifiers = tool.getAttributeModifiers(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND);
        for (AttributeModifier modifier : modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
        {
            attackDamage += modifier.getAmount();
        }

        quake.damage_per_hit = attackDamage * 1.5D;
        player.world.spawnEntity(quake);
        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 1.2F);
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
