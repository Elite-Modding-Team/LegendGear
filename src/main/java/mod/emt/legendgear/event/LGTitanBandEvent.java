package mod.emt.legendgear.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import baubles.api.BaublesApi;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGTitanBandEvent
{
    @SubscribeEvent
    public static void titanBandLift(PlayerInteractEvent.EntityInteractSpecific event)
    {
        Entity entity = event.getTarget();
        EntityPlayer player = event.getEntityPlayer();
        World world = player.getEntityWorld();

        if (entity instanceof EntityLiving && entity.rideCooldown <= 0 && !player.isBeingRidden() && player.inventory.getCurrentItem().isEmpty() && BaublesApi.isBaubleEquipped(player, LGItems.TITAN_BAND) >= 0)
        {
            entity.startRiding(player);
            ((EntityLiving) entity).playLivingSound();
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_TITAN_BAND_LIFT.getSoundEvent(), SoundCategory.PLAYERS, 0.3F, 1.0F);
            player.getEntityData().setBoolean("TitanLift", true);
            player.getEntityData().setInteger("carryingTime", 0);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void titanBandThrow(PlayerInteractEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.getEntityWorld();

        if (player.isBeingRidden() && player.getEntityData().getInteger("carryingTime") > 5)
        {
            final Entity rider = player.getPassengers().get(0);
            rider.dismountRidingEntity();
            final float var3 = 1.5F;
            rider.motionX = -MathHelper.sin(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F) * var3;
            rider.motionZ = MathHelper.cos(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F) * var3;
            rider.motionY = -MathHelper.sin(player.rotationPitch / 180.0F * 3.1415927F) * var3 + 0.2F;

            if (rider instanceof EntityLiving)
            {
                ((EntityLiving) rider).playLivingSound();
            }

            rider.rotationYaw = player.rotationYaw;
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_TITAN_BAND_THROW.getSoundEvent(), SoundCategory.PLAYERS, 0.3F, 1.0F);
            player.getEntityData().setBoolean("TitanLift", false);
        }
    }

    @SubscribeEvent
    public static void titanBandDrop(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();

        if (entity instanceof EntityPlayer && entity.isBeingRidden() && entity.getPassengers().get(0) instanceof EntityLivingBase)
        {
            final EntityPlayer player = (EntityPlayer) entity;
            final EntityLivingBase rider = (EntityLivingBase) player.getPassengers().get(0);

            // Drop entity when the band is not equipped or either player or rider is hurt
            if (player.getEntityData().getBoolean("TitanLift") && (!(BaublesApi.isBaubleEquipped(player, LGItems.TITAN_BAND) >= 0) || player.hurtTime > 0 || rider.hurtTime > 0))
            {
                rider.dismountRidingEntity();
                world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_TITAN_BAND_THROW.getSoundEvent(), SoundCategory.PLAYERS, 0.3F, 1.0F);
                player.getEntityData().setBoolean("TitanLift", false);
            }
        }
    }
}
