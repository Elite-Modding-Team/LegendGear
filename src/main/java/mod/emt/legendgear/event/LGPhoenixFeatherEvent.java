package mod.emt.legendgear.event;

import baubles.api.BaublesApi;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGPhoenixFeatherEvent
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void phoenixEffect(LivingDeathEvent event)
    {
        if (!event.getEntityLiving().getEntityWorld().isRemote && event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            int slot = BaublesApi.isBaubleEquipped(player, LGItems.PHOENIX_CHARM);

            // Phoenix Feather
            if (InventoryHelper.deleteFirstMatchingItemStack(player, LGItems.PHOENIX_FEATHER))
            {
                player.setHealth(1);
                player.hurtResistantTime = 65;
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 28, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 65, 4));
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 65, 1));
                player.removePotionEffect(MobEffects.POISON);
                player.removePotionEffect(MobEffects.WITHER);
                player.setFire(3);
                player.getEntityWorld().playSound(null, player.getPosition(), LGSoundEvents.ITEM_PHOENIX_FEATHER_ACTIVATE.getSoundEvent(), SoundCategory.PLAYERS, 2.5F, 1.0F);
                player.getEntityWorld().playSound(null, player.getPosition(), LGSoundEvents.ITEM_PHOENIX_FEATHER_REVIVE.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.getEntityWorld().playSound(null, player.getPosition(), LGSoundEvents.ITEM_PHOENIX_FEATHER_USE.getSoundEvent(), SoundCategory.PLAYERS, 0.8F, 1.0F);
                event.setCanceled(true);

                // Phoenix Charm
            } else if (BaublesApi.isBaubleEquipped(player, LGItems.PHOENIX_CHARM) > 5)
            {
                player.setHealth(1);
                player.hurtResistantTime = 65;
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 28, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 65, 4));
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 65, 1));
                player.removePotionEffect(MobEffects.POISON);
                player.removePotionEffect(MobEffects.WITHER);
                player.setFire(3);
                player.getEntityWorld().playSound(null, player.getPosition(), LGSoundEvents.ITEM_PHOENIX_FEATHER_ACTIVATE.getSoundEvent(), SoundCategory.PLAYERS, 2.5F, 1.0F);
                player.getEntityWorld().playSound(null, player.getPosition(), LGSoundEvents.ITEM_PHOENIX_FEATHER_REVIVE.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.getEntityWorld().playSound(null, player.getPosition(), LGSoundEvents.ITEM_PHOENIX_FEATHER_USE.getSoundEvent(), SoundCategory.PLAYERS, 0.8F, 1.0F);
                BaublesApi.getBaublesHandler(player).extractItem(slot, 1, false);
                event.setCanceled(true);
            }
        }
    }
}
