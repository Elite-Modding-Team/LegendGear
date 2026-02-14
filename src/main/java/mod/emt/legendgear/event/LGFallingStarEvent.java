package mod.emt.legendgear.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.entity.LGEntityFallingStar;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGFallingStarEvent
{
    @SubscribeEvent
    public static void fallingStarSpawn(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();

        if (!world.isRemote && world.getTotalWorldTime() % 20 == 0 && entity instanceof EntityPlayer && !world.isDaytime() && world.canSeeSky(entity.getPosition()))
        {
            double chance = world.getCurrentMoonPhaseFactor() == 1 ? LGConfig.GENERAL_SETTINGS.starfallRarity * 2 : LGConfig.GENERAL_SETTINGS.starfallRarity;
            if (world.rand.nextDouble() < chance)
            {
                world.spawnEntity(new LGEntityFallingStar((EntityPlayer) entity));
            }
        }
    }
}
