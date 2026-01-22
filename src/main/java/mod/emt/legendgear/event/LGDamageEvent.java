package mod.emt.legendgear.event;

import baubles.api.BaublesApi;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGDamageEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void headbandHurtEvent(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        DamageSource damageSource = event.getSource();
        Entity trueSource = damageSource.getTrueSource();

        if (trueSource instanceof EntityPlayer && trueSource != null) {
            EntityPlayer player = (EntityPlayer) trueSource;
            ItemStack headband = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

            // The Headband of Valor increases damage by 4 while equipped
            if (headband.getItem() == LGItems.VALOR_HEADBAND) {
                world.playSound(null, entity.getPosition(), LGSoundEvents.RANDOM_DAMAGE_BOOST.getSoundEvent(), SoundCategory.PLAYERS, 0.5F, 0.8F + world.rand.nextFloat() * 0.4F);
                event.setAmount(event.getAmount() + 4.0F);
                headband.damageItem(1, player);
            }
        }
    }
}
