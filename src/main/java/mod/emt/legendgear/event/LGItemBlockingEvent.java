package mod.emt.legendgear.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.LGItemMagicMirror;
import mod.emt.legendgear.item.LGItemReedPipes;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGItemBlockingEvent
{
    @SubscribeEvent
    public static void resetHandOnDamage(LivingAttackEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.isActiveItemStackBlocking())
        {
            ItemStack held = entity.getHeldItem(entity.getActiveHand());
            if (held.getItem() instanceof LGItemMagicMirror)
            {
                entity.stopActiveHand();
            }
            if (held.getItem() instanceof LGItemReedPipes)
            {
                entity.world.playSound(null, entity.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.81F);
                entity.world.playSound(null, entity.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.98F);
                entity.world.playSound(null, entity.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.14F);
                entity.world.playSound(null, entity.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.29F);
                entity.world.playSound(null, entity.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.42F);
                entity.stopActiveHand();
            }
        }
    }
}
