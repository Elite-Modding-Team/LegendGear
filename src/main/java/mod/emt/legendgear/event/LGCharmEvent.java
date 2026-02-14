package mod.emt.legendgear.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import baubles.api.BaublesApi;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGCharmEvent
{
    @SubscribeEvent
    public static void charmAttackEvent(LivingAttackEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        DamageSource damageSource = event.getSource();

        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            // Blast Charm fully blocks explosion damage before cooling down for 10 seconds
            if (damageSource.isExplosion())
            {
                if (BaublesApi.isBaubleEquipped(player, LGItems.BLAST_CHARM) > 5 && !(player.getCooldownTracker().hasCooldown(LGItems.BLAST_CHARM)))
                {
                    if (!world.isRemote)
                        world.playSound(null, entity.getPosition(), LGSoundEvents.ITEM_CHARM_REPEL.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F + world.rand.nextFloat() * 0.4F);
                    event.setCanceled(true);
                    player.addStat(StatList.getObjectUseStats(LGItems.BLAST_CHARM));
                    player.getCooldownTracker().setCooldown(LGItems.BLAST_CHARM, 10 * 20);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void charmFallAttack(LivingAttackEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();

        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            // Feather Charm provides complete resistance to fall and wall collision damage (from Elytra usage)
            if (damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL)
            {
                if (BaublesApi.isBaubleEquipped(player, LGItems.FEATHER_CHARM) > 5)
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void charmFallEvent(LivingFallEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            // Needed so fall damage doesn't cause damage sounds while the Feather Charm is equipped
            if (BaublesApi.isBaubleEquipped(player, LGItems.FEATHER_CHARM) > 5)
            {
                event.setCanceled(true);
            }
        }
    }
}
