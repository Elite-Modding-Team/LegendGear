package mod.emt.legendgear.event;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGCriticalEvent
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCriticalHit(CriticalHitEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack mainhand = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

        // 50% chance for the Starglass Sword to cause a critical hit
        if (mainhand.getItem() == LGItems.STARGLASS_SWORD && world.rand.nextDouble() <= 0.5D) {
            world.playSound(null, entity.getPosition(), LGSoundEvents.RANDOM_SWORD_SLASH.getSoundEvent(), SoundCategory.PLAYERS, 0.6F, 1.3F + world.rand.nextFloat() * 0.4F);
            event.setResult(Result.ALLOW);
        }
    }
}
